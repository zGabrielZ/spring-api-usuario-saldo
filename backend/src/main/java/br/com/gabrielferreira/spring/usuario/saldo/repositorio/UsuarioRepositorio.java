package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u.email as email FROM Usuario u where u.email = :email")
    Optional<String> existsEmail(@Param("email") String email);

    @Query("SELECT u.cpf as cpf FROM Usuario u where u.cpf = :cpf")
    Optional<String> existsCpf(@Param("cpf") String cpf);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.excluido = :excluido and u.id = :id")
    Optional<Usuario> findByIdUsuario(@Param("id") Long id, @Param("excluido") boolean excluido);

}
