package rs.ac.singidunum.novisad.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.singidunum.novisad.backend.model.Permission;
import rs.ac.singidunum.novisad.backend.model.PermissionEnum;
import rs.ac.singidunum.novisad.backend.model.user.RegisteredUser;
import rs.ac.singidunum.novisad.backend.repository.PermissionRepository;
import rs.ac.singidunum.novisad.backend.repository.RegistrovaniKorisnikRepository;
import rs.ac.singidunum.novisad.backend.security.request.LoginRequest;
import rs.ac.singidunum.novisad.backend.security.request.SignupRequest;
import rs.ac.singidunum.novisad.backend.security.response.JwtResponse;
import rs.ac.singidunum.novisad.backend.security.response.MessageResponse;
import rs.ac.singidunum.novisad.backend.security.jwt.JwtUtils;
import rs.ac.singidunum.novisad.backend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  RegistrovaniKorisnikRepository userRepository;

  @Autowired
  PermissionRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
    	System.out.println("proslo ");
    	System.out.println(loginRequest.getEmail() +" "+ loginRequest.getPassword());
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities().stream()
              .map(item -> item.getAuthority())
              .collect(Collectors.toList());

      return ResponseEntity.ok(new JwtResponse(jwt,
              userDetails.getId(),
              userDetails.getEmail(),
              roles, userDetails.getUserType()));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(new MessageResponse("Pogrešna kombinacija korisničkog imena i lozinke."));
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    if (userRepository.existsRegistrovaniKorisnikByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    RegisteredUser user = new RegisteredUser(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getUsername());

    Set<String> strRoles = signUpRequest.getPermission();
    Set<Permission> roles = new HashSet<>();

    if (strRoles == null) {
    	Permission userRole = roleRepository.findByName(PermissionEnum.USER_PERMISSION)
          .orElseThrow(() -> new RuntimeException("Error: Permission has not been found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "student":
        	Permission studentRole = roleRepository.findByName(PermissionEnum.STUDENT_PERMISSION)
              .orElseThrow(() -> new RuntimeException("Error: Permission has not been found."));
          roles.add(studentRole);

          break;
        case "teacher":
        	Permission nastavnikRole = roleRepository.findByName(PermissionEnum.TEACHER_PERMISSION)
              .orElseThrow(() -> new RuntimeException("Error: Permission has not been found."));
          roles.add(nastavnikRole);

          break;
          case "studentskaSluzba":
        	  Permission studentskaSluzba = roleRepository.findByName(PermissionEnum.STUDENT_AFFAIRS_PERMISSION)
                    .orElseThrow(() -> new RuntimeException("Error: Permission has not been found."));
            roles.add(studentskaSluzba);

            break;
          case "administrator":
        	  Permission administratorRole = roleRepository.findByName(PermissionEnum.ADMINISTRATOR_PERMISSION)
                    .orElseThrow(() -> new RuntimeException("Error: Permission has not been found."));
            roles.add(administratorRole);

            break;
        default:
        	Permission korisnikRole = roleRepository.findByName(PermissionEnum.USER_PERMISSION)
              .orElseThrow(() -> new RuntimeException("Error: Permission has not been found."));
          roles.add(korisnikRole);
        }
      });
    }

    user.setPermissions(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("You have registered successfully!"));
  }
}
