package com.lmsuniversity.user;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.faculty.FacultyRepository;
import com.lmsuniversity.permission.Permission;
import com.lmsuniversity.permission.PermissionEnum;
import com.lmsuniversity.permission.PermissionRepository;

@Service
public class RegisteredUserService {

	@Autowired
	private RegisteredUserRepository repository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private FacultyRepository facultyRepository;

	@Autowired
	private RegisteredUserMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	public List<RegisteredUser> findAll() {
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

	public RegisteredUser create(RegisteredUserCreateDto dto) {
		RegisteredUser user = mapper.toEntity(dto);
		user.setPassword(encoder.encode(dto.getPassword()));
		Permission userRole = permissionRepository.findByName(PermissionEnum.USER_PERMISSION)
				.orElseThrow(() -> new IllegalStateException("Permission has not been found: USER_PERMISSION"));
		user.setPermissions(Set.of(userRole));
		return repository.save(user);
	}

	public RegisteredUser update(Long id, RegisteredUserUpdateDto dto) {
		RegisteredUser user = repository.findById(id).orElse(null);
		if (user == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, user);
		return repository.save(user);
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

	public boolean enrollStudent(long userId, EnrollStudentDto additionalStudentInfo) {

		RegisteredUser user = repository.findById(userId).orElse(null);
		if (user == null) {
			return false;
			}
		else if (!(user instanceof Student)) {
			Set<Permission> permissions = new HashSet<>();
			Optional<Permission> studentRole = permissionRepository.findByName(PermissionEnum.STUDENT_PERMISSION);
			permissions.add(studentRole.orElse(null));

			Student.StudentBuilder<?, ?> studentBuilder = Student.builder()
					.email(additionalStudentInfo.getEmail())
					.username(additionalStudentInfo.getUsername())
					.indexNumber(additionalStudentInfo.getIndexNumber())
					.password(encoder.encode(additionalStudentInfo.getPassword()))
					.firstName(additionalStudentInfo.getFirstName())
					.lastName(additionalStudentInfo.getLastName())
					.permissions(permissions);

			if (additionalStudentInfo.getFacultyId() != null) {
				Faculty faculty = facultyRepository.findById(additionalStudentInfo.getFacultyId())
						.orElseThrow(() -> new IllegalArgumentException("Faculty not found: " + additionalStudentInfo.getFacultyId()));
				studentBuilder.faculty(faculty);
			}

			repository.delete(user);
			repository.save(studentBuilder.build());
			return true;
		}
		else {return false;}
	}
	}
