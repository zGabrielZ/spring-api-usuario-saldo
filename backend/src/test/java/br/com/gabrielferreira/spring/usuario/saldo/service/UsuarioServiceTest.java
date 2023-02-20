//package br.com.gabrielferreira.spring.usuario.saldo.service;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Perfil;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
//import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.PerfilEntidadeFactory;
//import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
//import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
//import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
//import static org.junit.jupiter.api.Assertions.*;
//
//import br.com.gabrielferreira.spring.usuario.saldo.controller.AbstractController;
//import org.junit.jupiter.api.BeforeEach;
//import static org.mockito.Mockito.*;
//import static org.assertj.core.api.Assertions.*;
//import static br.com.gabrielferreira.spring.usuario.saldo.utils.MascarasUtils.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@ExtendWith(SpringExtension.class)
//class UsuarioServiceTest extends AbstractController {
//
//    private UsuarioRepositorio usuarioRepositorio;
//
//    private PerfilService perfilService;
//
//    private PasswordEncoder passwordEncoder;
//
//    private UsuarioService usuarioService;
//
//    @BeforeEach
//    void criarInstancias(){
//        usuarioRepositorio = mock(UsuarioRepositorio.class);
//        passwordEncoder = mock(PasswordEncoder.class);
//        perfilService = mock(PerfilService.class);
//        usuarioService = new UsuarioService(perfilService, usuarioRepositorio, passwordEncoder);
//    }
//
//    @Test
//    @DisplayName("Inserir usuário deveria salvar no banco de dados quando tiver com todas as informações corretas e sem usuário logado.")
//    void inserirUsuarioQuandoNaoTiverLogado(){
//        // Cenário
//
//        // Mock do enconde da senha
//        String senhaCriptografada = "senha teste";
//        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
//
//        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.empty());
//
//        // Mock de usuario sem está logado
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(null);
//
//        // Mock de usuário quando nao conter usuario logado
//        when(perfilService.isContemPerfilClienteUsuarioLogado()).thenReturn(false);
//
//        // Mock para retornar um usuário quando tiver salvo
//        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();
//
//        Usuario usuarioJaSalvo = Usuario.builder().id(1L).nome(usuarioFormDTO.getNome()).email(usuarioFormDTO.getEmail())
//                .senha(senhaCriptografada).cpf(usuarioFormDTO.getCpf()).dataNascimento(usuarioFormDTO.getDataNascimento()).build();
//
//        when(usuarioRepositorio.save(any())).thenReturn(usuarioJaSalvo);
//
//        // Executando
//        UsuarioViewDTO usuarioResultado = usuarioService.inserir(usuarioFormDTO);
//
//        // Verificando
//        verify(usuarioRepositorio).save(any(Usuario.class));
//        assertThat(usuarioResultado.id()).isNotNull();
//        assertThat(usuarioResultado.nome()).isEqualTo("Gabriel Ferreira");
//        assertThat(usuarioJaSalvo.getSenha()).isEqualTo(senhaCriptografada);
//        assertThat(usuarioResultado.email()).isEqualTo("ferreiragabriel2612@gmail.com");
//        assertThat(usuarioResultado.cpf()).isEqualTo(toCpfFormatado("33356983040"));
//        assertThat(usuarioResultado.dataNascimento()).isEqualTo(LocalDate.parse("1997-12-26"));
//    }
//
//    @Test
//    @DisplayName("Inserir usuário deveria salvar no banco de dados quando tiver com todas as informações corretas e com usuário logado como admin.")
//    void inserirUsuarioQuandoTiverAdminLogado(){
//        // Cenário
//
//        // Mock do enconde da senha
//        String senhaCriptografada = "senha teste";
//        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
//
//        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.empty());
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "José"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Mock para retornar um usuário quando tiver salvo
//        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();
//        usuarioFormDTO.getPerfis().addAll(criarPerfis());
//
//        Usuario usuarioJaSalvo = Usuario.builder().id(1L).nome(usuarioFormDTO.getNome()).email(usuarioFormDTO.getEmail())
//                .senha(senhaCriptografada).cpf(usuarioFormDTO.getCpf()).dataNascimento(usuarioFormDTO.getDataNascimento())
//                .perfis(PerfilEntidadeFactory.toPerfis(usuarioFormDTO.getPerfis()))
//                .build();
//
//        when(usuarioRepositorio.save(any())).thenReturn(usuarioJaSalvo);
//
//        // Executando
//        UsuarioViewDTO usuarioResultado = usuarioService.inserir(usuarioFormDTO);
//
//        // Verificando
//        verify(usuarioRepositorio).save(any(Usuario.class));
//        assertThat(usuarioResultado.id()).isNotNull();
//        assertThat(usuarioResultado.nome()).isEqualTo("Gabriel Ferreira");
//        assertThat(usuarioJaSalvo.getSenha()).isEqualTo(senhaCriptografada);
//        assertThat(usuarioResultado.email()).isEqualTo("ferreiragabriel2612@gmail.com");
//        assertThat(usuarioResultado.cpf()).isEqualTo(toCpfFormatado("33356983040"));
//        assertThat(usuarioResultado.dataNascimento()).isEqualTo(LocalDate.parse("1997-12-26"));
//        assertThat(usuarioResultado.perfis()).isNotEmpty();
//    }
//
//    @Test
//    @DisplayName("Não deve inserir usuário quando não informar perfil e estiver com o usuário admin logado")
//    void naoDeveInserirUsuarioQuandoNaoInformarPerfil(){
//        // Cenário
//
//        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.empty());
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 10L, "Marcos"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Usuário sem perfil
//        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();
//        usuarioFormDTO.setPerfis(new ArrayList<>());
//
//        // Verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.inserir(usuarioFormDTO));
//    }
//
//    @Test
//    @DisplayName("Não deve inserir usuário quando informar perfil e estiver com o usuário não admin logado")
//    void naoDeveInserirUsuarioQuandoInformarPerfilLoginNaoAdmin(){
//        // Cenário
//
//        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.empty());
//
//        // Recuperar Usuário logado como Cliente
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(3L, ROLE_CLIENTE, 11L, "Tiago"));
//
//        // Verificar Usuário logado como Cliente
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(false);
//
//        // Usuário sem perfil
//        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();
//        usuarioFormDTO.getPerfis().addAll(criarPerfis());
//
//        // Verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.inserir(usuarioFormDTO));
//    }
//
//    @Test
//    @DisplayName("Não deve inserir usuário quando informar perfis duplicadas")
//    void naoDeveInserirUsuarioQuandoInformarPerfisDuplicadas(){
//        // Cenário
//
//        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.empty());
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 35L, "Mariana"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Usuário com perfil duplicado
//        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();
//        usuarioFormDTO.getPerfis().addAll(criarPerfis());
//        usuarioFormDTO.getPerfis().addAll(criarPerfis());
//
//        // Verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.inserir(usuarioFormDTO));
//    }
//
//    @Test
//    @DisplayName("Inserir usuário não deveria salvar no banco de dados quando tiver com o email já cadastrado.")
//    void naoDeveInserirUsuarioEmail(){
//        // Cenário
//
//        // Mock de verificar email já cadastrado, nesse caso tem que retornar um usuário qualquer
//        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.of(Usuario.builder().build()));
//
//        // Executando e verificando
//        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.inserir(usuarioFormDTO));
//    }
//
//    @Test
//    @DisplayName("Inserir usuário não deveria salvar no banco de dados quando tiver com cpf já cadastrado.")
//    void naoDeveInserirUsuarioCpf() {
//        // Cenário
//
//        // Mock de verificar email já cadastrado, nesse caso tem que retornar vazio
//        when(usuarioRepositorio.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        // Mock de verificar cpf já cadastrado, nesse caso tem que retornar um usuário qualquer
//        when(usuarioRepositorio.findByCpf(anyString())).thenReturn(Optional.of(Usuario.builder().build()));
//
//        // Executando e verificando
//        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.inserir(usuarioFormDTO));
//    }
//
//    @Test
//    @DisplayName("Atualizar usuário deveria salvar no banco de dados quando tiver com as informações corretas.")
//    void deveAtualizarUsuario(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//        Usuario usuario = Usuario.builder().id(2L).nome("Gabriel Teste").dataNascimento(LocalDate.parse("12/10/1997", DTF))
//                .perfis(List.of(Perfil.builder().id(3L).nome("ROLE_CLIENTE").build())).build();
//
//        // Mock para retornar o usuário quando for buscado pelo id
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Mock para retornar com o valor atualizado
//        UsuarioUpdateFormDTO usuarioUpdateDTO = criarUsuarioUpdateFormDTO();
//
//        Usuario usuarioAtualizado = Usuario.builder().id(usuario.getId())
//                .nome(usuarioUpdateDTO.getNome()).dataNascimento(usuarioUpdateDTO.getDataNascimento())
//                .perfis(List.of(Perfil.builder().id(usuarioUpdateDTO.getPerfis().get(0).getId()).nome("ROLE_ADMIN").build())).build();
//        when(usuarioRepositorio.save(any())).thenReturn(usuarioAtualizado);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Jośe teste"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Executando
//        UsuarioViewDTO usuarioResultado = usuarioService.atualizar(idPesquisar,usuarioUpdateDTO);
//
//        // Verificando
//        verify(usuarioRepositorio).save(any(Usuario.class));
//        assertThat(usuarioResultado.nome()).isEqualTo("Gabriel Ferreira");
//        assertThat(usuarioResultado.dataNascimento()).isEqualTo(LocalDate.parse("26/12/1997",DTF));
//        assertThat(usuarioResultado.perfis().get(0).getId()).isEqualTo(1L);
//    }
//
//    @Test
//    @DisplayName("Atualizar usuário não deveria salvar no banco de dados quando não encontrar o id informado.")
//    void naoDeveAtualizarUsuarioId(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//
//        // Mock para retornar vazio quando for buscar pelo id
//        when(usuarioRepositorio.findById(idPesquisar)).thenReturn(Optional.empty());
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Juan"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Executando e verificando
//        UsuarioUpdateFormDTO usuarioUpdateDTO = criarUsuarioUpdateFormDTO();
//        assertThrows(RecursoNaoEncontrado.class, () -> usuarioService.atualizar(idPesquisar,usuarioUpdateDTO));
//    }
//
//    @Test
//    @DisplayName("Buscar usuário por id deveria retornar dados quando tiver registro no banco de dados.")
//    void deveBuscarUsuarioPorId(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//
//        // Mock para retornar um usuário
//        Usuario usuario = Usuario.builder().id(1L).nome("Teste").email("teste@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Ana"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Executando
//        UsuarioViewDTO usuarioResultado = usuarioService.buscarPorId(1L);
//
//        // Verificando
//        assertThat(usuarioResultado.id()).isNotNull();
//        assertThat(usuarioResultado.nome()).isEqualTo("Teste");
//        assertThat(usuarioResultado.email()).isEqualTo("teste@email.com");
//    }
//
//    @Test
//    @DisplayName("Buscar usuário por id não deveria retornar dados quando não tiver registro no banco de dados.")
//    void naoDeveBuscarUsuarioPorId(){
//        // Cenário
//        Long idPesquisar = 1L;
//
//        // Mock para retornar um valor vazio
//        doReturn(Optional.empty()).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Anna"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Executando e verificando
//        assertThrows(RecursoNaoEncontrado.class, () -> usuarioService.buscarPorId(idPesquisar));
//    }
//
//    @Test
//    @DisplayName("Não deve buscar usuário por id pois informou usuário diferente do logado sem adminstração.")
//    void naoDeveBuscarUsuarioPorIdNaoAdminLogado(){
//        // Cenário
//
//        Long idPesquisar = 10L;
//
//        // Mock para retornar um usuário
//        Usuario usuario = Usuario.builder().id(10L).nome("Teste 123").email("teste123@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Cliente
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(3L, ROLE_CLIENTE, 1L, "José da Silva"));
//
//        // Verificar Usuário logado como Cliente
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(false);
//
//        // Executando e verificando
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.buscarPorId(idPesquisar));
//    }
//
//    @Test
//    @DisplayName("Deveria deletar usuário por id quando tiver registro no banco de dados.")
//    void deveDeletarUsuarioPorId(){
//        // Cenário
//
//        Long idPesquisar = 10L;
//
//        // Mock para retornar um valor
//        Usuario usuario = Usuario.builder().id(10L).nome("Teste deletar").email("testedeletar@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Seila"));
//
//        // Executando
//        assertDoesNotThrow(() -> usuarioService.deletarPorId(idPesquisar));
//
//        // Verificando
//        verify(usuarioRepositorio).deleteById(idPesquisar);
//    }
//
//    @Test
//    @DisplayName("Não deveria deletar usuário por id quando tiver logado com a própria conta.")
//    void naoDeveDeletarUsuarioPorId(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//
//        // Mock para retornar um valor
//        Usuario usuario = Usuario.builder().id(1L).nome("Teste deletar").email("testedeletar@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Seila 2"));
//
//        // Executando
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.deletarPorId(idPesquisar));
//
//        // Verificando
//        verify(usuarioRepositorio, never()).deleteById(idPesquisar);
//    }
//
//    @Test
//    @DisplayName("Não deveria deletar usuário por id quando não tiver registro no banco de dados.")
//    void naoDeveDeletarUsuarioPorIdRegistroInexistente(){
//        // Cenário
//
//        Long idPesquisar = 10L;
//
//        // Mock para retornar um valor vazio
//        doReturn(Optional.empty()).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Teste"));
//
//        // Executando
//        assertThrows(RecursoNaoEncontrado.class, () -> usuarioService.deletarPorId(idPesquisar));
//
//        // Verificando
//        verify(usuarioRepositorio,never()).deleteById(idPesquisar);
//    }
//
//    @Test
//    @DisplayName("Deveria atualizar o saldo total do usuário quando tiver depositos.")
//    void deveAtualizarSaldoTotalAtual(){
//        // Cenário
//
//        BigDecimal valor = BigDecimal.valueOf(200.00);
//        Usuario usuario = Usuario.builder().id(1L).nome("Gabriel Ferreira").build();
//
//        // Mock para retornar um valor
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(anyLong());
//
//        // Executando
//        usuarioService.atualizarSaldoTotal(usuario.getId(),valor);
//
//        // Verificando
//        assertThat(usuario.getSaldoTotal()).isEqualTo(valor);
//
//    }
//
//    @Test
//    @DisplayName("Deve buscar saldos por usuário pelo id.")
//    void deveBuscarSaldoPorUsuario(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//
//        // Mock para retornar um usuário
//        Usuario usuario = Usuario.builder().id(1L).nome("Teste").saldoTotal(BigDecimal.valueOf(500.00)).email("teste@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Oi"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Executando
//        SaldoTotalViewDTO saldoTotalResultado = usuarioService.buscarSaldoTotal(idPesquisar);
//
//        // Verificando
//        assertThat(saldoTotalResultado.getSaldoTotal()).isEqualTo(BigDecimal.valueOf(500.00));
//    }
//
//    @Test
//    @DisplayName("Atualizar usuário não deveria salvar no banco de dados quando o usuário encontrado for diferente e também não tiver com a conta adminstração.")
//    void naoDeveAtualizarUsuarioPermissao(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//
//        // Mock para retornar um usuário
//        Usuario usuario = Usuario.builder().id(1L).nome("Teste 1223").email("teste321@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Cliente
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(3L, ROLE_CLIENTE, 11L, "Juan Teste"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(false);
//
//        // Executando e verificando
//        UsuarioUpdateFormDTO usuarioUpdateDTO = criarUsuarioUpdateFormDTO();
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.atualizar(idPesquisar,usuarioUpdateDTO));
//    }
//
//    @Test
//    @DisplayName("Atualizar usuário não deveria salvar no banco de dados quando o usuário encontrado for igual e não admin e perfil inserido.")
//    void naoDeveAtualizarUsuarioPerfil(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//
//        // Mock para retornar um usuário
//        Usuario usuario = Usuario.builder().id(1L).nome("Teste 1223").email("teste321@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Cliente
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(3L, ROLE_CLIENTE, 1L, "Teste 000"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(false);
//
//        // Executando e verificando
//        UsuarioUpdateFormDTO usuarioUpdateDTO = criarUsuarioUpdateFormDTO();
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.atualizar(idPesquisar,usuarioUpdateDTO));
//    }
//
//    @Test
//    @DisplayName("Atualizar usuário não deveria salvar no banco de dados quando não é perfil admin e sem perfil incluindo.")
//    void naoDeveAtualizarUsuarioSemPerfil(){
//        // Cenário
//
//        Long idPesquisar = 1L;
//
//        // Mock para retornar um usuário
//        Usuario usuario = Usuario.builder().id(1L).nome("Teste 1223").email("teste321@email.com").build();
//        doReturn(Optional.of(usuario)).when(usuarioRepositorio).findById(idPesquisar);
//
//        // Recuperar Usuário logado como Admin
//        when(perfilService.recuperarUsuarioLogado()).thenReturn(gerarUsuarioLogado(1L, ROLE_ADMIN, 1L, "Teste 000"));
//
//        // Verificar Usuário logado como Admin
//        when(perfilService.isContemPerfilAdminUsuarioLogado()).thenReturn(true);
//
//        // Executando e verificando
//        UsuarioUpdateFormDTO usuarioUpdateDTO = criarUsuarioUpdateFormDTO();
//        usuarioUpdateDTO.setPerfis(new ArrayList<>());
//        assertThrows(ExcecaoPersonalizada.class, () -> usuarioService.atualizar(idPesquisar,usuarioUpdateDTO));
//    }
//
//    private UsuarioInsertFormDTO criarUsuarioFormDTO(){
//        return UsuarioInsertFormDTO.builder().nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com").senha("123")
//                .cpf("33356983040").dataNascimento(LocalDate.parse("26/12/1997", DTF))
//                .perfis(new ArrayList<>())
//                .build();
//    }
//
//    private List<PerfilInsertFormDTO> criarPerfis(){
//        List<PerfilInsertFormDTO> perfis = new ArrayList<>();
//        PerfilInsertFormDTO perfil1 = PerfilInsertFormDTO.builder()
//                .id(1L).build();
//        PerfilInsertFormDTO perfil2 = PerfilInsertFormDTO.builder()
//                .id(2L).build();
//        PerfilInsertFormDTO perfil3 = PerfilInsertFormDTO.builder()
//                .id(3L).build();
//        perfis.add(perfil1); perfis.add(perfil2); perfis.add(perfil3);
//        return perfis;
//    }
//
//    private UsuarioUpdateFormDTO criarUsuarioUpdateFormDTO(){
//        return UsuarioUpdateFormDTO.builder().nome("Gabriel Ferreira").dataNascimento(LocalDate.parse("26/12/1997", DTF))
//                .perfis(List.of(PerfilInsertFormDTO.builder().id(1L).build())).build();
//    }
//}
