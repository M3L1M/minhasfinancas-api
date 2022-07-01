package com.melim.minhasfinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melim.minhasfinancas.exception.ErroAutenticar;
import com.melim.minhasfinancas.exception.RegraNegocioException;
import com.melim.minhasfinancas.model.entity.Usuario;
import com.melim.minhasfinancas.model.repository.UsuarioRepository;
import com.melim.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario=repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticar("Usuario não encontrado");
		}
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticar("Senha invalida");
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
		boolean existeEmail=repository.existsByEmail(email);
		
		if(existeEmail) {
			throw new RegraNegocioException("Este email ja esta cadastrado!");
		}

	}

}

/*
 {
	"nome":"usuario",
	"email":"usuario@email",
	"senha":"senha"
} 
  
 */
