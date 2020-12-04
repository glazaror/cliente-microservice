package com.glazaror.sample.service.models.dto;

import java.io.Serializable;

public class ClienteKPI implements Serializable {
  private static final long serialVersionUID = 1L;

  private Double promedioEdades;
  private Double desviacionEstandarEdades;

  public ClienteKPI(Double promedioEdades, Double desviacionEstandarEdades) {
    this.promedioEdades = promedioEdades;
    this.desviacionEstandarEdades = desviacionEstandarEdades;
  }

  public Double getPromedioEdades() {
    return promedioEdades;
  }

  public void setPromedioEdades(Double promedioEdades) {
    this.promedioEdades = promedioEdades;
  }

  public Double getDesviacionEstandarEdades() {
    return desviacionEstandarEdades;
  }

  public void setDesviacionEstandarEdades(Double desviacionEstandarEdades) {
    this.desviacionEstandarEdades = desviacionEstandarEdades;
  }
}
