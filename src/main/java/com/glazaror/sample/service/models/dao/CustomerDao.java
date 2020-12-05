package com.glazaror.sample.service.models.dao;

import com.glazaror.sample.service.models.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO definition to access customer data<br/>
 * Class: CustomerDAO<br/>
 * @author glazaror <br/>
 * @version 1.0
 */
public interface CustomerDao extends JpaRepository<Customer, Long> {

}
