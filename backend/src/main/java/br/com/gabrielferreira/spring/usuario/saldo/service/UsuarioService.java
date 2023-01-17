package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.UsuarioDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioInsertResponseDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioUpdateFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.*;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.enums.RoleEnum;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.UsuarioEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;
import static com.querydsl.core.group.GroupBy.*;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PerfilService perfilService;

    private final PerfilValidacaoService perfilValidacaoService;

    private final UsuarioRepositorio usuarioRepositorio;

    private final PasswordEncoder passwordEncoder;

    private final QueryDslDAO queryDslDAO;

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public UsuarioInsertResponseDTO inserir(UsuarioInsertFormDTO usuarioInsertFormDTO){
        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado().orElse(null);

        usuarioInsertFormDTO.setCpf(limparMascaraCpf(usuarioInsertFormDTO.getCpf()));
        verificarEmail(usuarioInsertFormDTO.getEmail());
        verificarCpf(usuarioInsertFormDTO.getCpf());

        List<PerfilInsertFormDTO> perfis = perfilValidacaoService.validarPerfilUsuarioInsert(usuarioLogado, usuarioInsertFormDTO.getPerfis());
        List<PerfilInsertFormDTO> perfisVerificado = perfilValidacaoService.validarPerfilInformadoUsuarioInsert(perfis);

        Usuario usuario = UsuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO, perfisVerificado, BigDecimal.ZERO, usuarioLogado);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepositorio.save(usuario);

        return UsuarioDTOFactory.toUsuarioInsertResponseDTO(usuario, "SECRETO");

//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
//        verificarPerfil(usuarioInsertFormDTO.getPerfis(), usuarioLogado, isUsuarioLogadoPerfilAdmin);

//        List<PerfilInsertFormDTO> perfisDtos = validarPerfis(usuarioInsertFormDTO.getPerfis(), isUsuarioLogadoPerfilAdmin);
//        Usuario usuario = UsuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO, perfisDtos, BigDecimal.ZERO);
//        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
//        usuario = usuarioRepositorio.save(usuario);
//
//        return UsuarioDTOFactory.toUsuarioInsertResponseDTO(usuario, "SECRETO");
    }

    public UsuarioViewDTO buscarPorId(Long id){


        QUsuario qUsuario = QUsuario.usuario;
        QPerfil qPerfil = QPerfil.perfil;
        QSaldo qSaldo = QSaldo.saldo;
        QSaque qSaque = QSaque.saque;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qUsuario.id.eq(id));

        UsuarioViewDTO usuarioViewDTO = queryDslDAO.query(JPAQuery::select)
                .from(qUsuario).innerJoin(qUsuario.perfis, qPerfil).leftJoin(qUsuario.saldos, qSaldo).leftJoin(qUsuario.saques, qSaque)
                .where(booleanBuilder).
                transform(groupBy(qUsuario.id)
                        .as(Projections.constructor(
                                UsuarioViewDTO.class,
                                qUsuario.id,
                                qUsuario.nome,
                                qUsuario.email,
                                qUsuario.cpf,
                                qUsuario.dataNascimento,
                                set(Projections.constructor(
                                        PerfilViewDTO.class,
                                        qPerfil.id,
                                        qPerfil.descricao
                                ).skipNulls())
                        ))).get(id);

        UsuarioViewDTO usuarioEncontrado = Optional.ofNullable(usuarioViewDTO).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
        verificarUsuarioLogado(usuarioEncontrado.id());
        return usuarioViewDTO;
    }

    public SaldoTotalViewDTO buscarSaldoTotal(Long id){

        Usuario usuarioEncontrado = buscarUsuario(id);
        verificarUsuarioLogado(usuarioEncontrado.getId());

        return SaldoDTOFactory.toSaldoTotalViewDTO(usuarioEncontrado.getSaldoTotal());
    }

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public void deletarPorId(Long id){

        Usuario usuarioEncontrado = buscarUsuario(id);

//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//
//        if(usuarioLogado.getId().equals(usuarioEncontrado.getId())){
//            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DELETAR_ADMIN_PROPRIO.getMensagem());
//        }

        usuarioRepositorio.deleteById(usuarioEncontrado.getId());
    }

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public UsuarioViewDTO atualizar(Long id, UsuarioUpdateFormDTO usuarioUpdateFormDTO){
        Usuario usuarioEncontrado = buscarUsuario(id);

//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
//        verificarUsuarioLogado(usuarioLogado, usuarioEncontrado, isUsuarioLogadoPerfilAdmin, usuarioUpdateFormDTO.getPerfis());

        Usuario usuario = UsuarioEntidadeFactory.toUsuarioUpdateEntidade(usuarioUpdateFormDTO, usuarioEncontrado, usuarioUpdateFormDTO.getPerfis());
        usuarioRepositorio.save(usuario);
        //return UsuarioDTOFactory.toUsuarioViewDTO(usuario);
        return null;
    }

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public BigDecimal atualizarSaldoTotal(Long id, BigDecimal valor){
        Usuario usuario = buscarUsuario(id);
        usuario.setSaldoTotal(valor);
        usuarioRepositorio.save(usuario);
        return usuario.getSaldoTotal();
    }

    //@Cacheable(cacheNames = USUARIO_AUTENTICADO, key = "#id")
    public Usuario buscarUsuarioAutenticado(Long id){
        return usuarioRepositorio.findById(id).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    //@Cacheable(cacheNames = USUARIO_AUTENTICADO_EMAIL, key = "#email")
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

    private void verificarUsuarioLogado(Long idUsuarioEncontrado){
//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
//
//        if(!usuarioLogado.getId().equals(idUsuarioEncontrado) && !isUsuarioLogadoPerfilAdmin){
//            throw new ExcecaoPersonalizada(PERFIL_USUARIO_DADOS_ADMIN.getMensagem());
//        }
    }

}
