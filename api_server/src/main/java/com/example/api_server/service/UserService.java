package com.example.api_server.service;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api_server.model.*;
import com.example.api_server.repository.*;

@Service
public class UserService extends BaseService<User>{
    private UserRepository userRepo;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public User create(User user) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException{
        user.setCreatedDate(LocalDate.now());
        user.setEntityKeyHash(CipherService.generateEntityKeyHash(user.getPassword()));
        return this.userRepo.save(user);
    }     

    @SuppressWarnings("null")
    public User update(Integer id, User user){
        User updateUser = this.userRepo.findById(id).get();
        if(Objects.isNull(updateUser)) return null;
        updateUser = this.mapData(user, updateUser);
        logger.info("user::" + user);
        logger.info("updateUser::" + updateUser);
        return this.userRepo.save(updateUser);
    }

    public Iterable<User> getUsers(){
        return this.userRepo.findAll();
    }
}
