package com.ssong.form.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLogInError {
    private String email;
    private String password;
}