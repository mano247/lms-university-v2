package com.lmsuniversity.seed;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Deterministically assigns (classroom, day, time-slot) triples per faculty so that
 * no two generated {@code ClassSchedule} sessions ever double-book the same room,
 * mirroring the real-time overlap check the Student Affairs Office module needs.
 */
public final class ClassroomAllocator {

	private static final DayOfWeek[] DAYS = {
			DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
	};

	private static final int[] START_HOURS = { 8, 10, 12, 14, 16, 18 };
	private static final int SESSION_MINUTES = 90;

	public record Slot(String classroom, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
	}

	private final String[] classrooms;
	private final Set<String> taken = new HashSet<>();
	private int cursor = 0;

	public ClassroomAllocator(String facultyCode, int roomCount) {
		this.classrooms = new String[roomCount];
		for (int i = 0; i < roomCount; i++) {
			classrooms[i] = facultyCode + "-" + (101 + i);
		}
	}

	/** Walks the room/day/slot grid from a rolling cursor until a free combination is found. */
	public Slot nextFreeSlot() {
		int totalCombinations = classrooms.length * DAYS.length * START_HOURS.length;
		for (int attempt = 0; attempt < totalCombinations; attempt++) {
			int idx = (cursor + attempt) % totalCombinations;
			int roomIdx = idx / (DAYS.length * START_HOURS.length);
			int dayIdx = (idx / START_HOURS.length) % DAYS.length;
			int hourIdx = idx % START_HOURS.length;

			String room = classrooms[roomIdx];
			DayOfWeek day = DAYS[dayIdx];
			int hour = START_HOURS[hourIdx];
			String key = room + "|" + day + "|" + hour;

			if (!taken.contains(key)) {
				taken.add(key);
				cursor = idx + 1;
				LocalTime start = LocalTime.of(hour, 0);
				return new Slot(room, day, start, start.plusMinutes(SESSION_MINUTES));
			}
		}
		throw new IllegalStateException("Classroom grid exhausted - increase roomCount");
	}
}
