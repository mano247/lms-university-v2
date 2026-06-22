package com.lmsuniversity.seed;

import java.util.List;

/** The 10 faculties that make up the seeded University of Ashford. */
public final class FacultyCatalog {

	private FacultyCatalog() {
	}

	public record FacultyTemplate(FacultyKey key, String name, String code, String address, String description, int foundingYear) {
	}

	public static final List<FacultyTemplate> FACULTIES = List.of(
			new FacultyTemplate(FacultyKey.CS, "Faculty of Computer Science and Information Technology", "FCSIT",
					"14 Innovation Boulevard, Ashford",
					"Educates engineers and researchers in computing, software, data, and information security, with strong industry partnerships and modern teaching laboratories.",
					1982),
			new FacultyTemplate(FacultyKey.EE, "Faculty of Electrical Engineering", "FEE",
					"7 Tesla Street, Ashford",
					"One of the founding technical faculties of the University, covering power systems, electronics, telecommunications, and control engineering.",
					1947),
			new FacultyTemplate(FacultyKey.ME, "Faculty of Mechanical Engineering", "FME",
					"21 Industrial Quay, Ashford",
					"Trains mechanical, industrial, and automotive engineers through a curriculum combining classical mechanics with modern manufacturing and automation.",
					1947),
			new FacultyTemplate(FacultyKey.CIV, "Faculty of Civil Engineering and Architecture", "FCEA",
					"3 Founders Square, Ashford",
					"Combines civil engineering, architecture, and urban planning programs, with design studios and a structural testing laboratory.",
					1954),
			new FacultyTemplate(FacultyKey.ECO, "Faculty of Economics and Business", "FEB",
					"56 Merchant Row, Ashford",
					"The University's largest faculty by enrollment, offering programs across economics, finance, marketing, and management.",
					1947),
			new FacultyTemplate(FacultyKey.LAW, "Faculty of Law", "FLAW",
					"9 Justice Court, Ashford",
					"The oldest faculty of the University, preparing students for careers in the judiciary, public administration, and international institutions.",
					1905),
			new FacultyTemplate(FacultyKey.MED, "Faculty of Medicine", "FMED",
					"100 Clinical Center Avenue, Ashford",
					"Operates in close cooperation with the Ashford University Clinical Center, offering medical, dental, pharmacy, and allied health programs.",
					1920),
			new FacultyTemplate(FacultyKey.HUM, "Faculty of Philosophy and Humanities", "FPH",
					"18 Liberty Park, Ashford",
					"Houses the social sciences and humanities, including psychology, sociology, history, languages, and education.",
					1954),
			new FacultyTemplate(FacultyKey.SCI, "Faculty of Natural Sciences and Mathematics", "FNSM",
					"5 Observatory Hill, Ashford",
					"Conducts teaching and research across mathematics, physics, chemistry, biology, and environmental science.",
					1947),
			new FacultyTemplate(FacultyKey.ART, "Faculty of Arts and Design", "FAD",
					"42 Gallery Lane, Ashford",
					"The University's youngest faculty, offering studio-based programs in design, fine arts, animation, and music.",
					1973));
}
