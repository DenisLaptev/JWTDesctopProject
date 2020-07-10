package com.nx.jwt_project;

import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, Worlds!");

        String id = "11";
        String issuer = "someIssuer";
        String subject = "top secret subject";
        long ttlMillis = 10_000L;

        Map<String, Object> myClaims = new HashMap<>();
        myClaims.put("myClaim1","value1");
        myClaims.put("myClaim2","value2");

        String jwtString = JWTManager.createJWT(id, issuer, subject, ttlMillis,myClaims);

        System.out.println("jwtString=" + jwtString);

        Claims claims = JWTManager.decodeJWT(jwtString);

        System.out.println("claims=" + claims);

        System.out.println("claims.get(\"myClaim1\")=" + claims.get("myClaim1"));
    }
}
