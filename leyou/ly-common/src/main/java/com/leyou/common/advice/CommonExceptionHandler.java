/*
信息:
*/
package com.leyou.common.advice;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler{
   @ExceptionHandler(LyException.class)
  public ResponseEntity<String> handlerException(LyException e){
     return ResponseEntity.status(e.getExceptionEnums().getCode()).body(e.getExceptionEnums().getMsg());
  }
}
