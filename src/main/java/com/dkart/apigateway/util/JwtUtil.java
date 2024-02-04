package com.dkart.apigateway.util;

import com.dkart.apigateway.exception.UnAuthorizedUserException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException|UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException e) {
            throw new UnAuthorizedUserException("Invalid Token");
        }
    }
    public Claims extractAllClaims(String token){
      return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
