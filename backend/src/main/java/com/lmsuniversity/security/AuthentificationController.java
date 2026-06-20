package com.lmsuniversity.security;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lmsuniversity.permission.Permission;
import com.lmsuniversity.permission.PermissionEnum;
import com.lmsuniversity.user.RegisteredUser;
import com.lmsuniversity.permission.PermissionRepository;
import com.lmsuniversity.user.RegisteredUserRepository;
import com.lmsuniversity.security.request.LoginRequest;
import com.lmsuniversity.security.request.SignupRequest;
import com.lmsuniversity.security.response.JwtResponse;
import com.lmsuniversity.security.response.MessageResponse;
import com.lmsuniversity.security.jwt.JwtUtils;
import com.lmsuniversity.security.services.UserDetailsImpl;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  RegisteredUserRepository userRepository;

  @Autowired
  PermissionRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
    	System.out.println("passed ");
    	System.out.println(loginRequest.getEmail() +" "+ loginRequest.getPassword());
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles = userDetails.getAuthorities().stream()
              .map(item -> item.getAuthority())
              .collect(Collectors.toList());

      return ResponseEntity.ok(JwtResponse.builder()
              .accessToken(jwt)
              .id(userDetails.getId())
              .email(userDetails.getEmail())
              .permissions(roles)
              .userType(userDetails.getUserType())
              .build());
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(new MessageResponse("Incorrect username or password combination."));
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    if (userRepository.existsRegisteredUserByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Signup always grants USER_PERMISSION only. Elevated roles (student/teacher/
    // student-affairs-office/administrator) can only be assigned afterwards through the
    // protected /api/registered-users/change-type endpoint (ADMINISTRATOR_PERMISSION
    // required) - a client can never self-assign a privileged role at signup time.
    Permission userRole = roleRepository.findByName(PermissionEnum.USER_PERMISSION)
        .orElseThrow(() -> new RuntimeException("Error: Permission has not been found."));

    RegisteredUser user = RegisteredUser.builder()
        .email(signUpRequest.getEmail())
        .password(encoder.encode(signUpRequest.getPassword()))
        .username(signUpRequest.getUsername())
        .permissions(Set.of(userRole))
        .build();

    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("You have registered successfully!"));
  }
}
