package com.glazaror.sample.service.models.services;

import static org.apache.commons.math3.util.Precision.round;

import com.glazaror.sample.service.models.dao.CustomerDao;
import com.glazaror.sample.service.models.dto.CustomerKpi;
import com.glazaror.sample.service.models.entity.Customer;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for customers<br/>
 * Class: CustomerService<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerDao customerDao;

  @Override
  @Transactional(readOnly = true)
  public List<Customer> findAll() {
    return customerDao.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Customer> findAll(Pageable pageable) {
    return customerDao.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Customer findById(Long id) {
    return customerDao.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public Customer save(Customer customer) {
    return customerDao.save(customer);
  }

  @Override
  public CustomerKpi getCustomerKpi() {
    List<Customer> customers = customerDao.findAll();
    return new CustomerKpi(
        round(customers.stream()
            .mapToDouble(Customer::getAge)
            .average()
            .orElse(0), 2),
        round(new DescriptiveStatistics(customers.stream()
            .mapToDouble(Customer::getAge).toArray())
            .getStandardDeviation(), 2));
  }

}
