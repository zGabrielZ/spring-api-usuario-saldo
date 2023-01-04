package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.UsuarioDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.UsuarioEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PerfilService perfilService;

    private final UsuarioRepositorio usuarioRepositorio;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public UsuarioViewDTO inserir(UsuarioInsertFormDTO usuarioInsertFormDTO){
        usuarioInsertFormDTO.setCpf(limparMascaraCpf(usuarioInsertFormDTO.getCpf()));

        verificarEmail(usuarioInsertFormDTO.getEmail());
        verificarCpf(usuarioInsertFormDTO.getCpf());

        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
        verificarPerfil(usuarioInsertFormDTO.getPerfis(), usuarioLogado, isUsuarioLogadoPerfilAdmin);

        List<PerfilInsertFormDTO> perfisDtos = validarPerfis(usuarioInsertFormDTO.getPerfis(), isUsuarioLogadoPerfilAdmin);
        Usuario usuario = UsuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO, perfisDtos);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepositorio.save(usuario);

        return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    public UsuarioViewDTO buscarPorId(Long id){

        Usuario usuarioEncontrado = buscarUsuario(id);
        verificarUsuarioLogado(usuarioEncontrado);

        return UsuarioDTOFactory.toUsuarioViewDTO(usuarioEncontrado);
    }

    public SaldoTotalViewDTO buscarSaldoTotal(Long id){

        Usuario usuarioEncontrado = buscarUsuario(id);
        verificarUsuarioLogado(usuarioEncontrado);

        return SaldoDTOFactory.toSaldoTotalViewDTO(usuarioEncontrado.getSaldoTotal());
    }

    @Transactional
    @CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public void deletarPorId(Long id){

        Usuario usuarioEncontrado = buscarUsuario(id);

        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();

        if(usuarioLogado.getId().equals(usuarioEncontrado.getId())){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DELETAR_ADMIN_PROPRIO.getMensagem());
        }

        usuarioRepositorio.deleteById(usuarioEncontrado.getId());
    }

    @Transactional
    @CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public UsuarioViewDTO atualizar(Long id, UsuarioUpdateFormDTO usuarioUpdateFormDTO){
        Usuario usuarioEncontrado = buscarUsuario(id);

        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
        verificarUsuarioLogado(usuarioLogado, usuarioEncontrado, isUsuarioLogadoPerfilAdmin, usuarioUpdateFormDTO.getPerfis());

        Usuario usuario = UsuarioEntidadeFactory.toUsuarioUpdateEntidade(usuarioUpdateFormDTO, usuarioEncontrado, usuarioUpdateFormDTO.getPerfis());
        usuarioRepositorio.save(usuario);
        return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    @Transactional
    @CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public BigDecimal atualizarSaldoTotal(Long id, BigDecimal valor){
        Usuario usuario = buscarUsuario(id);
        usuario.setSaldoTotal(valor);
        usuarioRepositorio.save(usuario);
        return usuario.getSaldoTotal();
    }

    @Cacheable(cacheNames = USUARIO_AUTENTICADO, key = "#id")
    public Usuario buscarUsuarioAutenticado(Long id){
        return usuarioRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    @Cacheable(cacheNames = USUARIO_AUTENTICADO_EMAIL, key = "#email")
    public Usuario buscarUsuarioEmailAutenticado(String email){
        return usuarioRepositorio.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    private Usuario buscarUsuario(Long id){
        return usuarioRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    private void verificarEmail(String email){
        usuarioRepositorio.existsEmail(email).ifPresent(usuario -> {
            throw new ExcecaoPersonalizada(EMAIL_CADASTRADO.getMensagem());
        });
    }

    private void verificarCpf(String cpf) {
        usuarioRepositorio.existsCpf(cpf).ifPresent(usuario -> {
            throw new ExcecaoPersonalizada(CPF_CADASTRADO.getMensagem());
        });
    }

    private void verificarPerfil(List<PerfilInsertFormDTO> perfis, Usuario usuarioLogado, boolean isUsuarioLogadoPerfilAdmin){
        if(usuarioLogado != null && isUsuarioLogadoPerfilAdmin && perfis.isEmpty()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
        } else if(usuarioLogado != null && !isUsuarioLogadoPerfilAdmin){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN.getMensagem());
        }

        verificarPerfisDuplicados(perfis);
    }

    private void verificarPerfisDuplicados(List<PerfilInsertFormDTO> perfis){
        perfis.forEach(perfilInsertFormDTO -> {
            int duplicados = Collections.frequency(perfis, perfilInsertFormDTO);

            if (duplicados > 1) {
                throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN_REPETIDO.getMensagem());
            }
        });
    }

    private List<PerfilInsertFormDTO> validarPerfis(List<PerfilInsertFormDTO> perfis, boolean isUsuarioLogadoPerfilAdmin){
        List<PerfilInsertFormDTO> perfilInsertFormDTOS = new ArrayList<>();
        if(isUsuarioLogadoPerfilAdmin){
            perfilInsertFormDTOS = perfis;
        } else {
            perfilInsertFormDTOS.add(PerfilInsertFormDTO.builder().id(RoleEnum.ROLE_CLIENTE.getId()).build());
        }
        return perfilInsertFormDTOS;
    }

    private void verificarUsuarioLogado(Usuario usuarioLogado, Usuario usuarioEncontrado, boolean isUsuarioLogadoPerfilAdmin, List<PerfilInsertFormDTO> perfis){
        if(!isUsuarioLogadoPerfilAdmin && !usuarioLogado.equals(usuarioEncontrado)){
            throw new ExcecaoPersonalizada(USUARIO_ATUALIZAR_PERMISSAO.getMensagem());
        } else if (usuarioLogado.equals(usuarioEncontrado) && !isUsuarioLogadoPerfilAdmin && !perfis.isEmpty()){
            throw new ExcecaoPersonalizada(USUARIO_INCLUIR_ALTERAR.getMensagem());
        } else if(isUsuarioLogadoPerfilAdmin && perfis.isEmpty()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
        }
    }

    private void verificarUsuarioLogado(Usuario usuarioEncontrado){
        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();

        if(!usuarioLogado.getId().equals(usuarioEncontrado.getId()) && !isUsuarioLogadoPerfilAdmin){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DADOS_ADMIN.getMensagem());
        }
    }

    private String limparMascaraCpf(String cpf){
        cpf = cpf.replace(".","");
        cpf = cpf.replace("-","");
        return cpf;
    }

}
