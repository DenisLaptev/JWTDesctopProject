package com.nx.jwt_project.git_jjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class MainHS {

    //Key key for HS algorithm
//    private static final String SECRET_KEY = "secret_2020_secret_topSecret2020_super_high_level_password2020";
//    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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

        String jwsString = JWTManager.createJwsString(key, SUBJECT, issuedAtSent, expirationSent, EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);

        System.out.println("----------------------------------------------");

        Jws<Claims> jws = JWTManager.readJwsFromJwsString(key, jwsString, CLOCK_SKEW_MINUTES);

        System.out.println("jws=" + jws);

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
