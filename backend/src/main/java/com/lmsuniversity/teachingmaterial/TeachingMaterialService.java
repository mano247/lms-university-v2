package com.lmsuniversity.teachingmaterial;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.course.CourseRepository;

@Service
public class TeachingMaterialService {
	@Autowired
	private TeachingMaterialRepository repository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeachingMaterialMapper mapper;

	public List<TeachingMaterial> findAll() {
		return repository.findAll();
	}

	public Optional<TeachingMaterial> findOne(Long id) {
		return repository.findById(id);
	}


	public TeachingMaterial save(TeachingMaterial newTeachingMaterial) {
		return repository.save(newTeachingMaterial);
	}

	public TeachingMaterial create(TeachingMaterialCreateDto dto) {
		TeachingMaterial teachingMaterial = mapper.toEntity(dto);
		if (dto.getCourseId() != null) {
			Course course = courseRepository.findById(dto.getCourseId())
					.orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId()));
			teachingMaterial.setCourse(course);
		}
		return repository.save(teachingMaterial);
	}

	public TeachingMaterial update(Long id, TeachingMaterialUpdateDto dto) {
		TeachingMaterial teachingMaterial = repository.findById(id).orElse(null);
		if (teachingMaterial == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, teachingMaterial);
		if (dto.getCourseId() != null) {
			Course course = courseRepository.findById(dto.getCourseId())
					.orElseThrow(() -> new IllegalArgumentException("Course not found: " + dto.getCourseId()));
			teachingMaterial.setCourse(course);
		} else {
			teachingMaterial.setCourse(null);
		}
		return repository.save(teachingMaterial);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(TeachingMaterial teachingMaterial) {
		repository.delete(teachingMaterial);
	}

}
