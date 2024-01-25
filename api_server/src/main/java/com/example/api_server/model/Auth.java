package com.example.api_server.model;
import com.example.api_server.persistence.UserMethod;
import com.fasterxml.jackson.annotation.JsonView;


public class Auth {
    @JsonView(value =  {UserMethod.SignIn.class})
    private String username;
    @JsonView(value = {UserMethod.SignIn.class})
    private String password;
    @JsonView(value = {UserMethod.Token.class})
    private String accessToken;

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return this.accessToken;
    }
}
