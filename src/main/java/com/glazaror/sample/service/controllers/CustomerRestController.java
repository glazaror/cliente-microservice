package com.glazaror.sample.service.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.glazaror.sample.service.models.dto.CustomerKpi;
import com.glazaror.sample.service.models.entity.Customer;
import com.glazaror.sample.service.models.services.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer controller<br/>
 * Class: CustomerController<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {

  @Autowired
  private CustomerService customerService;

  /**
   * Return all customers.
   * @return all customers.
   */
  @GetMapping
  public List<Customer> getCustomers() {
    return customerService.findAll();
  }

  /**
   * Find a customer by his/her identifier.
   * @return found customer.
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    Map<String, Object> response = new HashMap<>();

    try {
      Customer customer = customerService.findById(id);
      if (customer == null) {
        response.put("message", "The customer ID: " + id + " doesn't exist!");
        return new ResponseEntity<>(response, NOT_FOUND);
      }
      return new ResponseEntity<>(customer, OK);

    } catch (DataAccessException e) {
      response.put("message", "There was an error at querying the customer data");
      response.put("error", e.getMessage() + ":"  + e.getMostSpecificCause().getMessage());
      return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

  }

  /**
   * Return a list of customers by page
   * @return page of customers.
   */
  @GetMapping("/page/{page}")
  public Page<Customer> getCustomersByPage(@PathVariable Integer page) {
    return customerService.findAll(PageRequest.of(page, 4));
  }

  /**
   * Get Customer Statistics, such as average age and standard deviation
   * @return customer statistics.
   */
  @GetMapping("/kpi")
  public CustomerKpi getCustomerKpi() {
    return customerService.getCustomerKpi();
  }

  /**
   * Create a customer
   * @return result.
   */
  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody Customer customer, BindingResult result) {

    Map<String, Object> response = new HashMap<>();

    if (result.hasErrors()) {
      List<String> errors = result.getFieldErrors()
          .stream()
          .map(err -> "The field '" + err.getField() + "' " + err.getDefaultMessage())
          .collect(Collectors.toList());

      response.put("errors", errors);
      return new ResponseEntity<>(response, BAD_REQUEST);
    }

    try {
      Customer customerNew = customerService.save(customer);
      response.put("message", "The customer was successfully created!");
      response.put("customer", customerNew);
      return new ResponseEntity<>(response, CREATED);

    } catch (DataAccessException e) {
      response.put("message", "There was an error at saving customer");
      response.put("error", e.getMessage() + " : " + e.getMostSpecificCause().getMessage());
      return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }


  }
}
