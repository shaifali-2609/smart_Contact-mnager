package Smartcontactmanager.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
    private String name;

    @Column(unique = true)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email pattern")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private String role;
    private boolean enable;
    private String imageurl;

    @Column(length = 500)
    @NotBlank(message = "About section cannot be empty")
    private String about;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    // Getters and Setters

   

	public int getId() {
		return id;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int id,
			@NotBlank(message = "Name cannot be blank") @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters") String name,
			@NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email pattern") String email,
			@NotBlank(message = "Password cannot be empty") String password, String role, boolean enable,
			String imageurl, @NotBlank(message = "About section cannot be empty") String about,
			List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enable = enable;
		this.imageurl = imageurl;
		this.about = about;
		this.contacts = contacts;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	
}
