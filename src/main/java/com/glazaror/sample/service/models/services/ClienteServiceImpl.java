package com.glazaror.sample.service.models.services;

import java.util.List;

import com.glazaror.sample.service.models.dao.IClienteDao;
import com.glazaror.sample.service.models.dto.ClienteKPI;
import com.glazaror.sample.service.models.entity.Cliente;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private IClienteDao clienteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Override
	public ClienteKPI getClienteKPI() {
		List<Cliente> clientes = clienteDao.findAll();;
		Double promedioEdades = clientes.stream().mapToDouble(Cliente::getEdad).average().getAsDouble();
		DescriptiveStatistics statistics = new DescriptiveStatistics(clientes.stream().mapToDouble(Cliente::getEdad).toArray());
		ClienteKPI clienteKPI = new ClienteKPI(Precision.round(promedioEdades, 2), Precision.round(statistics.getStandardDeviation(), 2));
		return clienteKPI;
	}

}
