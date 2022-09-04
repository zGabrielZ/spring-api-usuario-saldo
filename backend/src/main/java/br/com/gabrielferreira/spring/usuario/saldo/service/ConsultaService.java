package br.com.gabrielferreira.spring.usuario.saldo.service;
import br.com.gabrielferreira.spring.usuario.saldo.dao.QueryDslDAO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.dto.saldo.SaldoViewDTO;
import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.QSaldo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConsultaService {

    private final QueryDslDAO queryDslDAO;

    // Fazer um endpoint paginada com nome usuario, cpf, dataNascimento, total de saques, total de depositos, total de saldo
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
