package com.example.api_server.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Pattern.Flag;

import com.example.api_server.persistence.*;
import com.example.api_server.validation.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonView(value = {UserScope.UserSummary.class, UserScope.External.class})
    private Integer id;

    @JsonView(value = {UserScope.UserSummary.class, UserScope.External.class, UserMethod.Post.class})
    @NotNull(message = "Username is required.", groups = UserMethod.Post.class)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{7,29}$", flags = {Flag.CASE_INSENSITIVE, Flag.MULTILINE}, message="Invalid username", groups = UserMethod.Post.class)
    @Column(unique=true)
    private String username;

    // @JsonView(value = {UserScope.UserDetail.class, UserScope.Internal.class})
    private String entityKeyHash;

    @JsonView(value = {UserMethod.Post.class, UserMethod.Put.class})
    @NotBlank(message = "Password is required.", groups = UserMethod.Post.class)
    @NullOrNotBlank(message = "Password is required.", groups = UserMethod.Put.class)
    @Transient
    private String password;

    @JsonView(value = {UserScope.UserSummary.class, UserScope.External.class, UserMethod.Post.class, UserMethod.Put.class})
    @NotBlank(message = "First name is required.", groups = UserMethod.Post.class)
    @NullOrNotBlank(message = "First name is required.", groups = UserMethod.Put.class)
    private String firstName;

    @JsonView(value = {UserScope.UserSummary.class, UserScope.External.class, UserMethod.Post.class, UserMethod.Put.class})
    @NotBlank(message = "Last name is required.", groups = UserMethod.Post.class)
    @NullOrNotBlank(message = "Last name is required.", groups = UserMethod.Put.class)
    private String lastName;

    @JsonView(value = {UserScope.UserSummary.class, UserScope.External.class, UserMethod.Post.class})
    @NotBlank(message = "Email is required", groups = UserMethod.Post.class)
    @Email(message="Invalid email", groups = UserMethod.Post.class)
    @Column(unique=true)
    private String email;

    @JsonView(value = {UserScope.UserDetail.class, UserScope.Internal.class, UserMethod.Post.class})
    @NotBlank(message = "Phone number is required.", groups = UserMethod.Post.class)
    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", flags = {Flag.CASE_INSENSITIVE, Flag.MULTILINE}, message="Invalid Phone Number")
    private String phoneNumber;

    @JsonView(value = {UserScope.UserDetail.class, UserScope.Internal.class, UserMethod.Post.class})
    private LocalDate createdDate;

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }
    
    public void setEntityKeyHash(String entityKeyHash){
        this.entityKeyHash = entityKeyHash;
    }

    public String getEntityKeyHash(){
        return this.entityKeyHash;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return this.lastName;
    }
    
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public void setCreatedDate(LocalDate createdDate){
        this.createdDate = createdDate;
    }

    public LocalDate getCreatedDate(){
        return this.createdDate;
    }   

    public String toString(){
        return "{" +
            "\"id\": \""+this.id+"\"," +
            "\"username\": \""+this.username+"\"," +
            "\"entityKeyHash\": \""+this.entityKeyHash+"\"," +
            "\"firstName\": \""+this.firstName+"\"," +
            "\"lastName\": \""+this.lastName+"\"," +
            "\"phoneNumber\": \""+this.phoneNumber+"\"," +
            "\"email\": \""+this.email+"\"" +
            "}";
    }
}
