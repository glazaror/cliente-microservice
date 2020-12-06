package com.glazaror.sample.service.models.services;

import com.glazaror.sample.service.models.dto.CustomerKpi;
import com.glazaror.sample.service.models.dto.CustomerWithProjectionDto;
import com.glazaror.sample.service.models.entity.Customer;
import com.glazaror.sample.service.models.exception.BusinessException;
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

  List<Customer> findAll() throws BusinessException;

  Page<Customer> findAll(Pageable pageable) throws BusinessException;

  CustomerWithProjectionDto findById(Long id) throws BusinessException;

  Customer save(Customer customer) throws BusinessException;

  CustomerKpi getCustomerKpi() throws BusinessException;

}
