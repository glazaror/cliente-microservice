package com.glazaror.sample.service.models.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * DTO for KPI about customers age<br/>
 * Class: CustomerKPI<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
@RequiredArgsConstructor
@Getter
public class CustomerKpi implements Serializable {
  private static final long serialVersionUID = 2000L;

  private final Double averageAge;
  private final Double standardDeviationAge;

}
