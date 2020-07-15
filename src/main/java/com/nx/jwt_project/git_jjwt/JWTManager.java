package com.nx.jwt_project.git_jjwt;

import io.jsonwebtoken.*;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JWTManager {

    public static String createJwsString(Key key, String subject, Instant issuedAt, Instant expiration, long expirationTimeMinutes) {

        Date issuedAtDateSent = Date.from(issuedAt);
        Date expirationDateSent = Date.from(expiration);

        String jwsString = Jwts.builder() // (1) create a JwtBuilder instance

                .setSubject(subject)      // (2) add header parameters and claims as desired

                .setIssuedAt(issuedAtDateSent)

                .setExpiration(expirationDateSent)

                .signWith(key)           // (3) Specify the SecretKey or asymmetric PrivateKey you want to use to sign the JWT

                .compact();              // (4) compact and sign, producing the final jws

        return jwsString;
    }

    public static Jws<Claims> readJwsFromJwsString(Key key, String jwsString, long clockSkewMinutes) {

        Jws<Claims> jws = null;

        try {
            jws = Jwts.parserBuilder()          // (1)  create a JwtParserBuilder instance

                    .requireSubject("Joe")      //(1.5) require that the JWS you are parsing has a specific sub (subject) value, otherwise you may not trust the token

                    .setAllowedClockSkewSeconds(clockSkewMinutes * 60) // account for differences between clocks on the parsing machine and the clock on the machine that created the JWT (usually no more than a few minutes)

                    .setSigningKey(key)         // (2)  Specify the SecretKey or asymmetric PublicKey you want to use to verify the JWS signature

                    .build()                    // (3)  return a thread-safe JwtParser

                    .parseClaimsJws(jwsString); // (4)  producing the original JWS

        } catch (MissingClaimException mce) {
            System.out.println("the parsed JWT did not have the sub field");
        } catch (IncorrectClaimException ice) {
            System.out.println("the parsed JWT had a sub field, but its value was not equal to 'Joe'");
        } catch (JwtException ex) {             // (5)  in case parsing or signature validation fails
            System.out.println("Exception!");
        }

        return jws;
    }
}
