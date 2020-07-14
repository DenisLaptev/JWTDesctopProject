package com.nx.jwt_project.git_example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JwtHSTest {

    private static final Logger logger = LogManager.getLogger();

    private static final String SECRET_KEY = "secret_2020_secret_topSecret2020_super_high_level_password2020";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private static final String SUBJECT = "Joe";

    private static final long CLOCK_SKEW_MINUTES = 3L;
    private static final long EXPIRATION_TIME_MINUTES = 5L;

    public static final long TIME_SHIFT_OF_TIME_ZONE = +8L;
    public static final long PLUS_TIME_CORRECTION = -8L;
    public static final long WRONG_PLUS_TIME_CORRECTION = -7L;

    @Test
    public void getNullJwsBecauseDifferentTimeZone() {

        String jwsString = JWTHSManager.createJwsString(key, SUBJECT, EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);
        logger.info("jwsString = " + jwsString );

        System.out.println("----------------------------------------------");

        Clock myClock = new MyClock(TIME_SHIFT_OF_TIME_ZONE, WRONG_PLUS_TIME_CORRECTION);

        Jws<Claims> jws = JWTHSManager.readJwsFromJwsString(key, jwsString, CLOCK_SKEW_MINUTES, myClock);

        assertNull(jws);
        System.out.println("jws=" + jws);
        logger.info("jws = " + jws );

        System.out.println("----------------------------------------------");
    }

    @Test
    public void getCorrectWorkWithDifferentTimeZones() {

        String jwsString = JWTHSManager.createJwsString(key,SUBJECT, EXPIRATION_TIME_MINUTES);

        System.out.println("jwsString=" + jwsString);
        logger.info("jwsString = " + jwsString );

        System.out.println("----------------------------------------------");

        Clock myClock = new MyClock(TIME_SHIFT_OF_TIME_ZONE, PLUS_TIME_CORRECTION);

        Jws<Claims> jws = JWTHSManager.readJwsFromJwsString(key, jwsString, CLOCK_SKEW_MINUTES, myClock);

        System.out.println("jws=" + jws);
        logger.info("jws = " + jws );

        System.out.println("----------------------------------------------");

        String jwtSubject = SUBJECT;

        assertEquals(jwtSubject, jws.getBody().getSubject());
    }

}
