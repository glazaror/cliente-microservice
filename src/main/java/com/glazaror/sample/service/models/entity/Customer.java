package com.glazaror.sample.service.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Customer Entity<br/>
 * Class: Customer<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
@Entity
@Data
@Table(name = "customer")
public class Customer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "it cannot be empty")
  @Size(min = 4, max = 12, message = "the size should be between 4 and 12")
  @Column(nullable = false)
  private String name;

  @NotEmpty(message = "it cannot be empty")
  @Column(name = "last_name")
  private String lastName;

  @NotNull(message = "cannot be null")
  @Column(nullable = false)
  private Integer age;

  @NotNull(message = "it cannot be empty")
  @Column(name = "birth_date")
  @Temporal(TemporalType.DATE)
  private Date birthDate;

  private static final long serialVersionUID = 1L;
}
