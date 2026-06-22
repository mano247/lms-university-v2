package com.lmsuniversity.seed;

import java.util.List;

/** Hand-authored global (university-wide) announcements. */
public final class AnnouncementCatalog {

	private AnnouncementCatalog() {
	}

	public record AnnouncementTemplate(String title, String content) {
	}

	public static final List<AnnouncementTemplate> GLOBAL_ANNOUNCEMENTS = List.of(
			new AnnouncementTemplate("Fall Semester Enrollment Now Open",
					"Enrollment for the upcoming fall semester is open through the Student Affairs Office of your home faculty. Please bring proof of tuition payment and a valid student ID."),
			new AnnouncementTemplate("University Library Extended Hours During Exam Sessions",
					"During exam sessions, the Central University Library will remain open until midnight on weekdays and until 8 PM on weekends. Please respect the quiet study floors."),
			new AnnouncementTemplate("Annual Career Fair",
					"The University of Ashford Career Fair will take place in the Main Hall, bringing together regional employers across engineering, business, healthcare, and the humanities. All students are welcome."),
			new AnnouncementTemplate("Scholarship Applications Deadline Approaching",
					"Students with an average grade above 9.00 are eligible to apply for the merit-based scholarship program administered by the Rectorate. Applications close at the end of the month."),
			new AnnouncementTemplate("Planned IT System Maintenance",
					"The student information system will be temporarily unavailable for scheduled maintenance over the weekend. Please complete any pending exam registrations beforehand."),
			new AnnouncementTemplate("Orientation Week for New Students",
					"Newly enrolled students are invited to Orientation Week, including campus tours, faculty introductions, and a welcome address from the Rector."),
			new AnnouncementTemplate("Winter Break Schedule",
					"Classes are suspended during the winter break. Administrative offices will operate on reduced hours; the exact schedule is published on each faculty's notice board."),
			new AnnouncementTemplate("Student Health Insurance Renewal",
					"Students are reminded to renew their health insurance coverage at the start of the academic year through the Student Affairs Office."),
			new AnnouncementTemplate("Call for Student Council Elections",
					"Nominations are open for the University Student Council. Interested candidates should submit their applications to their faculty's Student Affairs Office."),
			new AnnouncementTemplate("Research Grant Opportunities for Final-Year Students",
					"Final-year students working on their thesis may apply for a small research grant to cover materials, travel, or data collection costs."),
			new AnnouncementTemplate("Campus Wi-Fi Network Upgrade",
					"The campus wireless network is being upgraded across all faculty buildings. Some buildings may experience brief connectivity interruptions during the rollout."),
			new AnnouncementTemplate("International Exchange Program Information Session",
					"An information session on Erasmus-style exchange opportunities with partner universities will be held in the Main Hall. All interested students are encouraged to attend."),
			new AnnouncementTemplate("Updated Parking Regulations on Campus",
					"New parking regulations take effect at the start of the semester. Student vehicle permits can be requested from the Rectorate's general services office."),
			new AnnouncementTemplate("Sports and Recreation Club Sign-Ups",
					"The University Sports Center is opening sign-ups for basketball, football, swimming, and chess clubs for the upcoming semester."),
			new AnnouncementTemplate("Anti-Plagiarism Policy Reminder",
					"All students are reminded that submitted coursework, theses, and projects are subject to plagiarism screening in line with the University's academic integrity policy."),
			new AnnouncementTemplate("Graduation Ceremony Date Announced",
					"The annual graduation ceremony for all faculties will be held in the University Amphitheater. Graduating students will receive detailed instructions from their faculty."),
			new AnnouncementTemplate("Public Lecture Series: Visiting Professors",
					"The Rectorate is hosting a public lecture series featuring visiting professors from partner universities. Attendance is open to all students and staff."),
			new AnnouncementTemplate("Updated Tuition Payment Schedule",
					"The tuition payment schedule for the academic year has been updated. Students paying in installments should consult their faculty's Student Affairs Office for the new deadlines."));
}
