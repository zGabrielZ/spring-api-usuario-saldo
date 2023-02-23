package br.com.gabrielferreira.spring.usuario.saldo.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static br.com.gabrielferreira.spring.usuario.saldo.utils.ConstantesUtils.*;

@Component
@Slf4j
public class ScheduledCacheJob {

    private static final String MSG_CACHE = "Limpando o cache do {}";

    @CacheEvict(value = PERFIS, allEntries = true)
    @Scheduled(fixedDelay = 3600000)
    public void limparCachePerfil(){
        log.info(MSG_CACHE, PERFIS);
    }

    @CacheEvict(value = USUARIOS, allEntries = true)
    @Scheduled(fixedDelay = 3600000)
    public void limparCacheUsuariosAutenticados(){
        log.info(MSG_CACHE, USUARIOS);
    }

    @CacheEvict(value = SITUACOES, allEntries = true)
    @Scheduled(fixedDelay = 3600000)
    public void limparCacheSituacao(){
        log.info(MSG_CACHE, SITUACOES);
    }
}
