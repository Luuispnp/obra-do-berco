package com.github.luuispnp.obradoberco.gestante.controller;

import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequest;
import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequestUpdate;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponse;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponseById;
import com.github.luuispnp.obradoberco.gestante.service.GestanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gestantes")
@RequiredArgsConstructor
public class GestanteController {

    private final GestanteService gestanteService;

    @PostMapping
    public ResponseEntity<GestanteResponse> create(@RequestBody @Valid GestanteRequest request) {
        GestanteResponse response = gestanteService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<GestanteResponse>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String telefone
    ) {
        List<GestanteResponse> response = gestanteService.findAllWithFilter(nome, cpf, telefone);
        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GestanteResponseById> findById(@PathVariable UUID id) {
        GestanteResponseById response = gestanteService.findById(id);
        return ResponseEntity
                .ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GestanteResponse> updateById(@PathVariable UUID id, @RequestBody @Valid GestanteRequestUpdate request) {
        GestanteResponse response = gestanteService.updateById(id, request);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<GestanteResponse> inactivateById(@PathVariable UUID id) {
        GestanteResponse response = gestanteService.inactivateById(id);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<GestanteResponse> activateById(@PathVariable UUID id) {
        GestanteResponse response = gestanteService.activateById(id);
        return ResponseEntity
                .ok(response);
    }

}
