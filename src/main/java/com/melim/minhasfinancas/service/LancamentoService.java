package com.melim.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.melim.minhasfinancas.model.entity.Lancamento;
import com.melim.minhasfinancas.model.enums.StatusLancamento;
import com.melim.minhasfinancas.model.enums.TipoLancamento;


public interface LancamentoService {
	Lancamento salvar(Lancamento lancamento);
	Lancamento atualizar(Lancamento lancamento);
	void deletar(Lancamento lancamento);
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
	Optional<Lancamento> obterPorId(Integer id);
	
	BigDecimal obterSaldoPorUsuario(Integer id);
}
