package com.smart.smartcontact.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smart.smartcontact.entities.User;
//import org.springframework.stereotype.Controller;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;

public interface UserRepository extends JpaRepository<User,Integer>{
    @Query("select u from User u where u.email = : email ")
    public User getUserByUserName(@Param("email") String email);
}

