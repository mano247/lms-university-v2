package rs.ac.singidunum.novisad.backend.security.request;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequest {
	
  private String username;
	
  @NotBlank
  @Email
  private String email;

  private Set<String> permission;

  @NotBlank
  private String password;

 
 
  public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public Set<String> getPermission() {
	return permission;
}

public void setPermission(Set<String> permission) {
	this.permission = permission;
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
}
