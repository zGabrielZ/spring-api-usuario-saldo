package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;

import java.util.ArrayList;
import java.util.List;

public class PerfilDTOFactory {

    private PerfilDTOFactory(){}

    public static List<PerfilInsertFormDTO> toPerfisInsert(List<Perfil> perfis){
        List<PerfilInsertFormDTO> perfilDTOS = new ArrayList<>();
        perfis.forEach(perfil -> {
            PerfilInsertFormDTO perfilDTO = toPerfilInsert(perfil);
            perfilDTOS.add(perfilDTO);
        });
        return perfilDTOS;
    }

    public static PerfilInsertFormDTO toPerfilInsert(Perfil perfil){
        if(perfil != null){
            return PerfilInsertFormDTO.builder()
                    .id(perfil.getId())
                    .build();
        }
        return null;
    }

}
