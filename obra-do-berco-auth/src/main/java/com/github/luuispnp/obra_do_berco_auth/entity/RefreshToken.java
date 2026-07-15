package com.github.luuispnp.obra_do_berco_auth.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_refresh_token")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class RefreshToken {

    @Id
    @Column(name = "jti", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private UUID jti;

    @Column(name = "voluntario_id", nullable = false)
    private UUID voluntarioId;

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    private boolean revogado;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.revogado = false;
        this.criadoEm = LocalDateTime.now();
    }

}
