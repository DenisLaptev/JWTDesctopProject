package com.nx.jwt_project.git_jjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import java.security.Key;
import java.security.KeyPair;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JwtRSTest {

//    private static final Logger logger = LogManager.getLogger();

    //KeyPair KEY_PAIR = (KEY_PRIVATE, KEY_PUBLIC) for RS algorithm
    public static final KeyPair KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.RS256); //or RS384, RS512, PS256, PS384, PS512, ES256, ES384, ES512

    public static final Key KEY_PRIVATE = KEY_PAIR.getPrivate();
    public static final Key KEY_PUBLIC = KEY_PAIR.getPublic();

    private static final String SUBJECT = "Joe";

    private static final long CLOCK_SKEW_MINUTES = 3L;
    private static final long EXPIRATION_TIME_MINUTES = 5L;

    @Test
    public void getCorrectWorkWithDifferentTimeZones() {

        System.out.println("===========Test 1=============");
        System.out.println("Server has 1 hours GMT ZoneOffset");
        System.out.println("Client has 5 hours GMT ZoneOffset");

        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES);

        System.out.println("Server:");

        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.ofOffset("GMT", ZoneOffset.ofHours(1))));

        Instant serverUTC = Instant.now();
        System.out.println("server utc: " + serverUTC);

        Date serverDate = new Date();
        System.out.println("server local date: " + serverDate);

        //KEY_PRIVATE
        String jwsString = JWTManager.createJwsString(KEY_PRIVATE, SUBJECT, issuedAt, expiration, EXPIRATION_TIME_MINUTES);
        System.out.println("jwsString=" + jwsString);

        System.out.println("----------------------------------------------");

        System.out.println("Client:");

        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.ofOffset("GMT", ZoneOffset.ofHours(5))));

        Instant clientUTC = Instant.now();
        System.out.println("client utc: " + clientUTC);

        Date clientDate = new Date();
        System.out.println("client local date: " + clientDate);

        //KEY_PUBLIC
        Jws<Claims> jws = JWTManager.readJwsFromJwsString(KEY_PUBLIC, jwsString, CLOCK_SKEW_MINUTES);
        System.out.println("jws=" + jws);

        System.out.println("----------------------------------------------");

        String jwtSubject = SUBJECT;

        System.out.println("Ecpected jwtSubject:"+jwtSubject);
        System.out.println("Actual jwtSubject:"+jws.getBody().getSubject());

        System.out.println("----------------------------------------------");

        System.out.println("Ecpected issuedAt:"+issuedAt);
        System.out.println("Actual issuedAt:"+jws.getBody().getIssuedAt().toInstant());

        System.out.println("----------------------------------------------");

        System.out.println("Ecpected expiration:"+expiration);
        System.out.println("Actual expiration:"+jws.getBody().getExpiration().toInstant());

        assertEquals(jwtSubject, jws.getBody().getSubject());

        assertThat(jws.getBody().getIssuedAt().toInstant(), is(issuedAt));
        assertThat(jws.getBody().getExpiration().toInstant(), is(expiration));
    }


    @Test
    public void shouldMatchIssuedAtAndExpiration() {

        System.out.println("===========Test 2=============");

        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(EXPIRATION_TIME_MINUTES, ChronoUnit.MINUTES);

        //KEY_PRIVATE
        String jwsString = JWTManager.createJwsString(KEY_PRIVATE, SUBJECT, issuedAt, expiration, EXPIRATION_TIME_MINUTES);

        //KEY_PUBLIC
        Jws<Claims> jws = JWTManager.readJwsFromJwsString(KEY_PUBLIC, jwsString, CLOCK_SKEW_MINUTES);

        System.out.println("Ecpected issuedAt:"+issuedAt);
        System.out.println("Actual issuedAt:"+jws.getBody().getIssuedAt().toInstant());

        System.out.println("----------------------------------------------");

        System.out.println("Ecpected expiration:"+expiration);
        System.out.println("Actual expiration:"+jws.getBody().getExpiration().toInstant());

        assertThat(jws.getBody().getIssuedAt().toInstant(), is(issuedAt));
        assertThat(jws.getBody().getExpiration().toInstant(), is(expiration));
    }

}

