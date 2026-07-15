package github.luuispnp.obra_do_berco_eventos.exception;

public class EventoNaoEncontradoException extends RuntimeException {
    public EventoNaoEncontradoException() {
        super("Evento não encontrado.");
    }
}
