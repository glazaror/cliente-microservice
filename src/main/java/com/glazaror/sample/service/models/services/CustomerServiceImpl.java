package com.glazaror.sample.service.models.services;

import static org.apache.commons.math3.util.Precision.round;

import com.glazaror.sample.service.models.dto.CustomerKpi;
import com.glazaror.sample.service.models.dto.CustomerWithProjectionDto;
import com.glazaror.sample.service.models.entity.Customer;
import com.glazaror.sample.service.models.exception.BusinessException;
import com.glazaror.sample.service.models.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.dao.DataAccessException;
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
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerDao;

  private static final int DECIMAL_STATISTICS_PRECISION = 2;

  @Override
  @Transactional(readOnly = true)
  public List<Customer> findAll() throws BusinessException {
    try {
      return customerDao.findAll();
    } catch (DataAccessException e) {
      log.error("There was an error at querying the customer data", e);
      throw new BusinessException("There was an error at querying the customer data");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Customer> findAll(Pageable pageable) throws BusinessException {
    try {
      return customerDao.findAll(pageable);
    } catch (DataAccessException e) {
      log.error("There was an error at querying the customer data", e);
      throw new BusinessException("There was an error at querying the customer data");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CustomerWithProjectionDto> findById(Long id) throws BusinessException {
    try {
      var customer = customerDao.findById(id).orElse(null);
      if (customer == null) {
        return Optional.empty();
      }
      return Optional.of(CustomerWithProjectionDto.builder()
          .name(customer.getName())
          .lastName(customer.getLastName())
          .age(customer.getAge())
          .birthDate(customer.getBirthDate())
          .id(customer.getId())
          .calculatedDeathDate(DateUtils.addYears(customer.getBirthDate(),
              customerDao.findMaxAge()))
          .build());

    } catch (DataAccessException e) {
      log.error("There was an error at querying the customer data", e);
      throw new BusinessException("There was an error at querying the customer data");
    }
  }

  @Override
  @Transactional
  public Customer save(Customer customer) throws BusinessException {
    try {
      return customerDao.save(customer);
    } catch (DataAccessException e) {
      log.error("There was an error at saving customer", e);
      throw new BusinessException("There was an error at saving customer");
    }
  }

  @Override
  public Optional<CustomerKpi> getCustomerKpi() throws BusinessException {
    var customers = customerDao.findAll();
    if (customers.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new CustomerKpi(
        round(customers.stream()
            .mapToDouble(Customer::getAge)
            .average()
            .orElse(0), DECIMAL_STATISTICS_PRECISION),
        round(new DescriptiveStatistics(customers.stream()
            .mapToDouble(Customer::getAge).toArray())
            .getStandardDeviation(), DECIMAL_STATISTICS_PRECISION)));
  }

}
