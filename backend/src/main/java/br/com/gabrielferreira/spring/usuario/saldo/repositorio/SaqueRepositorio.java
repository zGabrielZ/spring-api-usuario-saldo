package br.com.gabrielferreira.spring.usuario.saldo.repositorio;
import br.com.gabrielferreira.spring.usuario.saldo.entidade.Saque;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaqueRepositorio extends JpaRepository<Saque,Long> {

    @Query("SELECT s FROM Saque s join s.usuario u where u.id = :idUsuario")
    List<Saque> buscarPorUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
