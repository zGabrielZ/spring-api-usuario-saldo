package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saque.SaqueViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.usuario.UsuarioViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaldo;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaque;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QUsuario;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConsultaService {

    private final QueryDslDAO queryDslDAO;

    public Page<SaldoViewDTO> saldosPorUsuario(Long idUsuario, PageRequest pageRequest) {

        QSaldo qSaldo = QSaldo.saldo;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSaldo.usuario.id.eq(idUsuario));

        List<SaldoViewDTO> result = queryDslDAO.query(q -> q.select(Projections.constructor(
                        SaldoViewDTO.class,
                        qSaldo.id,
                        qSaldo.deposito,
                        qSaldo.dataDeposito
                )))
                .from(qSaldo)
                .innerJoin(qSaldo.usuario)
                .where(booleanBuilder)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getSort(pageRequest.getSort(), QSaldo.saldo, qSaldo))
                .fetch();

        return new PageImpl<>(result, pageRequest, result.size());
    }

    public Page<SaqueViewDTO> saquesPorUsuario(Long idUsuario, PageRequest pageRequest){

        QSaque qSaque = QSaque.saque;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qSaque.usuario.id.eq(idUsuario));

        List<SaqueViewDTO> result = queryDslDAO.query(q -> q.select(Projections.constructor(
                            SaqueViewDTO.class,
                            qSaque.id,
                            qSaque.dataSaque,
                            qSaque.valor
                )))
                .from(qSaque)
                .innerJoin(qSaque.usuario)
                .where(booleanBuilder)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getSort(pageRequest.getSort(), QSaque.saque, qSaque))
                .fetch();

        return new PageImpl<>(result, pageRequest, result.size());
    }

    public Page<UsuarioViewDTO> listagem(PageRequest pageRequest){
        QUsuario qUsuario = QUsuario.usuario;

        List<UsuarioViewDTO> result = queryDslDAO.query(q -> q.select(Projections.constructor(
                            UsuarioViewDTO.class,
                            qUsuario.id,
                            qUsuario.nome,
                            qUsuario.email,
                            qUsuario.cpf,
                            qUsuario.dataNascimento
                    )))
                    .from(qUsuario)
                    .offset(pageRequest.getOffset())
                    .limit(pageRequest.getPageSize())
                    .orderBy(getSort(pageRequest.getSort(), QUsuario.usuario, qUsuario))
                    .fetch();

        return new PageImpl<>(result, pageRequest, result.size());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private OrderSpecifier<?>[] getSort(Sort sort, Object object, Path<?> qPath) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (Sort.Order order : sort.toList()) {
            Order orderQuery = order.getDirection().name().equals("ASC") ? Order.ASC : Order.DESC;
            SimplePath<Object> path = Expressions.path(object.getClass(), qPath, order.getProperty());
            orderSpecifiers.add(new OrderSpecifier(orderQuery, path));
        }
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }
}
