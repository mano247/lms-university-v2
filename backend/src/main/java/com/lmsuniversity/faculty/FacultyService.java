package com.lmsuniversity.faculty;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.lmsuniversity.filestorage.FileStorageService;
import com.lmsuniversity.rectorate.University;
import com.lmsuniversity.rectorate.UniversityRepository;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.studyprogram.StudyProgramRepository;
import com.lmsuniversity.user.StudentRepository;
import com.lmsuniversity.user.StudentAffairsOfficeRepository;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.TeacherRepository;

@Service
public class FacultyService {
	@Autowired
	private FacultyRepository repository;

	@Autowired
	private UniversityRepository universityRepository;

	@Autowired
	private StudyProgramRepository studyProgramRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentAffairsOfficeRepository studentAffairsOfficeRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FacultyMapper mapper;

	public List<Faculty> findAll() {
		return repository.findAll();
	}

	public Optional<Faculty> findOne(Long id) {
		return repository.findById(id);
	}

	public Optional<Faculty> findByCode(String facultyCode) {
		return repository.findByFacultyCode(facultyCode);
	}

	public Faculty save(Faculty newFaculty) {
		return repository.save(newFaculty);
	}

	public Faculty create(FacultyCreateDto dto) {
		University university = universityRepository.findById(dto.getUniversityId())
				.orElseThrow(() -> new IllegalArgumentException("University not found: " + dto.getUniversityId()));
		Faculty faculty = mapper.toEntity(dto);
		faculty.setUniversity(university);
		faculty.setDean(resolveDean(dto.getDeanId()));
		return repository.save(faculty);
	}

	public Faculty update(Long id, FacultyUpdateDto dto) {
		Faculty faculty = repository.findById(id).orElse(null);
		if (faculty == null) {
			return null;
		}
		University university = universityRepository.findById(dto.getUniversityId())
				.orElseThrow(() -> new IllegalArgumentException("University not found: " + dto.getUniversityId()));
		mapper.updateEntityFromDto(dto, faculty);
		faculty.setUniversity(university);
		faculty.setDean(resolveDean(dto.getDeanId()));
		return repository.save(faculty);
	}

	private Teacher resolveDean(Long deanId) {
		if (deanId == null) {
			return null;
		}
		return teacherRepository.findById(deanId)
				.orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + deanId));
	}

	public void delete(Long id) {
		if (studyProgramRepository.existsByFacultyId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete faculty: it still has study programs associated with it.");
		}
		if (studentRepository.existsByFacultyId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete faculty: it still has students associated with it.");
		}
		if (studentAffairsOfficeRepository.existsByFacultyId(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cannot delete faculty: it still has student affairs office staff associated with it.");
		}
		repository.deleteById(id);
	}

	public void delete(Faculty faculty) {
		repository.delete(faculty);
	}

	public Faculty uploadImage(Long id, MultipartFile file) {
		Faculty faculty = repository.findById(id).orElse(null);
		if (faculty == null) {
			return null;
		}
		String oldImage = faculty.getImage();
		faculty.setImage(fileStorageService.storeImage(file, "faculties"));
		Faculty saved = repository.save(faculty);
		fileStorageService.deleteIfExists(oldImage);
		return saved;
	}

	@SuppressWarnings("deprecation")
	public Set<StudyProgram> getAllCoursesOfFaculty(Long id) {
		return repository.getById(id).getStudyPrograms();
	}
}
