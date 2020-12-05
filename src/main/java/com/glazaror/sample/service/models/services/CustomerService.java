package com.glazaror.sample.service.models.services;

import com.glazaror.sample.service.models.dto.CustomerKpi;
import com.glazaror.sample.service.models.entity.Customer;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service definition for customers<br/>
 * Class: CustomerService<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
public interface CustomerService {

  List<Customer> findAll();

  Page<Customer> findAll(Pageable pageable);

  Customer findById(Long id);

  Customer save(Customer customer);

  CustomerKpi getCustomerKpi();

}
