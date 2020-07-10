package com.nx.jwt_project;

import io.jsonwebtoken.Claims;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertEquals;

public class JwtTest {

    private static final Logger logger = LogManager.getLogger();

    @Test
    public void createAndDecodeJWT() {

        String jwtId = "TestID1234";
        String jwtIssuer = "TestIssuer";
        String jwtSubject = "TestSubject";
        long jwtTimeToLive = 10_000L;

        Map<String, Object> myCustomClaims = new HashMap<>();
        myCustomClaims.put("myTestClaim1","valueTest1");
        myCustomClaims.put("myTestClaim2","valueTest2");

        //createJWT(String id, String issuer, String subject, long ttlMillis, Map<String, Object> customClaimsMap)

        String jwt = JWTManager.createJWT(
                jwtId, // claim = jti
                jwtIssuer, // claim = iss
                jwtSubject, // claim = sub
                jwtTimeToLive, // used to calculate expiration (claim = exp)
                myCustomClaims
        );

        logger.info("jwt = \"" + jwt.toString() + "\"");

        Claims claims = JWTManager.decodeJWT(jwt);

        logger.info("claims = " + claims.toString());

        assertEquals(jwtId, claims.getId());
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(jwtSubject, claims.getSubject());
        //assertEquals(jwtTimeToLive, claims.getExpiration());
        assertEquals("valueTest1", claims.get("myTestClaim1"));
        assertEquals("valueTest2", claims.get("myTestClaim2"));

    }

}
