package com.susana.auth.services;

import com.susana.auth.dto.LoginRequest;
import com.susana.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}
