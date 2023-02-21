package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.consulta.ConsultaDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.factory.ConsultaDTOFactory;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.pefil.PerfilViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioDepositoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioSaldoRelatorioDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioSaldoRelatorioFiltroDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.*;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.modelo.*;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ValidacaoEnum.*;
import static com.querydsl.core.group.GroupBy.*;


@RequiredArgsConstructor
@Service
public class ConsultaService {

    private final QueryDslDAO queryDslDAO;

    public Page<SaldoViewDTO> saldosPorUsuario(Long idUsuario, Integer pagina, Integer quantidadeRegistro, String[] sort) {

        List<Sort.Order> orders = getOrders(sort);
        PageRequest pageRequest = PageRequest.of(pagina, quantidadeRegistro, Sort.by(orders));

        QSaldo qSaldo = QSaldo.saldo;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSaldo.usuario.id.eq(idUsuario));
        booleanBuilder.and(qSaldo.usuario.excluido.eq(false));

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

        QSaque qSaque = QSaque.saque;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSaque.usuario.id.eq(idUsuario));
        booleanBuilder.and(qSaque.usuario.excluido.eq(false));

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

        List<Sort.Order> orders = getOrders(sort);
        PageRequest pageRequest = PageRequest.of(pagina, quantidadeRegistro, Sort.by(orders));

        QUsuario qUsuario = QUsuario.usuario;
        QUsuario qUsuarioSubQuery = QUsuario.usuario;
        QPerfil qPerfil = QPerfil.perfil;

        List<ConsultaDTO> dadosConsulta = ConsultaDTOFactory.getConsultas("Usuario", pageRequest.getSort().toList());

        Expression<Long> usuarioId = ExpressionUtils.as(qUsuarioSubQuery.id, ConsultaUsuario.ID_ALIAS);
        Expression<String> usuarioNome = ExpressionUtils.as(qUsuarioSubQuery.nome, ConsultaUsuario.NOME_ALIAS);
        Expression<String> usuarioEmail = ExpressionUtils.as(qUsuarioSubQuery.email, ConsultaUsuario.EMAIL_ALIAS);
        Expression<String> usuarioCpf = ExpressionUtils.as(qUsuarioSubQuery.cpf, ConsultaUsuario.CPF_ALIAS);
        Expression<LocalDate> usuarioNascimento = ExpressionUtils.as(qUsuarioSubQuery.dataNascimento, ConsultaUsuario.DATA_NASCIMENTO_ALIAS);

        List<Tuple> tuplesUsuarioSubQuery = queryDslDAO.query(q -> q.select(usuarioId, usuarioNome, usuarioEmail, usuarioCpf ,usuarioNascimento)).from(qUsuarioSubQuery)
                .where(qUsuarioSubQuery.excluido.eq(false))
                .offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).orderBy(getSort(dadosConsulta)).fetch();

        List<Long> ids = new ArrayList<>();
        tuplesUsuarioSubQuery.forEach(tuple -> ids.add(tuple.get(0, Long.class)));

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qUsuario.id.in(ids));

        List<ConsultaDTO> dadosConsulta2 = ConsultaDTOFactory.getConsultas("Perfil", pageRequest.getSort().toList());
        dadosConsulta.addAll(dadosConsulta2);

        List<UsuarioViewDTO> result = queryDslDAO.query(JPAQuery::select)
                .from(qUsuario)
                .innerJoin(qUsuario.perfis, qPerfil)
                .where(booleanBuilder)
                .orderBy(getSort(dadosConsulta))
                .transform(
                        groupBy(qUsuario.id)
                                .list(Projections.constructor(
                                        UsuarioViewDTO.class,
                                        usuarioId,
                                        usuarioNome,
                                        usuarioEmail,
                                        usuarioCpf,
                                        usuarioNascimento,
                                        set(
                                                Projections.constructor(
                                                        PerfilViewDTO.class,
                                                        qPerfil.id.as(ConsultaPerfil.ID_PERFIL_ALIAS),
                                                        qPerfil.descricao.as(ConsultaPerfil.PERFIL_DESCRICAO)
                                                )
                                        )
                                ))
                );

        return new PageImpl<>(result, pageRequest, result.size());
    }

    public Page<UsuarioSaldoRelatorioDTO> consultaUltimosSaldosUsuariosAtivos(UsuarioSaldoRelatorioFiltroDTO filtros, Integer pagina, Integer quantidadeRegistro, String[] sort){

        List<Sort.Order> orders = getOrders(sort);
        PageRequest pageRequest = PageRequest.of(pagina, quantidadeRegistro, Sort.by(orders));

        QUsuario qUsuario = QUsuario.usuario;
        QSaldo qSaldo = QSaldo.saldo;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        verificarFiltroUsuarioAtivos(booleanBuilder, filtros, qUsuario, qSaldo);
        booleanBuilder.and(qUsuario.excluido.eq(false));

        List<ConsultaDTO> dadosConsulta = ConsultaDTOFactory.getConsultas("Depositos Usuários Ativos", pageRequest.getSort().toList());

        List<UsuarioSaldoRelatorioDTO> result = queryDslDAO.query(q -> q.select(Projections.constructor(
                        UsuarioSaldoRelatorioDTO.class,
                        Projections.constructor(
                                UsuarioDepositoViewDTO.class,
                                qUsuario.id.as(ConsultaUsuarioDepositos.ID_USUARIO_ALIAS),
                                qUsuario.nome.as(ConsultaUsuarioDepositos.NOME_USUARIO_ALIAS),
                                qUsuario.email.as(ConsultaUsuarioDepositos.EMAIL_USUARIO_ALIAS),
                                qUsuario.cpf.as(ConsultaUsuarioDepositos.CPF_USUARIO_ALIAS)
                        ),
                        Projections.constructor(
                                SaldoViewDTO.class,
                                qSaldo.id.as(ConsultaUsuarioDepositos.ID_SALDO_ALIAS),
                                qSaldo.deposito.as(ConsultaUsuarioDepositos.DEPOSITO_SALDO_ALIAS),
                                qSaldo.dataDeposito.as(ConsultaUsuarioDepositos.DATA_SALDO_ALIAS)
                        ),
                        Projections.constructor(
                                UsuarioDepositoViewDTO.class,
                                qSaldo.usuarioDepositante.id.as(ConsultaUsuarioDepositos.ID_USUARIO_DEPOSITANTE_ALIAS),
                                qSaldo.usuarioDepositante.nome.as(ConsultaUsuarioDepositos.NOME_USUARIO_DEPOSITANTE_ALIAS),
                                qSaldo.usuarioDepositante.email.as(ConsultaUsuarioDepositos.EMAIL_USUARIO_DEPOSITANTE_ALIAS),
                                qSaldo.usuarioDepositante.cpf.as(ConsultaUsuarioDepositos.CPF_USUARIO_DEPOSITANTE_ALIAS)
                        )
                )))
                .from(qUsuario)
                .innerJoin(qUsuario.saldos, qSaldo)
                .innerJoin(qSaldo.usuarioDepositante)
                .where(booleanBuilder)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getSort(dadosConsulta))
                .fetch();

        return new PageImpl<>(result, pageRequest, result.size());
    }

    private void verificarFiltroUsuarioAtivos(BooleanBuilder booleanBuilder, UsuarioSaldoRelatorioFiltroDTO filtros, QUsuario qUsuario, QSaldo qSaldo){
        if(filtros.isContemNome()){
            booleanBuilder.and(qUsuario.nome.likeIgnoreCase(Expressions.asString("%").concat(filtros.getNome().trim()).concat("%")));
        }

        if(filtros.isContemNomeDepositante()){
            booleanBuilder.and(qSaldo.usuarioDepositante.nome.likeIgnoreCase(Expressions.asString("%").concat(filtros.getNomeUsuarioDepositante().trim()).concat("%")));
        }

        if(filtros.isContemEmail()){
            booleanBuilder.and(qUsuario.email.eq(filtros.getEmail().trim()));
        }

        if(filtros.isContemEmailDepositante()){
            booleanBuilder.and(qSaldo.usuarioDepositante.email.eq(filtros.getEmailUsuarioDepositante().trim()));
        }

        if(filtros.isContemCpf()){
            booleanBuilder.and(qUsuario.cpf.eq(filtros.getCpf().trim()));
        }

        if(filtros.isContemCpfDepositante()){
            booleanBuilder.and(qSaldo.usuarioDepositante.cpf.eq(filtros.getCpfUsuarioDepositante().trim()));
        }

        if(filtros.isContemSaldoDeposito()){
            booleanBuilder.and(qSaldo.deposito.goe(filtros.getSaldoDeposito()));
        }

        if(filtros.isContemDataInicioDeposito()){
            LocalDateTime dataInicio = filtros.getDataInicioDeposito().atTime(0,0,0,0);
            booleanBuilder.and(qSaldo.dataDeposito.goe(dataInicio));
        }

        if(filtros.isContemDataFinalDeposito()){
            LocalDateTime dataFinal = filtros.getDataFinalDeposito().atTime(23,59,59,999);
            booleanBuilder.and(qSaldo.dataDeposito.loe(dataFinal));
        }
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
