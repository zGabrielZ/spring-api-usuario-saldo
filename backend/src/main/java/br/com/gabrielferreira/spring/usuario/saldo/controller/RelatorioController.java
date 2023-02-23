package br.com.gabrielferreira.spring.usuario.saldo.controller;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.*;
import br.com.gabrielferreira.spring.usuario.saldo.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@Slf4j
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/consultas/depositos")
    public ResponseEntity<Page<UsuarioSaldoRelatorioDTO>> buscarUltimosSaldosUsuariosAtivos(
            @RequestParam(value = "pagina", required = false, defaultValue = "0") Integer pagina,
            @RequestParam(value = "quantidadeRegistro", required = false, defaultValue = "5") Integer quantidadeRegistro,
            @RequestParam(value = "sort", required = false, defaultValue = "usuario.id,asc") String[] sort,
            UsuarioSaldoRelatorioFiltroDTO filtros){

        Page<UsuarioSaldoRelatorioDTO> usuarios = relatorioService.consultaUltimosSaldosUsuariosAtivos(filtros, pagina, quantidadeRegistro, sort);
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/gerar/depositos")
    public ResponseEntity<Object> gerarRelatorioUltimosSaldosUsuariosAtivos(){
        HttpHeaders headersOut = new HttpHeaders();
        headersOut.setContentType(MediaType.APPLICATION_PDF);
        headersOut.setContentDispositionFormData("attachment",  "test.pdf");
        try {
            return ResponseEntity.ok().headers(headersOut).body(relatorioService.gerarRelatorioUltimosSaldosUsuariosAtivos());
        } catch (Exception e){
            log.warn("Erro ao gerar relatorio {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao gerar relatorio");
        }
    }

}
