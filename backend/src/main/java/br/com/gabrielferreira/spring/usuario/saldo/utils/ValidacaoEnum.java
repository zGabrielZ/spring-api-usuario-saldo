package br.com.gabrielferreira.spring.usuario.saldo.utils;

public enum ValidacaoEnum {

    DIRECAO_INCORRETA("A direção informada está incorreta, informe DESC ou ASC."),
    EMAIL_CADASTRADO("Este e-mail já foi cadastrado."),
    CPF_CADASTRADO("Este CPF já foi cadastrado."),
    USUARIO_NAO_ENCONTRADO("Usuário não foi encontrado, verifique o id informado."),
    SAQUE_NAO_ENCONTRADO("Não é possível sacar sem nenhum valor."),
    SALDO_TOTAL_USUARIO("Não é possível sacar pois o saldo total é "),
    DEPOSITO_MENOR_IGUAL_ZERO("O déposito não pode ser menor ou igual ao 0."),
    FINAL_DE_SEMANA("O déposito não pode ser realizado no sábado ou no domingo."),
    FERIADO_NACIONAL("Não vai ser possível dépositar pois é feriado nacional."),
    PERFIL_USUARIO("É necessário informar um perfil para este usuário."),
    PERFIL_USUARIO_ADMIN("Para inserir um perfil para este usuário é necessário logar na conta da administração."),
    PERFIL_USUARIO_ADMIN_REPETIDO("Não é possível cadastrar perfis duplicados."),
    PERFIL_USUARIO_DADOS_ADMIN("Para ver os dados deste usuário é necessário logar na conta da administração."),
    PERFIL_USUARIO_DELETAR_ADMIN_PROPRIO("Não é possível deletar o próprio perfil."),
    USUARIO_ATUALIZAR_PERMISSAO("Você não tem permissão para atualizar este usuário."),
    USUARIO_INCLUIR_ALTERAR("Você não tem permissão de incluir ou alterar perfil do usuário."),
    USUARIO_INCLUIR_DEPOSITO_ADMIN("Vocẽ não pode depositar saldo na própria conta."),
    SAQUE_MENOR_IGUAL_ZERO("Não é possível sacar com valor negativo ou valor zerado."),
    SAQUE_CONTA_PROPRIA("Somente é póssivel sacar o valor com a própria conta."),
    LISTA_SAQUES("Para ver os saques é preciso logar na conta da adminstração."),
    ERRO_MASCARA("Erro na máscara."),
    LISTA_SALDOS("Para ver os saldos é preciso logar na conta da adminstração."),
    DIRECAO_VAZIA("É necessário informar a direção (ASC ou DESC)"),
    REALIZAR_CONSULTA("Erro Inesperado ao realizar a consulta");


    private final String mensagem;
    ValidacaoEnum(String mensagem){
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
