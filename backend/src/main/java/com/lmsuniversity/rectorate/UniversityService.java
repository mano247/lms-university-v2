package com.lmsuniversity.rectorate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {
	@Autowired
	private UniversityRepository repository;

	@Autowired
	private RectorateRepository rectorateRepository;

	@Autowired
	private UniversityMapper mapper;

	public List<University> findAll() {
		return repository.findAll();
	}

	public Optional<University> findOne(Long id) {
		return repository.findById(id);
	}


	public University save(University newUniversity) {
		return repository.save(newUniversity);
	}

	public University create(UniversityCreateDto dto) {
		Rectorate rectorate = rectorateRepository.findById(dto.getRectorateId())
				.orElseThrow(() -> new IllegalArgumentException("Rectorate not found: " + dto.getRectorateId()));
		University university = mapper.toEntity(dto);
		university.setRectorate(rectorate);
		return repository.save(university);
	}

	public University update(Long id, UniversityUpdateDto dto) {
		University university = repository.findById(id).orElse(null);
		if (university == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, university);
		return repository.save(university);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(University university) {
		repository.delete(university);
	}

}
