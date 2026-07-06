package com.github.luuispnp.obradoberco.gestante.service;

import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequest;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponse;
import com.github.luuispnp.obradoberco.gestante.entity.Gestante;
import com.github.luuispnp.obradoberco.gestante.exception.GestanteNaoEncontradaException;
import com.github.luuispnp.obradoberco.gestante.mapper.GestanteMapper;
import com.github.luuispnp.obradoberco.gestante.repository.GestanteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GestanteService {

    private final GestanteRepository gestanteRepository;

    private final GestanteMapper gestanteMapper;

    public ResponseEntity<GestanteResponse> createGestante(GestanteRequest gestanteRequest) {
        Gestante gestante = gestanteMapper.gestanteRequestToEntity(gestanteRequest);
        Gestante gestanteSalva = gestanteRepository.save(gestante);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GestanteResponse(gestanteSalva));
    }

    public ResponseEntity<List<GestanteResponse>> getGestantes() {
            List<Gestante> gestantes = gestanteRepository.findAll();
            List<GestanteResponse> gestanteResponses = gestantes.stream()
                    .map(GestanteResponse::new)
                    .toList();
            return ResponseEntity.ok(gestanteResponses);
    }

    public ResponseEntity<GestanteResponse> getGestanteById(Long gestanteId) {
        Gestante gestante = gestanteRepository.findById(gestanteId)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada"));
        return ResponseEntity.ok(new GestanteResponse(gestante));
    }

    public ResponseEntity<GestanteResponse> updateGestante(Long gestanteId, GestanteRequest gestanteAtualizada) {
        Gestante gestanteExistente = gestanteRepository.findById(gestanteId)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada"));
        gestanteMapper.updateGestanteFromGestanteRequest(gestanteAtualizada, gestanteExistente);
        Gestante gestanteSalva = gestanteRepository.save(gestanteExistente);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GestanteResponse(gestanteExistente));
    }
}
