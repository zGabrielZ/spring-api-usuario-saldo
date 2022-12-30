package br.com.gabrielferreira.spring.usuario.saldo.exception.handler;
import br.com.gabrielferreira.spring.usuario.saldo.exception.modelo.ErroPadrao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ControllerAdvice
@Slf4j
public class ServiceHandlerAutenticacao implements AuthenticationEntryPoint {

    private static final String MSG = "Você precisa fazer login primeiro para executar esta função";


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn(MSG);

        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());

        ErroPadrao erroPadrao = new ErroPadrao(LocalDateTime.now(),httpStatus.value(),"Verifique o erro abaixo"
                ,MSG,new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(erroPadrao);

        response.getWriter().write(json);
    }
}
