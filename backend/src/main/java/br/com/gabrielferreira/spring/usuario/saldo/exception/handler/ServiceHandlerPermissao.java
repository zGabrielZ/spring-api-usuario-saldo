package br.com.gabrielferreira.spring.usuario.saldo.exception.handler;
import br.com.gabrielferreira.spring.usuario.saldo.exception.modelo.ErroPadrao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@ControllerAdvice
public class ServiceHandlerPermissao implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());

        ErroPadrao erroPadrao = new ErroPadrao(LocalDateTime.now(),httpStatus.value(),"Verifique o erro abaixo"
                ,"Você não tem a permissão de realizar esta ação",new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(erroPadrao);

        response.getWriter().write(json);
    }
}
