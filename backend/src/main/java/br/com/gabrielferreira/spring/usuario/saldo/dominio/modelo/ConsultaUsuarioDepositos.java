package br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo;

import com.querydsl.core.types.dsl.Expressions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaUsuarioDepositos implements Consulta {

    public static final String ID_USUARIO_ALIAS = "idUsuario";
    public static final String NOME_USUARIO_ALIAS = "nomeUsuario";
    public static final String EMAIL_USUARIO_ALIAS = "emailUsuario";
    public static final String CPF_USUARIO_ALIAS = "cpfUsuario";
    public static final String ID_SALDO_ALIAS = "idSaldo";
    public static final String DEPOSITO_SALDO_ALIAS = "depositoSaldo";
    public static final String DATA_SALDO_ALIAS = "dataSaldo";
    public static final String ID_USUARIO_DEPOSITANTE_ALIAS = "idUsuarioDepositante";
    public static final String NOME_USUARIO_DEPOSITANTE_ALIAS = "nomeUsuarioDepositante";
    public static final String EMAIL_USUARIO_DEPOSITANTE_ALIAS = "emailUsuarioDepositante";
    public static final String CPF_USUARIO_DEPOSITANTE_ALIAS = "cpfUsuarioDepositante";

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
        consultas.add(AbstractConsulta.builder().atributo("usuario.id").alias(ID_USUARIO_ALIAS).path(Expressions.numberPath(Long.class, ID_USUARIO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("usuario.nome").alias(NOME_USUARIO_ALIAS).path(Expressions.stringPath(NOME_USUARIO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("usuario.email").alias(EMAIL_USUARIO_ALIAS).path(Expressions.stringPath(EMAIL_USUARIO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("usuario.cpf").alias(CPF_USUARIO_ALIAS).path(Expressions.stringPath(CPF_USUARIO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("saldo.id").alias(ID_SALDO_ALIAS).path(Expressions.numberPath(Long.class, ID_SALDO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("saldo.deposito").alias(DEPOSITO_SALDO_ALIAS).path(Expressions.numberPath(BigDecimal.class, DEPOSITO_SALDO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("saldo.data").alias(DATA_SALDO_ALIAS).path(Expressions.dateTimePath(LocalDateTime.class, DATA_SALDO_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("usuarioDepositante.id").alias(ID_USUARIO_DEPOSITANTE_ALIAS).path(Expressions.numberPath(Long.class, ID_USUARIO_DEPOSITANTE_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("usuarioDepositante.nome").alias(NOME_USUARIO_DEPOSITANTE_ALIAS).path(Expressions.stringPath(NOME_USUARIO_DEPOSITANTE_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("usuarioDepositante.email").alias(EMAIL_USUARIO_DEPOSITANTE_ALIAS).path(Expressions.stringPath(EMAIL_USUARIO_DEPOSITANTE_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("usuarioDepositante.cpf").alias(CPF_USUARIO_DEPOSITANTE_ALIAS).path(Expressions.stringPath(CPF_USUARIO_DEPOSITANTE_ALIAS)).build());
        return consultas;
    }
}
