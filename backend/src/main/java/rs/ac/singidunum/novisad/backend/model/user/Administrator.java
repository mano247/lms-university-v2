package rs.ac.singidunum.novisad.backend.model.user;

import java.util.Set;

import jakarta.persistence.Entity;
import rs.ac.singidunum.novisad.backend.model.Permission;

	@Entity
	public class Administrator extends RegisteredUser{

		public Administrator() {
			super();
		}

		public Administrator(Long id, String username, String password, String email, Set<Permission> permissions) {
			super(id, username, password, email, permissions);
		}

		public Administrator(Long id, String firstName, String lastName,String username, String email, String password, Set<Permission> permissions) {
			super(id,firstName, lastName, username, email, password, permissions);
		}
}
