package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Situacao;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SituacaoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class SituacaoService {

    private final SituacaoRepositorio situacaoRepositorio;

    @Cacheable(value = SITUACOES, key = "T(java.lang.String).format('%s_%s_%s', #root.target.Class.simpleName, #root.methodName, #codigo)")
    public Situacao buscarPorCodigo(String codigo){
        return situacaoRepositorio.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontrado(SITUACAO_NAO_ENCONTRADA.getMensagem()));
    }
}
