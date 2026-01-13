package com.vbotelho.book_network.services;

import com.vbotelho.book_network.domain.role.RoleRepository;
import com.vbotelho.book_network.domain.token.Token;
import com.vbotelho.book_network.domain.user.User;
import com.vbotelho.book_network.domain.user.UserRepository;
import com.vbotelho.book_network.enums.EmailTemplateName;
import com.vbotelho.book_network.services.dto.AuthenticationRequest;
import com.vbotelho.book_network.services.dto.AuthenticationResponse;
import com.vbotelho.book_network.services.dto.RegistrationRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {

        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = tokenService.generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Map<String, Object> claims = new HashMap<>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return new AuthenticationResponse(jwtToken);
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        var savedToken = tokenService.findByToken(token);
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenService.save(savedToken);
    }
}
