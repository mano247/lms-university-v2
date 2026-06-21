package com.lmsuniversity.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {
	@Autowired
	private AdministratorRepository repository;

	@Autowired
	private AdministratorMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	public List<Administrator> findAll() {
		return repository.findAll();
	}

	public Optional<Administrator> findOne(Long id) {
		return repository.findById(id);
	}


	public Administrator save(Administrator newAdministrator) {
		return repository.save(newAdministrator);
	}

	public Administrator create(AdministratorCreateDto dto) {
		Administrator administrator = mapper.toEntity(dto);
		administrator.setPassword(encoder.encode(dto.getPassword()));
		return repository.save(administrator);
	}

	public Administrator update(Long id, AdministratorUpdateDto dto) {
		Administrator administrator = repository.findById(id).orElse(null);
		if (administrator == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, administrator);
		return repository.save(administrator);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Administrator administrator) {
		repository.delete(administrator);
	}
}
