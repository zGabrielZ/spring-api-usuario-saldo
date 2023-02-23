package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.utils.ReportEnum;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    public Resource gerarRelatorio(Map<String, Object> parametros, List<?> list, String caminhoRelatorio, ReportEnum reportEnum) throws JRException {
        JasperReport compileRelatorio = JasperCompileManager.compileReport(caminhoRelatorio);
        JasperPrint jasperPrint = JasperFillManager.fillReport(compileRelatorio, parametros, new JRBeanCollectionDataSource(list));
        return new ByteArrayResource(gerarBytes(reportEnum, jasperPrint));
    }

    private byte[] gerarBytes(ReportEnum reportEnum, JasperPrint jasperPrint) throws JRException {
        if (reportEnum.equals(ReportEnum.PDF)) {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
        throw new ExcecaoPersonalizada("Erro gerar relatório");
    }
}
