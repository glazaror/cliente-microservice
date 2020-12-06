package com.glazaror.sample.service.controllers;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.glazaror.sample.service.models.dto.CustomerKpi;
import com.glazaror.sample.service.models.dto.CustomerWithProjectionDto;
import com.glazaror.sample.service.models.entity.Customer;
import com.glazaror.sample.service.models.exception.BusinessException;
import com.glazaror.sample.service.models.services.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
  @Operation(summary = "Get all customers",
      description = "Get complete information about customers")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customers were found."),
      @ApiResponse(responseCode = "400", description = "Client has sent invalid data."),
      @ApiResponse(responseCode = "404", description = "Customers were not found."),
      @ApiResponse(responseCode = "500", description = "There was an system error.")})
  public List<Customer> findCustomers() {
    return customerService.findAll();
  }

  /**
   * Find a customer by his/her identifier.
   * @return found customer.
   */
  @GetMapping("/{id}")
  @Operation(summary = "Get customer projection by an identifier",
      description = "Get complete information (analysis and projection) about one customer")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customer has been found."),
      @ApiResponse(responseCode = "400", description = "Client has sent invalid data."),
      @ApiResponse(responseCode = "404", description = "Customer doesn't exist."),
      @ApiResponse(responseCode = "500", description = "There was an system error.")})
  public ResponseEntity<?> findById(@PathVariable Long id) {
    try {
      CustomerWithProjectionDto customer = customerService.findById(id);
      if (customer == null) {
        return new ResponseEntity<>(
            singletonMap("message", "The customer ID: " + id + " doesn't exist!"),
            NOT_FOUND);
      }
      return new ResponseEntity<>(customer, OK);

    } catch (BusinessException e) {
      return new ResponseEntity<>(singletonMap("message", e.getMessage()),
          INTERNAL_SERVER_ERROR);
    }

  }

  /**
   * Return a list of customers by page
   * @return page of customers.
   */
  @GetMapping("/page/{page}")
  @Operation(summary = "Get customers by a page",
      description = "Get complete information about a page of customers")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customers were found."),
      @ApiResponse(responseCode = "400", description = "Client has sent invalid data."),
      @ApiResponse(responseCode = "404", description = "Customers were not found."),
      @ApiResponse(responseCode = "500", description = "There was an system error.")})
  public Page<Customer> findCustomersByPage(@PathVariable Integer page) {
    return customerService.findAll(PageRequest.of(page, 4));
  }

  /**
   * Get Customer Statistics, such as average age and standard deviation
   * @return customer statistics.
   */
  @GetMapping("/kpi")
  @Operation(summary = "Get customer statistics",
      description = "Get customer statistics based on their age")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customer statistics has been collected."),
      @ApiResponse(responseCode = "400", description = "Client has sent invalid data."),
      @ApiResponse(responseCode = "404", description = "Customer statistics doesn't exist."),
      @ApiResponse(responseCode = "500", description = "There was an system error.")})
  public CustomerKpi getCustomerKpi() {
    return customerService.getCustomerKpi();
  }

  /**
   * Create a customer
   * @return result.
   */
  @PostMapping
  @Operation(summary = "Creates a new customer",
      description = "Creates a new customer")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Customer has been successfully saved."),
      @ApiResponse(responseCode = "400", description = "Client has sent invalid data."),
      @ApiResponse(responseCode = "500", description = "There was an system error.")})
  public ResponseEntity<?> create(@Valid @RequestBody Customer customer, BindingResult result) {

    if (result.hasErrors()) {
      List<String> errors = result.getFieldErrors()
          .stream()
          .map(err -> "The field '" + err.getField() + "' " + err.getDefaultMessage())
          .collect(Collectors.toList());

      return new ResponseEntity<>(singletonMap("errors", errors), BAD_REQUEST);
    }

    try {
      Customer customerNew = customerService.save(customer);
      Map<String, Object> response = new HashMap<>();
      response.put("message", "The customer was successfully created!");
      response.put("customer", customerNew);
      return new ResponseEntity<>(response, CREATED);

    } catch (BusinessException e) {
      return new ResponseEntity<>(singletonMap("message", e.getMessage()),
          INTERNAL_SERVER_ERROR);
    }


  }
}
