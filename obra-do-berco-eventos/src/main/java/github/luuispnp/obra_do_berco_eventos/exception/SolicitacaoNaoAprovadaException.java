package github.luuispnp.obra_do_berco_eventos.exception;

public class SolicitacaoNaoAprovadaException extends RuntimeException {
    public SolicitacaoNaoAprovadaException() {
        super("Somente solicitações aprovadas podem ser vinculadas a um evento.");
    }
}
