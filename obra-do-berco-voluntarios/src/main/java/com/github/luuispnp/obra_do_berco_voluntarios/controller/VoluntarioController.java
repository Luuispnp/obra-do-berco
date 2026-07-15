package com.github.luuispnp.obra_do_berco_voluntarios.controller;

import com.github.luuispnp.obra_do_berco_voluntarios.dto.request.VoluntarioAutenticacaoRequest;
import com.github.luuispnp.obra_do_berco_voluntarios.dto.request.VoluntarioRequest;
import com.github.luuispnp.obra_do_berco_voluntarios.dto.response.VoluntarioAutenticacaoResponse;
import com.github.luuispnp.obra_do_berco_voluntarios.dto.response.VoluntarioResponse;
import com.github.luuispnp.obra_do_berco_voluntarios.service.VoluntarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/voluntarios")
@RequiredArgsConstructor
@Validated
public class VoluntarioController {

    private final VoluntarioService voluntarioService;

    @PostMapping
    public ResponseEntity<VoluntarioResponse> create(@RequestBody @Valid VoluntarioRequest request) {
        VoluntarioResponse response = voluntarioService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<VoluntarioResponse>> findVoluntarios(
            @RequestParam(required = false) String nomeCompleto,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean ativo
    ) {
        List<VoluntarioResponse> response = voluntarioService.findWithFilter(nomeCompleto, email, ativo);
        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoluntarioResponse> findById(@PathVariable UUID id) {
        VoluntarioResponse response = voluntarioService.findById(id);
        return ResponseEntity
                .ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoluntarioResponse> updateById(@PathVariable UUID id, @RequestBody @Valid VoluntarioRequest voluntarioRequest) {
        VoluntarioResponse response = voluntarioService.updateById(id, voluntarioRequest);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<VoluntarioResponse> inactivateVoluntario(@PathVariable UUID id) {
        VoluntarioResponse response = voluntarioService.inactivateVoluntario(id);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<VoluntarioResponse> reactivateVoluntario(@PathVariable UUID id) {
        VoluntarioResponse response = voluntarioService.reactivateVoluntario(id);
        return ResponseEntity
                .ok(response);
    }

    @PostMapping("/autenticar")
    public ResponseEntity<VoluntarioAutenticacaoResponse> autenticar(@RequestBody @Valid VoluntarioAutenticacaoRequest request) {
        VoluntarioAutenticacaoResponse response = voluntarioService.autenticar(request.email(), request.senha());
        return ResponseEntity
                .ok(response);
    }

}
