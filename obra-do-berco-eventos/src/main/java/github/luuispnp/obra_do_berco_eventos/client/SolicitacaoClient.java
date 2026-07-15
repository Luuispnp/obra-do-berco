package github.luuispnp.obra_do_berco_eventos.client;

import github.luuispnp.obra_do_berco_eventos.dto.request.AtualizacaoStatusLoteRequest;
import github.luuispnp.obra_do_berco_eventos.dto.response.SolicitacaoResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.SolicitacaoResumoResponse;
import github.luuispnp.obra_do_berco_eventos.enums.StatusSolicitacao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "OBRA-DO-BERCO-SOLICITACOES")
public interface SolicitacaoClient {

    @PatchMapping("/solicitacoes/status-lote")
    void atualizarStatusLote(@RequestBody AtualizacaoStatusLoteRequest request);

    @GetMapping("/solicitacoes")
    List<SolicitacaoResumoResponse> findAllWithFilter(@RequestParam("status") StatusSolicitacao status);

    @GetMapping("/solicitacoes/{id}")
    SolicitacaoResponse findById(@PathVariable("id") UUID id);

}
