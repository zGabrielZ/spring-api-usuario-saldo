package br.com.gabrielferreira.spring.usuario.saldo.dao;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.function.Function;

@Component
public class QueryDslDAO {

    private final EntityManager entityManager;


    public QueryDslDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> T query(Function<JPAQuery<?>, T> function) {
        return function.apply(new JPAQuery<>(this.entityManager));
    }

}
