package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.Saldo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaldoRepositorio extends JpaRepository<Saldo,Long> {

    @Query("SELECT s FROM Saldo s join s.usuario u where u.id = :idUsuario")
    List<Saldo> buscarPorUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);

    List<Saldo> findByUsuarioId(Long idUsuario);
}
