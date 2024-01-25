package com.example.api_server.service;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.api_server.model.*;
import com.example.api_server.repository.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;

    private String ISS = "application::chat::authenticate";
    private String ALGORITHM = "HS256";
    private int EXPIRE_IN = 3600000; //in milliseconds
    
    @Value("application.data.server_key")
    private String SERVER_KEY;

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    public Auth token(Auth auth){
        User user = userRepo.findByUsername(auth.getUsername());
        if(Objects.isNull(user)) throw new JWTCreationException("Invalid combination of username/password", null);
        Auth result = new Auth();
        result.setAccessToken(this.generateToken(user));
        return result;
    }

    public User authenticate(String token) throws JWTVerificationException{
        return this.verifyToken(token);
    }

    private String generateToken(User user){
        HashMap<String, Object> headers = new HashMap<String, Object>();  
        headers.put("alg", this.ALGORITHM);
        headers.put("typ", "JWT");
        Calendar expiredTime = Calendar.getInstance();
        expiredTime.add(Calendar.SECOND, this.EXPIRE_IN);
        return JWT.create()
                  .withHeader(headers)
                  .withIssuer(this.ISS)
                  .withClaim("id", user.getId())
                  .withClaim("username", user.getUsername())
                  .withClaim("email", user.getEmail())
                  .withClaim("firstName", user.getFirstName())
                  .withClaim("lastName", user.getLastName())
                  .withIssuedAt(new Date())
                  .withExpiresAt(expiredTime.getTime())
                  .sign(Algorithm.HMAC256(this.SERVER_KEY));
    }

    private User verifyToken(String token){
        logger.info("verifyToken::" + token);
        User user = new User();
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.SERVER_KEY))
                                  .build();
        DecodedJWT jwt = verifier.verify(token);
        user.setId(jwt.getClaim("id").asInt());
        user.setUsername(jwt.getClaim("username").asString());
        user.setEmail(jwt.getClaim("email").asString());
        user.setFirstName(jwt.getClaim("firstName").asString());
        user.setLastName(jwt.getClaim("lastName").asString());
        return user;        
    }

}
