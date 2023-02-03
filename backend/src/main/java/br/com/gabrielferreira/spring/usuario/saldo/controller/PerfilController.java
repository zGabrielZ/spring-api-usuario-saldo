package br.com.gabrielferreira.spring.usuario.saldo.controller;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/perfis")
@RequiredArgsConstructor
@Api("Pefil API")
public class PerfilController {

    private final PerfilService perfilService;

    @ApiOperation("Buscar perfil por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou um usuário"),
            @ApiResponse(code = 404,message = "Usuário não foi encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PerfilViewDTO> buscarPorId(@PathVariable Long id){
        PerfilViewDTO perfil = perfilService.buscarPorId(id);
        return ResponseEntity.ok().body(perfil);
    }

    @ApiOperation(value = "Lista de perfis")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Retornou uma lista de perfis"),
    })
    @GetMapping
    public ResponseEntity<List<PerfilViewDTO>> listagem(){
        List<PerfilViewDTO> perfis = perfilService.listagem();
        return ResponseEntity.ok().body(perfis);
    }

}
