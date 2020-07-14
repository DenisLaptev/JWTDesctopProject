package com.nx.jwt_project.git_example;

import io.jsonwebtoken.*;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JWTHSManager {


    public static String createJwsString(Key key,String subject,long expirationTimeMinutes) {

        Instant issuedAtSent = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expirationSent = issuedAtSent.plus(expirationTimeMinutes, ChronoUnit.MINUTES);

        Date issuedAtDateSent = Date.from(issuedAtSent);
        Date expirationDateSent = Date.from(expirationSent);

        System.out.println("~~~~~~~~~~~~~~~~~~~");
        System.out.println("issuedAtSent=" + issuedAtSent);
        System.out.println("expirationSent=" + expirationSent);
        System.out.println("~~~~~~~~~~~~~~~~~~~");
        System.out.println("issuedAtDateSent=" + issuedAtDateSent);
        System.out.println("expirationDateSent=" + expirationDateSent);
        System.out.println("~~~~~~~~~~~~~~~~~~~");

        // We need a signing key, so we'll create one just for this example. Usually
        // the key would be read from your application configuration instead.
        //Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512

//        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256); //or RS384, RS512, PS256, PS384, PS512, ES256, ES384, ES512

//        System.out.println("key=" + key.getAlgorithm());
//        System.out.println("keyPair.getPublic()=" + keyPair.getPublic());
//        System.out.println("keyPair.getPrivate()=" + keyPair.getPrivate());

        String jwsString = Jwts.builder() // (1) create a JwtBuilder instance

                .setSubject(subject)       // (2) add header parameters and claims as desired

                .setIssuedAt(issuedAtDateSent)

                .setExpiration(expirationDateSent)

                .signWith(key)           // (3) Specify the SecretKey or asymmetric PrivateKey you want to use to sign the JWT

                .compact();             // (4) compact and sign, producing the final jws

        assert Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwsString).getBody().getSubject().equals("Joe");

        return jwsString;
    }

    public static Jws<Claims> readJwsFromJwsString(Key key, String jwsString, long clockSkewMinutes, Clock myClock) {
    //public static Jws<Claims> readJwsFromJwsString(Key key, String jwsString, long clockSkewMinutes) {

        Jws<Claims> jws = null;

        try {
            jws = Jwts.parserBuilder()          // (1)  create a JwtParserBuilder instance

                    //.requireSubject("Joe")      //(1.5) require that the JWS you are parsing has a specific sub (subject) value, otherwise you may not trust the token

                    .setAllowedClockSkewSeconds(clockSkewMinutes * 60) // account for differences between clocks on the parsing machine and the clock on the machine that created the JWT (usually no more than a few minutes)

                    .setClock(myClock)        // Custom Clock with an implementation of the io.jsonwebtoken.Clock interface

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
