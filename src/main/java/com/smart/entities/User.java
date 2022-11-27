package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// @Entity annotation is used to define the class as an entity.
// @Table annotation is used to define the table name of this perticular entity.

@Entity
@Table(name = "USER")
public class User {

	// Below @Id marks the attribute as primary ID and key of the entity.
	// GeneratedValue annotation defines the type of ID generation
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	// Below annotaions are used here for Spring auto validation feature.
	@NotBlank(message = "Name must not be blank !!")
	@Size(min = 2, max = 50, message = "Length of name should be between 2 and 50 !!")
	private String name;

	// Below annotaion is used to define database table's column properties of this
	// attribute
	@Column(unique = true)
	private String email;
	private String password;
	private String role;
	private String imageUrl;

	// Below is also used to define property of table column
	@Column(length = 500)
	private String about;
	private boolean enabled;

	// Below is used to define relation with another entity ( Doubtful on the
	// implementation )
	// By default it will create another table with two columns ( ID of user and ID
	// of contact to define mapping between them )
	// In this case, we have give mappedBy = "user"
	// to stop it from creatig another table and just use a column user ID in the
	// contact table as a foreign key.
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public User() {
		super();
	}

	public int getId() {
		return id;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", imageUrl=" + imageUrl + ", about=" + about + ", enabled=" + enabled + ", contacts=" + contacts
				+ "]";
	}

}
