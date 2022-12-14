package com.dsouza.cursospring.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.dsouza.cursospring.minhasfinancas.model.entity.Lancamento;
import com.dsouza.cursospring.minhasfinancas.model.enums.StatusLancamento;
import com.dsouza.cursospring.minhasfinancas.model.enums.TipoLancamento;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = criarLancamento();
											
		lancamento = repository.save(lancamento);
		assertThat(lancamento.getId()).isNotNull();								
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarEhPersistirUmLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEhPersistirUmLancamento();
		
		lancamento.setAno(2018);
		lancamento.setDescricao("Teste atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste atualizar");
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		Lancamento lancamento = criarEhPersistirUmLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	public static Lancamento criarLancamento() {
		Lancamento lancamento = Lancamento.builder()
											.ano(2019)
											.mes(1)
											.descricao("lancamento qualquer")
											.valor(BigDecimal.valueOf(10))
											.tipo(TipoLancamento.RECEITA)
											.status(StatusLancamento.PENDENTE)
											.dataCadastro(LocalDate.now())
											.build();
		return lancamento;
	}
	
	private Lancamento criarEhPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		return lancamento;
	}
	
}
