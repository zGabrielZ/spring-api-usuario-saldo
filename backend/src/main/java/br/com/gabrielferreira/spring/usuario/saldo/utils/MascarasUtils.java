package br.com.gabrielferreira.spring.usuario.saldo.utils;

import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import lombok.extern.slf4j.Slf4j;
import javax.swing.text.MaskFormatter;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Slf4j
public class MascarasUtils {

    private MascarasUtils(){}

    public static String toCpfFormatado(String cpf){
        try {
            MaskFormatter cpfFormatacao = new MaskFormatter("###.###.###-##");
            cpfFormatacao.setValueContainsLiteralCharacters(false);
            return cpfFormatacao.valueToString(cpf);
        } catch (Exception e){
            log.warn("Erro ao colocar a máscara cpf {}", e.getMessage());
            throw new ExcecaoPersonalizada(ERRO_MASCARA.getMensagem());
        }
    }

}