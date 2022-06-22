package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioUpdateDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario inserir(UsuarioFormDTO usuarioFormDTO){
        String senhaCriptografada = passwordEncoder.encode(usuarioFormDTO.getSenha());
        String cpfFormatado = formataCpf(usuarioFormDTO.getCpf());
        Usuario usuario = new Usuario(null,usuarioFormDTO.getNome(), usuarioFormDTO.getEmail()
                , senhaCriptografada, cpfFormatado, usuarioFormDTO.getDataNascimento());
        verificarEmail(usuario.getEmail());
        verificarCpf(usuario.getCpf());
        return usuarioRepositorio.save(usuario);
    }

    public Usuario atualizar(Long id,UsuarioUpdateDTO usuarioUpdateDTO){
        Usuario usuario = buscarPorId(id);

        if(!usuario.getEmail().equals(usuarioUpdateDTO.getEmail())){
            verificarEmail(usuarioUpdateDTO.getEmail());
        }

        String cpfFormatado = formataCpf(usuarioUpdateDTO.getCpf());
        if(!usuario.getCpf().equals(cpfFormatado)){
            verificarCpf(cpfFormatado);
        }

        dtoParaEntidade(usuario,usuarioUpdateDTO,cpfFormatado);
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
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByEmail(email);
        if(optionalUsuario.isPresent()){
            throw new ExcecaoPersonalizada(EMAIL_CADASTRADO.getMensagem());
        }
    }

    private void verificarCpf(String cpf) {
        Optional<Usuario> optionalUsuario = usuarioRepositorio.findByCpf(cpf);
        if (optionalUsuario.isPresent()) {
            throw new ExcecaoPersonalizada(CPF_CADASTRADO.getMensagem());
        }
    }

    private void dtoParaEntidade(Usuario usuario,UsuarioUpdateDTO usuarioUpdateDTO, String cpfFormatado){
        usuario.setNome(usuarioUpdateDTO.getNome());
        usuario.setEmail(usuarioUpdateDTO.getEmail());
        usuario.setCpf(cpfFormatado);
        usuario.setSenha(passwordEncoder.encode(usuarioUpdateDTO.getSenha()));
        usuario.setDataNascimento(usuarioUpdateDTO.getDataNascimento());
    }

    private String formataCpf(String cpf){
        cpf = cpf.replace(".","");
        cpf = cpf.replace("-","");
        return cpf;
    }

}
