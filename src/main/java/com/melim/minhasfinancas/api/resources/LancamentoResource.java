package com.melim.minhasfinancas.api.resources;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melim.minhasfinancas.api.dto.LancamentoDTO;
import com.melim.minhasfinancas.api.dto.LancamentoStatusDTO;
import com.melim.minhasfinancas.exception.RegraNegocioException;
import com.melim.minhasfinancas.model.entity.Lancamento;
import com.melim.minhasfinancas.model.entity.Usuario;
import com.melim.minhasfinancas.model.enums.StatusLancamento;
import com.melim.minhasfinancas.model.enums.TipoLancamento;
import com.melim.minhasfinancas.service.LancamentoService;
import com.melim.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {

	private final LancamentoService service;
	private final UsuarioService usuarioService;

	/*
	 * public LancamentoResource(LancamentoService service,UsuarioService
	 * serviceUsuario) { this.service=service; this.serviceUsuario=serviceUsuario; }
	 */

	@GetMapping
	public ResponseEntity buscar(
			// @RequestParam Map<String,String> params -> Esse aqui todos são opcionais,
			// porem menor,
			// por isso o ideal é fazer igual o de baiixo
			// que por mais que fique maior, é mais facil
			// de entender
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("idUsuario") Integer idUsuario) {

		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);

		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);

		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta");
		} else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);

		return ResponseEntity.ok(lancamentos);

	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);

			entidade = service.salvar(entidade);

			return new ResponseEntity(entidade, HttpStatus.CREATED);

		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Integer id, @RequestBody LancamentoDTO dto) {

		return service.obterPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na Base de Dados", HttpStatus.BAD_REQUEST));

	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizaStatus(@PathVariable("id") Integer id,@RequestBody LancamentoStatusDTO dto) {
		return service.obterPorId(id).map(entity -> {
			System.out.println(id);
			System.out.println(dto.getStatusLancamento());
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatusLancamento());
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido");
			}
			try {
				entity.setStatusLancamento(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrato na base de Dados", HttpStatus.BAD_REQUEST));
	}
	

	@DeleteMapping("{id}")
	public ResponseEntity delete(@PathVariable("id") Integer id) {
		return service.obterPorId(id).map(entidade -> {
			try {
				service.deletar(entidade);
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na Base de Dados", HttpStatus.BAD_REQUEST));

	}
	
	

	private LancamentoDTO converter(Lancamento lancamento) {
		return LancamentoDTO.builder().id(lancamento.getId()).descricao(lancamento.getDescricao())
				.valor(lancamento.getValor()).mes(lancamento.getMes()).ano(lancamento.getAno())
				.statusLancamento(lancamento.getStatusLancamento().name())
				.tipoLancamento(lancamento.getTipoLancamento().name()).idUsuario(lancamento.getUsuario().getId())
				.build();
	}
	
	

	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();

		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());

		Usuario usuario = usuarioService.obterPorId(dto.getIdUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario não encontrado para o ID informado"));

		lancamento.setUsuario(usuario);

		

		if (dto.getTipoLancamento() != null) {
			lancamento.setTipoLancamento(TipoLancamento.valueOf(dto.getTipoLancamento()));
		}
		if (dto.getStatusLancamento() != null) {
			lancamento.setStatusLancamento(StatusLancamento.valueOf(dto.getStatusLancamento()));
		}
		return lancamento;
	}

}
