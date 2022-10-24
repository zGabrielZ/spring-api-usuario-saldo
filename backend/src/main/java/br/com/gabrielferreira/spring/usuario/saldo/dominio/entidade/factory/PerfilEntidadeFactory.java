package br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;

import java.util.ArrayList;
import java.util.List;

public class PerfilEntidadeFactory {

    private PerfilEntidadeFactory(){}

    public static List<Perfil> toPerfis(List<PerfilDTO> perfis){
        List<Perfil> perfils = new ArrayList<>();
        perfis.forEach(perfilDto ->{
            Perfil perfil = toPerfil(perfilDto);
            perfils.add(perfil);
        });
        return perfils;
    }

    public static Perfil toPerfil(PerfilDTO perfilDTO){
        if(perfilDTO != null){
            return Perfil.builder()
                    .id(perfilDTO.getId())
                    .build();
        }
        return null;
    }


}
