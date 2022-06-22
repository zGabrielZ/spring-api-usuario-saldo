package br.com.gabrielferreira.spring.usuario.saldo.exception.hanlder;

import br.com.gabrielferreira.spring.usuario.saldo.exception.modelo.ErroFormulario;
import br.com.gabrielferreira.spring.usuario.saldo.exception.modelo.ErroPadrao;
import br.com.gabrielferreira.spring.usuario.saldo.exception.ExcecaoPersonalizada;
import br.com.gabrielferreira.spring.usuario.saldo.exception.RecursoNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class InterceptadorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> erroValidacao(MethodArgumentNotValidException e){

        // Percorrendo na lista se tem algum erro de validação
        List<ErroFormulario> erroFormularios = new ArrayList<>();
        for(FieldError fieldError : e.getBindingResult().getFieldErrors()){
            ErroFormulario erroFormulario = new ErroFormulario(fieldError.getField(),fieldError.getDefaultMessage());
            erroFormularios.add(erroFormulario);
        }

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErroPadrao erroPadrao = new ErroPadrao(LocalDateTime.now(),httpStatus.value(),"Verifque os erros","Erros encontrados após realizar a requisição",erroFormularios);
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(ExcecaoPersonalizada.class)
    public ResponseEntity<ErroPadrao> excessaoPersonalizada(ExcecaoPersonalizada e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErroPadrao erroPadrao = new ErroPadrao(LocalDateTime.now(),httpStatus.value(),"Ocorreu um erro",e.getMessage(),new ArrayList<>());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }

    @ExceptionHandler(RecursoNaoEncontrado.class)
    public ResponseEntity<ErroPadrao> entidadeNaoEncontrada(RecursoNaoEncontrado e){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErroPadrao erroPadrao = new ErroPadrao(LocalDateTime.now(),httpStatus.value(),"Entidade não encontrada",e.getMessage(),new ArrayList<>());
        return ResponseEntity.status(httpStatus).body(erroPadrao);
    }
}
