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
import org.springframework.cache.annotation.Cacheable;
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
    public UsuarioInsertResponseDTO inserir(UsuarioInsertFormDTO usuarioInsertFormDTO){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();

        usuarioInsertFormDTO.setCpf(limparMascaraCpf(usuarioInsertFormDTO.getCpf()));
        verificarEmail(usuarioInsertFormDTO.getEmail());
        verificarCpf(usuarioInsertFormDTO.getCpf());

        List<PerfilInsertFormDTO> perfis = perfilValidacaoService.validarPerfilUsuarioInsert(usuarioInsertFormDTO.getPerfis(), usuarioLogado, isAdmin(), isFuncionario(), isCliente());
        List<PerfilInsertFormDTO> perfisVerificado = perfilValidacaoService.validarPerfilInformadoUsuarioInsert(perfis, isAdmin());

        Usuario usuario = UsuarioEntidadeFactory.toUsuarioInsertEntidade(usuarioInsertFormDTO, perfisVerificado, BigDecimal.ZERO, false);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        verificarUsuarioLogado(usuarioLogado, usuario);
        usuario = usuarioRepositorio.save(usuario);

        return UsuarioDTOFactory.toUsuarioInsertResponseDTO(usuario, "SECRETO");
    }

    public UsuarioViewDTO buscarPorId(Long id){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.validarPerfilUsuarioVisualizacao(id, usuarioLogado, isAdmin(), isFuncionario(), isCliente());

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
    public UsuarioUpdateResponseDTO atualizar(Long id, UsuarioUpdateFormDTO usuarioUpdateFormDTO){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        perfilValidacaoService.validarPerfilUsuarioUpdate(usuarioUpdateFormDTO.getPerfis(), usuarioLogado, id, isAdmin(), isFuncionario(), isCliente());

        Usuario usuarioEncontrado = buscarUsuario(id);
        Usuario usuario = UsuarioEntidadeFactory.toUsuarioUpdateEntidade(usuarioUpdateFormDTO, usuarioEncontrado, usuarioUpdateFormDTO.getPerfis(), usuarioLogado);
        usuarioRepositorio.save(usuario);

        return UsuarioDTOFactory.toUsuarioUpdateResponseDTO(usuario);
    }

    @Transactional
    public void deletarPorId(Long id){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();

        Usuario usuarioEncontrado = buscarUsuario(id);
        perfilValidacaoService.validarPerfilUsuarioDelete(usuarioEncontrado, usuarioLogado);

        usuarioEncontrado.setUsuarioExclusao(usuarioLogado);
        usuarioEncontrado.setDataExclusao(ZonedDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)));
        usuarioEncontrado.setExcluido(true);
        usuarioRepositorio.save(usuarioEncontrado);
    }

    public Page<UsuarioViewDTO> buscarUsuarioPaginado(Integer pagina, Integer quantidadeRegistro, String[] sort){
        return consultaService.listagem(pagina, quantidadeRegistro, sort);
    }

    public SaldoTotalViewDTO buscarSaldoTotal(Long id){
        Usuario usuarioLogado = getRecuperarUsuarioLogado();
        Usuario usuarioEncontrado = buscarUsuario(id);
        perfilValidacaoService.validarPerfilUsuarioVisualizacaoSaldo(usuarioLogado, usuarioEncontrado, isAdmin(), isFuncionario(), isCliente());

        return SaldoDTOFactory.toSaldoTotalViewDTO(usuarioEncontrado.getSaldoTotal());
    }

    public Usuario buscarUsuarioAutenticado(Long id){
        return usuarioRepositorio.findByIdUsuario(id).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    @Cacheable(value = USUARIOS, key = "T(java.lang.String).format('%s_%s_%s', #root.target.Class.simpleName, #root.methodName, #email)")
    public Usuario buscarUsuarioEmailAutenticado(String email){
        return usuarioRepositorio.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USUARIO_NAO_ENCONTRADO.getMensagem()));
    }

    public Usuario buscarUsuario(Long id){
        return usuarioRepositorio.findByIdUsuario(id).orElseThrow(() -> new RecursoNaoEncontrado(USUARIO_NAO_ENCONTRADO.getMensagem()));
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
        if(usuarioLogado != null && !usuarioLogado.isExcluido()){
            usuarioAoInserir.setUsuarioInclusao(usuarioLogado);
        } else {
            usuarioAoInserir.setUsuarioInclusao(usuarioAoInserir);
            usuarioAoInserir.setUsuarioAlteracao(usuarioAoInserir);
        }
    }

}
