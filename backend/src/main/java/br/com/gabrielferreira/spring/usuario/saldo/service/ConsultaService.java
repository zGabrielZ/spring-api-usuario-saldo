package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.consulta.ConsultaDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.ConsultaDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.*;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo.ConsultaSaldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo.ConsultaSaque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo.ConsultaUsuario;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.spring.usuario.saldo.utils.LoginUsuarioUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;


@RequiredArgsConstructor
@Service
public class ConsultaService {

    private final LoginUsuarioUtils loginUsuarioUtils;

    private final QueryDslDAO queryDslDAO;

    private final UsuarioRepositorio usuarioRepositorio;

    public Page<SaldoViewDTO> saldosPorUsuario(Long idUsuario, Integer pagina, Integer quantidadeRegistro, String[] sort) {

//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//        boolean isUsuarioLogadoPerfilCliente = perfilService.isContemPerfilClienteUsuarioLogado();
//        if(isUsuarioLogadoPerfilCliente && !usuarioLogado.getId().equals(idUsuario)){
//            throw new ExcecaoPersonalizada(LISTA_SALDOS.getMensagem());
//        }

        List<Sort.Order> orders = getOrders(sort);
        PageRequest pageRequest = PageRequest.of(pagina, quantidadeRegistro, Sort.by(orders));

        QSaldo qSaldo = QSaldo.saldo;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSaldo.usuario.id.eq(idUsuario));

        List<ConsultaDTO> dadosConsulta = ConsultaDTOFactory.getConsultas("Saldo", pageRequest.getSort().toList());

        List<SaldoViewDTO> result = queryDslDAO.query(q -> q.select(Projections.constructor(
                        SaldoViewDTO.class,
                        qSaldo.id.as(ConsultaSaldo.ID_ALIAS),
                        qSaldo.deposito.as(ConsultaSaldo.DEPOSITO_ALIAS),
                        qSaldo.dataDeposito.as(ConsultaSaldo.DATA_ALIAS)
                )))
                .from(qSaldo)
                .innerJoin(qSaldo.usuario)
                .where(booleanBuilder)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getSort(dadosConsulta))
                .fetch();

        return new PageImpl<>(result, pageRequest, result.size());
    }

    public Page<SaqueViewDTO> saquesPorUsuario(Long idUsuario, Integer pagina, Integer quantidadeRegistro, String[] sort){

//        Usuario usuarioLogado = perfilService.recuperarUsuarioLogado();
//        boolean isUsuarioLogadoPerfilAdmin = perfilService.isContemPerfilAdminUsuarioLogado();
//        verificarUsuarioLogado(usuarioLogado, isUsuarioLogadoPerfilAdmin, idUsuario);

        QSaque qSaque = QSaque.saque;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSaque.usuario.id.eq(idUsuario));

        List<Sort.Order> orders = getOrders(sort);
        PageRequest pageRequest = PageRequest.of(pagina, quantidadeRegistro, Sort.by(orders));

        List<ConsultaDTO> dadosConsulta = ConsultaDTOFactory.getConsultas("Saque", pageRequest.getSort().toList());

        List<SaqueViewDTO> result = queryDslDAO.query(q -> q.select(Projections.constructor(
                            SaqueViewDTO.class,
                            qSaque.id.as(ConsultaSaque.ID_ALIAS),
                            qSaque.dataSaque.as(ConsultaSaque.DATA_ALIAS),
                            qSaque.valor.as(ConsultaSaque.VALOR_ALIAS)
                )))
                .from(qSaque)
                .innerJoin(qSaque.usuario)
                .where(booleanBuilder)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getSort(dadosConsulta))
                .fetch();

        return new PageImpl<>(result, pageRequest, result.size());
    }

    public Page<UsuarioViewDTO> listagem(Integer pagina, Integer quantidadeRegistro, String[] sort){

        QUsuario qUsuario = QUsuario.usuario;
        QPerfil qPerfil = QPerfil.perfil;

        List<Sort.Order> orders = getOrders(sort);
        PageRequest pageRequest = PageRequest.of(pagina, quantidadeRegistro, Sort.by(orders));

        List<ConsultaDTO> dadosConsulta = ConsultaDTOFactory.getConsultas("Usuario", pageRequest.getSort().toList());

        List<UsuarioViewDTO> result = queryDslDAO.query(q -> q.select()
                        .offset(pageRequest.getOffset())
                        .limit(pageRequest.getPageSize())
                        .orderBy(getSort(dadosConsulta)))
                .from(qUsuario)
                .innerJoin(qUsuario.perfis, qPerfil)
                .transform(
                        groupBy(qUsuario.id)
                                .list(Projections.constructor(
                                        UsuarioViewDTO.class,
                                        qUsuario.id.as(ConsultaUsuario.ID_ALIAS),
                                        qUsuario.nome.as(ConsultaUsuario.NOME_ALIAS),
                                        qUsuario.email.as(ConsultaUsuario.EMAIL_ALIAS),
                                        qUsuario.cpf.as(ConsultaUsuario.CPF_ALIAS),
                                        qUsuario.dataNascimento.as(ConsultaUsuario.DATA_NASCIMENTO_ALIAS),
                                        list(
                                                Projections.constructor(
                                                        PerfilViewDTO.class,
                                                        qPerfil.id.as(ConsultaUsuario.ID_PERFIL_ALIAS),
                                                        qPerfil.descricao.as(ConsultaUsuario.PERFIL_DESCRICAO)
                                                )
                                        )
                                ))
                );

        return new PageImpl<>(result, pageRequest, result.size());
    }

    private OrderSpecifier<?>[] getSort(List<ConsultaDTO> consultas) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (ConsultaDTO consulta : consultas) {

            String nome = consulta.getPath().getClass().getSimpleName();
            Order order = consulta.getDirecao().equals("ASC") ? Order.ASC : Order.DESC;

            switch (nome){
                case "StringPath" -> orderSpecifiers.add(new OrderSpecifier<>(order, Expressions.stringPath(consulta.getAlias())));
                case "NumberPath" -> {
                    Class<?> type = consulta.getPath().getType();
                    NumberPath<?> numberPath = null;
                    if(type.equals(Long.class)){
                        numberPath = Expressions.numberPath(Long.class, consulta.getAlias());
                    } else if(type.equals(BigDecimal.class)){
                        numberPath = Expressions.numberPath(BigDecimal.class, consulta.getAlias());
                    }
                    orderSpecifiers.add(new OrderSpecifier<>(order, numberPath));
                }
                case "DatePath" -> {
                    Class<?> type = consulta.getPath().getType();
                    DatePath<?> datePath = null;
                    if(type.equals(LocalDate.class)){
                        datePath = Expressions.datePath(LocalDate.class, consulta.getAlias());
                    }
                    orderSpecifiers.add(new OrderSpecifier<>(order, datePath));
                }
                case "DateTimePath" -> {
                    Class<?> type = consulta.getPath().getType();
                    DateTimePath<?> dateTimePath = null;
                    if(type.equals(LocalDateTime.class)){
                        dateTimePath = Expressions.dateTimePath(LocalDate.class, consulta.getAlias());
                    }
                    orderSpecifiers.add(new OrderSpecifier<>(order, dateTimePath));
                }
                default -> throw new ExcecaoPersonalizada(REALIZAR_CONSULTA.getMensagem());
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

    private void verificarUsuarioLogado(Usuario usuarioLogado, boolean isUsuarioAdmin, Long idUsuario){
        if(usuarioLogado != null && !isUsuarioAdmin && !usuarioLogado.getId().equals(idUsuario)){
            throw new ExcecaoPersonalizada(LISTA_SAQUES.getMensagem());
        }
    }

    private List<Sort.Order> getOrders(String[] sorts){
        List<Sort.Order> orders = new ArrayList<>();
        if (sorts != null && sorts.length != 0 && sorts[0].contains(",")) {
            for (String sort : sorts) {
                String[] dados = sort.split(",");
                Sort.Order order = new Sort.Order(verificarDirecao(dados.length != 2 ? null : dados[1]), dados[0]);
                orders.add(order);
            }
        } else if(sorts != null && sorts.length != 0 && !sorts[0].contains(",")){
            Sort.Order order = new Sort.Order(verificarDirecao(sorts.length != 2 ? null : sorts[1]), sorts[0]);
            orders.add(order);
        }
        return orders;
    }

    private Sort.Direction verificarDirecao(String direcao){
        if(StringUtils.isBlank(direcao)){
            throw new ExcecaoPersonalizada(DIRECAO_VAZIA.getMensagem());
        }
        return Sort.Direction.fromOptionalString(direcao)
                .orElseThrow(() -> new ExcecaoPersonalizada(DIRECAO_INCORRETA.getMensagem()));
    }

}
