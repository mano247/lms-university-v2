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
import rs.ac.singidunum.novisad.backend.repository.RegisteredUserRepository;

@Service
public class RegisteredUserService {

	@Autowired
	private RegisteredUserRepository repository;

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

	public RegisteredUser save(RegisteredUser newUser) {
		return repository.save(newUser);
	}

	public RegisteredUser update(RegisteredUser user) {
		if(repository.findById(user.getId()).isPresent()) {
			return repository.save(user);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(RegisteredUser user) {
		repository.delete(user);
	}

	public boolean changeUserType(long userId, String type) {
		RegisteredUser user = repository.findById(userId).orElse(null);

		if (user == null) {
			return false;
		}
		if ("student_premission".equalsIgnoreCase(type) && !(user instanceof Student)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentRole = permissionRepository.findByName(PermissionEnum.STUDENT_PERMISSION);
			permissions.add(studentRole.orElse(null));
			Student student = new Student();
			student.setUsername(user.getUsername());
			student.setEmail(user.getEmail());
			student.setPassword(user.getPassword());
			student.setPermissions(permissions);
			repository.delete(user);
			repository.save(student);
			return true;
		}
		else if ("nastavnik_premission".equalsIgnoreCase(type) && !(user instanceof Teacher)) {
				Set<Permission> permissions = new HashSet<>();
				Optional<Permission> teacherRole = permissionRepository.findByName(PermissionEnum.TEACHER_PERMISSION);
				permissions.add(teacherRole.orElse(null));
				Teacher teacher = new Teacher();
				teacher.setFirstName(user.getUsername());
				teacher.setEmail(user.getEmail());
				teacher.setPassword(user.getPassword());
				teacher.setPermissions(permissions);
				repository.delete(user);
				repository.save(teacher);
			 return true;
		}
		else if ("studentskaSluzba_premission".equalsIgnoreCase(type) && !(user instanceof StudentAffairsOffice)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentAffairsRole = permissionRepository.findByName(PermissionEnum.STUDENT_AFFAIRS_PERMISSION);
			permissions.add(studentAffairsRole.orElse(null));
			StudentAffairsOffice studentAffairsOffice = new StudentAffairsOffice();
			studentAffairsOffice.setUsername(user.getUsername());
			studentAffairsOffice.setEmail(user.getEmail());
			studentAffairsOffice.setPassword(user.getPassword());
			studentAffairsOffice.setPermissions(permissions);
			repository.delete(user);
			repository.save(studentAffairsOffice);
			 return true;
		}
		else if ("administrator_premission".equalsIgnoreCase(type) && !(user instanceof Administrator)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> administratorRole = permissionRepository.findByName(PermissionEnum.ADMINISTRATOR_PERMISSION);
			permissions.add(administratorRole.orElse(null));
			Administrator administrator = new Administrator();
			administrator.setUsername(user.getUsername());
			administrator.setEmail(user.getEmail());
			administrator.setPassword(user.getPassword());
			administrator.setPermissions(permissions);
			repository.delete(user);
			repository.save(administrator);
			 return true;
		}
		else {

			return false;
		}
	}

	public boolean enrollStudent (long userId, StudentDTO additionalStudentInfo) {

		RegisteredUser user = repository.findById(userId).orElse(null);
		if (user == null) {
			return false;
			}
		else if (!(user instanceof Student)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentRole = permissionRepository.findByName(PermissionEnum.STUDENT_PERMISSION);
			permissions.add(studentRole.orElse(null));

			Student student = new Student();

			student.setEmail(additionalStudentInfo.getEmail());
			student.setUsername(additionalStudentInfo.getUsername());
			student.setIndexNumber(additionalStudentInfo.getIndexNumber());
			student.setPassword(additionalStudentInfo.getPassword());
			student.setFirstName(additionalStudentInfo.getFirstName());
			student.setLastName(additionalStudentInfo.getLastName());
			student.setFaculty(additionalStudentInfo.getFaculty());
			student.setPermissions(permissions);
			repository.delete(user);
			repository.save(student);
			return true;
		}
		else {return false;}
	}
	}
