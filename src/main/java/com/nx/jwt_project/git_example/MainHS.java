package com.nx.jwt_project.git_example;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class MainHS {

    private static final String SECRET_KEY = "secret_2020_secret_topSecret2020_super_high_level_password2020";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private static final String SUBJECT = "Joe";

    private static final long CLOCK_SKEW_MINUTES = 3L;
    private static final long EXPIRATION_TIME_MINUTES = 5L;

    public static final long TIME_SHIFT_OF_TIME_ZONE = +8L;
    public static final long PLUS_TIME_CORRECTION = -8L;


    public static void main(String[] args) {

        String jwsString = JWTHSManager.createJwsString(key, SUBJECT,EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);

        System.out.println("----------------------------------------------");

        Clock myClock = new MyClock(TIME_SHIFT_OF_TIME_ZONE, PLUS_TIME_CORRECTION);

        Jws<Claims> jws = JWTHSManager.readJwsFromJwsString(key, jwsString, CLOCK_SKEW_MINUTES, myClock);

        System.out.println("jws=" + jws);

        System.out.println("----------------------------------------------");

        if (jws != null) {
            Date issuedAtDateReceived = jws.getBody().getIssuedAt();
            Date expirationDateReceived = jws.getBody().getExpiration();

            System.out.println("issuedAtDateReceived=" + issuedAtDateReceived);
            System.out.println("expirationDateReceived=" + expirationDateReceived);
        }

    }
}
