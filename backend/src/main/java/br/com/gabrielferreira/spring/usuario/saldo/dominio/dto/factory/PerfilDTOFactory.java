package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;

import java.util.ArrayList;
import java.util.List;

public class PerfilDTOFactory {

    private PerfilDTOFactory(){}

    public static List<PerfilViewDTO> toPerfis(List<Perfil> perfis){
        List<PerfilViewDTO> perfilDTOS = new ArrayList<>();
        perfis.forEach(perfil -> {
            PerfilViewDTO perfilDTO = toPerfil(perfil);
            perfilDTOS.add(perfilDTO);
        });
        return perfilDTOS;
    }

    public static PerfilViewDTO toPerfil(Perfil perfil){
        if(perfil != null){
            return PerfilViewDTO.builder()
                    .id(perfil.getId())
                    .descricao(RoleEnum.getDescricao(perfil.getId()))
                    .build();
        }
        return null;
    }

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
