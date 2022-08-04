package com.dsouza.cursospring.minhasfinancas.exceptions;

public class AutenticacaoException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AutenticacaoException(String mensagem) {
		super(mensagem);
	}
	
}
