package com.lmsuniversity.announcement;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService {
	@Autowired
	private AnnouncementRepository repository;

	@Autowired
	private AnnouncementMapper mapper;

	public Page<Announcement> findAll(Pageable pageable) {
		return repository.findAllGlobal(pageable);
	}

	public Optional<Announcement> findOne(Long id) {
		return repository.findById(id);
	}


	public Announcement save(Announcement newAnnouncement) {
		return repository.save(newAnnouncement);
	}

	public Announcement create(AnnouncementCreateDto dto) {
		Announcement announcement = mapper.toEntity(dto);
		announcement.setDate(LocalDateTime.now());
		return repository.save(announcement);
	}

	public Announcement update(Long id, AnnouncementUpdateDto dto) {
		Announcement announcement = repository.findById(id).orElse(null);
		if (announcement == null) {
			return null;
		}
		mapper.updateEntityFromDto(dto, announcement);
		if (dto.getImage() != null && !dto.getImage().isBlank()) {
			announcement.setImage(dto.getImage());
		}
		announcement.setDate(LocalDateTime.now());
		return repository.save(announcement);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void delete(Announcement announcement) {
		repository.delete(announcement);
	}
}
