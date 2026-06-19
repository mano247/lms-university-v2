package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.user.StudentAffairsOffice;
import rs.ac.singidunum.novisad.backend.repository.StudentAffairsOfficeRepository;



@Service
public class StudentAffairsOfficeService {
	@Autowired
	private StudentAffairsOfficeRepository repository;

	public Iterable<StudentAffairsOffice> findAll() {
		return repository.findAll();
	}

	public Optional<StudentAffairsOffice> findOne(Long id) {
		return repository.findById(id);
	}


	public StudentAffairsOffice save(StudentAffairsOffice newStudentAffairsOffice) {
		return repository.save(newStudentAffairsOffice);
	}

	public StudentAffairsOffice update(StudentAffairsOffice studentAffairsOffice) {
		if(repository.findById(studentAffairsOffice.getId()).isPresent()) {
			return repository.save(studentAffairsOffice);
		}
		return null;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(StudentAffairsOffice studentAffairsOffice) {
		repository.delete(studentAffairsOffice);
	}
}
