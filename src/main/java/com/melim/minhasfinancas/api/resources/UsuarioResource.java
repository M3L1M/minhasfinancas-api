package com.melim.minhasfinancas.api.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melim.minhasfinancas.api.dto.UsuarioDTO;
import com.melim.minhasfinancas.exception.ErroAutenticar;
import com.melim.minhasfinancas.exception.RegraNegocioException;
import com.melim.minhasfinancas.model.entity.Usuario;
import com.melim.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService service;
	
	public UsuarioResource(UsuarioService service) {
		this.service=service;
	}
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticar=service.autenticar(dto.getEmail(), dto.getSenha());
			
			//return ResponseEntity.ok(usuarioAutenticar);
			return new ResponseEntity(usuarioAutenticar,HttpStatus.OK);
		}catch (ErroAutenticar e) {
			return ResponseEntity.badRequest().body(e.getMessage());	
		}
	}
	
	
	@PostMapping
	public ResponseEntity salvar (@RequestBody UsuarioDTO dto) {
		
		Usuario usuario=Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalva=service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalva,HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());					
		}
		
	}

}
