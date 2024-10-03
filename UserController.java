package Smartcontactmanager.controller;

import Smartcontactmanager.Entities.Contact;
import Smartcontactmanager.Entities.User;
import Smartcontactmanager.dao.ContactRepository;
import Smartcontactmanager.dao.UserRepository;
import Smartcontactmanager.helper.Messege;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import com.razorpay.*;
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private BCryptPasswordEncoder ps;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ContactRepository corepo;

	@GetMapping("/profile")
	public String user() {
	    return "user_profile";
	}

	@ModelAttribute
	public void Commonuser(Principal p, Model m) {
	    if (p != null) {
	        String email = p.getName();
	        User user = userRepo.getUserByUserName(email);
	        m.addAttribute("user", user);
	    }
	}

	@GetMapping("/add")
	public String Addcontactform(Model m) {
	    m.addAttribute("title", "Add Contact");
	    m.addAttribute("contact", new Contact());
	    return "normal/addform";
	}

	@PostMapping("/contact")
	public String processAddContact(@ModelAttribute("contact") Contact contact,
	                                Principal principal,
	                                Model m,
	                                @RequestParam("imagefile") MultipartFile file, HttpSession session) {
	    try {
	        String userEmail = principal.getName();
	        User user = userRepo.getUserByUserName(userEmail);

	        if (!file.isEmpty()) {
	            // Handle file upload
	            contact.setImage(file.getOriginalFilename());

	            // Save the file to a location
	            File savefile = new ClassPathResource("static/image").getFile();
	            Path path = Paths.get(savefile.getAbsolutePath() + File.separator + contact.getCid() + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        } else {
	            // Set a default image if no file is uploaded
	            contact.setImage("default.jpg");
	        }

	        contact.setUser(user);
	        user.getContacts().add(contact);

	        userRepo.save(user);

	        session.setAttribute("messege", new Messege("Your contact is added successfully", "success"));

	        return "normal/addform";
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.setAttribute("messege", new Messege("Something went wrong", "danger"));
	        System.out.println(e.getMessage());

	        return "normal/addform";
	    }
	}

	@GetMapping("/showcontact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model model, Principal principal) {
	    try {
	        model.addAttribute("title", "Show Contact");
	        String username = principal.getName();
	        User user = this.userRepo.getUserByUserName(username);
	        Pageable pageable = PageRequest.of(page, 3);
	        Page<Contact> contacts = this.corepo.findcontactByUser(user.getId(), pageable);
	        model.addAttribute("contacts", contacts);
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", contacts.getTotalPages());
	        return "showcontact"; // Ensure this template exists
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "user_profile"; 

    }
	@GetMapping("/{cid}/show")
	public String Showalldetail(@PathVariable("cid") int cid,Model  m ,Principal p  ) {
		System.out.println(cid);
		Optional<Contact> contacts = corepo.findById(cid);
		Contact contact=contacts.get();
	
		String username=p.getName();
		User user=userRepo.getUserByUserName(username);
		if(user.getId()==contact.getUser().getId()) {
			m.addAttribute("contact", contact);
		}
		return "show";
		
	}
	@GetMapping("/delete/{cid}")
	public String deletecontact(@PathVariable("cid") int cid,HttpSession session ) {
		try{System.out.println(cid);
		Contact contact= this.corepo.findById(cid).get();
		System.out.println("contact"+contact.getCid());
		contact.setUser(null);
		this.corepo.delete(contact);
		session.setAttribute("messege", new Messege("your contact is deleted","succses"));
		 return "redirect:/user/showcontact/1";
		
	}
	catch(Exception e) {
		e.printStackTrace();
		
		return "user_profile"; }
	}
	

@PostMapping("/update/{cid}")
public String update(Model m, @PathVariable("cid")int cid) {
	m.addAttribute("title", "update contact");
	Contact contact=this.corepo.findById(cid).get();
	m.addAttribute("contact", contact);
	return "updateform";
	
}	

@PostMapping("/updatecon")
public String updateContact(@ModelAttribute Contact contact,
                            @RequestParam("imagefile") MultipartFile file,
                            Principal principal,
                            HttpSession session) {
    try {
        Contact oldContact = this.corepo.findById(contact.getCid()).orElse(null);

        if (oldContact != null) {
            User user = this.userRepo.getUserByUserName(principal.getName());

            // Handle file upload if file is not empty
            if (!file.isEmpty()) {
                // Delete old file
                File deleteFile = new ClassPathResource("static/image").getFile();
                File oldFile = new File(deleteFile, oldContact.getImage());
                oldFile.delete();

                // Save new file
                File saveFile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + contact.getCid() + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());
            } else {
                // If no new file uploaded, keep the old image
                contact.setImage(oldContact.getImage());
            }

            contact.setUser(user);
            this.corepo.save(contact);

            session.setAttribute("message", new Messege("Contact updated successfully", "success"));
        } else {
            session.setAttribute("message", new Messege("Contact not found", "danger"));
        }
    } catch (Exception e) {
        e.printStackTrace();
        session.setAttribute("message", new Messege("Failed to update contact", "danger"));
    }

    return "redirect:/user/showcontact/1";
}
@GetMapping("/yourprofile")
public String profile(Model m) {
	m.addAttribute("title", "your profile");
	return"normal/yourprofile";
}
@GetMapping("/setting")
public String setting(Model m) {
	m.addAttribute("title", "change password");
	return "setting";
}
@PostMapping("/change")
public String setting(Model m,@RequestParam("oldp")String old,@RequestParam("newp") 
String n ,HttpSession session,Principal p) {
	System.out.println(old);
	System.out.println(n);
	User user=this.userRepo.getUserByUserName(p.getName());
	System.out.println(user.getPassword());
	
	if(this.ps.matches(old, user.getPassword())) {
		user.setPassword(this.ps.encode(n));
		this.userRepo.save(user);
		session.setAttribute("messege", new Messege("your password is changed","succses"));
	}
	else {
		
		session.setAttribute("messege", new Messege("please Enter the right passwrod","succses"));
		return"redirect:/user/setting";
	}
	
	return"redirect:/user/setting";
	
}
@PostMapping("/order")
@ResponseBody
public String order(@RequestBody Map<String,Object>data) throws Exception {
	System.out.println("hey order function is executed");
	System.out.println(data);
	int amt = Integer.parseInt(data.get("amount").toString());
	var client=new RazorpayClient("rzp_test_jEz6lwA6e9PN6M", "QeJ8H92lvATfDkyn60v8es4j");
	JSONObject ob =new  JSONObject();
	ob.put("amount", amt*100);
	ob.put("currency", "INR");
	 ob.put("payment_capture", 1);
	Order order=client.orders.create(ob);
	System.out.println(order);
	return order.toString();
	
}

}
