package com.github.luuispnp.obra_do_berco_voluntarios.service;

import com.github.luuispnp.obra_do_berco_voluntarios.dto.request.LoginRequest;
import com.github.luuispnp.obra_do_berco_voluntarios.dto.response.LoginResponse;
import com.github.luuispnp.obra_do_berco_voluntarios.security.CustomUserDetails;
import com.github.luuispnp.obra_do_berco_voluntarios.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getSenha());
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}
