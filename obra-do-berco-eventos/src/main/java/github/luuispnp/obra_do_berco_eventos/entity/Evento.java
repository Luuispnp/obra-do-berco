package github.luuispnp.obra_do_berco_eventos.entity;

import github.luuispnp.obra_do_berco_eventos.enums.StatusEvento;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_evento")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "evento_id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private UUID eventoId;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "data_evento", nullable = false)
    private LocalDate dataEvento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_evento", nullable = false, length = 20)
    private StatusEvento status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_finalizacao")
    private LocalDateTime dataFinalizacao;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participante> participantes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.status = StatusEvento.CRIADO;
        this.dataCriacao = LocalDateTime.now();
    }

}
