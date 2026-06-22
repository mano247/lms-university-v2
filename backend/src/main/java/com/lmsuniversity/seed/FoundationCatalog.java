package com.lmsuniversity.seed;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Years 1-2 "core curriculum" shared by every study program within a given faculty,
 * keyed by {@link FacultyKey}. Each array has exactly 16 entries: indices 0-7 are
 * taught in study year 1, indices 8-15 in study year 2.
 */
public final class FoundationCatalog {

	private FoundationCatalog() {
	}

	private static final String[] CS = {
			"Introduction to Programming|7", "Discrete Mathematics|6", "Calculus I|6",
			"Computer Architecture|6", "Academic and Technical Writing|4", "Linear Algebra|6",
			"Digital Logic Design|6", "Web Technologies Fundamentals|6",
			"Data Structures and Algorithms|7", "Object-Oriented Programming|6", "Database Systems I|6",
			"Operating Systems|6", "Probability and Statistics|5", "Computer Networks I|6",
			"Software Engineering Principles|6", "Calculus II|6"
	};

	private static final String[] EE = {
			"Calculus I|6", "Physics I: Mechanics and Thermodynamics|6", "Circuit Theory I|7",
			"Linear Algebra|6", "Engineering Drawing and CAD|5", "Programming for Engineers|6",
			"Academic and Technical Writing|4", "Digital Logic Design|6",
			"Calculus II|6", "Physics II: Electromagnetism|6", "Circuit Theory II|7",
			"Signals and Systems|6", "Probability and Statistics for Engineers|5", "Materials Science|5",
			"Electrical Measurements|6", "Numerical Methods|5"
	};

	private static final String[] ME = {
			"Calculus I|6", "Physics I: Mechanics|6", "Engineering Drawing and CAD|6",
			"Statics|6", "Materials Science|6", "Programming for Engineers|5",
			"Academic and Technical Writing|4", "Linear Algebra|6",
			"Calculus II|6", "Dynamics|6", "Strength of Materials|7",
			"Thermodynamics I|6", "Fluid Mechanics|6", "Manufacturing Technologies|6",
			"Probability and Statistics for Engineers|5", "Machine Drawing and Tolerancing|5"
	};

	private static final String[] CIV = {
			"Calculus I|6", "Descriptive Geometry|5", "Engineering Drawing and CAD|6",
			"Statics|6", "Building Materials|6", "Introduction to Architectural Design|6",
			"Academic and Technical Writing|4", "Linear Algebra|6",
			"Calculus II|6", "Strength of Materials|7", "Surveying and Geodesy|6",
			"Soil Mechanics|6", "History of Architecture|5", "Building Physics|5",
			"Structural Analysis I|6", "Construction Technology|6"
	};

	private static final String[] ECO = {
			"Principles of Microeconomics|6", "Mathematics for Economists|6", "Principles of Macroeconomics|6",
			"Business Communication|5", "Introduction to Accounting|6", "Information Technology for Business|5",
			"Academic and Technical Writing|4", "Sociology of Organizations|5",
			"Statistics for Business|6", "Intermediate Microeconomics|6", "Financial Accounting|6",
			"Principles of Management|5", "Business Law Fundamentals|5", "Marketing Fundamentals|6",
			"Intermediate Macroeconomics|6", "Quantitative Methods for Business|5"
	};

	private static final String[] LAW = {
			"Introduction to Law|6", "Roman Law|5", "Constitutional Law|7",
			"Legal History|5", "Sociology of Law|5", "Academic and Legal Writing|4",
			"Political Science|5", "Economics for Lawyers|5",
			"Civil Law I: General Part|7", "Administrative Law|6", "Criminal Law I: General Part|6",
			"International Public Law|6", "Family Law|5", "Legal Logic and Argumentation|5",
			"Labor Law Fundamentals|5", "Comparative Legal Systems|5"
	};

	private static final String[] MED = {
			"Human Anatomy I|8", "Medical Biophysics|5", "Medical Chemistry|5",
			"Histology and Embryology|6", "Medical Biology and Genetics|6", "Latin Terminology in Medicine|4",
			"Academic and Technical Writing|4", "Introduction to Public Health|5",
			"Human Anatomy II|7", "Physiology I|7", "Biochemistry|6",
			"Microbiology and Immunology|6", "Physiology II|6", "Pathophysiology|6",
			"Medical Statistics|5", "Introduction to Pharmacology|5"
	};

	private static final String[] HUM = {
			"Introduction to Philosophy|6", "Introduction to Psychology|6", "Introduction to Sociology|6",
			"Academic Writing and Rhetoric|5", "World History I: Ancient and Medieval|6", "Introduction to Linguistics|5",
			"Research Methods in Social Sciences|5", "Logic|5",
			"History of Modern Philosophy|6", "Developmental Psychology|6", "Social Theory|6",
			"World History II: Modern Era|6", "Ethics|5", "Statistics for Social Sciences|5",
			"Comparative Cultural Studies|5", "Introduction to Media and Communication|5"
	};

	private static final String[] SCI = {
			"Calculus I|7", "General Physics I|6", "General Chemistry|6",
			"Linear Algebra|6", "Introduction to Programming for Scientists|5", "Academic and Technical Writing|4",
			"Analytic Geometry|5", "Introduction to Earth and Environmental Science|5",
			"Calculus II|7", "General Physics II|6", "Organic Chemistry|6",
			"Probability Theory|6", "Cell Biology|5", "Differential Equations|6",
			"Mathematical Analysis|6", "Scientific Computing|5"
	};

	private static final String[] ART = {
			"Drawing I|6", "Color Theory and Composition|6", "Art History I: Ancient to Renaissance|5",
			"Introduction to Digital Design Tools|6", "Visual Communication Fundamentals|6", "Academic and Technical Writing|4",
			"Typography Fundamentals|5", "Sculpture and 3D Form Basics|6",
			"Drawing II|6", "Art History II: Modern and Contemporary|5", "Design Methodology|6",
			"Photography Fundamentals|5", "Digital Illustration|6", "Materials and Techniques Workshop|5",
			"Visual Branding Fundamentals|6", "Portfolio Development I|5"
	};

	private static final Map<FacultyKey, String[]> BY_FACULTY = Map.ofEntries(
			Map.entry(FacultyKey.CS, CS), Map.entry(FacultyKey.EE, EE), Map.entry(FacultyKey.ME, ME),
			Map.entry(FacultyKey.CIV, CIV), Map.entry(FacultyKey.ECO, ECO), Map.entry(FacultyKey.LAW, LAW),
			Map.entry(FacultyKey.MED, MED), Map.entry(FacultyKey.HUM, HUM), Map.entry(FacultyKey.SCI, SCI),
			Map.entry(FacultyKey.ART, ART));

	/** Always 16 entries: [0..7] = study year 1, [8..15] = study year 2. */
	public static List<CourseSpec> forFaculty(FacultyKey key) {
		String[] raw = BY_FACULTY.get(key);
		if (raw == null || raw.length != 16) {
			throw new IllegalStateException("Foundation catalog for " + key + " must have exactly 16 courses");
		}
		return Stream.of(raw).map(CourseSpec::of).toList();
	}
}
