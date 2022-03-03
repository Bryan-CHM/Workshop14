package sg.edu.nus.iss.workshop14.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.workshop14.model.Contact;
import sg.edu.nus.iss.workshop14.service.ContactsRepo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class AddressbookController {
    private Logger logger = Logger.getLogger(AddressbookController.class.getName());
    
    @Autowired
    ContactsRepo service;
    
    @GetMapping("/")
    public String contactForm(Model model){
        model.addAttribute("contact", new Contact());
        return "contact";
    }

    @GetMapping("/getContact/{contactId}")
    public String getContact(Model model, @PathVariable(value="contactId") 
            String contactId) {
        logger.log(Level.INFO, "contactId > " + contactId);
        Contact ctc = service.findById(contactId);
        logger.log(Level.INFO, "getId > " + ctc.getId());
        logger.log(Level.INFO, "getEmail > " + ctc.getEmail());

        model.addAttribute("contact", ctc);
        return "showContact";
    }

    @GetMapping("/contact")
    public String getAllContact(Model model, @RequestParam(name="startIndex") 
        String startIndex) {
            List<Contact> resultFromSvc = service.findAll(Integer
                .parseInt(startIndex));
            logger.log(Level.INFO, "resultFromSvc > " + resultFromSvc);
            return "listContact";
        }
    
    @PostMapping("/contact")
    public String contactSubmit(@ModelAttribute Contact contact, Model model) {
        logger.info("Email > " + contact.getEmail());
        logger.info("Name > " + contact.getName());
        logger.info("Phone Number > " + contact.getPhoneNumber());
        Contact persistToRedisCtc = new Contact(
                contact.getName(),
                contact.getEmail(),
                contact.getPhoneNumber()
        );
        service.save(persistToRedisCtc);
        model.addAttribute("contact", persistToRedisCtc);
        return "showContact";
    }

}