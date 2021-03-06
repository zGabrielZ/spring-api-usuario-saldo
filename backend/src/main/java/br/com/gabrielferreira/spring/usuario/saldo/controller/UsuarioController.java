package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaqueService;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Api("Usuário API")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final SaldoService saldoService;

    private final SaqueService saqueService;

    @ApiOperation("Inserir um usuário")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Inseriu um usuário"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
    })
    @PostMapping
    public ResponseEntity<UsuarioViewDTO> inserir(@Valid @RequestBody UsuarioInsertFormDTO usuarioInsertFormDTO, UriComponentsBuilder uriComponentsBuilder){
        UsuarioViewDTO usuario = usuarioService.inserir(usuarioInsertFormDTO);
        URI uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @ApiOperation("Buscar usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou um usuário"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioViewDTO> buscarPorId(@PathVariable Long id){
        UsuarioViewDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(usuario);
    }

    @ApiOperation("Deletar usuário por ID")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204,message = "Deletou um usuário"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id){
        usuarioService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Atualizar usuário")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Atualizou um usuário"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioViewDTO> atualizarDados(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateFormDTO usuarioUpdateFormDTO){
        UsuarioViewDTO usuario = usuarioService.atualizar(id, usuarioUpdateFormDTO);
        return ResponseEntity.ok().body(usuario);
    }

    @ApiOperation(value = "Lista de usuários")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou uma lista de usuários"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
    })
    @GetMapping
    public ResponseEntity<Page<UsuarioViewDTO>> listagem(
            @RequestParam(value = "pagina", required = false, defaultValue = "0") Integer pagina,
            @RequestParam(value = "quantidadeRegistro", required = false, defaultValue = "5") Integer quantidadeRegistro,
            @RequestParam(value = "direcao", required = false, defaultValue = "ASC") String direcao,
            @RequestParam(value = "ordenar", required = false, defaultValue = "nome") String ordenar
    ){
        Optional<Sort.Direction> optionalDirecao = Sort.Direction.fromOptionalString(direcao);
        if(optionalDirecao.isEmpty()){
            throw new ExcecaoPersonalizada(DIRECAO_INCORRETA.getMensagem());
        }

        PageRequest pageRequest = PageRequest.of(pagina,quantidadeRegistro, optionalDirecao.get(),ordenar);
        Page<UsuarioViewDTO> usuarios = usuarioService.listagem(pageRequest);
        return ResponseEntity.ok().body(usuarios);
    }

    @ApiOperation("Lista de saldos por usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou uma lista de saldos"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
    })
    @GetMapping("/saldos/{id}")
    public ResponseEntity<Page<SaldoViewDTO>> listaDeSaldosPorUsuario(@PathVariable Long id,
        @RequestParam(value = "pagina", required = false, defaultValue = "0") Integer pagina,
        @RequestParam(value = "quantidadeRegistro", required = false, defaultValue = "5") Integer quantidadeRegistro,
        @RequestParam(value = "direcao", required = false, defaultValue = "ASC") String direcao,
        @RequestParam(value = "ordenar", required = false, defaultValue = "deposito") String ordenar){

        Optional<Sort.Direction> optionalDirecao = Sort.Direction.fromOptionalString(direcao);
        if(optionalDirecao.isEmpty()){
            throw new ExcecaoPersonalizada(DIRECAO_INCORRETA.getMensagem());
        }

        PageRequest pageRequest = PageRequest.of(pagina,quantidadeRegistro, optionalDirecao.get(),ordenar);
        Page<SaldoViewDTO> saldos = saldoService.saldosPorUsuario(id,pageRequest);
        return ResponseEntity.ok(saldos);
    }


    @ApiOperation("Saldo total do usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou um saldo total do usuário"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado"),
    })
    @GetMapping("/saldo-total/{id}")
    public ResponseEntity<SaldoTotalViewDTO> saldoTotalPorUsuario(@PathVariable Long id){
        SaldoTotalViewDTO saldoTotal = usuarioService.buscarSaldoTotal(id);
        return ResponseEntity.ok().body(saldoTotal);
    }

    @ApiOperation("Saque de um saldo total do usuário")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Sacou um valor do saldo total do usuário"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado")
    })
    @PostMapping("/sacar")
    public ResponseEntity<SacarViewDTO> sacarSaldoPorUsuario(@Valid @RequestBody SacarFormDTO sacarFormDTO){
        SacarViewDTO sacarViewDTO = saqueService.sacar(sacarFormDTO);
        return new ResponseEntity<>(sacarViewDTO, HttpStatus.CREATED);
    }

    @ApiOperation("Lista de saques por usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou uma lista de saques"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
    })
    @GetMapping("/saques/{id}")
    public ResponseEntity<Page<SaqueViewDTO>> buscarSaquesPorUsuario(@PathVariable Long id,
        @RequestParam(value = "pagina", required = false, defaultValue = "0") Integer pagina,
        @RequestParam(value = "quantidadeRegistro", required = false, defaultValue = "5") Integer quantidadeRegistro,
        @RequestParam(value = "direcao", required = false, defaultValue = "ASC") String direcao,
        @RequestParam(value = "ordenar", required = false, defaultValue = "valor") String ordenar){

        Optional<Sort.Direction> optionalDirecao = Sort.Direction.fromOptionalString(direcao);
        if(optionalDirecao.isEmpty()){
            throw new ExcecaoPersonalizada(DIRECAO_INCORRETA.getMensagem());
        }
        PageRequest pageRequest = PageRequest.of(pagina,quantidadeRegistro, optionalDirecao.get(),ordenar);
        Page<SaqueViewDTO> saques = saqueService.saquesPorUsuario(id,pageRequest);
        return ResponseEntity.ok().body(saques);
    }
}
