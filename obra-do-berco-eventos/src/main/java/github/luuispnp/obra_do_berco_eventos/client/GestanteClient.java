package github.luuispnp.obra_do_berco_eventos.client;

import github.luuispnp.obra_do_berco_eventos.dto.response.GestanteResumoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "OBRA-DO-BERCO-GESTANTE")
public interface GestanteClient {

    @GetMapping("/gestantes/{id}")
    GestanteResumoResponse findById(@PathVariable("id") UUID id);

}
