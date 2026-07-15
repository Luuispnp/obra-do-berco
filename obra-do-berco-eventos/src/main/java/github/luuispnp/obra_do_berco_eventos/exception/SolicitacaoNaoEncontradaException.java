package github.luuispnp.obra_do_berco_eventos.exception;

public class SolicitacaoNaoEncontradaException extends RuntimeException {
    public SolicitacaoNaoEncontradaException() {
        super("Solicitação não encontrada.");
    }
}
