package com.melim.minhasfinancas.service.impl;

import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melim.minhasfinancas.exception.RegraNegocioException;
import com.melim.minhasfinancas.model.entity.Lancamento;
import com.melim.minhasfinancas.model.enums.StatusLancamento;
import com.melim.minhasfinancas.model.enums.TipoLancamento;
import com.melim.minhasfinancas.model.repository.LancamentoRepository;
import com.melim.minhasfinancas.service.LancamentoService;

import java.math.BigDecimal;
import java.util.List;


@Service
public class LancamentoServiceImpl implements LancamentoService {
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		//super();
		this.repository=repository;
	}
	
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatusLancamento(StatusLancamento.PENDENTE);
		Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example=Example.of(lancamentoFiltro,
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING)
				);
		return repository.findAll();
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatusLancamento(status);
		atualizar(lancamento);
	}

	@Override
	public void atualizarTipo(Lancamento lancamento, TipoLancamento tipo) {
		lancamento.setTipoLancamento(tipo);
		atualizar(lancamento);
	}


	@Override
	public void validar(Lancamento lancamento) {
		if(lancamento.getDescricao()==null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma DESCRIÇÃO vállida.");
		}
		if(lancamento.getMes()==null || lancamento.getMes()<=1 || lancamento.getMes()>=12) {
			throw new RegraNegocioException("Informe um MÊS válido.");
		}
		if(lancamento.getAno()==null || lancamento.getAno().toString().length()!=4) {
			throw new RegraNegocioException("Informe um ANO válido.");
		}
		if(lancamento.getUsuario()==null || lancamento.getUsuario().getId()==null) {
			throw new RegraNegocioException("Informe um USUARIO válido.");
		}
		if(lancamento.getValor()==null || lancamento.getValor().compareTo(BigDecimal.ZERO)<1) {
			throw new RegraNegocioException("Informe um VALOR válido");
		}
		if(lancamento.getTipoLancamento()==null) {
			throw new RegraNegocioException("Informe um TIPO de lançamento");
		}
		
	}

}