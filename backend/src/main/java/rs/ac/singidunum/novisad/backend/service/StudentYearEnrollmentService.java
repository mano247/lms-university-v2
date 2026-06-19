package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.StudentYearEnrollment;
import rs.ac.singidunum.novisad.backend.repository.StudentNaGodiniRepository;

@Service
public class StudentNaGodiniService {
	
	@Autowired
	private StudentNaGodiniRepository sngRepository;
	
	public Iterable<StudentYearEnrollment> findAll() {
		return sngRepository.findAll();
	}
	
	public Optional<StudentYearEnrollment> findOne(Long id) {
		return sngRepository.findById(id);
	}

	
	public StudentYearEnrollment save(StudentYearEnrollment noviStudentNaGodini) {
		return sngRepository.save(noviStudentNaGodini);
	}
	
	public StudentYearEnrollment update(StudentYearEnrollment studentNaGodini) {
		if(sngRepository.findById(studentNaGodini.getId()).isPresent()) {
			return sngRepository.save(studentNaGodini);
		}
		return null;
	}
	
	public void delete(Long id) {
		sngRepository.deleteById(id);
	}
	
	public void delete(StudentYearEnrollment studentNaGodini) {
		sngRepository.delete(studentNaGodini);
	}

}
