package com.github.luuispnp.obra_do_berco_solicitacoes.repository;

import com.github.luuispnp.obra_do_berco_solicitacoes.entity.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>  {

}
