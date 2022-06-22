package br.com.gabrielferreira.spring.usuario.saldo.utils;

public enum ValidacaoEnum {

    DIRECAO_INCORRETA("A direção informada está incorreta, informe DESC ou ASC."),
    EMAIL_CADASTRADO("Este e-mail já foi cadastrado."),
    CPF_CADASTRADO("Este CPF já foi cadastrado."),
    USUARIO_NAO_ENCONTRADO("Usuário não foi encontrado, verifique o id informado."),
    SAQUE_NAO_ENCONTRADO("Não é possível sacar sem nenhum valor."),
    SALDO_TOTAL_USUARIO("Não é possível sacar pois o saldo total é "),
    DEPOSITO_MENOR_IGUAL_ZERO("O déposito não pode ser menor ou igual ao 0.");

    private final String mensagem;
    ValidacaoEnum(String mensagem){
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}