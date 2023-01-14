package com.smart.smartcontact.controller;


import com.smart.smartcontact.dao.UserRepository;
import com.smart.smartcontact.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String Home(Model model){
        model.addAttribute("title","Home - Smart Contact Manager");
        return "home";
    }


    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title","Home - about page");
        return "about";
    }


    @RequestMapping("/signup")
    public String Signup(Model model){
        model.addAttribute("title","SignUp page");
        model.addAttribute("user", new User());
        return "Signup";
    }

//    register user handler
    @RequestMapping(value="/register_user",method = RequestMethod.POST)
    public String registerUser(@Valid  @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session){
       try{
           System.out.println("User" +user);
           user.setRole("ROLE_USER");

           user.setEnabled(true);
           user.setImageUrl("default img");
           user.setPassword(passwordEncoder.encode(user.getPassword()));

           User Result = this.userRepository.save(user);

           if(result.hasErrors()) {
               System.out.print("error"+result.toString());
               model.addAttribute("user",user);
               return "Signup";
           }

           return "Signup";

       }

       catch (Exception e){
           e.printStackTrace();
           model.addAttribute("user",user);
           return  "Signup";
       }

    }

    @GetMapping("/Signin")
    public String loginScreen(Model model){
        model.addAttribute("loginScreen");
        return "login";
    }


}
