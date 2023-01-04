package com.example.webflux_jwt.models.reqResBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ReqLogin {
    private String email;
    private String password;


}
