package com.lmsuniversity.officesupply;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficeSupplyService {
	@Autowired
	private OfficeSupplyRepository repository;

	@Autowired
	private OfficeSupplyMapper mapper;

	public List<OfficeSupply> findAll() {
		return repository.findAll();
	}

	public Optional<OfficeSupply> findOne(Long id) {
		return repository.findById(id);
	}


	public OfficeSupply save(OfficeSupply newOfficeSupply) {
		return repository.save(newOfficeSupply);
	}

	public OfficeSupply create(OfficeSupplyCreateDto dto) {
		OfficeSupply officeSupply = mapper.toEntity(dto);
		return repository.save(officeSupply);
	}

	public OfficeSupply update(Long id, OfficeSupplyUpdateDto dto) {
		OfficeSupply officeSupply = repository.findById(id).orElse(null);
		if (officeSupply == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, officeSupply);
		return repository.save(officeSupply);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(OfficeSupply officeSupply) {
		repository.delete(officeSupply);
	}
}
