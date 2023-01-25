package br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo;
import com.querydsl.core.types.dsl.Expressions;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultaUsuario implements Consulta {

    public static final String ID_ALIAS = "idUsuario";
    public static final String NOME_ALIAS = "nomeUsuario";
    public static final String EMAIL_ALIAS = "emailUsuario";
    public static final String CPF_ALIAS = "cpfUsuario";
    public static final String DATA_NASCIMENTO_ALIAS = "nascimentoUsuario";

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
        consultas.add(AbstractConsulta.builder().atributo("id").alias(ID_ALIAS).path(Expressions.numberPath(Long.class, ID_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("nome").alias(NOME_ALIAS).path(Expressions.stringPath(NOME_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("email").alias(EMAIL_ALIAS).path(Expressions.stringPath(EMAIL_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("cpf").alias(CPF_ALIAS).path(Expressions.stringPath(CPF_ALIAS)).build());
        consultas.add(AbstractConsulta.builder().atributo("dataNascimento").alias(DATA_NASCIMENTO_ALIAS).path(Expressions.datePath(LocalDate.class, DATA_NASCIMENTO_ALIAS)).build());
        return consultas;
    }
}
