package com.lmsuniversity.rectorate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RectorateService {

	@Autowired
	private RectorateRepository repository;

	@Autowired
	private RectorateMapper mapper;


	public List<Rectorate> findAll() {
		return repository.findAll();
	}

	public Optional<Rectorate> findOne(Long id) {
		return repository.findById(id);
	}


	public Rectorate save(Rectorate newRectorate) {
		return repository.save(newRectorate);
	}

	public Rectorate create(RectorateCreateDto dto) {
		Rectorate rectorate = mapper.toEntity(dto);
		return repository.save(rectorate);
	}

	public Rectorate update(Long id, RectorateUpdateDto dto) {
		Rectorate rectorate = repository.findById(id).orElse(null);
		if (rectorate == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, rectorate);
		return repository.save(rectorate);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Rectorate rectorate) {
		repository.delete(rectorate);
	}

	@SuppressWarnings("deprecation")
	public Set<University> getAllUniversitiesForRectorate(Long id) {
		return repository.getById(id).getUniversities();
	}
}
