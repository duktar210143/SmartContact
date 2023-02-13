package com.smart.smartcontact.controller;


import com.smart.smartcontact.dao.ContactRepository;
import com.smart.smartcontact.dao.UserRepository;
import com.smart.smartcontact.entities.User;
import com.smart.smartcontact.entities.contact;
import com.smart.smartcontact.helper.Message;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

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
                Contact.setImageUrl("smart_contact_profile.png");
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
//    perPageContact_Show=5
//    current page =page[0];

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal){
        m.addAttribute("title","view contacts");

//        show contact lists
        String userName = principal.getName();

//        current Page
//        contact per-page
        Pageable pageable = PageRequest.of(page,3);
        User user = this.userRepository.getUserByUserName(userName);
        Page<contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);

        m.addAttribute("contacts",contacts);
        m.addAttribute("currentPage",page);
        m.addAttribute("totalPages",contacts.getTotalPages());

        return "normal/show_contacts";
    }

//    showing individual contact details
    @RequestMapping("/{id}/contacts")
    public String view_detail(@PathVariable("id") Integer id,Model model, Principal principal)
    {
        System.out.println("id"+id);
        Optional<contact> contactOptional = this.contactRepository.findById(id);
        contact Contact = contactOptional.get();

//        validate if the user logged in is the only user checking his contacts
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);

        if(user.getId()==Contact.getUser().getId())
            model.addAttribute("Contact",Contact);


        return "normal/contact_detail";
    }

//    delete handler
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") Integer id, Principal principal, HttpSession session){
        Optional<contact> contactOptional = this.contactRepository.findById(id);
        contact Contact =  contactOptional.get();

        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);

        if(user.getId()==Contact.getUser().getId()) {
            Contact.setUser(null);
            this.contactRepository.delete(Contact);
            session.setAttribute("message",new Message("Contact deleted successfully","success"));
        }

        return "redirect:/user/show-contacts/0";
    }
}
