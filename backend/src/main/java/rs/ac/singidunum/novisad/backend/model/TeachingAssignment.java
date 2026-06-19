package rs.ac.singidunum.novisad.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TeachingAssignment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int classHours;

	@Column(name = "nastavnik_id")
	private Long teacherId;

	@Enumerated(EnumType.STRING)
	private TeachingType teachingType;



	public TeachingAssignment() {
		super();
	}

	public TeachingAssignment(Long id, int classHours, Long teacherId, TeachingType teachingType) {
		super();
		this.id = id;
		this.classHours = classHours;
		this.teacherId = teacherId;
		this.teachingType = teachingType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getClassHours() {
		return classHours;
	}

	public void setClassHours(int classHours) {
		this.classHours = classHours;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public TeachingType getTeachingType() {
		return teachingType;
	}

	public void setTeachingType(TeachingType teachingType) {
		this.teachingType = teachingType;
	}
}
