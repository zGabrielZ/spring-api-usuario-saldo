package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SituacaoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.situacao.SituacaoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Situacao;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.SituacaoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class SituacaoService {

    private final SituacaoRepositorio situacaoRepositorio;

    @Cacheable(value = SITUACOES)
    public List<SituacaoViewDTO> listagem(){
        return SituacaoDTOFactory.toSituacaoViewDTO(situacaoRepositorio.findAll());
    }

    @Cacheable(value = SITUACOES, key = "T(java.lang.String).format('%s_%s_%s', #root.target.Class.simpleName, #root.methodName, #id)")
    public SituacaoViewDTO buscarPorId(Long id){
        return SituacaoDTOFactory.toSituacaoViewDTO(situacaoRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontrado(SITUACAO_NAO_ENCONTRADA.getMensagem())));
    }

    @Cacheable(value = SITUACOES, key = "T(java.lang.String).format('%s_%s_%s', #root.target.Class.simpleName, #root.methodName, #codigo)")
    public Situacao buscarPorCodigo(String codigo){
        return situacaoRepositorio.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontrado(SITUACAO_NAO_ENCONTRADA.getMensagem()));
    }
}
