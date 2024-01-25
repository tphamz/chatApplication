package com.example.api_server.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(classes=CipherSerivceTest.class)
public class CipherSerivceTest {
    Logger logger = LoggerFactory.getLogger(CipherSerivceTest.class);
    @Test
    void entityKeyTest() throws Exception{
        String password = "Letmein";
        String keyHash = CipherService.generateEntityKeyHash(password); 
        logger.info("keyHash::" + keyHash);
        String newEntityKey = CipherService.decryptEntityKeyHash(password, keyHash);
        // logger.info("newEntityKey::" + CipherService.stringEncoded(newEntityKey.getBytes()));
        assertNotNull(newEntityKey);
    }
}
