package com.dslearn.CRUD.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dslearn.CRUD.dto.ClientDTO;
import com.dslearn.CRUD.entities.Client;
import com.dslearn.CRUD.repository.ClientRepository;
import com.dslearn.CRUD.services.exception.DataBaseException;
import com.dslearn.CRUD.services.exception.ResourceNotFoundException;


@Service
public class ClientServices {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPages(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("ID not found!"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getReferenceById(id);//getone or getbyid is deprecated
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not founded " + id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not exist " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found " + id);
		}catch(DataIntegrityViolationException e){
			throw new DataBaseException("Integrit violation " + id);
		}
	}
	
	
	private void copyDtoToEntity (ClientDTO dto, Client entity) {
		
		entity.setBirthdate(dto.getBirthdate());
		entity.setChildren(dto.getChildren());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setName(dto.getName());
		
	}
	
}
