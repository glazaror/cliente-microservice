package com.glazaror.sample.service.models.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Customer projection representation <br/>
 * Class: CustomerWithProjectionDto<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
@Getter
@Builder
@RequiredArgsConstructor
public class CustomerWithProjectionDto implements Serializable {
  private static final long serialVersionUID = 3000L;

  private final Long id;
  private final String name;
  private final String lastName;
  private final Integer age;
  private final Date birthDate;
  private final Date calculatedDeathDate;

}
