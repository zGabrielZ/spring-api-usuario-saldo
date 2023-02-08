package br.com.gabrielferreira.spring.usuario.saldo.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class KeyCustomizadaGenerator implements KeyGenerator {

    @Override
    @NonNull
    public Object generate(@NonNull Object target,@NonNull Method method,@NonNull Object... params) {
        return String.format("%s_%s_%s",
                target.getClass().getSimpleName(),
                method.getName(),
                StringUtils.arrayToDelimitedString(params, "_"));
    }
}
