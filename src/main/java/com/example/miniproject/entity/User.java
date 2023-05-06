package com.example.miniproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

//    public User(SignupRequestDto signupRequestDto) {
//        this.userId = signupRequestDto.getUserId();
//        this.password = signupRequestDto.getPassword();
//    }
    public User(String userId, String password){
        this.userId = userId;
        this.password = password;
    }
}
