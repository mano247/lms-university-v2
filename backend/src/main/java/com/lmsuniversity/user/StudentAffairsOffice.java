package com.lmsuniversity.user;

import java.util.Set;

import jakarta.persistence.Entity;
import com.lmsuniversity.permission.Permission;

@Entity
public class StudentAffairsOffice extends RegisteredUser{

	public StudentAffairsOffice() {
		super();
	}

	public StudentAffairsOffice(Long id, String firstName, String lastName,String username, String email, String password, Set<Permission> permissions) {
		super(id, firstName, lastName, username, email, password, permissions);
	}

	public StudentAffairsOffice(String username, String password, String email) {
		super(username, password, email);
	}
}
