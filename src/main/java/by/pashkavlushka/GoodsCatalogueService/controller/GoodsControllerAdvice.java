package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GoodsControllerAdvice {
    
    @ExceptionHandler(EntityException.class)
    public ResponseEntity<String> handleEntityException(EntityException e){
        return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
    }
}
