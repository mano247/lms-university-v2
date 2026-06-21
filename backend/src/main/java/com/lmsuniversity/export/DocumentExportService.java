package com.lmsuniversity.export;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lmsuniversity.course.Course;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.examattempt.ExamAttemptRepository;
import com.lmsuniversity.studentyearenrollment.StudentYearEnrollment;
import com.lmsuniversity.studentyearenrollment.StudentYearEnrollmentRepository;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.user.StudentRepository;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.TeacherRepository;
import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class DocumentExportService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ExamAttemptRepository examAttemptRepository;

	@Autowired
	private StudentYearEnrollmentRepository studentYearEnrollmentRepository;

	public byte[] studentTranscriptPdf(Long studentId) {
		Student student = resolveStudent(studentId);
		List<ExamAttempt> examAttempts = examAttemptRepository.findByStudentId(studentId);
		Optional<StudentYearEnrollment> currentEnrollment = currentEnrollment(studentId);

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			com.lowagie.text.Document document = new com.lowagie.text.Document();
			PdfWriter.getInstance(document, out);
			document.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

			document.add(new Paragraph("Transcript of Records", titleFont));
			document.add(Chunk.NEWLINE);

			document.add(new Paragraph("Student: " + student.getFirstName() + " " + student.getLastName(), normalFont));
			document.add(new Paragraph("Index number: " + student.getIndexNumber(), normalFont));
			document.add(new Paragraph("Email: " + student.getEmail(), normalFont));
			if (student.getFaculty() != null) {
				document.add(new Paragraph("Faculty: " + student.getFaculty().getName(), normalFont));
			}
			if (currentEnrollment.isPresent()) {
				StudentYearEnrollment enrollment = currentEnrollment.get();
				document.add(new Paragraph("Study program: " + enrollment.getStudyProgram().getName(), normalFont));
				document.add(new Paragraph("Study year: " + enrollment.getStudyYear().getYear(), normalFont));
			}
			document.add(Chunk.NEWLINE);

			document.add(new Paragraph("Exam history", headingFont));
			document.add(Chunk.NEWLINE);

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			addHeaderCell(table, "Course");
			addHeaderCell(table, "ECTS");
			addHeaderCell(table, "Points");
			addHeaderCell(table, "Grade");
			addHeaderCell(table, "Examiner");

			for (ExamAttempt attempt : examAttempts) {
				Course course = attempt.getCourse();
				table.addCell(new PdfPCell(new Paragraph(course.getName(), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(course.getEcts()), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(attempt.getPoints()), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(attempt.getFinalGrade()), normalFont)));
				Teacher examiner = attempt.getTeacher();
				String examinerName = examiner != null ? examiner.getFirstName() + " " + examiner.getLastName() : "";
				table.addCell(new PdfPCell(new Paragraph(examinerName, normalFont)));
			}
			document.add(table);

			document.close();
			return out.toByteArray();
		} catch (com.lowagie.text.DocumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate PDF document", e);
		}
	}

	public byte[] studentTranscriptXml(Long studentId) {
		Student student = resolveStudent(studentId);
		List<ExamAttempt> examAttempts = examAttemptRepository.findByStudentId(studentId);
		Optional<StudentYearEnrollment> currentEnrollment = currentEnrollment(studentId);

		Document doc = newXmlDocument();
		Element root = doc.createElement("studentTranscript");
		doc.appendChild(root);

		appendTextElement(doc, root, "firstName", student.getFirstName());
		appendTextElement(doc, root, "lastName", student.getLastName());
		appendTextElement(doc, root, "indexNumber", student.getIndexNumber());
		appendTextElement(doc, root, "email", student.getEmail());
		if (student.getFaculty() != null) {
			appendTextElement(doc, root, "faculty", student.getFaculty().getName());
		}
		if (currentEnrollment.isPresent()) {
			StudentYearEnrollment enrollment = currentEnrollment.get();
			appendTextElement(doc, root, "studyProgram", enrollment.getStudyProgram().getName());
			appendTextElement(doc, root, "studyYear", String.valueOf(enrollment.getStudyYear().getYear()));
		}

		Element examsElement = doc.createElement("examAttempts");
		root.appendChild(examsElement);
		for (ExamAttempt attempt : examAttempts) {
			Element examElement = doc.createElement("examAttempt");
			Course course = attempt.getCourse();
			appendTextElement(doc, examElement, "course", course.getName());
			appendTextElement(doc, examElement, "ects", String.valueOf(course.getEcts()));
			appendTextElement(doc, examElement, "points", String.valueOf(attempt.getPoints()));
			appendTextElement(doc, examElement, "grade", String.valueOf(attempt.getFinalGrade()));
			Teacher examiner = attempt.getTeacher();
			if (examiner != null) {
				appendTextElement(doc, examElement, "examiner", examiner.getFirstName() + " " + examiner.getLastName());
			}
			examsElement.appendChild(examElement);
		}

		return transformToBytes(doc);
	}

	public byte[] teacherProfilePdf(Long teacherId) {
		Teacher teacher = resolveTeacher(teacherId);

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			com.lowagie.text.Document document = new com.lowagie.text.Document();
			PdfWriter.getInstance(document, out);
			document.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

			document.add(new Paragraph("Teacher Profile", titleFont));
			document.add(Chunk.NEWLINE);

			document.add(new Paragraph("Name: " + teacher.getFirstName() + " " + teacher.getLastName(), normalFont));
			document.add(new Paragraph("Email: " + teacher.getEmail(), normalFont));
			if (teacher.getPersonalIdNumber() != null) {
				document.add(new Paragraph("Personal ID number: " + teacher.getPersonalIdNumber(), normalFont));
			}
			if (teacher.getUniversity() != null) {
				document.add(new Paragraph("University: " + teacher.getUniversity().getName(), normalFont));
			}
			if (teacher.getBiography() != null && !teacher.getBiography().isBlank()) {
				document.add(Chunk.NEWLINE);
				document.add(new Paragraph("Biography", headingFont));
				document.add(new Paragraph(teacher.getBiography(), normalFont));
			}
			document.add(Chunk.NEWLINE);

			document.add(new Paragraph("Courses taught", headingFont));
			document.add(Chunk.NEWLINE);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			addHeaderCell(table, "Course");
			addHeaderCell(table, "ECTS");

			List<Course> courses = teacher.getCourses() != null ? List.copyOf(teacher.getCourses()) : List.of();
			for (Course course : courses) {
				table.addCell(new PdfPCell(new Paragraph(course.getName(), normalFont)));
				table.addCell(new PdfPCell(new Paragraph(String.valueOf(course.getEcts()), normalFont)));
			}
			document.add(table);

			document.close();
			return out.toByteArray();
		} catch (com.lowagie.text.DocumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate PDF document", e);
		}
	}

	public byte[] teacherProfileXml(Long teacherId) {
		Teacher teacher = resolveTeacher(teacherId);

		Document doc = newXmlDocument();
		Element root = doc.createElement("teacherProfile");
		doc.appendChild(root);

		appendTextElement(doc, root, "firstName", teacher.getFirstName());
		appendTextElement(doc, root, "lastName", teacher.getLastName());
		appendTextElement(doc, root, "email", teacher.getEmail());
		if (teacher.getPersonalIdNumber() != null) {
			appendTextElement(doc, root, "personalIdNumber", teacher.getPersonalIdNumber());
		}
		if (teacher.getUniversity() != null) {
			appendTextElement(doc, root, "university", teacher.getUniversity().getName());
		}
		if (teacher.getBiography() != null) {
			appendTextElement(doc, root, "biography", teacher.getBiography());
		}

		Element coursesElement = doc.createElement("courses");
		root.appendChild(coursesElement);
		List<Course> courses = teacher.getCourses() != null ? List.copyOf(teacher.getCourses()) : List.of();
		for (Course course : courses) {
			Element courseElement = doc.createElement("course");
			appendTextElement(doc, courseElement, "name", course.getName());
			appendTextElement(doc, courseElement, "ects", String.valueOf(course.getEcts()));
			coursesElement.appendChild(courseElement);
		}

		return transformToBytes(doc);
	}

	private Student resolveStudent(Long studentId) {
		return studentRepository.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found: " + studentId));
	}

	private Teacher resolveTeacher(Long teacherId) {
		return teacherRepository.findWithCoursesById(teacherId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found: " + teacherId));
	}

	private Optional<StudentYearEnrollment> currentEnrollment(Long studentId) {
		return studentYearEnrollmentRepository.findByStudentId(studentId).stream()
				.max(Comparator.comparing(StudentYearEnrollment::getEnrollmentDate));
	}

	private void addHeaderCell(PdfPTable table, String text) {
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
		PdfPCell cell = new PdfPCell(new Paragraph(text, headerFont));
		cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_LEFT);
		table.addCell(cell);
	}

	private Document newXmlDocument() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.newDocument();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate XML document", e);
		}
	}

	private void appendTextElement(Document doc, Element parent, String tagName, String text) {
		Element element = doc.createElement(tagName);
		element.setTextContent(text);
		parent.appendChild(element);
	}

	private byte[] transformToBytes(Document doc) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(new DOMSource(doc), new StreamResult(out));
			return out.toByteArray();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate XML document", e);
		}
	}
}
