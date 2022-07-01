package com.melim.minhasfinancas.service;

import java.util.List;

import com.melim.minhasfinancas.model.entity.Lancamento;
import com.melim.minhasfinancas.model.enums.StatusLancamento;
import com.melim.minhasfinancas.model.enums.TipoLancamento;


public interface LancamentoService {
	Lancamento salvar(Lancamento lancamento);
	Lancamento atualizar(Lancamento lancamento);
	void deletar(Lancamento lancamento);
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	void atualizarTipo(Lancamento lancamento, TipoLancamento tipo);
	
	void validar(Lancamento lancamento);
}
