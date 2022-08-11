package com.dsouza.cursospring.minhasfinancas.api.resources;

import java.math.BigDecimal;
import java.util.Optional;

import com.dsouza.cursospring.minhasfinancas.api.dto.UsuarioDTO;
import com.dsouza.cursospring.minhasfinancas.exceptions.AutenticacaoException;
import com.dsouza.cursospring.minhasfinancas.exceptions.RegraNegocioException;
import com.dsouza.cursospring.minhasfinancas.model.entity.Usuario;
import com.dsouza.cursospring.minhasfinancas.service.LancamentoService;
import com.dsouza.cursospring.minhasfinancas.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private final UsuarioService usuarioService;
	private final LancamentoService lancamentoService;

	@PostMapping(path = "/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		
		try {
			Usuario usuarioAutenticado = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch(AutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
									.email(dto.getEmail())
									.nome(dto.getNome())
									.senha(dto.getSenha())
									.build();
		
		try {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		Optional<Usuario> usuario = usuarioService.obterPorId(id);
		
		if(!usuario.isPresent()){
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
	
}
