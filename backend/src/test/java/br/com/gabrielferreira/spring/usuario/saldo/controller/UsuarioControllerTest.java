package br.com.gabrielferreira.spring.usuario.saldo.controller;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SacarFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaldoService;
import br.com.gabrielferreira.spring.usuario.saldo.service.SaqueService;
import br.com.gabrielferreira.spring.usuario.saldo.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    private static final String API = "/usuarios";

    private static final MediaType JSON_MEDIATYPE = MediaType.APPLICATION_JSON;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private SaldoService saldoService;

    @MockBean
    private SaqueService saqueService;

    @Test
    @DisplayName("Inserir um usuário deveria retornar um status 201 quando informar os dados corretamente.")
    void inserirUsuario() throws Exception{
        // Cenário

        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();

        // Mock para retornar um usuário quando tiver salvo
        UsuarioViewDTO usuario = criarUsuario(usuarioFormDTO);
        when(usuarioService.inserir(any())).thenReturn(usuario);

        // Transformar o objeto dto em json
        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(usuarioFormDTO);

        // Criar uma requisição via post
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Inserir um usuário não deveria retornar um status 201 quando não informar dados.")
    void naoDeveInserirUsuario() throws Exception{
        // Cenário
        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTOVazio();

        // Transformar o objeto dto em json
        String json = new ObjectMapper().writeValueAsString(usuarioFormDTO);

        // Criar uma requisição via post
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erroFormularios", Matchers.hasSize(5)));

    }

    @Test
    @DisplayName("Inserir um usuário não deveria retornar um status 201 quando informar um email já cadastrado.")
    void naoDeveInserirUsuarioEmail() throws Exception{
        // Cenário

        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();

        // Mock para retornar uma exceção de email já cadastrado
        when(usuarioService.inserir(any())).thenThrow(new ExcecaoPersonalizada("Este e-mail já foi cadastrado."));

        // Transformar o objeto dto em json
        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(usuarioFormDTO);

        // Criar uma requisição via post
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem",equalTo("Este e-mail já foi cadastrado.")));

    }

    @Test
    @DisplayName("Inserir um usuário não deveria retornar um status 201 quando informar um cpf já cadastrado.")
    void naoDeveInserirUsuarioCpf() throws Exception{
        // Cenário
        UsuarioInsertFormDTO usuarioFormDTO = criarUsuarioFormDTO();

        // Mock para retornar uma exceção de email já cadastrado
        when(usuarioService.inserir(any())).thenThrow(new ExcecaoPersonalizada("Este CPF já foi cadastrado."));

        // Transformar o objeto dto em json
        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(usuarioFormDTO);

        // Criar uma requisição via post
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem",equalTo("Este CPF já foi cadastrado.")));

    }

    @Test
    @DisplayName("Buscar usuário deveria retornar status 200 quando informar o id.")
    void deveBuscarUsuario() throws Exception{
        // Cenário
        Long idPesquisar = 22L;

        // Mock para retornar um valor quando pesquisar por id
        UsuarioViewDTO usuario = criarUsuarioBuscado();
        when(usuarioService.buscarPorId(idPesquisar)).thenReturn(usuario);

        // Criar requisição do tipo get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/{id}",idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Buscar usuário não deveria retornar status 200 quando informar o id de um usuário não cadastrado.")
    void naoDeveBuscarUsuario() throws Exception{
        // Cenário

        Long idPesquisar = 22L;

        // Mock para retornar uma exceção quando for buscar pelo id
        when(usuarioService.buscarPorId(idPesquisar)).thenThrow(new RecursoNaoEncontrado("Usuário não foi encontrado, verifique o id informado."));

        // Criar requisição do tipo get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/{id}",idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deletar usuário por id deveria retornar status 204 quando informar id de um usuário já cadastrado.")
    void deveDeletarUsuario() throws Exception{
        // Cenário
        Long idPesquisar = 1L;

        // Mock para retornar um valor quando pesquisar por id
        UsuarioViewDTO usuario = criarUsuarioBuscado();
        when(usuarioService.buscarPorId(idPesquisar)).thenReturn(usuario);

        // Criar requisição do tipo delete
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API + "/{id}",idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Atualizar usuário deveria retornar status 200 quando informar o id de um usuário já cadastrado e o atributo na qual quer atualizar.")
    void deveAtualizarUsuario() throws Exception{
        // Cenário
        UsuarioUpdateFormDTO usuarioUpdateDTO = criarUsuarioUpdateDTO();

        // Mock para retornar um valor quando for atualizar
        UsuarioViewDTO usuario = criarUsuarioBuscado();
        when(usuarioService.atualizar(any(),any())).thenReturn(usuario);

        // Transformar o objeto dto em json
        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(usuarioUpdateDTO);

        // Criar uma requisição via put
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API + "/{id}",1L).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("nome",equalTo(usuarioUpdateDTO.getNome())))
                .andExpect(jsonPath("dataNascimento",equalTo(usuarioUpdateDTO.getDataNascimento().format(FORMATTER))));

    }

    @Test
    @DisplayName("Listagem de usuários deveria retornar status 200 quando tiver dados cadastrados.")
    void deveListarUsuariosPaginados() throws Exception{
        // Cenário

        int pagina=0; int quantidade = 2;
        Sort.Direction direcao = Sort.Direction.DESC;
        String ordenar = "nome";

        PageRequest pageRequest = PageRequest.of(pagina,quantidade,direcao,ordenar);

        // Mock para retornar dados de uma lista
        when(usuarioService.listagem(pageRequest)).thenReturn(listUsuariosParaPage(criarListaUsuarios(),pageRequest));

        // Paginação
        String query = API + "?pagina="+pagina+"&quantidadeRegistro="+quantidade+"&direcao="+direcao+"&ordenar="+ordenar;

        // Criar uma requisição via get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(query).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements",equalTo(5)))
                .andExpect(jsonPath("size",equalTo(2)))
                .andExpect(jsonPath("totalPages",equalTo(3)))
                .andExpect(jsonPath("empty",equalTo(false)));
    }

    @Test
    @DisplayName("Listagem de usuários não deveria retornar status 200 quando informar a direção errada.")
    void naoDeveListarUsuariosPaginados() throws Exception{
        // Cenário
        // Paginação
        String query = API + "?pagina=0&quantidadeRegistro=10&direcao=TESTE&ordenar=ordenar";

        // Criar uma requisição via get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(query).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem",equalTo("A direção informada está incorreta, informe DESC ou ASC.")));
    }

    @Test
    @DisplayName("Mostrar saldos por usuário deveria retornar status 200 quando tiver saldos relacionados ao usuário.")
    void deveMostrarSaldosPorUsuario() throws Exception{
        // Cenário
        Long idPesquisar = 1L;
        // Paginação
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"deposito");

        // Mock para retornar uma lista de saldos
        when(saldoService.saldosPorUsuario(idPesquisar,pageRequest)).thenReturn(listSaldosParaPage(criarListaDeSaldos(),pageRequest));

        // Query
        String queryPaginacao = "?pagina=0&quantidadeRegistro=2&direcao=DESC&ordenar=deposito";

        // Criar requisição do tipo get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/saldos/{id}" + queryPaginacao,idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Mostrar saldos por usuário não deveria retornar status 200 quando tiver direção incorreta.")
    void naoDeveMostrarSaldosPorUsuario() throws Exception{
        // Cenário
        Long idPesquisar = 1L;

        // Query
        String queryPaginacao = "?pagina=0&quantidadeRegistro=2&direcao=TESTE&ordenar=deposito";

        // Criar requisição do tipo get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/saldos/{id}" + queryPaginacao,idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem",equalTo("A direção informada está incorreta, informe DESC ou ASC.")));
    }

    @Test
    @DisplayName("Buscar saldo total para usuário deveria retornar status 200 quando informar o id.")
    void deveBuscarSaldoTotalUsuario() throws Exception{
        // Cenário
        Long idPesquisar = 22L;

        // Mock para retornar um valor quando pesquisar por id
        UsuarioViewDTO usuario = criarUsuarioBuscado();
        when(usuarioService.buscarPorId(idPesquisar)).thenReturn(usuario);

        // Criar requisição do tipo get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/saldo-total/{id}",idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Inserir um saque para o usuário deveria retornar um status 201 quando informar os dados corretamente.")
    void sacarValorSaldoTotalUsuario() throws Exception{
        // Cenário
        SacarFormDTO sacarFormDTO = SacarFormDTO.builder()
                .quantidade(BigDecimal.valueOf(500.00)).idUsuario(1L).build();

        // Mock para retornar um saque quando tiver salvo
        SacarViewDTO sacarViewDTO = SacarViewDTO.builder().saldoTotal(BigDecimal.valueOf(200.00)).build();
        when(saqueService.sacar(any())).thenReturn(sacarViewDTO);

        // Transformar o objeto dto em json
        String json = new ObjectMapper().writeValueAsString(sacarFormDTO);

        // Criar uma requisição via post
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API + "/sacar/").accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE).content(json);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Mostrar saques por usuário deveria retornar status 200 quando tiver saques relacionados ao usuário.")
    void deveMostrarSaquesPorUsuario() throws Exception{
        // Cenário
        Long idPesquisar = 1L;
        // Paginação
        PageRequest pageRequest = PageRequest.of(0,2, Sort.Direction.DESC,"valor");

        // Mock para retornar uma lista de saldos
        when(saqueService.saquesPorUsuario(idPesquisar,pageRequest)).thenReturn(listSaquesParaPage(criarListaDeSaques(),pageRequest));

        // Query
        String queryPaginacao = "?pagina=0&quantidadeRegistro=2&direcao=DESC&ordenar=valor";

        // Criar requisição do tipo get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/saques/{id}" + queryPaginacao,idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Mostrar saques por usuário não deveria retornar status 200 quando tiver direção incorreta.")
    void naoDeveMostrarSaquesPorUsuario() throws Exception{
        // Cenário
        Long idPesquisar = 1L;

        // Query
        String queryPaginacao = "?pagina=0&quantidadeRegistro=2&direcao=TESTE&ordenar=valor";

        // Criar requisição do tipo get
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/saques/{id}" + queryPaginacao,idPesquisar).accept(JSON_MEDIATYPE).contentType(JSON_MEDIATYPE);

        // Verificando
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("mensagem",equalTo("A direção informada está incorreta, informe DESC ou ASC.")));
    }

    private Page<SaldoViewDTO> listSaldosParaPage(List<SaldoViewDTO> saldos, PageRequest pageRequest){
        return new PageImpl<>(saldos,pageRequest,saldos.size());
    }

    private Page<UsuarioViewDTO> listUsuariosParaPage(List<UsuarioViewDTO> usuarios, PageRequest pageRequest){
        return new PageImpl<>(usuarios,pageRequest,usuarios.size());
    }

    private Page<SaqueViewDTO> listSaquesParaPage(List<SaqueViewDTO> saques, PageRequest pageRequest){
        return new PageImpl<>(saques,pageRequest,saques.size());
    }

    private UsuarioInsertFormDTO criarUsuarioFormDTO(){
        return UsuarioInsertFormDTO.builder().nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com").senha("123")
                .cpf("33356983040").dataNascimento(LocalDate.parse("26/12/1997",FORMATTER)).build();
    }

    private UsuarioViewDTO criarUsuarioBuscado(){
        return UsuarioViewDTO.builder().id(1L).nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com")
                .cpf("33356983040").dataNascimento(LocalDate.parse("26/12/1997",FORMATTER)).build();
    }

    private UsuarioUpdateFormDTO criarUsuarioUpdateDTO(){
        return UsuarioUpdateFormDTO.builder().nome("Gabriel Ferreira").email("ferreiragabriel2612@gmail.com").senha("123")
                .dataNascimento(LocalDate.parse("1997-12-26")).build();
    }

    private UsuarioInsertFormDTO criarUsuarioFormDTOVazio(){
        return UsuarioInsertFormDTO.builder().nome(null).email(null).senha(null)
                .cpf(null).dataNascimento(null).build();
    }
    private UsuarioViewDTO criarUsuario(UsuarioInsertFormDTO usuarioFormDTO){
        return UsuarioViewDTO.builder().id(1L).nome(usuarioFormDTO.getNome()).email(usuarioFormDTO.getEmail())
                .cpf(usuarioFormDTO.getCpf()).dataNascimento(usuarioFormDTO.getDataNascimento()).build();
    }

    private List<UsuarioViewDTO> criarListaUsuarios(){
        List<UsuarioViewDTO> usuarios = new ArrayList<>();
        usuarios.add(UsuarioViewDTO.builder().id(1L).nome("Gabriel").email("gabriel@email.com").build());
        usuarios.add(UsuarioViewDTO.builder().id(2L).nome("José").email("jose@email.com").build());
        usuarios.add(UsuarioViewDTO.builder().id(3L).nome("Marcos").email("marcos@email.com").build());
        usuarios.add(UsuarioViewDTO.builder().id(4L).nome("Juliana").email("juliana@email.com").build());
        usuarios.add(UsuarioViewDTO.builder().id(5L).nome("Júlia").email("julia@email.com").build());
        return usuarios;
    }

    private List<SaldoViewDTO> criarListaDeSaldos(){
        List<SaldoViewDTO> saldos = new ArrayList<>();
        saldos.add(SaldoViewDTO.builder().id(1L).deposito(BigDecimal.valueOf(200.00)).dataDeposito(LocalDateTime.now()).build());
        saldos.add(SaldoViewDTO.builder().id(2L).deposito(BigDecimal.valueOf(500.00)).dataDeposito(LocalDateTime.now()).build());
        saldos.add(SaldoViewDTO.builder().id(3L).deposito(BigDecimal.valueOf(700.00)).dataDeposito(LocalDateTime.now()).build());
        return saldos;
    }

    private List<SaqueViewDTO> criarListaDeSaques(){
        List<SaqueViewDTO> saques = new ArrayList<>();
        saques.add(SaqueViewDTO.builder().valor(BigDecimal.valueOf(200.00)).dataSaque(LocalDateTime.now()).build());
        saques.add(SaqueViewDTO.builder().valor(BigDecimal.valueOf(500.00)).dataSaque(LocalDateTime.now()).build());
        saques.add(SaqueViewDTO.builder().valor(BigDecimal.valueOf(700.00)).dataSaque(LocalDateTime.now()).build());
        return saques;
    }

}
