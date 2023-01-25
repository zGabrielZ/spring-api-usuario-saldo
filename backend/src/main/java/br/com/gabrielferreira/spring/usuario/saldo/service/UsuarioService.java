package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.SaldoDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.UsuarioDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilInsertFormDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoTotalViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.*;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.*;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.factory.UsuarioEntidadeFactory;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;
import static com.querydsl.core.group.GroupBy.*;
import static br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils.*;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;



@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PerfilValidacaoService perfilValidacaoService;

    private final UsuarioRepositorio usuarioRepositorio;

    private final PasswordEncoder passwordEncoder;

    private final QueryDslDAO queryDslDAO;

    private final ConsultaService consultaService;

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public UsuarioInsertResponseDTO inserir(UsuarioInsertFormDTO usuarioInsertFormDTO){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();

        usuarioInsertFormDTO.setCpf(limparMascaraCpf(usuarioInsertFormDTO.getCpf()));
        verificarEmail(usuarioInsertFormDTO.getEmail());
        verificarCpf(usuarioInsertFormDTO.getCpf());

        List<PerfilInsertFormDTO> perfis = perfilValidacaoService.validarPerfilUsuarioInsert(usuarioInsertFormDTO.getPerfis(), usuarioLogado);
        List<PerfilInsertFormDTO> perfisVerificado = perfilValidacaoService.validarPerfilInformadoUsuarioInsert(perfis, isAdmin());

        Usuario usuario = UsuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO, perfisVerificado, BigDecimal.ZERO, false);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        verificarUsuarioLogado(usuarioLogado, usuario);
        usuario = usuarioRepositorio.save(usuario);

        return UsuarioDTOFactory.toUsuarioInsertResponseDTO(usuario, "SECRETO");
    }

    public UsuarioViewDTO buscarPorId(Long id){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.validarPerfilUsuarioVisualizacao(id, usuarioLogado, isAdmin());

        QUsuario qUsuario = QUsuario.usuario;
        QPerfil qPerfil = QPerfil.perfil;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qUsuario.id.eq(id)).and(qUsuario.excluido.eq(false));

        UsuarioViewDTO usuarioViewDTO = queryDslDAO.query(JPAQuery::select)
                .from(qUsuario).innerJoin(qUsuario.perfis, qPerfil)
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

        return Optional.ofNullable(usuarioViewDTO).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public UsuarioUpdateResponseDTO atualizar(Long id, UsuarioUpdateFormDTO usuarioUpdateFormDTO){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.validarPerfilUsuarioUpdate(usuarioUpdateFormDTO.getPerfis(), usuarioLogado, id, isAdmin());

        Usuario usuarioEncontrado = buscarUsuario(id, false);
        verificarSituacaoUsuarioLogado(usuarioLogado);
        Usuario usuario = UsuarioEntidadeFactory.toUsuarioUpdateEntidade(usuarioUpdateFormDTO, usuarioEncontrado, usuarioUpdateFormDTO.getPerfis(), usuarioLogado);
        usuarioRepositorio.save(usuario);

        return UsuarioDTOFactory.toUsuarioUpdateResponseDTO(usuario);
    }

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public void deletarPorId(Long id){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        verificarSituacaoUsuarioLogado(usuarioLogado);

        Usuario usuarioEncontrado = buscarUsuario(id, false);

        perfilValidacaoService.validarPerfilUsuarioDelete(usuarioEncontrado, usuarioLogado);

        usuarioEncontrado.setUsuarioExclusao(usuarioLogado);
        usuarioEncontrado.setDataExclusao(ZonedDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)));
        usuarioEncontrado.setExcluido(true);
        usuarioRepositorio.save(usuarioEncontrado);
    }

    public Page<UsuarioViewDTO> buscarUsuarioPaginado(Integer pagina, Integer quantidadeRegistro, String[] sort){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        verificarSituacaoUsuarioLogado(usuarioLogado);
        return consultaService.listagem(pagina, quantidadeRegistro, sort);
    }

    public SaldoTotalViewDTO buscarSaldoTotal(Long id){

        Usuario usuarioEncontrado = buscarUsuario(id, false);
        //verificarUsuarioLogado(usuarioEncontrado.getId());

        return SaldoDTOFactory.toSaldoTotalViewDTO(usuarioEncontrado.getSaldoTotal());
    }

    @Transactional
    //@CacheEvict(value = {USUARIO_AUTENTICADO, USUARIO_AUTENTICADO_EMAIL}, allEntries = true)
    public BigDecimal atualizarSaldoTotal(Long id, BigDecimal valor){
        Usuario usuario = buscarUsuario(id, false);
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

    private Usuario buscarUsuario(Long id, boolean excluido){
        return usuarioRepositorio.findByIdUsuario(id, excluido).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
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

    private void verificarUsuarioLogado(Usuario usuarioLogado, Usuario usuarioAoInserir){
        verificarSituacaoUsuarioLogado(usuarioLogado);
        if(usuarioLogado != null && !usuarioLogado.isExcluido()){
            usuarioAoInserir.setUsuarioInclusao(usuarioLogado);
        } else {
            usuarioAoInserir.setUsuarioInclusao(usuarioAoInserir);
            usuarioAoInserir.setUsuarioAlteracao(usuarioAoInserir);
        }
    }

    private void verificarSituacaoUsuarioLogado(Usuario usuarioLogado){
        if(usuarioLogado != null && usuarioLogado.isExcluido()){
            throw new ExcecaoPersonalizada(OPERACAO_USUARIO_NAO_ENCONTRADO.getMensagem());
        }
    }

}
