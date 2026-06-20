package com.lmsuniversity.rectorate;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.user.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class University {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@NotBlank
	private String name;
	private LocalDateTime foundingDate;
	private String contact;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	private String image;
	private String address;

	@OneToMany(mappedBy = "university")
	private Set<Faculty> faculties;

	@OneToMany(mappedBy = "university")
	private Set<Teacher> teachers;

	@ManyToOne
	@JoinColumn(name = "rectorate_id")
	@JsonIgnore
	private Rectorate rectorate;
}
