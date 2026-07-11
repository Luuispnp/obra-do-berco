package com.github.luuispnp.obra_do_berco_solicitacoes.repository;

import com.github.luuispnp.obra_do_berco_solicitacoes.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolicitacaoRepository extends
        JpaRepository<Solicitacao, UUID>,
        JpaSpecificationExecutor<Solicitacao>
{



}
