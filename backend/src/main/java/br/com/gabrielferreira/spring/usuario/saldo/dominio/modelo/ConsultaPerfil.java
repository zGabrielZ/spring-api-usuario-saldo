package br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo;

import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;

public class ConsultaPerfil implements Consulta {

    public static final String ID_PERFIL_ALIAS = "idPerfil";
    public static final String PERFIL_DESCRICAO = "descricaoPerfil";

    @Override
    public AbstractConsulta getBuscarConsulta(String atributo) {
        for (AbstractConsulta consulta : getListagens()) {
            if(consulta.getAtributo().equals(atributo)){
                return consulta;
            }
        }
        return null;
    }

    @Override
    public List<AbstractConsulta> getListagens() {
        List<AbstractConsulta> consultas = new ArrayList<>();
        consultas.add(AbstractConsulta.builder().atributo("perfis.id").alias(ID_PERFIL_ALIAS).path(Expressions.numberPath(Long.class, ID_PERFIL_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("perfis.descricao").alias(PERFIL_DESCRICAO).path(Expressions.stringPath(PERFIL_DESCRICAO)).build());
        return consultas;
    }
}
