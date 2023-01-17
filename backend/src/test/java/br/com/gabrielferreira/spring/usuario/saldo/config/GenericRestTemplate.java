//package br.com.gabrielferreira.spring.usuario.saldo.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class GenericRestTemplate {
//
//    public <T> ResponseEntity<T> genericRequest(Object in, ParameterizedTypeReference<T> responseType, MediaType mediaType, HttpMethod httpMethod, String urlProxy, String authorization, TestRestTemplate testRestTemplate){
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(mediaType);
//        headers.set("Authorization", authorization);
//
//        HttpEntity<Object> request = new HttpEntity<>(in, headers);
//        return testRestTemplate.exchange(urlProxy, httpMethod, request, responseType);
//    }
//
//}
