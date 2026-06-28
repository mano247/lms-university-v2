package com.lmsuniversity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lmsuniversity.security.services.UserDetailsImpl;

@Controller
@RequestMapping(path = "/api/registered-users")
public class RegisteredUserController {

	@Autowired
	private RegisteredUserService service;

	@Autowired
	private RegisteredUserMapper mapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private AdministratorMapper administratorMapper;

	@Autowired
	private StudentAffairsOfficeMapper studentAffairsOfficeMapper;


	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<RegisteredUserDto>> getAll(Pageable pageable) {
		Page<RegisteredUserDto> response = service.findAll(pageable).map(mapper::toListDto);
		return new ResponseEntity<Page<RegisteredUserDto>>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'USER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
		RegisteredUser k = service.findOne(id).orElse(null);
		if (k != null) {
			if (k instanceof Student student) {
				return new ResponseEntity<StudentDto>(studentMapper.toDto(student), HttpStatus.OK);
			} else if (k instanceof Teacher teacher) {
				return new ResponseEntity<TeacherDto>(teacherMapper.toDto(teacher), HttpStatus.OK);
			} else if (k instanceof Administrator administrator) {
				return new ResponseEntity<AdministratorDto>(administratorMapper.toDto(administrator), HttpStatus.OK);
			} else if (k instanceof StudentAffairsOffice studentAffairsOffice) {
				return new ResponseEntity<StudentAffairsOfficeDto>(studentAffairsOfficeMapper.toDto(studentAffairsOffice), HttpStatus.OK);
			}
			return new ResponseEntity<RegisteredUserDto>(mapper.toDto(k), HttpStatus.OK);
		}
		return new ResponseEntity<RegisteredUserDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<RegisteredUserDto> create(@Valid @RequestBody RegisteredUserCreateDto dto) {
		RegisteredUser user = service.create(dto);
		return new ResponseEntity<RegisteredUserDto>(mapper.toDto(user), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'STUDENT_PERMISSION', 'TEACHER_PERMISSION', 'USER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<RegisteredUserDto> update(@PathVariable("id") Long id, @Valid @RequestBody RegisteredUserUpdateDto dto, Authentication authentication) {
		if (authentication.isAuthenticated()) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getId();

			if (id.equals(userId)) {
				RegisteredUser user = service.update(id, dto);
				if (user != null) {
					return new ResponseEntity<RegisteredUserDto>(mapper.toDto(user), HttpStatus.OK);
				}
				return new ResponseEntity<RegisteredUserDto>(HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<RegisteredUserDto>(HttpStatus.UNAUTHORIZED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<RegisteredUser> delete(@PathVariable("id") Long id) {
		if (service.findOne(id).isPresent()) {
			service.delete(id);
			return new ResponseEntity<RegisteredUser>(HttpStatus.OK);
		}
		return new ResponseEntity<RegisteredUser>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION')")
	@RequestMapping(path="/change-type/{type}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateUserType(@Valid @RequestBody ChangeUserTypeDto dto,
			@PathVariable("type") String type) {
		boolean success = service.changeUserType(dto.getUserId(), type);

		if (success) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(type,HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path="/{id}/enroll-student", method = RequestMethod.PUT)
	public ResponseEntity<String> enrollStudent(@PathVariable("id") long userId, @Valid @RequestBody EnrollStudentDto newStudentInfo) {
		boolean success = service.enrollStudent(userId, newStudentInfo);

		if (success) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}


}
