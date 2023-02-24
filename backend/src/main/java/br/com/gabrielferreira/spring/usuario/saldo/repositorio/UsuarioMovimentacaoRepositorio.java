package br.com.gabrielferreira.spring.usuario.saldo.repositorio;

import br.com.gabrielferreira.spring.usuario.saldo.dominio.entidade.UsuarioMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioMovimentacaoRepositorio extends JpaRepository<UsuarioMovimentacao, Long> {

}
