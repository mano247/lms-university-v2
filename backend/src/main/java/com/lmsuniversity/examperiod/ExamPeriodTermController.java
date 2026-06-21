package com.lmsuniversity.examperiod;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.examattempt.ExamAttemptDto;
import com.lmsuniversity.examattempt.ExamAttemptMapper;
import com.lmsuniversity.security.services.UserDetailsImpl;

@Controller
@RequestMapping(path = "/api/exam-period-terms")
public class ExamPeriodTermController {

	@Autowired
	private ExamPeriodTermService service;

	@Autowired
	private ExamPeriodTermMapper mapper;

	@Autowired
	private ExamAttemptMapper examAttemptMapper;

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExamPeriodTermDto> get(@PathVariable("id") Long id, Authentication authentication) {
		Optional<ExamPeriodTerm> term = service.findOne(id);
		if (term.isEmpty()) {
			return new ResponseEntity<ExamPeriodTermDto>(HttpStatus.NOT_FOUND);
		}
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		if (!service.canManage(term.get(), userDetails.getId(), isAdmin, isStudentAffairs)) {
			return new ResponseEntity<ExamPeriodTermDto>(HttpStatus.NOT_FOUND);
		}
		ExamPeriodTermDto dto = mapper.toDto(term.get());
		dto.setRegisteredCount(service.countRegistered(id));
		return new ResponseEntity<ExamPeriodTermDto>(dto, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION')")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ExamPeriodTermDto> update(@PathVariable("id") Long id, @Valid @RequestBody ExamPeriodTermUpdateDto dto, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		boolean isAdmin = hasAuthority(authentication, "ADMINISTRATOR_PERMISSION");
		boolean isStudentAffairs = hasAuthority(authentication, "STUDENT_AFFAIRS_PERMISSION");
		ExamPeriodTerm term = service.update(id, dto, userDetails.getId(), isAdmin, isStudentAffairs);
		if (term != null) {
			return new ResponseEntity<ExamPeriodTermDto>(mapper.toDto(term), HttpStatus.OK);
		}
		return new ResponseEntity<ExamPeriodTermDto>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyAuthority('ADMINISTRATOR_PERMISSION', 'STUDENT_AFFAIRS_PERMISSION', 'TEACHER_PERMISSION')")
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

	@PreAuthorize("hasAnyAuthority('STUDENT_PERMISSION')")
	@RequestMapping(path = "/{id}/register", method = RequestMethod.POST)
	public ResponseEntity<ExamAttemptDto> register(@PathVariable("id") Long id, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ExamAttempt attempt = service.register(id, userDetails.getId());
		return new ResponseEntity<ExamAttemptDto>(examAttemptMapper.toDto(attempt), HttpStatus.CREATED);
	}

	private boolean hasAuthority(Authentication authentication, String authority) {
		return authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(authority));
	}
}
