package com.vbotelho.book_network.services;

import com.vbotelho.book_network.domain.token.Token;
import com.vbotelho.book_network.domain.token.TokenRepository;
import com.vbotelho.book_network.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public Token findByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void save(Token token) {
        tokenRepository.save(token);
    }

}
