package com.dsouza.cursospring.minhasfinancas.service.impl;

import java.util.Optional;

import com.dsouza.cursospring.minhasfinancas.exceptions.AutenticacaoException;
import com.dsouza.cursospring.minhasfinancas.exceptions.RegraNegocioException;
import com.dsouza.cursospring.minhasfinancas.model.entity.Usuario;
import com.dsouza.cursospring.minhasfinancas.model.repository.UsuarioRepository;
import com.dsouza.cursospring.minhasfinancas.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new AutenticacaoException("Usuario não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new AutenticacaoException("Senha Invalida.");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email");
		}
	}

}
