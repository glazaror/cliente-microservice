package com.glazaror.sample.service.models.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.glazaror.sample.service.models.entity.Customer;
import com.glazaror.sample.service.models.exception.BusinessException;
import com.glazaror.sample.service.models.repository.CustomerRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Customer service tests
 * Class: CustomerServiceImplTests<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTests {

  @Mock
  CustomerRepository repositoryThatThrowsException;

  @Mock
  CustomerRepository repositoryThatReturnsData;

  @Mock
  Customer customer;

  @Mock
  Pageable pageable;

  @Mock
  Page page;

  @Test
  public void givenADataAccessException_whenFindAll_thenReturnBusinessException() {
    var customerService = new CustomerServiceImpl(repositoryThatThrowsException);

    when(repositoryThatThrowsException.findAll()).thenThrow(mock(DataAccessException.class));
    assertThrows(BusinessException.class, customerService::findAll);
  }

  @Test
  public void givenADataAccessException_whenFindAllByPage_thenReturnBusinessException() {
    var customerService = new CustomerServiceImpl(repositoryThatThrowsException);

    when(repositoryThatThrowsException.findAll(pageable)).thenThrow(mock(DataAccessException.class));
    assertThrows(BusinessException.class, () -> customerService.findAll(pageable));
  }

  @Test
  public void givenADataAccessException_whenFindById_thenReturnBusinessException() {
    var customerService = new CustomerServiceImpl(repositoryThatThrowsException);

    when(repositoryThatThrowsException.findById(any())).thenThrow(mock(DataAccessException.class));
    assertThrows(BusinessException.class, () -> customerService.findById(10L));
  }

  @Test
  public void givenCurrentData_whenFindAll_thenReturnAllCurrentData() {
    var customerService = new CustomerServiceImpl(repositoryThatReturnsData);

    List<Customer> customers = new ArrayList<>();
    IntStream.range(0, 10).forEach(i -> customers.add(customer));

    when(repositoryThatReturnsData.findAll()).thenReturn(customers);

    assertEquals(10, customerService.findAll().size());
  }

  @Test
  public void givenCurrentData_whenFindAllByPage_thenReturnAllCurrentDataByPage() {
    var customerService = new CustomerServiceImpl(repositoryThatReturnsData);
    when(page.getTotalElements()).thenReturn(10L);
    when(repositoryThatReturnsData.findAll(any(Pageable.class))).thenReturn(page);

    assertEquals(10L, customerService.findAll(pageable).getTotalElements());
  }

  @Test
  public void givenCurrentData_whenFindById_thenReturnCustomerProjection() {
    var customerService = new CustomerServiceImpl(repositoryThatReturnsData);

    var birthDate = java.util.Date.from(
        LocalDate.of(2000, 4, 20).atStartOfDay()
          .atZone(ZoneId.systemDefault())
          .toInstant());

    var expectedFinishDate = java.util.Date.from(
        LocalDate.of(2090, 4, 20).atStartOfDay()
          .atZone(ZoneId.systemDefault())
          .toInstant());

    when(customer.getAge()).thenReturn(20);
    when(customer.getBirthDate()).thenReturn(birthDate);
    when(customer.getId()).thenReturn(10L);
    when(customer.getName()).thenReturn("any-name");
    when(customer.getLastName()).thenReturn("any-last-name");

    when(repositoryThatReturnsData.findById(any())).thenReturn(Optional.of(customer));
    when(repositoryThatReturnsData.findMaxAge()).thenReturn(90);

    var customerProjection = customerService.findById(20L);
    assertTrue(customerProjection.isPresent());
    assertEquals(expectedFinishDate, customerService.findById(20L).get().getCalculatedDeathDate());
  }
}
