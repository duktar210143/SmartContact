package com.smart.smartcontact.controller;


import com.smart.smartcontact.dao.UserRepository;
import com.smart.smartcontact.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){
        String userName = principal.getName();
        System.out.print("username"+userName);
//        get user deatails using username
        User user = userRepository.getUserByUserName(userName);
        System.out.print("USER Details"+ user);

        model.addAttribute("user",user);


        return "normal/user_dashboard";
    }
}
