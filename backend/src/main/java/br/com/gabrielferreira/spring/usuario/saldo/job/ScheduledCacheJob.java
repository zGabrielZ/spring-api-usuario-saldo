package br.com.gabrielferreira.spring.usuario.saldo.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;

@Component
@Slf4j
public class ScheduledCacheJob {

    @CacheEvict(value = PERFIS, allEntries = true)
    // 3600000 deixa uma hora para limpar o cache
    @Scheduled(fixedDelay = 3000)
    public void limparCachePerfil(){
        log.info("Limpando o cache do {} - {}", PERFIS, LocalDateTime.now());
    }
}
