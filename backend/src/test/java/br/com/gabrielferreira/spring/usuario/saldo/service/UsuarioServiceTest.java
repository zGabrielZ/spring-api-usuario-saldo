package br.com.gabrielferreira.spring.usuario.saldo.service;

import br.com.gabrielferreira.spring.usuario.saldo.entidade.Usuario;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.dto.UsuarioUpdateDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UsuarioServiceTest {

    private UsuarioRepositorio usuarioRepositorio;
    private PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    @BeforeEach
    void criarInstancias(){
        usuarioRepositorio = mock(UsuarioRepositorio.class);
        passwordEncoder = mock(PasswordEncoder.class);
        usuarioService = new UsuarioService(usuarioRepositorio,passwordEncoder);
    }

    @Test
    @DisplayName("Inserir usuário deveria salvar no banco de dados quando tiver com todas as informações corretas.")
    void inserirUsuario(){
        // Cenário
        // Mock do enconde da senha
        String senhaCriptografada = "senha teste";
        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);

        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());

        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar vazio
        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.empty());

        // Mock para retornar um usuário quando tiver salvo
        UsuarioFormDTO usuarioFormDTO = criarUsuarioFormDTO();
        Usuario usuarioJaSalvo = Usuario.builder().id(1L).nome(usuarioFormDTO.getNome()).email(usuarioFormDTO.getEmail())
                .senha(senhaCriptografada).cpf(usuarioFormDTO.getCpf()).dataNascimento(usuarioFormDTO.getDataNascimento()).build();
        when(usuarioRepositorio.save(any())).thenReturn(usuarioJaSalvo);

        // Executando
        Usuario usuarioResultado = usuarioService.inserir(usuarioFormDTO);

        // Verificando
        verify(usuarioRepositorio).save(any(Usuario.class));
        assertThat(usuarioResultado.getId()).isNotNull();
        assertThat(usuarioResultado.getNome()).isEqualTo("Gabriel Ferreira");
        assertThat(usuarioResultado.getSenha()).isEqualTo(senhaCriptografada);
        assertThat(usuarioResultado.getEmail()).isEqualTo("ferreiragabriel2612@gmail.com");
        assertThat(usuarioResultado.getCpf()).isEqualTo("33356983040");
        assertThat(usuarioResultado.getDataNascimento()).isEqualTo(LocalDate.parse("1997-12-26"));
    }

    @Test
    @DisplayName("Inserir usuário não deveria salvar no banco de dados quando tiver com o email já cadastrado.")
    void naoDeveInserirUsuarioEmail(){
        // Cenário
        // Mock de verificar email já cadastrado, nesse caso tem que retornar um usuário qualquer
        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.of(Usuario.builder().build()));

        // Executando e verificando
        UsuarioFormDTO usuarioFormDTO = criarUsuarioFormDTO();
        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.inserir(usuarioFormDTO));
    }

    @Test
    @DisplayName("Inserir usuário não deveria salvar no banco de dados quando tiver com cpf já cadastrado.")
    void naoDeveInserirUsuarioCpf(){
        // Cenário
        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());

        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar um usuário qualquer
        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.of(Usuario.builder().build()));

        // Executando e verificando
        UsuarioFormDTO usuarioFormDTO = criarUsuarioFormDTO();
        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.inserir(usuarioFormDTO));
    }

    @Test
    @DisplayName("Atualizar usuário deveria salvar no banco de dados quando tiver com as informações corretas.")
    void deveAtualizarUsuario(){
        // Cenário
        Long idPesquisar = 1L;
        Usuario usuario = Usuario.builder().id(1L).nome("Gabriel").dataNascimento(LocalDate.parse("1999-12-12")).build();

        // Mock para retornar o usuário quando for buscado pelo id
        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);

        // Mock para retornar com o valor atualizado
        UsuarioUpdateDTO usuarioUpdateDTO = criarUsuarioUpdateFormDTO();
        Usuario usuarioAtualizado = Usuario.builder().id(usuario.getId())
                .nome(usuarioUpdateDTO.getNome()).dataNascimento(usuario.getDataNascimento()).build();
        when(usuarioRepositorio.save(any())).thenReturn(usuario);

        // Executando
        Usuario usuarioResultado = usuarioService.atualizar(idPesquisar,usuarioUpdateDTO);

        // Verificando
        verify(usuarioRepositorio).save(any(Usuario.class));
        assertThat(usuarioResultado.getNome()).isEqualTo("José Pereira");
        assertThat(usuarioResultado.getDataNascimento()).isEqualTo(LocalDate.parse("1998-12-26"));
    }

    @Test
    @DisplayName("Atualizar usuário não deveria salvar no banco de dados quando não encontrar o id informado.")
    void naoDeveAtualizarUsuarioId(){
        // Cenário
        Long idPesquisar = 1L;

        // Mock para retornar vazio quando for buscar pelo id
        when(usuarioRepositorio.findById(idPesquisar)).thenReturn(Optional.empty());

        // Executando e verificando
        UsuarioUpdateDTO usuarioUpdateDTO = criarUsuarioUpdateFormDTO();
        assertThrows(RecursoNaoEncontrado.class, () -> usuarioService.atualizar(idPesquisar,usuarioUpdateDTO));
    }

    @Test
    @DisplayName("Buscar usuário por id deveria retornar dados quando tiver registro no banco de dados.")
    void deveBuscarUsuarioPorId(){
        // Cenário
        Long idPesquisar = 1L;

        // Mock para retornar um valor
        Usuario usuario = Usuario.builder().id(1L).nome("Teste").email("teste@email.com").build();
        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);

        // Executando
        Usuario usuarioResultado = usuarioService.buscarPorId(1L);

        // Verificando
        assertThat(usuarioResultado.getId()).isNotNull();
        assertThat(usuarioResultado.getNome()).isEqualTo("Teste");
        assertThat(usuarioResultado.getEmail()).isEqualTo("teste@email.com");
    }

    @Test
    @DisplayName("Buscar usuário por id não deveria retornar dados quando não tiver registro no banco de dados.")
    void naoDeveBuscarUsuarioPorId(){
        // Cenário
        Long idPesquisar = 1L;

        // Mock para retornar um valor vazio
        doReturn(Optional.empty()).when(usuarioRepositorio).findById(idPesquisar);

        // Executando e verificando
        assertThrows(RecursoNaoEncontrado.class, () -> usuarioService.buscarPorId(idPesquisar));
    }

    @Test
    @DisplayName("Deveria deletar usuário por id quando tiver registro no banco de dados.")
    void deveDeletarUsuarioPorId(){
        // Cenário
        Long idPesquisar = 1L;

        // Mock para retornar um valor
        Usuario usuario = Usuario.builder().id(1L).nome("Teste").email("teste@email.com").build();
        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);

        // Executando
        assertDoesNotThrow(() -> usuarioService.deletarPorId(idPesquisar));

        // Verificando
        verify(usuarioRepositorio).deleteById(idPesquisar);
    }

    @Test
    @DisplayName("Não deveria deletar usuário por id quando não tiver registro no banco de dados.")
    void naoDeveDeletarUsuarioPorId(){
        // Cenário
        Long idPesquisar = 1L;

        // Mock para retornar um valor vazio
        doReturn(Optional.empty()).when(usuarioRepositorio).findById(idPesquisar);

        // Executando
        assertThrows(RecursoNaoEncontrado.class, () -> usuarioService.deletarPorId(idPesquisar));

        // Verificando
        verify(usuarioRepositorio,never()).deleteById(idPesquisar);
    }

    @Test
    @DisplayName("Deveria atualizar o saldo total do usuário quando tiver depositos.")
    void deveAtualizarSaldoTotalAtual(){
        // Cenário
        BigDecimal valor = BigDecimal.valueOf(200.00);
        Usuario usuario = Usuario.builder().nome("Gabriel Ferreira").build();

        // Executando
        usuarioService.atualizarSaldoTotal(usuario,valor);

        // Verificando
        assertThat(usuario.getSaldoTotal()).isEqualTo(valor);

    }

    @Test
    @DisplayName("Deve retornar lista de usuários quando tiver dados no banco de dados.")
    void deveRetornarUsuariosPaginados(){
        // Cenário
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(Usuario.builder().id(1L).nome("Teste").email("teste@email.com").build());
        usuarios.add(Usuario.builder().id(2L).nome("Teste 2").email("teste2email.com").build());
        usuarios.add(Usuario.builder().id(3L).nome("Teste 3").email("teste3@email.com").build());

        // Mock para retornar os dados de cima
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"nome");
        when(usuarioRepositorio.findAll(pageRequest)).thenReturn(listParaPage(usuarios,pageRequest));

        // Execução
        Page<Usuario> usuarioPage = usuarioService.listagem(pageRequest);

        // Verificação
        assertThat(usuarioPage.getTotalElements()).isEqualTo(3);
        assertThat(usuarioPage.getTotalPages()).isEqualTo(2);
        assertThat(usuarioPage.isEmpty()).isFalse();

    }

    private UsuarioFormDTO criarUsuarioFormDTO(){
        return UsuarioFormDTO.builder().nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com").senha("123")
                .cpf("33356983040").dataNascimento(LocalDate.parse("1997-12-26")).build();
    }

    private UsuarioUpdateDTO criarUsuarioUpdateFormDTO(){
        return UsuarioUpdateDTO.builder().nome("José Pereira").dataNascimento(LocalDate.parse("1998-12-26")).build();
    }

    private Page<Usuario> listParaPage(List<Usuario> usuarios, PageRequest pageRequest){
        return new PageImpl<>(usuarios,pageRequest,usuarios.size());
    }
}
