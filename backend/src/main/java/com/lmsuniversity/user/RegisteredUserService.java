package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsuniversity.permission.Permission;
import com.lmsuniversity.permission.PermissionEnum;
import com.lmsuniversity.permission.PermissionRepository;

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
			Student student = Student.builder()
					.username(user.getUsername())
					.email(user.getEmail())
					.password(user.getPassword())
					.permissions(permissions)
					.build();
			repository.delete(user);
			repository.save(student);
			return true;
		}
		else if ("teacher_premission".equalsIgnoreCase(type) && !(user instanceof Teacher)) {
				Set<Permission> permissions = new HashSet<>();
				Optional<Permission> teacherRole = permissionRepository.findByName(PermissionEnum.TEACHER_PERMISSION);
				permissions.add(teacherRole.orElse(null));
				Teacher teacher = Teacher.builder()
						.firstName(user.getUsername())
						.email(user.getEmail())
						.password(user.getPassword())
						.permissions(permissions)
						.build();
				repository.delete(user);
				repository.save(teacher);
			 return true;
		}
		else if ("student_affairs_premission".equalsIgnoreCase(type) && !(user instanceof StudentAffairsOffice)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentAffairsRole = permissionRepository.findByName(PermissionEnum.STUDENT_AFFAIRS_PERMISSION);
			permissions.add(studentAffairsRole.orElse(null));
			StudentAffairsOffice studentAffairsOffice = StudentAffairsOffice.builder()
					.username(user.getUsername())
					.email(user.getEmail())
					.password(user.getPassword())
					.permissions(permissions)
					.build();
			repository.delete(user);
			repository.save(studentAffairsOffice);
			 return true;
		}
		else if ("administrator_premission".equalsIgnoreCase(type) && !(user instanceof Administrator)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> administratorRole = permissionRepository.findByName(PermissionEnum.ADMINISTRATOR_PERMISSION);
			permissions.add(administratorRole.orElse(null));
			Administrator administrator = Administrator.builder()
					.username(user.getUsername())
					.email(user.getEmail())
					.password(user.getPassword())
					.permissions(permissions)
					.build();
			repository.delete(user);
			repository.save(administrator);
			 return true;
		}
		else {

			return false;
		}
	}

	public boolean enrollStudent (long userId, StudentDto additionalStudentInfo) {

		RegisteredUser user = repository.findById(userId).orElse(null);
		if (user == null) {
			return false;
			}
		else if (!(user instanceof Student)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentRole = permissionRepository.findByName(PermissionEnum.STUDENT_PERMISSION);
			permissions.add(studentRole.orElse(null));

			Student student = Student.builder()
					.email(additionalStudentInfo.getEmail())
					.username(additionalStudentInfo.getUsername())
					.indexNumber(additionalStudentInfo.getIndexNumber())
					.password(additionalStudentInfo.getPassword())
					.firstName(additionalStudentInfo.getFirstName())
					.lastName(additionalStudentInfo.getLastName())
					.faculty(additionalStudentInfo.getFaculty())
					.permissions(permissions)
					.build();
			repository.delete(user);
			repository.save(student);
			return true;
		}
		else {return false;}
	}
	}
