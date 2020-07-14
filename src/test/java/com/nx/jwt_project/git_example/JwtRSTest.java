package com.nx.jwt_project.git_example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JwtRSTest {

    private static final Logger logger = LogManager.getLogger();

    public static final KeyPair KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS256); //or RS384, RS512, PS256, PS384, PS512, ES256, ES384, ES512

    public static final Key KEY_PRIVATE = KEY_PAIR.getPrivate();
    public static final Key KEY_PUBLIC = KEY_PAIR.getPublic();

    private static final String SUBJECT = "Joe";

    private static final long CLOCK_SKEW_MINUTES = 3L;
    private static final long EXPIRATION_TIME_MINUTES = 5L;

    public static final long TIME_SHIFT_OF_TIME_ZONE = +8L;
    public static final long PLUS_TIME_CORRECTION = -8L;
    public static final long WRONG_PLUS_TIME_CORRECTION = -7L;

    @Test
    public void getNullJwsBecauseDifferentTimeZone() {
        //KEY_PRIVATE
        String jwsString = JWTHSManager.createJwsString(KEY_PRIVATE, SUBJECT, EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);
        logger.info("jwsString = " + jwsString );

        System.out.println("----------------------------------------------");

        Clock myClock = new MyClock(TIME_SHIFT_OF_TIME_ZONE, WRONG_PLUS_TIME_CORRECTION);

        //KEY_PUBLIC
        Jws<Claims> jws = JWTHSManager.readJwsFromJwsString(KEY_PUBLIC, jwsString, CLOCK_SKEW_MINUTES, myClock);

        assertNull(jws);
        System.out.println("jws=" + jws);
        logger.info("jws = " + jws );

        System.out.println("----------------------------------------------");
    }

    @Test
    public void getCorrectWorkWithDifferentTimeZones() {

        //KEY_PRIVATE
        String jwsString = JWTHSManager.createJwsString(KEY_PRIVATE,SUBJECT, EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);
        logger.info("jwsString = " + jwsString );

        System.out.println("----------------------------------------------");

        Clock myClock = new MyClock(TIME_SHIFT_OF_TIME_ZONE, PLUS_TIME_CORRECTION);

        //KEY_PUBLIC
        Jws<Claims> jws = JWTHSManager.readJwsFromJwsString(KEY_PUBLIC, jwsString, CLOCK_SKEW_MINUTES, myClock);

        System.out.println("jws=" + jws);
        logger.info("jws = " + jws );

        System.out.println("----------------------------------------------");

        String jwtSubject = SUBJECT;

        assertEquals(jwtSubject, jws.getBody().getSubject());
    }

}
