package com.github.luuispnp.obra_do_berco_voluntarios.controller;

import com.github.luuispnp.obra_do_berco_voluntarios.dto.request.VoluntarioRequest;
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
    public ResponseEntity<List<VoluntarioResponse>> findAll() {
        List<VoluntarioResponse> responses = voluntarioService.findAll();
        return ResponseEntity
                .ok(responses);
    }

    @GetMapping("/{voluntarioId}")
    public ResponseEntity<VoluntarioResponse> findById(@PathVariable UUID voluntarioId) {
        VoluntarioResponse response = voluntarioService.findById(voluntarioId);
        return ResponseEntity
                .ok(response);
    }

}
