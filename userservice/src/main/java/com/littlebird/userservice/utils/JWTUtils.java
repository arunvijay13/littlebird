package com.littlebird.userservice.utils;

import com.littlebird.userservice.contants.JWTConstants;
import com.littlebird.userservice.contants.JWTErrorCode;
import com.littlebird.userservice.dao.UserInfoDAO;
import com.littlebird.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Component
public class JWTUtils {

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateJwtToken(UserInfoDAO user) {
        try {
            Date issueDate = Date.from(Instant.now());
            Date expiryDate = Date.from(Instant.now().plus(expiration, ChronoUnit.HOURS));

            return Jwts.builder()
                    .setClaims(getClaims(user))
                    .setSubject(user.getUsername())
                    .setId(UUID.randomUUID().toString())
                    .setIssuedAt(issueDate)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
        } catch (Exception e) {
            log.error("code {} | message : {} | Error : {}", JWTErrorCode.JWT_GEN_FAILED,
                    JWTErrorCode.JWT_GEN_FAILED.getMessage(),e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            String username = claims.getSubject();
            if(username == null)
                return false;
            Optional<UserInfoDAO> optionalUser =  userRepository.findByUsername(username);
            return optionalUser.map(this::isValidUser).orElse(false);
        } catch (Exception e) {
            log.error("code {} | message : {} | Error : {}", JWTErrorCode.INVALID_JWT_TOKEN,
                    JWTErrorCode.INVALID_JWT_TOKEN.getMessage(),e.getMessage());
            return false;
        }
    }

    public UserDetails getUserDetails(String token) {
        Claims claims = extractClaims(token);
        String username = (String) claims.get(JWTConstants.USERNAME);
        return userRepository.findByUsername(username).orElse(null);
    }

    private HashMap<String, Object> getClaims(UserInfoDAO user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JWTConstants.USER_EMAIL, user.getEmail());
        claims.put(JWTConstants.USERNAME, user.getUsername());
        return claims;
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody();
    }

    private boolean isValidUser(UserInfoDAO user) {
        return user.isAccountNonExpired() && user.isAccountNonLocked() && user.isCredentialsNonExpired() && user.isEnabled();
    }

}
