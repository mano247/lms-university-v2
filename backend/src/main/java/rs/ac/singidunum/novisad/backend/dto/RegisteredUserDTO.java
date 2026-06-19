package rs.ac.singidunum.novisad.backend.dto;

import java.util.HashSet;
import java.util.Set;

import rs.ac.singidunum.novisad.backend.model.Permission;

public class RegisteredUserDTO {

	private String userType;
	private Long id;
	private String username;
	private String email;
	private String password;
	private String firstName;
	private String lastName;

	private Set<Permission> permission = new HashSet<>();

	public RegisteredUserDTO() {
		super();
	}

	public RegisteredUserDTO(String userType, Long id, String username, String email, String password,
			Set<Permission> permission,String firstName ,String lastName) {
		super();
		this.userType = userType;
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.permission = permission;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Permission> getPermission() {
		return permission;
	}

	public void setPermission(Set<Permission> permission) {
		this.permission = permission;
	}



}
