package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioSaldoRelatorioDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioSaldoRelatorioFiltroDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ConsultaService consultaService;

    public Page<UsuarioSaldoRelatorioDTO> consultaUltimosSaldosUsuariosAtivos(UsuarioSaldoRelatorioFiltroDTO filtros, Integer pagina, Integer quantidadeRegistro, String[] sort){
        validarNome(filtros.getNome(), filtros.getNomeUsuarioDepositante(), filtros.isContemNome(), filtros.isContemNomeDepositante());
        validarEmail(filtros.getEmail(), filtros.getEmailUsuarioDepositante(), filtros.isContemEmail(), filtros.isContemEmailDepositante());
        validarCpf(filtros);
        validarSaldoDeposito(filtros.getSaldoDeposito(), filtros.isContemSaldoDeposito());
        validarDataDeposito(filtros);
        return consultaService.consultaUltimosSaldosUsuariosAtivos(filtros, pagina, quantidadeRegistro, sort);
    }

    private void validarNome(String nome, String nomeDepositante, boolean isContemNome, boolean isContemNomeDepositante){
        if(isContemNome && !isTamanhoCorreto(nome.length(), 3)){
            throw new ExcecaoPersonalizada(NOME_CONSULTA.getMensagem());
        }

        if(isContemNomeDepositante && !isTamanhoCorreto(nomeDepositante.length(), 3)){
            throw new ExcecaoPersonalizada(NOME_DEPOSITANTE_CONSULTA.getMensagem());
        }
    }

    private void validarEmail(String email, String emailDepositante, boolean isContemEmail, boolean isContemEmailDepositante){
        if(isContemEmail){
            verificarValidadeEmail(email, EMAIL_CONSULTA.getMensagem());
            verificarTamanhoEmail(email, EMAIL_TAMANHO_CONSULTA.getMensagem());
        }

        if(isContemEmailDepositante){
            verificarValidadeEmail(emailDepositante, EMAIL_DEPOSITANTE_CONSULTA.getMensagem());
            verificarTamanhoEmail(emailDepositante, EMAIL_DEPOSITANTE_TAMANHO_CONSULTA.getMensagem());
        }
    }

    private void validarCpf(UsuarioSaldoRelatorioFiltroDTO filtros){
        if(filtros.isContemCpf()){
            filtros.setCpf(limparMascaraCpf(filtros.getCpf()));
            verificarValidadeCpf(filtros.getCpf(), CPF_CONSULTA.getMensagem());
        }

        if(filtros.isContemCpfDepositante()){
            filtros.setCpfUsuarioDepositante(limparMascaraCpf(filtros.getCpfUsuarioDepositante()));
            verificarValidadeCpf(filtros.getCpfUsuarioDepositante(), CPF_DEPOSITANTE_CONSULTA.getMensagem());
        }
    }

    private void validarSaldoDeposito(BigDecimal saldoDeposito, boolean isContemSaldo){
        if(isContemSaldo && BigDecimal.ZERO.compareTo(saldoDeposito) >= 0){
            throw new ExcecaoPersonalizada(DEPOSITO_MENOR_IGUAL_ZERO.getMensagem());
        }
    }

    private void validarDataDeposito(UsuarioSaldoRelatorioFiltroDTO filtros){
        if(filtros.isContemDataInicioDeposito() && filtros.isContemDataFinalDeposito()){
            verificarDataInicioDeposito(filtros.getDataInicioDeposito(), filtros.getDataFinalDeposito());
            verificarPeriodoDatas(filtros.getDataInicioDeposito(), filtros.getDataFinalDeposito());
        }
    }

    private void verificarValidadeEmail(String email, String msg){
        String expressaoRegular = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(expressaoRegular);
        if(!pattern.matcher(email).matches()){
            throw new ExcecaoPersonalizada(msg);
        }
    }

    private void verificarTamanhoEmail(String email, String msg){
        if(!isTamanhoCorreto(email.length(), 5)){
            throw new ExcecaoPersonalizada(msg);
        }
    }

    private void verificarValidadeCpf(String cpf, String msg){
        CPFValidator cpfValidator = new CPFValidator();
        List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf);
        if(!erros.isEmpty()){
            throw new ExcecaoPersonalizada(msg);
        }
    }

    private void verificarDataInicioDeposito(LocalDate dataInicio, LocalDate dataFim){
        if(dataInicio.isAfter(dataFim)){
            throw new ExcecaoPersonalizada(DATA_DEPOSITO_INICIO_CONSULTA.getMensagem());
        }
    }

    private void verificarPeriodoDatas(LocalDate dataInicio, LocalDate dataFim){
        if(ChronoUnit.DAYS.between(dataInicio, dataFim) > 365) {
            throw new ExcecaoPersonalizada(PERIODO_DEPOSITO_CONSULTA.getMensagem());
        }
    }

    private boolean isTamanhoCorreto(int tamanho, int min){
        return tamanho >= min && tamanho <= 150;
    }
}
