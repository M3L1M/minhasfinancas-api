package com.melim.minhasfinancas.service;

import java.util.Optional;

import com.melim.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);

	Usuario salvarUsuario(Usuario usuario);

	void validarEmail(String email);
	
	Optional<Usuario> obterPorId(Integer id);
}
