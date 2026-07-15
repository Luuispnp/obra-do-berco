package github.luuispnp.obra_do_berco_eventos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "tb_participante")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "evento")
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "participante_id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private UUID participanteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    @JsonIgnore
    private Evento evento;

    @Column(name = "solicitacao_id", nullable = false)
    private UUID solicitacaoId;

    @Column(name = "gestante_id", nullable = false)
    private UUID gestanteId;

    @Column(nullable = false)
    private Boolean presente = false;

    @PrePersist
    public void prePersist() {
        this.presente = false;
    }

}
