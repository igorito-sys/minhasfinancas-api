package com.dsouza.cursospring.minhasfinancas.api.resources;

import com.dsouza.cursospring.minhasfinancas.api.dto.UsuarioDTO;
import com.dsouza.cursospring.minhasfinancas.exceptions.AutenticacaoException;
import com.dsouza.cursospring.minhasfinancas.exceptions.RegraNegocioException;
import com.dsouza.cursospring.minhasfinancas.model.entity.Usuario;
import com.dsouza.cursospring.minhasfinancas.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch(AutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping
	public ResponseEntity<?> salvar( @RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
									.email(dto.getEmail())
									.nome(dto.getNome())
									.senha(dto.getSenha())
									.build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
}
