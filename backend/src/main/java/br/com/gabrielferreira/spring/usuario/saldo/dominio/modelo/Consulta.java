package br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo;

import java.util.List;

public interface Consulta {

    AbstractConsulta getBuscarConsulta(String atributo);

    List<AbstractConsulta> getListagens();
}
