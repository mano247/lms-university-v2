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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lmsuniversity.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "registered_users",
	uniqueConstraints = {
				@UniqueConstraint(columnNames = "email")
		})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RegisteredUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@NotBlank
	@Size(max = 50)
	private String username;

	// Accepted on input (create/update) but never serialized back out in API responses.
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

	@NotBlank
	@Size(max = 50)
	private String firstName;

	@NotBlank
	@Size(max = 50)
	private String lastName;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "registered_user_permissions",
			joinColumns = @JoinColumn(name = "registered_user_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	@Builder.Default
	private Set<Permission> permissions = new HashSet<>();
}
