package com.github.luuispnp.obra_do_berco_voluntarios.service;

import com.github.luuispnp.obra_do_berco_voluntarios.dto.request.VoluntarioRequest;
import com.github.luuispnp.obra_do_berco_voluntarios.dto.response.VoluntarioResponse;
import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import com.github.luuispnp.obra_do_berco_voluntarios.exception.VoluntarioNotFoundException;
import com.github.luuispnp.obra_do_berco_voluntarios.mapper.VoluntarioMapper;
import com.github.luuispnp.obra_do_berco_voluntarios.repository.VoluntarioRepository;
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
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;

    private final VoluntarioMapper mapper;

    @Transactional
    public VoluntarioResponse create(VoluntarioRequest request) {
        Voluntario voluntario = mapper.toEntity(request);
        Voluntario voluntarioSalvo = voluntarioRepository.save(voluntario);
        return mapper.toResponse(voluntarioSalvo);
    }

    public List<VoluntarioResponse> findWithFilter(String nomeCompleto, String email, Boolean ativo) {
        Specification<Voluntario> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nomeCompleto != null && !nomeCompleto.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nomeCompleto")), "%" + nomeCompleto.toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get("ativo"), ativo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Voluntario> voluntarios = voluntarioRepository.findAll(spec);
        return mapper.toResponseList(voluntarios);
    }

    public VoluntarioResponse findById(UUID voluntarioId) {
        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntário não encontrado"));
        return mapper.toResponse(voluntario);
    }

    @Transactional
    public VoluntarioResponse updateById(UUID voluntarioId, VoluntarioRequest voluntarioRequest) {
        Voluntario voluntarioExistente = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntário não encontrado."));
        mapper.updateVoluntario(voluntarioRequest, voluntarioExistente);
        Voluntario voluntarioAtualizado = voluntarioRepository.save(voluntarioExistente);
        return mapper.toResponse(voluntarioAtualizado);
    }

    @Transactional
    public VoluntarioResponse inactivateVoluntario(UUID id) {
        Voluntario voluntario = voluntarioRepository.findById(id)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntario não encontrado."));
        voluntario.setAtivo(false);
        Voluntario voluntarioAtualizado = voluntarioRepository.save(voluntario);
        return mapper.toResponse(voluntarioAtualizado);
    }

    @Transactional
    public VoluntarioResponse reactivateVoluntario(UUID id) {
        Voluntario voluntario = voluntarioRepository.findById(id)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntário não encontrado."));
        voluntario.setAtivo(true);
        Voluntario voluntarioAtualizado = voluntarioRepository.save(voluntario);
        return mapper.toResponse(voluntarioAtualizado);
    }
}
