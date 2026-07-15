package github.luuispnp.obra_do_berco_eventos.exception;

public class SolicitacaoJaVinculadaException extends RuntimeException {
    public SolicitacaoJaVinculadaException() {
        super("Solicitação já vinculada a um evento em aberto.");
    }
}
