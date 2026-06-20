package com.lmsuniversity.teachingmaterial;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeachingMaterialService {
	@Autowired
	private TeachingMaterialRepository repository;

	public Iterable<TeachingMaterial> findAll() {
		return repository.findAll();
	}

	public Optional<TeachingMaterial> findOne(Long id) {
		return repository.findById(id);
	}


	public TeachingMaterial save(TeachingMaterial newTeachingMaterial) {
		return repository.save(newTeachingMaterial);
	}

	public TeachingMaterial update(TeachingMaterial teachingMaterial) {
		if(repository.findById(teachingMaterial.getId()).isPresent()) {
			return repository.save(teachingMaterial);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(TeachingMaterial teachingMaterial) {
		repository.delete(teachingMaterial);
	}

}
