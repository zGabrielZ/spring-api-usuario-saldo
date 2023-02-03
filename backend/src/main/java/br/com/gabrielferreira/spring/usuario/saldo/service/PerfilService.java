package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.PerfilDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.PerfilRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepositorio perfilRepositorio;

    //@Cacheable(cacheNames = LISTAGEM_PERFIS)
    public List<PerfilViewDTO> listagem(){
        return PerfilDTOFactory.toPerfisViewDTO(perfilRepositorio.findAll());
    }

    //@Cacheable(cacheNames = BUSCAR_PERFIL_ID)
    public PerfilViewDTO buscarPorId(Long id){
        return PerfilDTOFactory.toPerfilViewDTO(perfilRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontrado(PERFIL_NAO_ENCONTRADO.getMensagem())));
    }
}
