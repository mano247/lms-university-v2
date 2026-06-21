package com.lmsuniversity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.lmsuniversity.faculty.Faculty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class StudentAffairsOffice extends RegisteredUser {

	@ManyToOne
	@JoinColumn(name = "faculty_id")
	private Faculty faculty;
}
