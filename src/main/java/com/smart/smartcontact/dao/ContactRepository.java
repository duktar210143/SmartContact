package com.smart.smartcontact.dao;

import com.smart.smartcontact.entities.User;
import com.smart.smartcontact.entities.contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<contact,Integer> {
//    pagination..
    @Query("from contact as c where c.user.id =:userId")
    public Page<contact> findContactsByUser(@Param("userId") Integer userId, Pageable pePagable);

//    search
    public List<contact> findByNameContainingAndUser(String name, User user);
}
