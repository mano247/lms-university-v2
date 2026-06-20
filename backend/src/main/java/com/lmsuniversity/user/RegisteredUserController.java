package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.permission.PermissionRepository;
import com.lmsuniversity.security.services.UserDetailsImpl;

@Controller
@RequestMapping(path = "/api/registrovaniKorisnici")
@CrossOrigin(origins = "http://localhost:4200")
public class RegisteredUserController {

	@Autowired
	private RegisteredUserService service;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	PermissionRepository permissionRepository;


	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Set<RegisteredUserDto>> getAll() {
		HashSet<RegisteredUser> users = new HashSet<RegisteredUser>();
		HashSet<RegisteredUserDto> response = new HashSet<>();
		for (RegisteredUser k : service.findAll()) {
			users.add(new RegisteredUser(k.getUsername(),k.getFirstName(),k.getLastName(), k.getEmail(), k.getPassword()));
			response.add(new RegisteredUserDto(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getEmail(), k.getPassword(), k.getPermissions(),k.getFirstName(),k.getLastName()));
		}
		return new ResponseEntity<Set<RegisteredUserDto>>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'USER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
		RegisteredUser k = service.findOne(id).orElse(null);
				if (k != null) {

					if (k instanceof Student) {
						String indexNumber = ((Student) k).getIndexNumber();
						StudentDto student = new StudentDto(k.getId(), k.getClass().getSimpleName(), k.getFirstName(), k.getLastName(), k.getEmail(), k.getPassword(), k.getPermissions(), indexNumber, k.getUsername());
						return new ResponseEntity<StudentDto>(student, HttpStatus.OK);
					} else if (k instanceof Teacher) {
						TeacherDto teacher = new TeacherDto(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getEmail(), k.getPassword(),k.getFirstName(),k.getLastName());
						return new ResponseEntity<TeacherDto>(teacher, HttpStatus.OK);
					} else if (k instanceof Administrator) {
						AdministratorDto admin = new AdministratorDto(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getPassword(), k.getEmail(),k.getFirstName(),k.getLastName());
						return new ResponseEntity<AdministratorDto>(admin, HttpStatus.OK);
					}
					else if (k instanceof StudentAffairsOffice) {
						StudentAffairsOfficeDto ss = new StudentAffairsOfficeDto(k.getId(), k.getClass().getSimpleName(), k.getUsername(), k.getEmail(), k.getPassword(),k.getFirstName(),k.getLastName());
						return new ResponseEntity<StudentAffairsOfficeDto>(ss, HttpStatus.OK);
					}
					RegisteredUserDto user = new RegisteredUserDto(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getEmail(), k.getPassword(), k.getPermissions(),k.getFirstName(),k.getLastName());
					return new ResponseEntity<RegisteredUserDto>(user, HttpStatus.OK);

		}
		return new ResponseEntity<RegisteredUserDto>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<RegisteredUser> create(@RequestBody RegisteredUser r) {
		try {
			service.save(r);
			return new ResponseEntity<RegisteredUser>(r, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<RegisteredUser>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'USER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<RegisteredUser> update(@PathVariable("id") Long id, @RequestBody RegisteredUser user, Authentication authentication) {
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			if (id.equals(userId)) {
				RegisteredUser u = service.findOne(id).orElse(null);
				if (u != null) {
					user.setId(id);
					user.setPassword(user.getPassword());
					user.setPermissions(u.getPermissions());
					user = service.save(user);
					return new ResponseEntity<RegisteredUser>(user, HttpStatus.OK);
				}
				return new ResponseEntity<RegisteredUser>(HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<RegisteredUser>(HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<RegisteredUser> delete(@PathVariable("id") Long id) {
		if (service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<RegisteredUser>(HttpStatus.OK);
		}
		return new ResponseEntity<RegisteredUser>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path="/izmeniTip/{tip}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateUserType(@RequestBody Student student,
			@PathVariable("tip") String type) {
		boolean success = service.changeUserType(student.getId(), type);

		if (success) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(type,HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path="/{id}/dodeliStudenta", method = RequestMethod.PUT)
	public ResponseEntity<String> enrollStudent(@PathVariable("id") long userId, @RequestBody StudentDto newStudentInfo) {
		boolean success = false;

		RegisteredUser k = service.findOne(userId).orElse(null);
		if (k != null) {
			if (k instanceof RegisteredUser) {
				success = service.enrollStudent(userId, newStudentInfo);
			}}
		if (success) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}


}
