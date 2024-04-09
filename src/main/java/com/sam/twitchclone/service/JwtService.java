package com.sam.twitchclone.service;

import com.sam.twitchclone.dao.postgres.model.user.User;
import com.sam.twitchclone.dao.postgres.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String SECRET_KEY = "6027e725f42c75df0afb49076f5b4284c8d626b11ec9c0640efe06c64c597f2e";
    private final TokenRepository tokenRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

//    public boolean isValid(String token, UserDetails user) {
//        String username = extractUsername(token);
//
//        boolean isValidToken = tokenRepository.findByToken(token).map(t -> !t.isLoggedOut()).orElse(false);
//
//        return username.equals(user.getUsername()) && !isTokenExpired(token) && isValidToken;
////        return username.equals(user.getUsername()) && !isTokenExpired(token) && isValidToken;
//    }
    public boolean isValid(String token, User user) {
        String username = extractUsername(token); // here we are extracting userId from the token

        boolean isValidToken = tokenRepository.findByToken(token).map(t -> !t.isLoggedOut()).orElse(false);

        return username.equals(String.valueOf(user.getId())) && !isTokenExpired(token) && isValidToken;
//        return username.equals(user.getUsername()) && !isTokenExpired(token) && isValidToken;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user) {
        String token = Jwts
                .builder()
//                .subject(user.getUsername())
                .subject(String.valueOf(user.getId()))  // here we  are storing userId in the subject
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
