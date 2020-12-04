package com.glazaror.sample.service.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glazaror.sample.service.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long>{

}
