package com.dsouza.cursospring.minhasfinancas.model.repository;

import java.math.BigDecimal;

import com.dsouza.cursospring.minhasfinancas.model.entity.Lancamento;
import com.dsouza.cursospring.minhasfinancas.model.enums.TipoLancamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

	@Query(value = "select sum(l.valor) "
				 + "from Lancamento l join l.usuario u "
				 + "where u.id = :idUsuario and "
				 + "l.tipo = :tipo "
				 + "group by u")
	BigDecimal obterSaldoPorTipoLancamentoEhUsuario(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);
	
}
