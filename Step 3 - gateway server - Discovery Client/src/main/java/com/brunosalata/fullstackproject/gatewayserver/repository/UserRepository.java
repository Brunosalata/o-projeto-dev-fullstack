package com.brunosalata.fullstackproject.gatewayserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.brunosalata.fullstackproject.gatewayserver.model.User;

@Service
public interface UserRepository extends CrudRepository<User, String> {

    @Query("SELECT obj FROM User obj WHERE obj.login = :login")
    public User findByLogin(@Param("login") String login) throws Exception;
}