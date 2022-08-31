package com.dslearn.CRUD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dslearn.CRUD.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
