package com.nx.jwt_project.git_jjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.security.KeyPair;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class MainRS {

    //KeyPair KEY_PAIR = (KEY_PRIVATE, KEY_PUBLIC) for RS algorithm
    public static final KeyPair KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS256); //or RS384, RS512, PS256, PS384, PS512, ES256, ES384, ES512

    public static final Key KEY_PRIVATE = KEY_PAIR.getPrivate();
    public static final Key KEY_PUBLIC = KEY_PAIR.getPublic();

    private static final String SUBJECT = "Joe";

    private static final long CLOCK_SKEW_MINUTES = 3L;
    private static final long EXPIRATION_TIME_MINUTES = 5L;

    public static void main(String[] args) {

        Instant issuedAtSent = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expirationSent = issuedAtSent.plus(EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES);

        Date issuedAtDateSent = Date.from(issuedAtSent);
        Date expirationDateSent = Date.from(expirationSent);

        System.out.println("~~~~~~~~~~~~~~~~~~~");
        System.out.println("issuedAtSent=" + issuedAtSent);
        System.out.println("expirationSent=" + expirationSent);
        System.out.println("~~~~~~~~~~~~~~~~~~~");
        System.out.println("issuedAtDateSent=" + issuedAtDateSent);
        System.out.println("expirationDateSent=" + expirationDateSent);
        System.out.println("~~~~~~~~~~~~~~~~~~~");

        //KEY_PRIVATE
        String jwsString = JWTManager.createJwsString(KEY_PRIVATE, SUBJECT, issuedAtSent, expirationSent, EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);

        System.out.println("----------------------------------------------");

        //KEY_PUBLIC
        Jws<Claims> jws = JWTManager.readJwsFromJwsString(KEY_PUBLIC, jwsString, CLOCK_SKEW_MINUTES);

        System.out.println("jws=" + jws);

        System.out.println("----------------------------------------------");

        if (jws != null) {

            Date issuedAtDateReceived = jws.getBody().getIssuedAt();
            Date expirationDateReceived = jws.getBody().getExpiration();

            System.out.println("~~~~~~~~~~~~~~~~~~~");
            System.out.println("issuedAtDateReceived=" + issuedAtDateReceived);
            System.out.println("expirationDateReceived=" + expirationDateReceived);
            System.out.println("~~~~~~~~~~~~~~~~~~~");
        }

    }
}
