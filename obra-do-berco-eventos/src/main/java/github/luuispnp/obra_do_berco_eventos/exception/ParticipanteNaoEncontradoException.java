package github.luuispnp.obra_do_berco_eventos.exception;

public class ParticipanteNaoEncontradoException extends RuntimeException {
    public ParticipanteNaoEncontradoException() {
        super("Participante não encontrado.");
    }
}
