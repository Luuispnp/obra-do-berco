package com.github.luuispnp.obra_do_berco_voluntarios.config;

import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import com.github.luuispnp.obra_do_berco_voluntarios.enums.PerfilAcesso;
import com.github.luuispnp.obra_do_berco_voluntarios.repository.VoluntarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements ApplicationRunner {

    private final VoluntarioRepository voluntarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.seed.email:}")
    private String adminEmail;

    @Value("${admin.seed.senha:}")
    private String adminSenha;

    @Value("${admin.seed.nome:Administrador}")
    private String adminNome;

    @Value("${admin.seed.telefone:(00) 00000-0000}")
    private String adminTelefone;

    @Override
    public void run(ApplicationArguments args) {
        if (adminEmail.isBlank() || adminSenha.isBlank()) {
            log.warn("admin.seed.email/admin.seed.senha não configurados — nenhum admin foi criado.");
            return;
        }

        if (voluntarioRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        Voluntario admin = new Voluntario();
        admin.setNomeCompleto(adminNome);
        admin.setEmail(adminEmail);
        admin.setSenha(passwordEncoder.encode(adminSenha));
        admin.setTelefone(adminTelefone);
        admin.setPerfil(PerfilAcesso.ADMIN);
        voluntarioRepository.save(admin);

        log.info("Admin padrão criado: {}", adminEmail);
    }

}
