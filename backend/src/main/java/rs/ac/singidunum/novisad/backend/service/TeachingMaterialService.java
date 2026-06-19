package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.academic.TeachingMaterial;
import rs.ac.singidunum.novisad.backend.repository.NastavniMaterijalRepository;



@Service
public class NastavniMaterijalService {
	@Autowired
	private NastavniMaterijalRepository repository;
	
	public Iterable<TeachingMaterial> findAll() {
		return repository.findAll();
	}
	
	public Optional<TeachingMaterial> findOne(Long id) {
		return repository.findById(id);
	}

	
	public TeachingMaterial save(TeachingMaterial novaNastavniMaterijal) {
		return repository.save(novaNastavniMaterijal);
	}
	
	public TeachingMaterial update(TeachingMaterial teachingMaterials) {
		if(repository.findById(teachingMaterials.getId()).isPresent()) {
			return repository.save(teachingMaterials);
		}
		return null;
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void delete(TeachingMaterial teachingMaterials) {
		repository.delete(teachingMaterials);
	}
	
}