package com.smart.smartcontact.controller;

import com.smart.smartcontact.dao.ContactRepository;
import com.smart.smartcontact.dao.UserRepository;
import com.smart.smartcontact.entities.User;
import com.smart.smartcontact.entities.contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class searchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @GetMapping("search/{query}")
    public ResponseEntity<?> search(@PathVariable ("query")String query, Principal principal){
        System.out.println(query);
        User user = userRepository.getUserByUserName(principal.getName());
        List<contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
        return ResponseEntity.ok(contacts);
    }

}
