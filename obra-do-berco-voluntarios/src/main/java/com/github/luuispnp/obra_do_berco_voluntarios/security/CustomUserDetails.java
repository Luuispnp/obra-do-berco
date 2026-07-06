package com.github.luuispnp.obra_do_berco_voluntarios.security;

import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Voluntario voluntario;

    public CustomUserDetails(Voluntario voluntario) {
        this.voluntario = voluntario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + voluntario.getPerfil())
        );
    }

    @Override
    public @Nullable String getPassword() {
        return voluntario.getSenha();
    }

    @Override
    public String getUsername() {
        return voluntario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
