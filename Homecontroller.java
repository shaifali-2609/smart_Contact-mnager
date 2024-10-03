package Smartcontactmanager.controller;

import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Smartcontactmanager.Entities.User;
import Smartcontactmanager.dao.UserRepository;
import Smartcontactmanager.helper.Messege;
import Smartcontactmanager.service.Emailser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class Homecontroller {
	Random random=new Random(1000);
	@Autowired
	private Emailser emailser;
	
    @Autowired
    private UserRepository userrepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/doresister")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result,
                           @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                           Model model, HttpSession session) {
        try {
            if (!agreement) {
                throw new Exception("You must agree to the terms and conditions.");
            }
            if (result.hasErrors()) {
                model.addAttribute("user", user);
                return "signup";
            }
            user.setRole("ROLE_USER"); // Ensure the role matches expected format
            user.setEnable(true);
            user.setImageurl("img.png");
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password

            User savedUser = userrepo.save(user);
            model.addAttribute("user", new User());
            session.setAttribute("msg", new Messege("Registration successful!", "alert-success"));
            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("msg", new Messege("Something went wrong!! " + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login - Contact Manager");
        return "login";
    }
   /* @GetMapping("/error")
	public String error() {
	
	return "error"; 
} */
  

@GetMapping("/forgot")
public String forgot() {
	return "forgot";
}
@PostMapping("/send")
public String send(@RequestParam("email")String email,HttpSession session) {
	System.out.println(email);
	
	int otp=random.nextInt(99999);
	System.out.println(otp);
	String subject="otp from scm";
	String messege="<div>"
			+ "<h1>"
			+ "otp is"+otp
			
			+ "</h1>"
			+ "</div>";
	String to=email;
	boolean flag = this.emailser.sendEmail(subject, messege, to );
	if (flag) {
		session.setAttribute("myotp", otp); 
		session.setAttribute("email", email);
		
		return "verify";
	} else 
		
		session.setAttribute("messege", "chack your email id ");
	return "forgot";
}

@PostMapping("/verifyotp")
public String change(@RequestParam("otp") int otp ,HttpSession s) {
	
	int myotp=(int)s.getAttribute("myotp");
	String email=(String)s.getAttribute("email");
	if(otp==myotp) {
		User user=this.userrepo.getUserByUserName(email);
		if(user==null) {
			
			s.setAttribute("messege", "user not exits");
			return "forgot";
			
		}
		return "change";
	}
	else {
	
		s.setAttribute("messege", "otp is wrong");
		return "verify";
	}
}

@PostMapping("/changep")
public String changePassword(@RequestParam("newp") String newp, @RequestParam("newc") String newc, HttpSession session) {
	String email = (String) session.getAttribute("email");
	User user = this.userrepo.getUserByUserName(email);

	if (newp.equals(newc)) {
		user.setPassword(this.passwordEncoder.encode(newc));
		this.userrepo.save(user);
		return "redirect:/login?change=Password changed successfully.";
	} else {
		session.setAttribute("messege", "Passwords do not match.");
		return "change";
	}
}
}



