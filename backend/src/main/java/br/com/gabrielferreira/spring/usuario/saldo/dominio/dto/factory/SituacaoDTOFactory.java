package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.situacao.SituacaoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Situacao;

import java.util.ArrayList;
import java.util.List;

public class SituacaoDTOFactory {

    private SituacaoDTOFactory(){}

    public static List<SituacaoViewDTO> toSituacaoViewDTO(List<Situacao> situacoes){
        List<SituacaoViewDTO> situacaoDTOS = new ArrayList<>();
        situacoes.forEach(situacao -> {
            SituacaoViewDTO situacaoDTO = toSituacaoViewDTO(situacao);
            situacaoDTOS.add(situacaoDTO);
        });
        return situacaoDTOS;
    }

    public static SituacaoViewDTO toSituacaoViewDTO(Situacao situacao){
        if(situacao != null){
            return new SituacaoViewDTO(situacao.getId(), situacao.getDescricao(), situacao.getCodigo());
        }
        return null;
    }

}
