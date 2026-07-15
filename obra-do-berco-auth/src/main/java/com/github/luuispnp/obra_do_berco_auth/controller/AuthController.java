package com.github.luuispnp.obra_do_berco_auth.controller;

import com.github.luuispnp.obra_do_berco_auth.dto.request.LoginRequest;
import com.github.luuispnp.obra_do_berco_auth.dto.request.LogoutRequest;
import com.github.luuispnp.obra_do_berco_auth.dto.request.RefreshRequest;
import com.github.luuispnp.obra_do_berco_auth.dto.response.LoginResponse;
import com.github.luuispnp.obra_do_berco_auth.dto.response.RefreshResponse;
import com.github.luuispnp.obra_do_berco_auth.dto.response.UsuarioResponse;
import com.github.luuispnp.obra_do_berco_auth.exception.SessaoInvalidaException;
import com.github.luuispnp.obra_do_berco_auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity
                .ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        RefreshResponse response = authService.refresh(request.refreshToken());
        return ResponseEntity
                .ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(HttpServletRequest httpRequest) {
        String accessToken = extrairToken(httpRequest);
        UsuarioResponse response = authService.me(accessToken);
        return ResponseEntity
                .ok(response);
    }

    private String extrairToken(HttpServletRequest httpRequest) {
        String header = httpRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new SessaoInvalidaException();
        }
        return header.substring("Bearer ".length());
    }

}
