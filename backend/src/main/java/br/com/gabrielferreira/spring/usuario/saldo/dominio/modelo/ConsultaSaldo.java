package br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo;

import com.querydsl.core.types.dsl.Expressions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaSaldo implements Consulta {

    public static final String ID_ALIAS = "idSaldo";
    public static final String DEPOSITO_ALIAS = "depositoSaldo";
    public static final String DATA_ALIAS = "dataSaldo";

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
    public List<AbstractConsulta> getListagens(){
        List<AbstractConsulta> consultas = new ArrayList<>();
        consultas.add(AbstractConsulta.builder().atributo("id").alias(ID_ALIAS).path(Expressions.numberPath(Long.class, ID_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("deposito").alias(DEPOSITO_ALIAS).path(Expressions.numberPath(BigDecimal.class, DEPOSITO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("data").alias(DATA_ALIAS).path(Expressions.dateTimePath(LocalDateTime.class, DATA_ALIAS)).build());
        return consultas;
    }
}
