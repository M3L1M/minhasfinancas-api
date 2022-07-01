package com.melim.minhasfinancas.exception;

public class ErroAutenticar extends RuntimeException{
	public ErroAutenticar(String mensagem) {
		super(mensagem);
	}
}
