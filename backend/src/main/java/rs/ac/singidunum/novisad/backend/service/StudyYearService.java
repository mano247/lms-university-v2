package rs.ac.singidunum.novisad.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.singidunum.novisad.backend.model.StudyYear;
import rs.ac.singidunum.novisad.backend.repository.GodinaStudijaRepository;


@Service
public class GodinaStudijaService {

	@Autowired
	private GodinaStudijaRepository godinaRepository;
	
	public Iterable<StudyYear> findAll() {
		return godinaRepository.findAll();
	}
	
	public Optional<StudyYear> findOne(Long id) {
		return godinaRepository.findById(id);
	}

	public StudyYear save(StudyYear novaGodinaStudija) {
		return godinaRepository.save(novaGodinaStudija);
	}
	
	public StudyYear update(StudyYear studyYear) {
		if(godinaRepository.findById(studyYear.getId()).isPresent()) {
			return godinaRepository.save(studyYear);
		}
		return null;
	}
	
	public void delete(Long id) {
		godinaRepository.deleteById(id);
	}
}
