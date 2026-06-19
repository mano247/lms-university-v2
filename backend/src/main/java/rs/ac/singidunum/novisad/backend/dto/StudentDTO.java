package rs.ac.singidunum.novisad.backend.dto;

import java.util.HashSet;
import java.util.Set;

import rs.ac.singidunum.novisad.backend.model.Permission;
import rs.ac.singidunum.novisad.backend.model.academic.Faculty;

public class StudentDTO {
	
	private String userType;
	private Long id;
	private String email;
	private String username;
	private String indexNumber;
	private String password;
    private String firstName;
    private String lastName;
    private Faculty faculty;
	private Set<Permission> permission = new HashSet<>();


	public StudentDTO() {
		super();
	}
	
	public StudentDTO(Long id, String email, String username, String indexNumber, String firstName, String lastName,
			Faculty faculty) {
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.indexNumber = indexNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.faculty = faculty;
	}


	public StudentDTO(Long id, String userType, String email, String password, String username, String indexNumber,
			Set<Permission> permission) {
		super();

		this.id = id;
		this.userType = userType;
		this.email = email;
		this.username = username;
		this.indexNumber = indexNumber;
		this.permission = permission;
		this.password = password;
	}
	
	public StudentDTO(Long id, String firstName, String lastName, String email, String password, String indexNumber, String username) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.indexNumber = indexNumber;
		this.username = username;

	}
	
	public StudentDTO(Long id, String userType, String firstName, String lastName, String email, String password, Set<Permission> permission, String indexNumber, String username) {
		super();
		this.id = id;
		this.userType = userType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.indexNumber = indexNumber;
		this.permission = permission;
		this.username = username;

	}
	
	public StudentDTO(Long id, String userType, String firstName, String lastName, String email, String password, Set<Permission> permission, String indexNumber, String username,Faculty faculty) {
		super();
		this.id = id;
		this.userType = userType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.indexNumber = indexNumber;
		this.permission = permission;
		this.username = username;
		this.faculty = faculty;

	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public String getIndexNumber() {
		return indexNumber;
	}


	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Permission> getPermission() {
		return permission;
	}

	public void setPermission(Set<Permission> permission) {
		this.permission = permission;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
}
