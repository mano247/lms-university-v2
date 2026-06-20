package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import com.lmsuniversity.permission.Permission;

@Entity
@Table(name = "registrovani_korisnici",
	uniqueConstraints = {
				@UniqueConstraint(columnNames = "email")
		})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class RegisteredUser {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		private String username;

		private String password;

		private String email;

		private String firstName;

		private String lastName;

		@ManyToMany(fetch = FetchType.LAZY)
		@JoinTable(name = "registrovani_korisnik_permissions",
				joinColumns = @JoinColumn(name = "registrovaniKorisnik_id"),
				inverseJoinColumns = @JoinColumn(name = "permission_id"))
		private Set<Permission> permissions = new HashSet<>();

		public  RegisteredUser() {
			super();
		}

		public RegisteredUser(Long id, String username, String password, String email,
				Set<Permission> permissions) {
			super();
			this.id = id;
			this.username = username;
			this.password = password;
			this.email = email;
			this.permissions = permissions;

		}

		public RegisteredUser(Long id, String username, String password, String email) {
			super();
			this.id = id;
			this.username = username;
			this.password = password;
			this.email = email;
		}

		public RegisteredUser( String email, String password, String username) {
			this.email = email;
			this.password = password;
			this.username = username;
		}

		public RegisteredUser(String username, String firstName, String lastName, String email, String password) {
			this.username = username;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.password = password;
		}

		public RegisteredUser(String firstName, String lastName, String email, String password) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.password = password;
		}

		public RegisteredUser(Long id, String firstName, String lastName, String email, String password, Set<Permission> permissions) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.password = password;
			this.permissions = permissions;
		}

		public RegisteredUser(Long id, String firstName, String lastName,String username, String email, String password, Set<Permission> permissions) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.username = username;
			this.email = email;
			this.password = password;
			this.permissions = permissions;
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

		public Set<Permission> getPermissions() {
			return permissions;
		}

		public void setPermissions(Set<Permission> permissions) {
			this.permissions = permissions;
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
