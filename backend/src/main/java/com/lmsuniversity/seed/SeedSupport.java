package com.lmsuniversity.seed;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;

/**
 * Shared randomness, name/contact generation, and small content builders used by
 * {@link DataSeeder}. A fixed seed keeps repeated seed runs reproducible.
 */
public final class SeedSupport {

	public static final Random RANDOM = new Random(42);
	public static final Faker FAKER = new Faker(RANDOM);
	private static final ObjectMapper JSON = new ObjectMapper();
	private static final AtomicInteger EMAIL_COUNTER = new AtomicInteger(1);

	private static final Map<FacultyKey, String> DISCIPLINE_FOCUS = Map.ofEntries(
			Map.entry(FacultyKey.CS, "algorithmic thinking, software craftsmanship, and hands-on system building"),
			Map.entry(FacultyKey.EE, "circuit-level analysis, measurement practice, and applied electrical design"),
			Map.entry(FacultyKey.ME, "mechanical design reasoning, materials behavior, and workshop and laboratory practice"),
			Map.entry(FacultyKey.CIV, "structural and spatial reasoning, regulatory compliance, and on-site construction practice"),
			Map.entry(FacultyKey.ECO, "quantitative analysis, market reasoning, and real-world case studies"),
			Map.entry(FacultyKey.LAW, "statutory interpretation, case analysis, and courtroom and contract reasoning"),
			Map.entry(FacultyKey.MED, "clinical reasoning, patient safety, and supervised practical training"),
			Map.entry(FacultyKey.HUM, "critical reading, research methodology, and structured academic argument"),
			Map.entry(FacultyKey.SCI, "mathematical rigor, experimental method, and quantitative problem-solving"),
			Map.entry(FacultyKey.ART, "studio practice, visual reasoning, and iterative creative development"));

	private static final String[] PUBLISHERS = {
			"Pearson", "Wiley", "McGraw-Hill Education", "Springer", "Oxford University Press",
			"Cambridge University Press", "Cengage Learning", "Elsevier"
	};

	private static final String[] TEXTBOOK_SUFFIXES = {
			"Principles and Practice", "A Modern Approach", "Concepts and Applications",
			"Theory and Practice", "Foundations and Methods", "An Introduction"
	};

	private SeedSupport() {
	}

	public static <T> T pick(List<T> items) {
		return items.get(RANDOM.nextInt(items.size()));
	}

	public static <T> T pick(T[] items) {
		return items[RANDOM.nextInt(items.length)];
	}

	public static int between(int minInclusive, int maxInclusive) {
		return minInclusive + RANDOM.nextInt(maxInclusive - minInclusive + 1);
	}

	public static boolean chance(double probability) {
		return RANDOM.nextDouble() < probability;
	}

	public static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static java.sql.Date toSqlDate(LocalDate localDate) {
		return java.sql.Date.valueOf(localDate);
	}

	public static String uniqueEmail(String firstName, String lastName, String domain) {
		String local = (firstName + "." + lastName).toLowerCase()
				.replaceAll("[^a-z.]", "");
		return local + EMAIL_COUNTER.getAndIncrement() + "@" + domain;
	}

	public static String buildDescription(String courseName, FacultyKey key) {
		String focus = DISCIPLINE_FOCUS.get(key);
		return "This course covers " + courseName + ", with an emphasis on " + focus
				+ ". Students complete weekly assignments, practical exercises, and a final assessment.";
	}

	public static String buildSyllabusJson(String courseName) {
		List<String> topics = new ArrayList<>(List.of(
				"Introduction and overview of " + courseName,
				"Core theoretical foundations",
				"Key methods and techniques in " + courseName,
				"Practical applications and case studies",
				"Problem-solving and laboratory workshops",
				"Advanced topics and current developments",
				"Group project work",
				"Review and final assessment preparation"));
		try {
			return JSON.writeValueAsString(topics);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to serialize syllabus topics", e);
		}
	}

	public static String randomTextbookTitle(String courseName) {
		return courseName + ": " + pick(TEXTBOOK_SUFFIXES);
	}

	public static String randomPublisher() {
		return pick(PUBLISHERS);
	}

	public static String randomTextbookDescription(String courseName) {
		return "A comprehensive textbook covering the core concepts of " + courseName
				+ ", used as the primary reference for the course.";
	}
}
