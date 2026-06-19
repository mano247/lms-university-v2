package rs.ac.singidunum.novisad.backend.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.dto.StudentDTO;
import rs.ac.singidunum.novisad.backend.model.Permission;
import rs.ac.singidunum.novisad.backend.model.PermissionEnum;
import rs.ac.singidunum.novisad.backend.model.user.Administrator;
import rs.ac.singidunum.novisad.backend.model.user.Teacher;
import rs.ac.singidunum.novisad.backend.model.user.RegisteredUser;
import rs.ac.singidunum.novisad.backend.model.user.Student;
import rs.ac.singidunum.novisad.backend.model.user.StudentAffairsOffice;
import rs.ac.singidunum.novisad.backend.repository.PermissionRepository;
import rs.ac.singidunum.novisad.backend.repository.RegistrovaniKorisnikRepository;

@Service
public class RegistrovaniKorisnikService {

	@Autowired
	private RegistrovaniKorisnikRepository repository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	public Iterable<RegisteredUser> findAll() {
		return repository.findAll();
	}
	
	public Optional<RegisteredUser> findOne(Long id) {
		return repository.findById(id);
	}
	
	public Optional<RegisteredUser> findByEmail(String email) {
	    return repository.findByEmail(email);
	}

	public RegisteredUser save(RegisteredUser novaKorisnik) {
		return repository.save(novaKorisnik);
	}
	
	public RegisteredUser update(RegisteredUser korisnik) {
		if(repository.findById(korisnik.getId()).isPresent()) {
			return repository.save(korisnik);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(RegisteredUser korisnik) {
		repository.delete(korisnik);
	}
		
	public boolean promeniTipKorisnika(long korisnikId, String tip) {
		RegisteredUser korisnik = repository.findById(korisnikId).orElse(null);

		if (korisnik == null) {
			return false;
		}
		if ("student_premission".equalsIgnoreCase(tip) && !(korisnik instanceof Student)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentRole = permissionRepository.findByName(PermissionEnum.STUDENT_PERMISSION);
			permissions.add(studentRole.orElse(null));
			Student student = new Student();
			student.setUsername(korisnik.getUsername());
			student.setEmail(korisnik.getEmail());
			student.setPassword(korisnik.getPassword());
			student.setPermissions(permissions);
			repository.delete(korisnik);
			repository.save(student);
			return true;
		}
		else if ("nastavnik_premission".equalsIgnoreCase(tip) && !(korisnik instanceof Teacher)) {
				Set<Permission> permissions = new HashSet<>();
				Optional<Permission> profesorRole = permissionRepository.findByName(PermissionEnum.TEACHER_PERMISSION);
				permissions.add(profesorRole.orElse(null));
				Teacher profesor = new Teacher();
				profesor.setFirstName(korisnik.getUsername());
				profesor.setEmail(korisnik.getEmail());
				profesor.setPassword(korisnik.getPassword());
				profesor.setPermissions(permissions);
				repository.delete(korisnik);
				repository.save(profesor);
			 return true;
		}
		else if ("studentskaSluzba_premission".equalsIgnoreCase(tip) && !(korisnik instanceof StudentAffairsOffice)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentskasluzbaRole = permissionRepository.findByName(PermissionEnum.STUDENT_AFFAIRS_PERMISSION);
			permissions.add(studentskasluzbaRole.orElse(null));
			StudentAffairsOffice studentskaSluzba = new StudentAffairsOffice();
			studentskaSluzba.setUsername(korisnik.getUsername());
			studentskaSluzba.setEmail(korisnik.getEmail());
			studentskaSluzba.setPassword(korisnik.getPassword());
			studentskaSluzba.setPermissions(permissions);
			repository.delete(korisnik);
			repository.save(studentskaSluzba);
			 return true;
		}
		else if ("administrator_premission".equalsIgnoreCase(tip) && !(korisnik instanceof Administrator)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> administratorRole = permissionRepository.findByName(PermissionEnum.ADMINISTRATOR_PERMISSION);
			permissions.add(administratorRole.orElse(null));
			Administrator administrator = new Administrator();
			administrator.setUsername(korisnik.getUsername());
			administrator.setEmail(korisnik.getEmail());
			administrator.setPassword(korisnik.getPassword());
			administrator.setPermissions(permissions);
			repository.delete(korisnik);
			repository.save(administrator);
			 return true;
		}
		else {

			return false;
		}
	}
	
	public boolean upisStudenta (long korisnikId, StudentDTO dodatneInfStudent) {
		
		RegisteredUser korisnik = repository.findById(korisnikId).orElse(null);
		if (korisnik == null) {
			return false;
			}
		else if (!(korisnik instanceof Student)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentRole = permissionRepository.findByName(PermissionEnum.STUDENT_PERMISSION);
			permissions.add(studentRole.orElse(null));
			
			Student student = new Student();
			
			student.setEmail(dodatneInfStudent.getEmail());
			student.setUsername(dodatneInfStudent.getUsername());
			student.setIndexNumber(dodatneInfStudent.getIndexNumber());
			student.setPassword(dodatneInfStudent.getPassword());
			student.setFirstName(dodatneInfStudent.getFirstName());
			student.setLastName(dodatneInfStudent.getLastName());
			student.setFaculty(dodatneInfStudent.getFaculty());
			student.setPermissions(permissions);
			repository.delete(korisnik);
			repository.save(student);
			return true;
		}
		else {return false;}
	}
	}
