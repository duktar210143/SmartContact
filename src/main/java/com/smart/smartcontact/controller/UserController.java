package com.smart.smartcontact.controller;


import com.smart.smartcontact.dao.UserRepository;
import com.smart.smartcontact.entities.User;
import com.smart.smartcontact.entities.contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String userName = principal.getName();
//        get user deatails using username
        User user = userRepository.getUserByUserName(userName);

        model.addAttribute("user",user);
    }

//    home dashboard
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("title","user Dashboard");
        return "normal/user_dashboard";
    }
//    open add form handler

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model)
    {
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new contact());
        return "normal/add_contact_form";
    }
}
