package Smartcontactmanager.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;
    private String name;
    private String secondname;
    private String work;
    private String email;
    private String phone;
    @Column(length = 750)
    private String description;
    private String image; // Field to store the image file name

    @ManyToOne
    private User user;

    // Getters and setters

    public Contact() {
    }

    public Contact(int cid, String name, String secondname, String work, String email, String phone, String description, String image, User user) {
        this.cid = cid;
        this.name = name;
        this.secondname = secondname;
        this.work = work;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.image = image;
        this.user = user;
    }

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondname() {
		return secondname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

   
}
