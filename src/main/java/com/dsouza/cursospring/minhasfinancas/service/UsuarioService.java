package com.dsouza.cursospring.minhasfinancas.service;

import com.dsouza.cursospring.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);

	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
	
	
}
