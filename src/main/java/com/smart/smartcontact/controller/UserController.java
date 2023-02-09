package com.smart.smartcontact.controller;


import com.smart.smartcontact.dao.ContactRepository;
import com.smart.smartcontact.dao.UserRepository;
import com.smart.smartcontact.entities.User;
import com.smart.smartcontact.entities.contact;
import com.smart.smartcontact.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

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

//    processing add contact
    @PostMapping("/process-contact")
    public String processContact(
            @ModelAttribute contact Contact,
            @RequestParam("profileImage") MultipartFile file,
            Principal principal, HttpSession session){

        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

//            processing and uploading file
            if(file.isEmpty()){
//                if the file is empty
                System.out.println("the file is empty");
            }
            else {
//                upload the file to folder and update the name to contact
                Contact.setImageUrl(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());

                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("image is uploaded");

            }
            user.getContacts().add(Contact);
            Contact.setUser(user);
            this.userRepository.save(user);

            System.out.println("DATA" + Contact);
            System.out.println("Added to database");

//            success message
            session.setAttribute("message",new Message("Your contact is Added ","success"));

        }catch(Exception e){
            System.out.println("Error"+e.getMessage());
            e.printStackTrace();
//            error message
            session.setAttribute("message",new Message("Something went wrong try again ","danger"));

        }
        return "normal/add_contact_form";
    }

//    handelers for show contact's

    @GetMapping("/show-contacts")
    public String showContacts(Model m, Principal principal){
        m.addAttribute("title","view contacts");

//        show contact lists
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        List<contact> contacts = this.contactRepository.findContactsByUser(user.getId());

        m.addAttribute("contacts",contacts);

        return "normal/show_contacts";
    }
}
