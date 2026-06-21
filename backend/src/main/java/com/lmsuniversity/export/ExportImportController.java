package com.lmsuniversity.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/export")
@PreAuthorize("hasAnyAuthority('STUDENT_AFFAIRS_PERMISSION', 'ADMINISTRATOR_PERMISSION')")
public class ExportImportController {

	@Autowired
	private DocumentExportService documentExportService;

	@GetMapping(path = "/students/{id}/pdf")
	public ResponseEntity<byte[]> exportStudentPdf(@PathVariable("id") Long id) {
		byte[] pdf = documentExportService.studentTranscriptPdf(id);
		return fileResponse(pdf, MediaType.APPLICATION_PDF, "student-" + id + "-transcript.pdf");
	}

	@GetMapping(path = "/students/{id}/xml")
	public ResponseEntity<byte[]> exportStudentXml(@PathVariable("id") Long id) {
		byte[] xml = documentExportService.studentTranscriptXml(id);
		return fileResponse(xml, MediaType.APPLICATION_XML, "student-" + id + "-transcript.xml");
	}

	@GetMapping(path = "/teachers/{id}/pdf")
	public ResponseEntity<byte[]> exportTeacherPdf(@PathVariable("id") Long id) {
		byte[] pdf = documentExportService.teacherProfilePdf(id);
		return fileResponse(pdf, MediaType.APPLICATION_PDF, "teacher-" + id + "-profile.pdf");
	}

	@GetMapping(path = "/teachers/{id}/xml")
	public ResponseEntity<byte[]> exportTeacherXml(@PathVariable("id") Long id) {
		byte[] xml = documentExportService.teacherProfileXml(id);
		return fileResponse(xml, MediaType.APPLICATION_XML, "teacher-" + id + "-profile.xml");
	}

	private ResponseEntity<byte[]> fileResponse(byte[] content, MediaType mediaType, String filename) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
		return ResponseEntity.ok().contentType(mediaType).headers(headers).body(content);
	}
}
