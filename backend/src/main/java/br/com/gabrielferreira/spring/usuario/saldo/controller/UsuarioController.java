package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saldo;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.*;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaqueService;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@Api("Usuário API")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final SaldoService saldoService;

    private final SaqueService saqueService;

    public UsuarioController(UsuarioService usuarioService, SaldoService saldoService, SaqueService saqueService) {
        this.usuarioService = usuarioService;
        this.saldoService = saldoService;
        this.saqueService = saqueService;
    }

    @ApiOperation("Inserir um usuário")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Inseriu um usuário"),
            @ApiResponse(code = 400,message = "Ocorreu um erro personalizado"),
    })
    @PostMapping
    public ResponseEntity<UsuarioViewDTO> inserir(@Valid @RequestBody UsuarioFormDTO usuarioFormDTO, UriComponentsBuilder uriComponentsBuilder){
        Usuario usuario = usuarioService.inserir(usuarioFormDTO);
        URI uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioViewDTO(usuario));
    }

    @ApiOperation("Buscar usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou um usuário"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioViewDTO> buscarPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(new UsuarioViewDTO(usuario));
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
            @ApiResponse(code = 404,message = "Usuário não foi encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioViewDTO> atualizarDados(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO){
        Usuario usuario = usuarioService.atualizar(id,usuarioUpdateDTO);
        return ResponseEntity.ok().body(new UsuarioViewDTO(usuario));
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
            throw new ExcecaoPersonalizada("A direção informada está incorreta, informe DESC ou ASC");
        }

        PageRequest pageRequest = PageRequest.of(pagina,quantidadeRegistro, optionalDirecao.get(),ordenar);
        Page<Usuario> usuarios = usuarioService.listagem(pageRequest);
        return ResponseEntity.ok().body(UsuarioViewDTO.converterParaDto(usuarios));
    }

    @ApiOperation("Lista de saldos por usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou uma lista de saldos"),
            @ApiResponse(code = 404,message = "Usuário ou nenhum saldo foi encontrado"),
    })
    @GetMapping("/saldos/{id}")
    public ResponseEntity<List<SaldoViewDTO>> listaDeSaldosPorUsuario(@PathVariable Long id){
        List<Saldo> saldos = saldoService.saldosPorUsuario(id);
        return ResponseEntity.ok().body(SaldoViewDTO.listParaDto(saldos));
    }

    @ApiOperation("Saldo total do usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou um saldo total do usuário"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado"),
    })
    @GetMapping("/saldo-total/{id}")
    public ResponseEntity<SaldoTotalViewDTO> saldoTotalPorUsuario(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(new SaldoTotalViewDTO(usuario.getSaldoTotal()));
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
        BigDecimal saldoTotal = saqueService.sacar(sacarFormDTO);
        return new ResponseEntity<>(new SacarViewDTO(saldoTotal), HttpStatus.CREATED);
    }
}
