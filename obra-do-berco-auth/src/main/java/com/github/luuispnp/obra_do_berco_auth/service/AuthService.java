package com.github.luuispnp.obra_do_berco_auth.service;

import com.github.luuispnp.obra_do_berco_auth.client.VoluntarioClient;
import com.github.luuispnp.obra_do_berco_auth.dto.request.LoginRequest;
import com.github.luuispnp.obra_do_berco_auth.dto.request.VoluntarioAutenticacaoRequest;
import com.github.luuispnp.obra_do_berco_auth.dto.response.LoginResponse;
import com.github.luuispnp.obra_do_berco_auth.dto.response.RefreshResponse;
import com.github.luuispnp.obra_do_berco_auth.dto.response.UsuarioResponse;
import com.github.luuispnp.obra_do_berco_auth.dto.response.VoluntarioAutenticacaoResponse;
import com.github.luuispnp.obra_do_berco_auth.dto.response.VoluntarioResponse;
import com.github.luuispnp.obra_do_berco_auth.entity.RefreshToken;
import com.github.luuispnp.obra_do_berco_auth.exception.CredenciaisInvalidasException;
import com.github.luuispnp.obra_do_berco_auth.exception.RefreshTokenInvalidoException;
import com.github.luuispnp.obra_do_berco_auth.exception.SessaoInvalidaException;
import com.github.luuispnp.obra_do_berco_auth.repository.RefreshTokenRepository;
import com.github.luuispnp.obra_do_berco_auth.security.JwtService;
import feign.FeignException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final VoluntarioClient voluntarioClient;

    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        VoluntarioAutenticacaoResponse voluntario = autenticarNoVoluntariosService(request.email(), request.senha());

        String role = voluntario.perfil().toLowerCase();
        String accessToken = jwtService.gerarAccessToken(voluntario.voluntarioId(), voluntario.email(), role);

        JwtService.RefreshTokenGerado refreshTokenGerado = jwtService.gerarRefreshToken(voluntario.voluntarioId());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setJti(refreshTokenGerado.jti());
        refreshToken.setVoluntarioId(voluntario.voluntarioId());
        refreshToken.setExpiraEm(refreshTokenGerado.expiraEm());
        refreshTokenRepository.save(refreshToken);

        UsuarioResponse user = new UsuarioResponse(
                voluntario.voluntarioId(),
                voluntario.nomeCompleto(),
                voluntario.email(),
                role
        );

        return new LoginResponse(
                accessToken,
                refreshTokenGerado.token(),
                jwtService.getAccessExpirationSeconds(),
                user
        );
    }

    private VoluntarioAutenticacaoResponse autenticarNoVoluntariosService(String email, String senha) {
        try {
            return voluntarioClient.autenticar(new VoluntarioAutenticacaoRequest(email, senha));
        } catch (FeignException.Unauthorized | FeignException.NotFound exception) {
            throw new CredenciaisInvalidasException();
        }
    }

    @Transactional
    public RefreshResponse refresh(String refreshTokenValue) {
        Claims claims = parseRefreshToken(refreshTokenValue);

        UUID jti = UUID.fromString(claims.getId());
        RefreshToken refreshToken = refreshTokenRepository.findById(jti)
                .orElseThrow(RefreshTokenInvalidoException::new);

        if (refreshToken.isRevogado() || refreshToken.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenInvalidoException();
        }

        UUID voluntarioId = UUID.fromString(claims.getSubject());
        VoluntarioResponse voluntario = buscarVoluntario(voluntarioId);
        String role = voluntario.perfil().toLowerCase();

        String accessToken = jwtService.gerarAccessToken(voluntarioId, voluntario.email(), role);
        return new RefreshResponse(accessToken, jwtService.getAccessExpirationSeconds());
    }

    @Transactional
    public void logout(String refreshTokenValue) {
        Claims claims = parseRefreshToken(refreshTokenValue);
        UUID jti = UUID.fromString(claims.getId());

        refreshTokenRepository.findById(jti).ifPresent(refreshToken -> {
            refreshToken.setRevogado(true);
            refreshTokenRepository.save(refreshToken);
        });
    }

    public UsuarioResponse me(String accessToken) {
        Claims claims;
        try {
            claims = jwtService.parseToken(accessToken);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new SessaoInvalidaException();
        }

        UUID voluntarioId = UUID.fromString(claims.getSubject());
        VoluntarioResponse voluntario = buscarVoluntario(voluntarioId);

        return new UsuarioResponse(
                voluntarioId,
                voluntario.nomeCompleto(),
                voluntario.email(),
                voluntario.perfil().toLowerCase()
        );
    }

    private VoluntarioResponse buscarVoluntario(UUID voluntarioId) {
        try {
            return voluntarioClient.findById(voluntarioId);
        } catch (FeignException.NotFound exception) {
            throw new SessaoInvalidaException();
        }
    }

    private Claims parseRefreshToken(String refreshTokenValue) {
        Claims claims;
        try {
            claims = jwtService.parseToken(refreshTokenValue);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new RefreshTokenInvalidoException();
        }

        if (!"refresh".equals(claims.get("typ", String.class))) {
            throw new RefreshTokenInvalidoException();
        }

        return claims;
    }

}
