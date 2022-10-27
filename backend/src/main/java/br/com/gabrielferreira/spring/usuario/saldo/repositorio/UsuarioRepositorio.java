package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    @Query(value = "SELECT u FROM Usuario u " +
            "JOIN FETCH u.perfis p ",
            countQuery = "SELECT count(u) FROM Usuario u " +
                    "JOIN u.perfis p ")
    Page<Usuario> buscarUsuarios(Pageable pageable);
}
