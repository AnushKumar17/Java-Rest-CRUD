package com.ak.crud.rest.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ak.crud.rest.models.User;

public interface UserRepo extends JpaRepository<User,Long>{

}
