package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.UsuarioDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.UsuarioUpdateDTO;
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
import java.util.Optional;

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

    public Usuario atualizar(Long id,UsuarioUpdateDTO usuarioUpdateDTO){
        Usuario usuario = buscarPorId(id);

        if(!usuario.getEmail().equals(usuarioUpdateDTO.getEmail())){
            verificarEmail(usuarioUpdateDTO.getEmail());
        }

        dtoParaEntidade(usuario,usuarioUpdateDTO);
        usuario = usuarioRepositorio.save(usuario);
        return usuario;
    }

    public Page<Usuario> listagem(Pageable pageable){
        return usuarioRepositorio.findAll(pageable);
    }

    public Usuario buscarPorId(Long id){
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);
        if(optionalUsuario.isEmpty()){
            throw new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem());
        }
        return optionalUsuario.get();
    }

    public void deletarPorId(Long id){
        Usuario usuario = buscarPorId(id);
        usuarioRepositorio.deleteById(usuario.getId());
    }
    public void atualizarSaldoTotal(Usuario usuario, BigDecimal valor){
        usuario.setSaldoTotal(valor);
        usuarioRepositorio.save(usuario);
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

    private void dtoParaEntidade(Usuario usuario,UsuarioUpdateDTO usuarioUpdateDTO){
        usuario.setNome(usuarioUpdateDTO.getNome());
        usuario.setEmail(usuarioUpdateDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioUpdateDTO.getSenha()));
        usuario.setDataNascimento(usuarioUpdateDTO.getDataNascimento());
    }

    private String limparMascaraCpf(String cpf){
        cpf = cpf.replace(".","");
        cpf = cpf.replace("-","");
        return cpf;
    }

}
