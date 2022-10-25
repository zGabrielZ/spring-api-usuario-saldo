package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.UsuarioDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.PerfilEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.UsuarioEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PerfilService perfilService;

    private final UsuarioRepositorio usuarioRepositorio;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioViewDTO inserir(UsuarioInsertFormDTO usuarioInsertFormDTO){
        usuarioInsertFormDTO.setCpf(limparMascaraCpf(usuarioInsertFormDTO.getCpf()));

        verificarEmail(usuarioInsertFormDTO.getEmail());
        verificarCpf(usuarioInsertFormDTO.getCpf());

        List<Perfil> perfis = verificarUsuarioLogado(usuarioInsertFormDTO.getPerfis());
        Usuario usuario = UsuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO, perfis);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepositorio.save(usuario);

        return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    public UsuarioViewDTO buscarPorId(Long id){

        Usuario usuario = buscarUsuario(id);

        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();

        if(!usuarioLogado.getId().equals(usuario.getId()) && !isUsuarioLogadoPerfilAdmin){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DADOS_ADMIN.getMensagem());
        }


        return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    public SaldoTotalViewDTO buscarSaldoTotal(Long id){
        return SaldoDTOFactory.toSaldoTotalViewDTO(buscarUsuario(id).getSaldoTotal());
    }

    @Transactional
    public void deletarPorId(Long id){
        Usuario usuario = buscarUsuario(id);
        usuarioRepositorio.deleteById(usuario.getId());
    }

    @Transactional
    public UsuarioViewDTO atualizar(Long id, UsuarioUpdateFormDTO usuarioUpdateFormDTO){
        Usuario usuarioEncontrado = buscarUsuario(id);

        if(!usuarioEncontrado.getEmail().equals(usuarioUpdateFormDTO.getEmail())){
            verificarEmail(usuarioUpdateFormDTO.getEmail());
        }


        Usuario usuario = UsuarioEntidadeFactory.toUsuarioUpdateEntidade(usuarioUpdateFormDTO, usuarioEncontrado);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepositorio.save(usuario);
        return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    @Transactional
    public BigDecimal atualizarSaldoTotal(Long id, BigDecimal valor){
        Usuario usuario = buscarUsuario(id);
        usuario.setSaldoTotal(valor);
        usuarioRepositorio.save(usuario);
        return usuario.getSaldoTotal();
    }

    private Usuario buscarUsuario(Long id){
        return usuarioRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    private void verificarEmail(String email){
        usuarioRepositorio.findByEmail(email).ifPresent(usuario -> {
            throw new ExcecaoPersonalizada(EMAIL_CADASTRADO.getMensagem());
        });
    }

    private void verificarCpf(String cpf) {
        usuarioRepositorio.findByCpf(cpf).ifPresent(usuario -> {
            throw new ExcecaoPersonalizada(CPF_CADASTRADO.getMensagem());
        });
    }

    private List<Perfil> verificarUsuarioLogado(List<PerfilDTO> perfis){

        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();

        if(usuarioLogado != null && isUsuarioLogadoPerfilAdmin && perfis.isEmpty()){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO.getMensagem());
        } else if(usuarioLogado != null && !isUsuarioLogadoPerfilAdmin){
            throw new ExcecaoPersonalizada(PERFIL_USUARIO_ADMIN.getMensagem());
        } else if(usuarioLogado != null){
            return PerfilEntidadeFactory.toPerfis(perfis);
        }

        return List.of(Perfil.builder().id(RoleEnum.ROLE_CLIENTE.getId()).build());
    }

    private String limparMascaraCpf(String cpf){
        cpf = cpf.replace(".","");
        cpf = cpf.replace("-","");
        return cpf;
    }

}
