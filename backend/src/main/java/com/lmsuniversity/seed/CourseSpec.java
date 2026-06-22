package com.lmsuniversity.seed;

/**
 * One row of the hand-authored curriculum catalog: "Course Name|ects".
 * Parsed once at startup into a real CourseSpec via {@link #of}.
 */
public record CourseSpec(String name, int ects) {

	static CourseSpec of(String encoded) {
		int i = encoded.lastIndexOf('|');
		return new CourseSpec(encoded.substring(0, i), Integer.parseInt(encoded.substring(i + 1)));
	}
}
