package com.glazaror.sample.service.models.exception;

/**
 * Business Exception that encapsulates business errors<br/>
 * Class: BusinessException<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }
}
