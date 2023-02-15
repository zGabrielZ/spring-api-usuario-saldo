package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.utils.AbstractTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ConsultaServiceTest extends AbstractTests {

    @InjectMocks
    private ConsultaService consultaService;

    @Test
    @DisplayName("Saldos por usuário não deveria retornar uma lista quando não informar a direção.")
    void naoDeveMostrarSaldosPorUsuarioQuandoNaoInformarDirecao(){
        // Cenário

        // Usuário informado
        Long idUsuarioInformado = 1L;

        // Parametros a informar
        Integer pagina = 0;
        Integer quantidadeRegistro = 2;
        String[] sort = new String[2];
        sort[0] = "id";

        // Executando e verificando
        ExcecaoPersonalizada thrown = assertThrows(
                ExcecaoPersonalizada.class,
                () -> consultaService.saldosPorUsuario(idUsuarioInformado, pagina, quantidadeRegistro, sort),
                "É necessário informar a direção (ASC ou DESC)"
        );

        assertTrue(thrown.getMessage().contentEquals("É necessário informar a direção (ASC ou DESC)"));
    }

    @Test
    @DisplayName("Saldos por usuário não deveria retornar uma lista quando não informar a direção correta.")
    void naoDeveMostrarSaldosPorUsuarioQuandoInformarDirecaoErrada(){
        // Cenário

        // Usuário informado
        Long idUsuarioInformado = 1L;

        // Parametros a informar
        Integer pagina = 0;
        Integer quantidadeRegistro = 2;
        String[] sort = new String[2];
        sort[0] = "id,test";

        // Executando e verificando
        ExcecaoPersonalizada thrown = assertThrows(
                ExcecaoPersonalizada.class,
                () -> consultaService.saldosPorUsuario(idUsuarioInformado, pagina, quantidadeRegistro, sort),
                "A direção informada está incorreta, informe DESC ou ASC."
        );

        assertTrue(thrown.getMessage().contentEquals("A direção informada está incorreta, informe DESC ou ASC."));
    }

}
