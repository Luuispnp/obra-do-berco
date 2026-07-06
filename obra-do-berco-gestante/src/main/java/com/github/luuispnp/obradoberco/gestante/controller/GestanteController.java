package com.github.luuispnp.obradoberco.gestante.controller;

import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequest;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponse;
import com.github.luuispnp.obradoberco.gestante.service.GestanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GestanteController {

    @Autowired
    GestanteService gestanteService;

    @PostMapping("/gestantes")
    public ResponseEntity<GestanteResponse> createGestante(@RequestBody GestanteRequest gestanteDTO) {
        return gestanteService.createGestante(gestanteDTO);
    }

    @GetMapping("/gestantes")
    public ResponseEntity<List<GestanteResponse>> getGestantes() {
        return gestanteService.getGestantes();
    }

    @GetMapping("/gestantes/{gestanteId}")
    public ResponseEntity<GestanteResponse> getGestanteById(@PathVariable Long gestanteId) {
        return gestanteService.getGestanteById(gestanteId);
    }

    @PutMapping("/gestantes/{gestanteId}")
    public ResponseEntity<GestanteResponse> updateGestante(@PathVariable Long gestanteId, @RequestBody GestanteRequest gestanteAtualizada) {
        return gestanteService.updateGestante(gestanteId, gestanteAtualizada);
    }

}
