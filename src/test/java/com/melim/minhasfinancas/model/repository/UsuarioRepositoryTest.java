package com.melim.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.melim.minhasfinancas.model.entity.Usuario;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void verificaSeExisteEsseEmail() {
		//Cenário
		Usuario usuario=criarUsuario();
		//repository.save(usuario);
		entityManager.persist(usuario);
		//Ação/Execução
		boolean existe=repository.existsByEmail("email@email.com");
		
		//Verificação
		Assertions.assertThat(existe).isTrue();
		
	}
	
	@Test
	public void verificaSeNaoExisteEsseEmail(){
		//repository.deleteAll();
		
		boolean existe=repository.existsByEmail("email@email.com");
		
		Assertions.assertThat(existe).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		Usuario usuario=criarUsuario();
		
		Usuario usuarioSalvo=repository.save(usuario);
		
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();

	}
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		Usuario usuario=criarUsuario();
		entityManager.persist(usuario);
		
		Optional<Usuario> user=repository.findByEmail("email@email.com");
		Assertions.assertThat(user.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPeloEmail() {
		
		Optional<Usuario> user=repository.findByEmail("email@email.com");
		Assertions.assertThat(user.isPresent()).isFalse();
	}
	
	
	
	public static Usuario criarUsuario() {
		return Usuario.builder()
		.nome("gabriel")
		.email("email@email.com")
		.senha("senha").build();
	}
	
	
}
