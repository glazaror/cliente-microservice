package com.glazaror.sample.service.models.services;

import java.util.List;

import com.glazaror.sample.service.models.dto.ClienteKPI;
import com.glazaror.sample.service.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente);

	public ClienteKPI getClienteKPI();

}
