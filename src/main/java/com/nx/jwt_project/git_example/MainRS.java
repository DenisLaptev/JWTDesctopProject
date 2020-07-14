package com.nx.jwt_project.git_example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.security.KeyPair;
import java.util.Date;

public class MainRS {

    public static final KeyPair KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS256); //or RS384, RS512, PS256, PS384, PS512, ES256, ES384, ES512


    public static final Key KEY_PRIVATE = KEY_PAIR.getPrivate();
    public static final Key KEY_PUBLIC = KEY_PAIR.getPublic();

    private static final String SUBJECT = "Joe";

    private static final long CLOCK_SKEW_MINUTES = 3L;
    private static final long EXPIRATION_TIME_MINUTES = 5L;

    public static final long TIME_SHIFT_OF_TIME_ZONE = +8L;
    public static final long PLUS_TIME_CORRECTION = -8L;


    public static void main(String[] args) {

        //KEY_PRIVATE
        String jwsString = JWTHSManager.createJwsString(KEY_PRIVATE, SUBJECT,EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);

        System.out.println("----------------------------------------------");

        Clock myClock = new MyClock(TIME_SHIFT_OF_TIME_ZONE, PLUS_TIME_CORRECTION);

        //KEY_PUBLIC
        Jws<Claims> jws = JWTHSManager.readJwsFromJwsString(KEY_PUBLIC, jwsString, CLOCK_SKEW_MINUTES, myClock);

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
