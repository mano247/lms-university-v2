package com.lmsuniversity.schedule;

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

@Controller
@RequestMapping(path = "/api/class-schedules")
public class ClassScheduleController {

	@Autowired
	private ClassScheduleService service;

	@Autowired
	private ClassScheduleMapper mapper;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public ResponseEntity<Page<ClassScheduleDto>> getAll(Pageable pageable, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		Page<ClassScheduleDto> schedules = service.findVisibleToStaff(userDetails.getId(), isAdmin, isStudentAffairs, pageable)
				.map(mapper::toDto);
		return new ResponseEntity<Page<ClassScheduleDto>>(schedules, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ClassScheduleDto> get(@PathVariable("id") Long id, Authentication authentication) {
		Optional<ClassSchedule> schedule = service.findOne(id);
		if (schedule.isEmpty()) {
			return new ResponseEntity<ClassScheduleDto>(HttpStatus.NOT_FOUND);
		}
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		if (!service.isVisibleToStaff(schedule.get(), userDetails.getId(), isAdmin, isStudentAffairs)) {
			return new ResponseEntity<ClassScheduleDto>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ClassScheduleDto>(mapper.toDto(schedule.get()), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<ClassScheduleDto> create(@Valid @RequestBody ClassScheduleCreateDto dto, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		ClassSchedule schedule = service.create(dto, userDetails.getId(), isAdmin, isStudentAffairs);
		return new ResponseEntity<ClassScheduleDto>(mapper.toDto(schedule), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ClassScheduleDto> update(@PathVariable("id") Long id, @Valid @RequestBody ClassScheduleUpdateDto dto, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		ClassSchedule schedule = service.update(id, dto, userDetails.getId(), isAdmin, isStudentAffairs);
		if (schedule != null) {
			return new ResponseEntity<ClassScheduleDto>(mapper.toDto(schedule), HttpStatus.OK);
		}
		return new ResponseEntity<ClassScheduleDto>(HttpStatus.NOT_FOUND);
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

	@PreAuthorize("hasAnyAuthority('TEACHER_PERMISSION')")
	@RequestMapping(path = "/my", method = RequestMethod.GET)
	public ResponseEntity<Page<ClassScheduleDto>> getMyTeaching(Pageable pageable, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Page<ClassScheduleDto> schedules = service.findMyTeaching(userDetails.getId(), pageable).map(mapper::toDto);
		return new ResponseEntity<Page<ClassScheduleDto>>(schedules, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
	@RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
	public ResponseEntity<Page<ClassScheduleDto>> getStudentSchedule(@PathVariable("id") Long id, Pageable pageable, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		if (!id.equals(userDetails.getId())) {
			return new ResponseEntity<Page<ClassScheduleDto>>(HttpStatus.NOT_FOUND);
		}
		Page<ClassScheduleDto> schedules = service.findMySchedule(id, pageable).map(mapper::toDto);
		return new ResponseEntity<Page<ClassScheduleDto>>(schedules, HttpStatus.OK);
	}

	private boolean hasAuthority(Authentication authentication, String authority) {
		return authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(authority));
	}
}
