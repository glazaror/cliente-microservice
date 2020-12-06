package com.glazaror.sample.service.models.repository;

import com.glazaror.sample.service.models.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * DAO definition to access customer data<br/>
 * Class: CustomerRepository<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  @Query("select max(p.age) from Customer p")
  Integer findMaxAge();
}
