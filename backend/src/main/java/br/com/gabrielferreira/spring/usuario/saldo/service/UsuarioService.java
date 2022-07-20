package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.UsuarioDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.UsuarioEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    private final PasswordEncoder passwordEncoder;

    public UsuarioViewDTO inserir(UsuarioInsertFormDTO usuarioInsertFormDTO){
        usuarioInsertFormDTO.setCpf(limparMascaraCpf(usuarioInsertFormDTO.getCpf()));

        verificarEmail(usuarioInsertFormDTO.getEmail());
        verificarCpf(usuarioInsertFormDTO.getCpf());

        usuarioInsertFormDTO.setSenha(passwordEncoder.encode(usuarioInsertFormDTO.getSenha()));

        Usuario usuario = usuarioRepositorio.save(UsuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO));
        return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    public UsuarioViewDTO buscarPorId(Long id){
        return UsuarioDTOFactory.toUsuarioViewDTO(buscarUsuario(id));
    }

    public SaldoTotalViewDTO buscarSaldoTotal(Long id){
        return SaldoDTOFactory.toSaldoTotalViewDTO(buscarUsuario(id).getSaldoTotal());
    }

    public void deletarPorId(Long id){
        Usuario usuario = buscarUsuario(id);
        usuarioRepositorio.deleteById(usuario.getId());
    }

    public UsuarioViewDTO atualizar(Long id, UsuarioUpdateFormDTO usuarioUpdateFormDTO){
        Usuario usuarioEncontrado = buscarUsuario(id);

        if(!usuarioEncontrado.getEmail().equals(usuarioUpdateFormDTO.getEmail())){
            verificarEmail(usuarioUpdateFormDTO.getEmail());
        }

        usuarioUpdateFormDTO.setSenha(passwordEncoder.encode(usuarioUpdateFormDTO.getSenha()));
        Usuario usuario = usuarioRepositorio.save(UsuarioEntidadeFactory.toUsuarioUpdateEntidade(usuarioUpdateFormDTO, usuarioEncontrado));
        return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    public Page<UsuarioViewDTO> listagem(Pageable pageable){
        return UsuarioDTOFactory.toPageUsuario(usuarioRepositorio.findAll(pageable));
    }

    public void atualizarSaldoTotal(Long id, BigDecimal valor){
        Usuario usuario = buscarUsuario(id);
        usuario.setSaldoTotal(valor);
        usuarioRepositorio.save(usuario);
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

    private String limparMascaraCpf(String cpf){
        cpf = cpf.replace(".","");
        cpf = cpf.replace("-","");
        return cpf;
    }

}
