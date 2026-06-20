package com.lmsuniversity.teachingmaterial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import com.lmsuniversity.course.Course;
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
public class TeachingMaterial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@NotBlank
	private String title;
	private String authors;
	private String publicationYear;
	private String publisher;

	@Column(columnDefinition = "LONGTEXT")
	private String description;

	private String url;

	@Min(0)
	private int pageCount;

	@Min(0)
	private int quantity;

	@Min(0)
	private int issuedQuantity;

	private Outcome outcome;

	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;
}
