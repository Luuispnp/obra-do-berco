package com.github.luuispnp.obra_do_berco_solicitacoes.mapper;

import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoUpdateRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.response.SolicitacaoResponse;
import com.github.luuispnp.obra_do_berco_solicitacoes.entity.Solicitacao;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {

    Solicitacao toEntity(SolicitacaoRequest request);

    SolicitacaoResponse toResponse(Solicitacao solicitacao);

    List<SolicitacaoResponse> toResponseList(List<Solicitacao> solicitacoes);

    void update(SolicitacaoUpdateRequest request, @MappingTarget Solicitacao solicitacao);

}
