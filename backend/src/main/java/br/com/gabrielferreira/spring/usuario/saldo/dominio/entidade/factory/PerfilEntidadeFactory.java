package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;

import java.util.ArrayList;
import java.util.List;

public class PerfilEntidadeFactory {

    private PerfilEntidadeFactory(){}

    public static List<Perfil> toPerfis(List<PerfilInsertFormDTO> perfis){
        List<Perfil> perfils = new ArrayList<>();
        perfis.forEach(perfilInsertFormDto ->{
            Perfil perfil = toPerfil(perfilInsertFormDto);
            perfils.add(perfil);
        });
        return perfils;
    }

    public static Perfil toPerfil(PerfilInsertFormDTO perfilInsertFormDTO){
        if(perfilInsertFormDTO != null){
            return Perfil.builder()
                    .id(perfilInsertFormDTO.getId())
                    .build();
        }
        return null;
    }


}
