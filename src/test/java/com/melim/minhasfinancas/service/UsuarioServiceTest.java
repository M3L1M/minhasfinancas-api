package com.melim.minhasfinancas.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.melim.minhasfinancas.exception.ErroAutenticar;
import com.melim.minhasfinancas.exception.RegraNegocioException;
import com.melim.minhasfinancas.model.entity.Usuario;
import com.melim.minhasfinancas.model.repository.UsuarioRepository;
import com.melim.minhasfinancas.service.impl.UsuarioServiceImpl;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	/*@BeforeEach
	public void setUp() {
		//repository = Mockito.mock(UsuarioRepository.class);
		service = new UsuarioServiceImpl(repository);

	}*/
	@Test
	public void deveSalvarUsuarioComSucesso() {
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario=Usuario.builder().id(1).nome("nome").email("email@email.com").senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		Usuario usuarioSalvo=service.salvarUsuario(new Usuario());
		
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
		
	}
	
	@Test
	public void naoDeveSalvarUsuarioComEmailCadastrado() {
		String email="email@email.com";
		Usuario usuario=Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		service.salvarUsuario(usuario);
		

		Mockito.verify(repository,Mockito.never()).save(usuario);
		
	}
	

	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenário
		String email = "email@email.com";
		String senha = "senha";
			
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		Assertions.assertThat(result).isNotNull();
	}

	// public void deveLancarErro

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		// cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		// acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticar.class).hasMessage("Usuario não encontrado");

	}

	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		String senha = "senha";

		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticar.class).hasMessage("Senha invalida");

	}

	@Test
	public void deveValidarEmail() {
		// cenario

		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		// ação
		service.validarEmail("email@email.com");
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		// cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

		// acao
		Throwable exception = Assertions.catchThrowable(() -> service.validarEmail("email@email.com"));
		Assertions.assertThat(exception);
	}

}
