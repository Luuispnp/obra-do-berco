package com.github.luuispnp.obradoberco.gestante.service;

import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequest;
import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequestUpdate;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponse;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponseById;
import com.github.luuispnp.obradoberco.gestante.entity.Gestante;
import com.github.luuispnp.obradoberco.gestante.exception.GestanteNaoEncontradaException;
import com.github.luuispnp.obradoberco.gestante.mapper.GestanteMapper;
import com.github.luuispnp.obradoberco.gestante.repository.GestanteRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GestanteService {

    private final GestanteRepository gestanteRepository;

    private final GestanteMapper mapper;

    @Transactional
    public GestanteResponse create(GestanteRequest request) {
        Gestante gestante = mapper.toEntity(request);
        Gestante gestanteSalva = gestanteRepository.save(gestante);
        return mapper.toResponse(gestanteSalva);
    }

    public List<GestanteResponse> findAllWithFilter(String nome, String cpf, String telefone) {
        Specification<Gestante> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome
                        .toLowerCase() + "%"));
            }
            if (cpf != null && !cpf.isBlank()) {
                predicates.add(cb.like(root.get("cpf"), "%" + cpf + "%"));
            }
            if (telefone != null && !telefone.isBlank()) {
                predicates.add(cb.like(root.get("telefone"), "%" + telefone + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<Gestante> gestantes = gestanteRepository.findAll(specification);
        return mapper.toResponseList(gestantes);
    }

    public GestanteResponseById findById(UUID id) {
        Gestante gestante = gestanteRepository.findById(id)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada."));
        return mapper.toResponseById(gestante);
    }

    @Transactional
    public GestanteResponse updateById(UUID id, GestanteRequestUpdate request) {
        Gestante gestante = gestanteRepository.findById(id)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada."));
        mapper.updateGestante(request, gestante);
        Gestante gestanteAtualizada = gestanteRepository.save(gestante);
        return mapper.toResponse(gestanteAtualizada);
    }

    @Transactional
    public GestanteResponse inactivateById(UUID id) {
        Gestante gestante = gestanteRepository.findById(id)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada."));
        gestante.setAtivo(false);
        Gestante gestanteSalva = gestanteRepository.save(gestante);
        return mapper.toResponse(gestanteSalva);
    }

    @Transactional
    public GestanteResponse activateById(UUID id) {
        Gestante gestante = gestanteRepository.findById(id)
                .orElseThrow(() -> new GestanteNaoEncontradaException("Gestante não encontrada."));
        gestante.setAtivo(true);
        Gestante gestanteSalva = gestanteRepository.save(gestante);
        return mapper.toResponse(gestanteSalva);
    }
}
