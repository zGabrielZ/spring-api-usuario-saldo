package br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.consulta.ConsultaDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo.*;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class ConsultaDTOFactory {

    private ConsultaDTOFactory(){}

    public static List<ConsultaDTO> getConsultas(String tipoConsulta, List<Sort.Order> orders){
        if(tipoConsulta.equalsIgnoreCase("Saldo")){
            return listagemOrders(orders, new ConsultaSaldo());
        } else if(tipoConsulta.equalsIgnoreCase("Saque")){
            return listagemOrders(orders, new ConsultaSaque());
        } else if(tipoConsulta.equalsIgnoreCase("Usuario")){
            return listagemOrders(orders, new ConsultaUsuario());
        } else if(tipoConsulta.equalsIgnoreCase("Perfil")){
            return listagemOrders(orders, new ConsultaPerfil());
        } else if (tipoConsulta.equalsIgnoreCase("Depositos Usuários Ativos")) {
            return listagemOrders(orders, new ConsultaUsuarioDepositos());
        }
        return new ArrayList<>();
    }

    private static List<ConsultaDTO> listagemOrders(List<Sort.Order> orders, Consulta consulta){
        List<ConsultaDTO> consultaDTOS = new ArrayList<>();
        for (Sort.Order order : orders) {
            AbstractConsulta consultaEncontrada = consulta.getBuscarConsulta(order.getProperty());
            if(consultaEncontrada != null){
                consultaDTOS.add(ConsultaDTO.builder().path(consultaEncontrada.getPath())
                        .direcao(order.getDirection().name())
                        .alias(consultaEncontrada.getAlias())
                        .build());
            }
        }
        return consultaDTOS;
    }
}
