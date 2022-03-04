package com.example.demo.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class CustomExceptionHandler {



    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CustomExceptionMessage> exception(Exception exception) {
      
    	CustomExceptionMessage exceptionBean = new CustomExceptionMessage();
        if (exception instanceof CustomException) {
        	CustomException customException = (CustomException) exception;
        	exceptionBean.setErrorCode(customException.getErrorCode());
        	exceptionBean.setMessage(customException.getMessage());
            return ResponseEntity.status(exceptionBean.getErrorCode()).body(exceptionBean);

        }

        else if (exception.getClass().equals(ConstraintViolationException.class)) {
            ConstraintViolationException constraintViolationException= (javax.validation.ConstraintViolationException) exception;
            constraintViolationException.getConstraintViolations().forEach(c -> log.error(c));
            exceptionBean.setErrorCode(CustomExceptionMessages.INVALID_INPUT_DATA.getId());
            exceptionBean.setMessage(constraintViolationException.getMessage());
            log.error(exception);

            return  ResponseEntity.status(exceptionBean.getErrorCode()).body(exceptionBean);
        }
        else if (exception.getClass().equals(MethodArgumentNotValidException.class)) {
            log.error(exception);
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            exceptionBean.setErrorCode(CustomExceptionMessages.INVALID_INPUT_DATA.getId());
            exceptionBean
                    .setMessage(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());

            return ResponseEntity.status(exceptionBean.getErrorCode()).body(exceptionBean);
        }
        else {
        	exceptionBean.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        	exceptionBean.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionBean);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder message=new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
        	
        	
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            message.append(fieldName);
            message.append(":");
            message.append(errorMessage);
            message.append(",");
            
        });
        
        
        CustomExceptionMessage exceptionBean = new CustomExceptionMessage();
        exceptionBean.setErrorCode(400);
        exceptionBean.setMessage(message.toString().substring(0, message.toString().length()-1));
		return ResponseEntity.status(400).body(exceptionBean);
	}

}
