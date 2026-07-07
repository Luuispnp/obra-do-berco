package com.github.luuispnp.obra_do_berco_voluntarios.service;

import com.github.luuispnp.obra_do_berco_voluntarios.dto.request.VoluntarioRequest;
import com.github.luuispnp.obra_do_berco_voluntarios.dto.response.VoluntarioResponse;
import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import com.github.luuispnp.obra_do_berco_voluntarios.enums.PerfilAcesso;
import com.github.luuispnp.obra_do_berco_voluntarios.exception.VoluntarioNotFoundException;
import com.github.luuispnp.obra_do_berco_voluntarios.mapper.VoluntarioMapper;
import com.github.luuispnp.obra_do_berco_voluntarios.repository.VoluntarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;

    private final VoluntarioMapper mapper;

//    private final PasswordEncoder encoder;

    @Transactional
    public VoluntarioResponse create(VoluntarioRequest request) {
        Voluntario voluntario = mapper.toEntity(request);
        voluntario.setDataCadastro(LocalDate.now());
//        voluntario.setSenha(encoder.encode(voluntario.getSenha()));
        voluntario.setSenha(request.senha());
        voluntario.setPerfil(PerfilAcesso.VOLUNTARIO);
        Voluntario voluntarioSalvo = voluntarioRepository.save(voluntario);
        return mapper.toResponse(voluntarioSalvo);
    }

    public List<VoluntarioResponse> findAll() {
        List<Voluntario> voluntarios = voluntarioRepository.findAll();
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
    public VoluntarioResponse deleteById(UUID voluntarioId) {
        Voluntario voluntario = voluntarioRepository.findById(voluntarioId)
                .orElseThrow(() -> new VoluntarioNotFoundException("Voluntário não encontrado."));
        voluntarioRepository.deleteById(voluntarioId);
        return mapper.toResponse(voluntario);
    }

    public List<VoluntarioResponse> searchByName(String name) {
        return voluntarioRepository
                .findByNomeCompletoContainingIgnoreCase(name)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
