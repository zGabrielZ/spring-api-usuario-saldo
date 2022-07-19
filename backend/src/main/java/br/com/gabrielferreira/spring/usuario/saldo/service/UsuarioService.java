package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.UsuarioDTOFactory;
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

    private final UsuarioEntidadeFactory usuarioEntidadeFactory;

    private final UsuarioDTOFactory usuarioDTOFactory;

    public UsuarioViewDTO inserir(UsuarioInsertFormDTO usuarioInsertFormDTO){
        usuarioInsertFormDTO.setCpf(limparMascaraCpf(usuarioInsertFormDTO.getCpf()));

        verificarEmail(usuarioInsertFormDTO.getEmail());
        verificarCpf(usuarioInsertFormDTO.getCpf());

        usuarioInsertFormDTO.setSenha(passwordEncoder.encode(usuarioInsertFormDTO.getSenha()));

        Usuario usuario = usuarioRepositorio.save(usuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO));
        return usuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    public UsuarioViewDTO buscarPorId(Long id){
        return usuarioDTOFactory.toUsuarioViewDTO(buscarUsuario(id));
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
        Usuario usuario = usuarioRepositorio.save(usuarioEntidadeFactory.toUsuarioUpdateEntidade(usuarioUpdateFormDTO, usuarioEncontrado));
        return usuarioDTOFactory.toUsuarioViewDTO(usuario);
    }

    public Page<Usuario> listagem(Pageable pageable){
        return usuarioRepositorio.findAll(pageable);
    }

    public void atualizarSaldoTotal(Usuario usuario, BigDecimal valor){
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
