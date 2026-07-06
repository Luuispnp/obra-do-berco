package com.github.luuispnp.obra_do_berco_voluntarios.security;

import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import com.github.luuispnp.obra_do_berco_voluntarios.repository.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    VoluntarioRepository voluntarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Voluntario voluntario = voluntarioRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario não encontrado: " + username));
        return new CustomUserDetails(voluntario);
    }
}
