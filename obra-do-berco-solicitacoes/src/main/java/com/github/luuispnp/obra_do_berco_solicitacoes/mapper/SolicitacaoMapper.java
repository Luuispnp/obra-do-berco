package com.github.luuispnp.obra_do_berco_solicitacoes.mapper;

import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.entity.Solicitacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {

    @Mapping(target = "dataSolicitacao", ignore = true)
    @Mapping(target = "status", ignore = true)
    Solicitacao solicitacaoRequestToEntity(SolicitacaoRequest solicitacaoRequest);

}
