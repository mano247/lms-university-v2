package com.lmsuniversity.examperiod;

import java.util.Optional;

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
import com.lmsuniversity.user.RegisteredUser;
import com.lmsuniversity.user.RegisteredUserRepository;

@Controller
@RequestMapping(path = "/api/exam-periods")
public class ExamPeriodController {

	@Autowired
	private ExamPeriodService service;

	@Autowired
	private ExamPeriodMapper mapper;

	@Autowired
	private RegisteredUserRepository registeredUserRepository;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<ExamPeriodDto>> getAll(Pageable pageable, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		Page<ExamPeriodDto> periods = service.findVisibleToStaff(userDetails.getId(), isAdmin, isStudentAffairs, pageable)
				.map(mapper::toDto);
		periods.forEach(service::populateRegisteredCounts);
		return new ResponseEntity<Page<ExamPeriodDto>>(periods, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExamPeriodDto> get(@PathVariable("id") Long id, Authentication authentication) {
		Optional<ExamPeriod> examPeriod = service.findOne(id);
		if (examPeriod.isEmpty()) {
			return new ResponseEntity<ExamPeriodDto>(HttpStatus.NOT_FOUND);
		}
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		if (!service.isVisibleToStaff(examPeriod.get(), userDetails.getId(), isAdmin, isStudentAffairs)) {
			return new ResponseEntity<ExamPeriodDto>(HttpStatus.NOT_FOUND);
		}
		ExamPeriodDto dto = mapper.toDto(examPeriod.get());
		service.populateRegisteredCounts(dto);
		return new ResponseEntity<ExamPeriodDto>(dto, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/my", method = RequestMethod.GET)
	public ResponseEntity<Page<ExamPeriodDto>> getMyTeacherPeriods(Pageable pageable, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Page<ExamPeriodDto> periods = service.findForTeacher(userDetails.getId(), pageable).map(mapper::toDto);
		periods.forEach(service::populateRegisteredCounts);
		return new ResponseEntity<Page<ExamPeriodDto>>(periods, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
	@RequestMapping(path = "/open", method = RequestMethod.GET)
	public ResponseEntity<Page<ExamPeriodDto>> getOpenForStudent(Pageable pageable, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Page<ExamPeriodDto> periods = service.findOpenForStudent(userDetails.getId(), pageable).map(mapper::toDto);
		return new ResponseEntity<Page<ExamPeriodDto>>(periods, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION', 'STUDENT_PERMISSION')")
	@RequestMapping(path = "/by-course/{courseId}", method = RequestMethod.GET)
	public ResponseEntity<Page<ExamPeriodDto>> getByCourse(@PathVariable("courseId") Long courseId, Pageable pageable, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		boolean isTeacher = hasAuthority(authentication, "TEACHER_PERMISSION");
		boolean isStudent = hasAuthority(authentication, "STUDENT_PERMISSION");
		Page<ExamPeriodDto> periods = service.findVisibleByCourse(courseId, userDetails.getId(), isAdmin, isStudentAffairs, isTeacher, isStudent, pageable)
				.map(mapper::toDto);
		periods.forEach(service::populateRegisteredCounts);
		return new ResponseEntity<Page<ExamPeriodDto>>(periods, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<ExamPeriodDto> create(@Valid @RequestBody ExamPeriodCreateDto dto, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		RegisteredUser createdBy = registeredUserRepository.findById(userDetails.getId())
				.orElseThrow(() -> new IllegalStateException("Authenticated user not found: " + userDetails.getId()));
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		ExamPeriod examPeriod = service.create(dto, createdBy, isAdmin, isStudentAffairs);
		return new ResponseEntity<ExamPeriodDto>(mapper.toDto(examPeriod), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ExamPeriodDto> update(@PathVariable("id") Long id, @Valid @RequestBody ExamPeriodUpdateDto dto, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		ExamPeriod examPeriod = service.update(id, dto, userDetails.getId(), isAdmin, isStudentAffairs);
		if (examPeriod != null) {
			return new ResponseEntity<ExamPeriodDto>(mapper.toDto(examPeriod), HttpStatus.OK);
		}
		return new ResponseEntity<ExamPeriodDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		if (service.delete(id, userDetails.getId(), isAdmin, isStudentAffairs)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

	@Autowired
	private ExamPeriodTermService termService;

	@Autowired
	private ExamPeriodTermMapper termMapper;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION')")
	@RequestMapping(path = "/{periodId}/terms", method = RequestMethod.POST)
	public ResponseEntity<ExamPeriodTermDto> createTerm(@PathVariable("periodId") Long periodId,
			@Valid @RequestBody ExamPeriodTermCreateDto dto, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		ExamPeriodTerm term = termService.create(periodId, dto, userDetails.getId(), isAdmin, isStudentAffairs);
		return new ResponseEntity<ExamPeriodTermDto>(termMapper.toDto(term), HttpStatus.CREATED);
	}

	private boolean hasAuthority(Authentication authentication, String authority) {
		return authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(authority));
	}
}
