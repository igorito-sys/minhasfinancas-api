package com.dsouza.cursospring.minhasfinancas.model.repository;

import com.dsouza.cursospring.minhasfinancas.model.entity.Lancamento;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
