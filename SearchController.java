package Smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import Smartcontactmanager.Entities.Contact;
import Smartcontactmanager.Entities.User;
import Smartcontactmanager.dao.ContactRepository;
import Smartcontactmanager.dao.UserRepository;

@RestController
public class SearchController {
	@Autowired
	private UserRepository urepo;
	@Autowired
	private ContactRepository crepo;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query ,Principal p) {
		 System.out.println(query);
		 User user=this.urepo.getUserByUserName(p.getName());
		 List<Contact> contact = this.crepo.findByNameContainingAndUser(query, user);
		 return ResponseEntity.ok(contact);
		 
	}
	

}
