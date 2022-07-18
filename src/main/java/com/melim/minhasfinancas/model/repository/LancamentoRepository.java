package com.melim.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.melim.minhasfinancas.model.entity.Lancamento;
import com.melim.minhasfinancas.model.enums.StatusLancamento;
import com.melim.minhasfinancas.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Integer> {
	@Query(value = 
			"SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u "
			+ "WHERE u.id =:idUsuario AND l.tipoLancamento =:tipo AND l.statusLancamento =:status GROUP BY u")
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus
		   (@Param("idUsuario") Integer idUsuario,
			@Param("tipo") TipoLancamento tipo,
			@Param("status") StatusLancamento status);
}
