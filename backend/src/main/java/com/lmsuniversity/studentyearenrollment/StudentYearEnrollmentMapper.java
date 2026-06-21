package com.lmsuniversity.studentyearenrollment;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.lmsuniversity.studyprogram.StudyProgramMapper;
import com.lmsuniversity.user.StudentMapper;

@Mapper(componentModel = "spring", uses = { StudentMapper.class, StudyProgramMapper.class })
public interface StudentYearEnrollmentMapper {

	StudentYearEnrollmentDto toDto(StudentYearEnrollment enrollment);

	List<StudentYearEnrollmentDto> toDtoList(List<StudentYearEnrollment> enrollments);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "studyYear", ignore = true)
	@Mapping(target = "studyProgram", ignore = true)
	StudentYearEnrollment toEntity(StudentYearEnrollmentCreateDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "studyYear", ignore = true)
	@Mapping(target = "studyProgram", ignore = true)
	void updateEntityFromDto(StudentYearEnrollmentUpdateDto dto, @MappingTarget StudentYearEnrollment entity);
}
