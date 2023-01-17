package br.com.gabrielferreira.spring.usuario.saldo.utils;

public class ConstantesUtils {

    private ConstantesUtils(){}

    public static final String USUARIO_AUTENTICADO = "buscarUsuarioAutenticado";
    public static final String USUARIO_AUTENTICADO_EMAIL = "buscarUsuarioEmailAutenticado";
    public static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    public static String limparMascaraCpf(String cpf){
        cpf = cpf.replace(".","");
        cpf = cpf.replace("-","");
        return cpf;
    }
}
