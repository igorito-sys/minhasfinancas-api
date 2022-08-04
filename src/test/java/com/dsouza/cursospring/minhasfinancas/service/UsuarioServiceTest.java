package com.dsouza.cursospring.minhasfinancas.service;

import java.util.Optional;

import com.dsouza.cursospring.minhasfinancas.exceptions.AutenticacaoException;
import com.dsouza.cursospring.minhasfinancas.exceptions.RegraNegocioException;
import com.dsouza.cursospring.minhasfinancas.model.entity.Usuario;
import com.dsouza.cursospring.minhasfinancas.model.repository.UsuarioRepository;
import com.dsouza.cursospring.minhasfinancas.service.impl.UsuarioServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	
	@Test
	public void deveSalvarUmUsuario() {
		
		// Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
									.id(1l)
									.nome("nome")
									.email("email@email.com")
									.senha("senha")
									.build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		// Ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		// Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test
	public void naoDeveSalvarUsuarioComOhEmailJaCadastrado() {
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> {
			
			// Cenário
		    String email = "email@email.com";
		    Usuario usuario = Usuario.builder().email(email).build();
		    Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		    
		    // Ação
		    service.salvarUsuario(usuario);
		    
		    // Verificação
		    Mockito.verify(repository, Mockito.never()).save(usuario);
		
		});
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		
		// Cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// Ação
		Usuario result = service.autenticar(email, senha);
		
		// Verfificação
		Assertions.assertThat(result).isNotNull();
		
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOhEmailInformado() {
			
			// Cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			// Ação
			Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));
			
			// Verificação
			Assertions.assertThat(exception).isInstanceOf(AutenticacaoException.class).hasMessage("Usuario não encontrado para o email informado.");
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoForCompativel() {
			
			// Cenário
	        String senha = "senha"; 
			Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
			
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
			
			// Ação
			Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
			
			// Verificação
			Assertions.assertThat(exception).isInstanceOf(AutenticacaoException.class).hasMessage("Senha Invalida.");
			
	}
	
	
	@Test
	public void deveValidarEmail() {
		
		// Cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		// Ação
		service.validarEmail("email@email.com");
		
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> {
	
			// Cenário
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			// Ação
			service.validarEmail("email@email.com");
			
		});
	}
	
}
