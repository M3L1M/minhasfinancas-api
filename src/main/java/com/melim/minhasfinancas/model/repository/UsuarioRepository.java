package com.melim.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melim.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	
	boolean existsByEmail(String email);

	Optional<Usuario> findByEmail(String email);
	
	
	
}
