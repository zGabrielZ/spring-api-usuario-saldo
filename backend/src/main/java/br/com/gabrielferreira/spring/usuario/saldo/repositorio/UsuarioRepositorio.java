package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.id = :id")
    Optional<Usuario> findByUsuarioAutenticado(Long id);

}
