package rs.ac.singidunum.novisad.backend.controller;

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

import rs.ac.singidunum.novisad.backend.dto.AdministratorDTO;
import rs.ac.singidunum.novisad.backend.dto.TeacherDTO;
import rs.ac.singidunum.novisad.backend.dto.RegisteredUserDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentDTO;
import rs.ac.singidunum.novisad.backend.dto.StudentAffairsOfficeDTO;
import rs.ac.singidunum.novisad.backend.model.user.Administrator;
import rs.ac.singidunum.novisad.backend.model.user.Teacher;
import rs.ac.singidunum.novisad.backend.model.user.RegisteredUser;
import rs.ac.singidunum.novisad.backend.model.user.Student;
import rs.ac.singidunum.novisad.backend.model.user.StudentAffairsOffice;
import rs.ac.singidunum.novisad.backend.repository.PermissionRepository;
import rs.ac.singidunum.novisad.backend.security.services.UserDetailsImpl;
import rs.ac.singidunum.novisad.backend.service.RegistrovaniKorisnikService;

@Controller
@RequestMapping(path = "/api/registrovaniKorisnici")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrovaniKorisnikController {

	@Autowired
	private RegistrovaniKorisnikService service;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	PermissionRepository permisionRepository;
	
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Set<RegisteredUserDTO>> getAll() {
		HashSet<RegisteredUser> korisnici = new HashSet<RegisteredUser>();
		HashSet<RegisteredUserDTO> response = new HashSet<>();
		for (RegisteredUser k : service.findAll()) {
			korisnici.add(new RegisteredUser(k.getUsername(),k.getFirstName(),k.getLastName(), k.getEmail(), k.getPassword()));
			response.add(new RegisteredUserDTO(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getEmail(), k.getPassword(), k.getPermissions(),k.getFirstName(),k.getLastName()));
		}
		return new ResponseEntity<Set<RegisteredUserDTO>>(response, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'USER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
		RegisteredUser k = service.findOne(id).orElse(null);
				if (k != null) {

					if (k instanceof Student) {
						String indexNumber = ((Student) k).getIndexNumber();
						StudentDTO student = new StudentDTO(k.getId(), k.getClass().getSimpleName(), k.getFirstName(), k.getLastName(), k.getEmail(), k.getPassword(), k.getPermissions(), indexNumber, k.getUsername());
						return new ResponseEntity<StudentDTO>(student, HttpStatus.OK);
					} else if (k instanceof Teacher) {
						TeacherDTO teacher = new TeacherDTO(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getEmail(), k.getPassword(),k.getFirstName(),k.getLastName());
						return new ResponseEntity<TeacherDTO>(teacher, HttpStatus.OK);
					} else if (k instanceof Administrator) {
						AdministratorDTO admin = new AdministratorDTO(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getPassword(), k.getEmail(),k.getFirstName(),k.getLastName());
						return new ResponseEntity<AdministratorDTO>(admin, HttpStatus.OK);
					}
					else if (k instanceof StudentAffairsOffice) {
						StudentAffairsOfficeDTO ss = new StudentAffairsOfficeDTO(k.getId(), k.getClass().getSimpleName(), k.getUsername(), k.getEmail(), k.getPassword(),k.getFirstName(),k.getLastName());
						return new ResponseEntity<StudentAffairsOfficeDTO>(ss, HttpStatus.OK);
					}
					RegisteredUserDTO korisnik = new RegisteredUserDTO(k.getClass().getSimpleName(), k.getId(), k.getUsername(), k.getEmail(), k.getPassword(), k.getPermissions(),k.getFirstName(),k.getLastName());
					return new ResponseEntity<RegisteredUserDTO>(korisnik, HttpStatus.OK);

		}
		return new ResponseEntity<RegisteredUserDTO>(HttpStatus.NOT_FOUND);
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
	public ResponseEntity<RegisteredUser> update(@PathVariable("id") Long id, @RequestBody RegisteredUser korisnik, Authentication authentication) {
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			if (id.equals(userId)) {
				RegisteredUser u = service.findOne(id).orElse(null);
				if (u != null) {
					korisnik.setId(id);
					korisnik.setPassword(korisnik.getPassword());
					korisnik.setPermissions(u.getPermissions());
					korisnik = service.save(korisnik);
					return new ResponseEntity<RegisteredUser>(korisnik, HttpStatus.OK);
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
	public ResponseEntity<String> dodelaStatusa(@RequestBody Student Studnet,
			@PathVariable("tip") String tip) {
		boolean uspesno = service.promeniTipKorisnika(Studnet.getId(), tip);
		
		if (uspesno) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(tip,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path="/{id}/dodeliStudenta", method = RequestMethod.PUT)
	public ResponseEntity<String> dodelaStudenta(@PathVariable("id") long korisnikId, @RequestBody StudentDTO noveInfStudneta) {
		boolean uspesno = false; 
		
		RegisteredUser k = service.findOne(korisnikId).orElse(null);
		if (k != null) {
			if (k instanceof RegisteredUser) {
				uspesno = service.upisStudenta(korisnikId, noveInfStudneta);
			}}
		if (uspesno) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	
}
