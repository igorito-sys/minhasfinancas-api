package com.dsouza.cursospring.minhasfinancas.model.repository;

import java.util.Optional;

import com.dsouza.cursospring.minhasfinancas.model.entity.Usuario;

import org.assertj.core.api.Assertions;
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
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entitymanager;
	
	@Test 
	public void deveVerificarAhExistenciaDeUmEmail() {
		
		// Cenário
		Usuario usuario = criarUsuario();
		entitymanager.persist(usuario);
		
		// Ação | Execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		// Verificação
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOhEmail() {
		
		// Cenário
		
		
		// Ação
		boolean result = repository.existsByEmail("usuario@emial.com");
		
		// Verificação
		Assertions.assertThat(result).isFalse();
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		// Cenário
		Usuario usuario = criarUsuario();
		
		// Ação
		Usuario usuarioSalvo = repository.save(usuario);
		
		// Verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		// Cenário
		Usuario usuario = criarUsuario();
		
		// Ação
		entitymanager.persist(usuario);
		
		// verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
		// verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criarUsuario() {
		return Usuario
					.builder()
					.nome("usuario")
					.email("usuario@email.com")
					.senha("1234")
					.build();
	}
	
}
