package com.nx.jwt_project.git_jjwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;

import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JwtHSTest {

//    private static final Logger logger = LogManager.getLogger();

    //Key key for HS algorithm
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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

        String jwsString = JWTManager.createJwsString(key, SUBJECT, issuedAt, expiration, EXPIRATION_TIME_MINUTES);
        System.out.println("jwsString=" + jwsString);

        System.out.println("----------------------------------------------");

        System.out.println("Client:");

        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.ofOffset("GMT", ZoneOffset.ofHours(5))));

        Instant clientUTC = Instant.now();
        System.out.println("client utc: " + clientUTC);

        Date clientDate = new Date();
        System.out.println("client local date: " + clientDate);


        Jws<Claims> jws = JWTManager.readJwsFromJwsString(key, jwsString, CLOCK_SKEW_MINUTES);
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

        String jwsString = JWTManager.createJwsString(key, SUBJECT, issuedAt, expiration, EXPIRATION_TIME_MINUTES);

        Jws<Claims> jws = JWTManager.readJwsFromJwsString(key, jwsString, CLOCK_SKEW_MINUTES);

        System.out.println("Ecpected issuedAt:"+issuedAt);
        System.out.println("Actual issuedAt:"+jws.getBody().getIssuedAt().toInstant());

        System.out.println("----------------------------------------------");

        System.out.println("Ecpected expiration:"+expiration);
        System.out.println("Actual expiration:"+jws.getBody().getExpiration().toInstant());

        assertThat(jws.getBody().getIssuedAt().toInstant(), is(issuedAt));
        assertThat(jws.getBody().getExpiration().toInstant(), is(expiration));
    }


}
