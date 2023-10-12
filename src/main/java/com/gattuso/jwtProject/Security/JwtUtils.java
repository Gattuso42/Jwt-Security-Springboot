package com.gattuso.jwtProject.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {
    private final String secretKey = "03f69902583e16ab2427697ea4d09e697abf92b3929689a959987e1840739e9d";
    private final String expirationTime = "60000";

    //Generate token
    public String generateAccessToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(expirationTime)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    //Validate token
    public boolean isTokenValid(String token) throws ExpiredJwtException {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }
        catch (ExpiredJwtException ex){
            System.out.println(ex.getLocalizedMessage());
            System.out.println("1");
            return false;
        }
        catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            System.out.println("2");
            return false;
        }
    }

    //Get all token claims
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Get just one claim
    public <T> T getClaim(String token, Function<Claims,T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    //Get username claim from token
    public String getUsernameFromToken(String token){
        return getClaim(token,Claims::getSubject);
    }

    //Generate sign key
    public Key getSignatureKey(){
        byte[]keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
