package br.com.gabrielferreira.spring.usuario.saldo.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtils {

    private JPAUtils(){}

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("tests");

    public static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
