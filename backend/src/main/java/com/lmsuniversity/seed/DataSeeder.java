package com.lmsuniversity.seed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lmsuniversity.announcement.Announcement;
import com.lmsuniversity.announcement.AnnouncementRepository;
import com.lmsuniversity.announcement.CourseAnnouncement;
import com.lmsuniversity.announcement.CourseAnnouncementRepository;
import com.lmsuniversity.course.Course;
import com.lmsuniversity.course.CourseRepository;
import com.lmsuniversity.examattempt.ExamAttempt;
import com.lmsuniversity.examattempt.ExamAttemptRepository;
import com.lmsuniversity.examperiod.ExamPeriod;
import com.lmsuniversity.examperiod.ExamPeriodRepository;
import com.lmsuniversity.examperiod.ExamPeriodTerm;
import com.lmsuniversity.faculty.Faculty;
import com.lmsuniversity.faculty.FacultyRepository;
import com.lmsuniversity.finalthesis.FinalThesis;
import com.lmsuniversity.finalthesis.FinalThesisRepository;
import com.lmsuniversity.officesupply.OfficeSupply;
import com.lmsuniversity.officesupply.OfficeSupplyRepository;
import com.lmsuniversity.permission.Permission;
import com.lmsuniversity.permission.PermissionEnum;
import com.lmsuniversity.permission.PermissionRepository;
import com.lmsuniversity.rectorate.Rectorate;
import com.lmsuniversity.rectorate.RectorateRepository;
import com.lmsuniversity.rectorate.University;
import com.lmsuniversity.rectorate.UniversityRepository;
import com.lmsuniversity.schedule.ClassSchedule;
import com.lmsuniversity.schedule.ClassScheduleRepository;
import com.lmsuniversity.schedule.ClassType;
import com.lmsuniversity.studentyearenrollment.StudentYearEnrollment;
import com.lmsuniversity.studentyearenrollment.StudentYearEnrollmentRepository;
import com.lmsuniversity.studyprogram.StudyProgram;
import com.lmsuniversity.studyprogram.StudyProgramRepository;
import com.lmsuniversity.studyyear.StudyYear;
import com.lmsuniversity.studyyear.StudyYearRepository;
import com.lmsuniversity.teachingmaterial.Outcome;
import com.lmsuniversity.teachingmaterial.TeachingMaterial;
import com.lmsuniversity.teachingmaterial.TeachingMaterialRepository;
import com.lmsuniversity.user.Administrator;
import com.lmsuniversity.user.AdministratorRepository;
import com.lmsuniversity.user.Student;
import com.lmsuniversity.user.StudentAffairsOffice;
import com.lmsuniversity.user.StudentAffairsOfficeRepository;
import com.lmsuniversity.user.StudentRepository;
import com.lmsuniversity.user.Teacher;
import com.lmsuniversity.user.TeacherRepository;

import net.datafaker.Faker;

/**
 * One-shot demo data generator for an otherwise empty {@code lms_v2} database.
 * Disabled by default; enable with {@code app.seed.enabled=true}.
 */
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
	private static final Faker FAKER = SeedSupport.FAKER;

	private static final LocalDate COURSE_START = LocalDate.of(2025, 9, 1);
	private static final LocalDate COURSE_END = LocalDate.of(2026, 6, 15);
	private static final LocalDate PAST_PERIOD_START = LocalDate.of(2026, 1, 10);
	private static final LocalDate PAST_PERIOD_END = LocalDate.of(2026, 1, 30);
	private static final LocalDateTime PAST_TERM_1 = LocalDateTime.of(2026, 1, 15, 10, 0);
	private static final LocalDateTime PAST_TERM_2 = LocalDateTime.of(2026, 1, 28, 10, 0);
	private static final LocalDate CURRENT_PERIOD_START = LocalDate.of(2026, 6, 20);
	private static final LocalDate CURRENT_PERIOD_END = LocalDate.of(2026, 7, 10);
	private static final LocalDateTime CURRENT_TERM_1 = LocalDateTime.of(2026, 7, 2, 10, 0);

	private static final int[] YEAR_DISTRIBUTION = { 30, 27, 23, 20 }; // out of 100 students per program
	private static final String STUDENT_DOMAIN = "student.ashford.edu";
	private static final String STAFF_DOMAIN = "ashford.edu";

	private final AtomicInteger indexSequence = new AtomicInteger(1);

	private final PermissionRepository permissionRepository;
	private final RectorateRepository rectorateRepository;
	private final UniversityRepository universityRepository;
	private final StudyYearRepository studyYearRepository;
	private final FacultyRepository facultyRepository;
	private final StudyProgramRepository studyProgramRepository;
	private final CourseRepository courseRepository;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;
	private final AdministratorRepository administratorRepository;
	private final StudentAffairsOfficeRepository studentAffairsOfficeRepository;
	private final StudentYearEnrollmentRepository studentYearEnrollmentRepository;
	private final ExamPeriodRepository examPeriodRepository;
	private final ExamAttemptRepository examAttemptRepository;
	private final ClassScheduleRepository classScheduleRepository;
	private final AnnouncementRepository announcementRepository;
	private final CourseAnnouncementRepository courseAnnouncementRepository;
	private final FinalThesisRepository finalThesisRepository;
	private final TeachingMaterialRepository teachingMaterialRepository;
	private final OfficeSupplyRepository officeSupplyRepository;
	private final PasswordEncoder passwordEncoder;

	public DataSeeder(PermissionRepository permissionRepository, RectorateRepository rectorateRepository,
			UniversityRepository universityRepository, StudyYearRepository studyYearRepository,
			FacultyRepository facultyRepository, StudyProgramRepository studyProgramRepository,
			CourseRepository courseRepository, TeacherRepository teacherRepository,
			StudentRepository studentRepository, AdministratorRepository administratorRepository,
			StudentAffairsOfficeRepository studentAffairsOfficeRepository,
			StudentYearEnrollmentRepository studentYearEnrollmentRepository,
			ExamPeriodRepository examPeriodRepository, ExamAttemptRepository examAttemptRepository,
			ClassScheduleRepository classScheduleRepository, AnnouncementRepository announcementRepository,
			CourseAnnouncementRepository courseAnnouncementRepository, FinalThesisRepository finalThesisRepository,
			TeachingMaterialRepository teachingMaterialRepository, OfficeSupplyRepository officeSupplyRepository,
			PasswordEncoder passwordEncoder) {
		this.permissionRepository = permissionRepository;
		this.rectorateRepository = rectorateRepository;
		this.universityRepository = universityRepository;
		this.studyYearRepository = studyYearRepository;
		this.facultyRepository = facultyRepository;
		this.studyProgramRepository = studyProgramRepository;
		this.courseRepository = courseRepository;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;
		this.administratorRepository = administratorRepository;
		this.studentAffairsOfficeRepository = studentAffairsOfficeRepository;
		this.studentYearEnrollmentRepository = studentYearEnrollmentRepository;
		this.examPeriodRepository = examPeriodRepository;
		this.examAttemptRepository = examAttemptRepository;
		this.classScheduleRepository = classScheduleRepository;
		this.announcementRepository = announcementRepository;
		this.courseAnnouncementRepository = courseAnnouncementRepository;
		this.finalThesisRepository = finalThesisRepository;
		this.teachingMaterialRepository = teachingMaterialRepository;
		this.officeSupplyRepository = officeSupplyRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {
		if (facultyRepository.count() > 0) {
			log.warn("Seed skipped: faculties already exist in the database.");
			return;
		}

		log.info("Seeding reference data...");
		List<Permission> permissions = seedPermissions();
		String sharedPasswordHash = passwordEncoder.encode("Password123!");

		University university = seedUniversityAndRectorate();
		List<StudyYear> studyYears = seedStudyYears();
		seedOfficeSupplies();
		seedAdministrators(permissions, sharedPasswordHash);
		seedGlobalAnnouncements();

		Permission teacherPermission = findPermission(permissions, PermissionEnum.TEACHER_PERMISSION);
		Permission studentPermission = findPermission(permissions, PermissionEnum.STUDENT_PERMISSION);
		Permission affairsPermission = findPermission(permissions, PermissionEnum.STUDENT_AFFAIRS_PERMISSION);

		int totalStudents = 0;
		int totalCourses = 0;
		for (FacultyCatalog.FacultyTemplate template : FacultyCatalog.FACULTIES) {
			log.info("Seeding faculty: {}", template.name());
			FacultyResult result = seedFaculty(template, university, studyYears, teacherPermission, studentPermission,
					affairsPermission, sharedPasswordHash);
			totalStudents += result.studentCount;
			totalCourses += result.courseCount;
		}

		log.info("Seed complete: {} faculties, {} courses, {} students.", FacultyCatalog.FACULTIES.size(), totalCourses, totalStudents);
	}

	private record FacultyResult(int studentCount, int courseCount) {
	}

	// ---------------------------------------------------------------- reference data

	private List<Permission> seedPermissions() {
		List<Permission> permissions = new ArrayList<>();
		for (PermissionEnum value : PermissionEnum.values()) {
			permissions.add(Permission.builder().name(value).build());
		}
		return permissionRepository.saveAll(permissions);
	}

	private Permission findPermission(List<Permission> permissions, PermissionEnum name) {
		return permissions.stream().filter(p -> p.getName() == name).findFirst()
				.orElseThrow(() -> new IllegalStateException("Missing permission " + name));
	}

	private University seedUniversityAndRectorate() {
		Rectorate rectorate = rectorateRepository.save(Rectorate.builder()
				.name("Rectorate of the University of Ashford")
				.contact("+1-555-201-3000")
				.address("1 University Avenue, Ashford")
				.rectorName(FAKER.name().fullName())
				.build());

		University university = University.builder()
				.name("University of Ashford")
				.foundingDate(LocalDateTime.of(1947, 9, 1, 0, 0))
				.contact("+1-555-201-3100")
				.description("A public research university with ten faculties spanning engineering, "
						+ "medicine, law, business, the sciences, humanities, and the arts.")
				.address("1 University Avenue, Ashford")
				.rectorate(rectorate)
				.build();
		return universityRepository.save(university);
	}

	private List<StudyYear> seedStudyYears() {
		List<StudyYear> years = new ArrayList<>();
		for (int y = 1; y <= 4; y++) {
			years.add(StudyYear.builder().year(y).build());
		}
		return studyYearRepository.saveAll(years);
	}

	private void seedOfficeSupplies() {
		String[][] items = {
				{ "A4 Paper (ream)", "300" }, { "Toner Cartridge - Black", "60" }, { "Toner Cartridge - Color", "40" },
				{ "Ballpoint Pens (box of 50)", "80" }, { "Staplers", "50" }, { "Staple Refills (box)", "70" },
				{ "Whiteboard Markers", "120" }, { "Printer Paper - Letter Size", "200" }, { "USB Flash Drives (16GB)", "90" },
				{ "Projector Bulbs", "25" }, { "Ethernet Cables (5m)", "100" }, { "Desk Organizers", "60" },
				{ "Sticky Notes (pack)", "150" }, { "Binder Clips (box)", "90" }, { "Envelopes (box of 100)", "70" },
				{ "Laminating Sheets", "110" }, { "External Hard Drives (1TB)", "30" }, { "Wireless Mice", "55" },
				{ "Keyboard - Standard", "55" }, { "Whiteboard Cleaner Spray", "40" }
		};
		List<OfficeSupply> supplies = new ArrayList<>();
		for (String[] item : items) {
			int quantity = Integer.parseInt(item[1]);
			supplies.add(OfficeSupply.builder()
					.name(item[0])
					.quantity(quantity)
					.issuedQuantity(SeedSupport.between(0, quantity / 2))
					.build());
		}
		officeSupplyRepository.saveAll(supplies);
	}

	private void seedAdministrators(List<Permission> permissions, String passwordHash) {
		Permission adminPermission = findPermission(permissions, PermissionEnum.ADMINISTRATOR_PERMISSION);
		List<Administrator> admins = new ArrayList<>();
		admins.add(Administrator.builder()
				.username("admin")
				.email("admin@" + STAFF_DOMAIN)
				.password(passwordHash)
				.firstName("System")
				.lastName("Administrator")
				.permissions(Set.of(adminPermission))
				.build());
		for (int i = 0; i < 2; i++) {
			String first = FAKER.name().firstName();
			String last = FAKER.name().lastName();
			admins.add(Administrator.builder()
					.username((first + "." + last).toLowerCase())
					.email(SeedSupport.uniqueEmail(first, last, STAFF_DOMAIN))
					.password(passwordHash)
					.firstName(first)
					.lastName(last)
					.permissions(Set.of(adminPermission))
					.build());
		}
		administratorRepository.saveAll(admins);
	}

	private void seedGlobalAnnouncements() {
		List<Announcement> announcements = new ArrayList<>();
		LocalDateTime postedAt = LocalDateTime.of(2026, 5, 1, 9, 0);
		for (AnnouncementCatalog.AnnouncementTemplate t : AnnouncementCatalog.GLOBAL_ANNOUNCEMENTS) {
			announcements.add(Announcement.builder()
					.title(t.title())
					.content(t.content())
					.date(postedAt)
					.startDate(SeedSupport.toDate(LocalDate.of(2026, 5, 1)))
					.endDate(SeedSupport.toDate(LocalDate.of(2026, 9, 30)))
					.build());
			postedAt = postedAt.plusDays(2);
		}
		announcementRepository.saveAll(announcements);
	}

	// ---------------------------------------------------------------- per faculty

	private FacultyResult seedFaculty(FacultyCatalog.FacultyTemplate template, University university,
			List<StudyYear> studyYears, Permission teacherPermission, Permission studentPermission,
			Permission affairsPermission, String passwordHash) {

		List<ProgramCatalog.ProgramEntry> programs = ProgramCatalog.forFaculty(template.key());
		int totalCoursesInFaculty = programs.size() * 32;
		int teacherPoolSize = Math.max(10, totalCoursesInFaculty / 6);

		List<Teacher> teacherPool = teacherRepository.saveAll(
				buildTeacherPool(teacherPoolSize, university, teacherPermission, passwordHash));

		Faculty faculty = facultyRepository.save(Faculty.builder()
				.facultyCode(template.code())
				.name(template.name())
				.contact(FAKER.phoneNumber().phoneNumber())
				.description(template.description())
				.dean(teacherPool.get(0))
				.address(template.address())
				.university(university)
				.build());

		seedStudentAffairsOffice(faculty, affairsPermission, passwordHash);

		ClassroomAllocator allocator = new ClassroomAllocator(template.code(), 35);
		AtomicInteger globalCourseCounter = new AtomicInteger(0);

		int studentCount = 0;
		int courseCount = 0;
		for (int programIndex = 0; programIndex < programs.size(); programIndex++) {
			ProgramCatalog.ProgramEntry programEntry = programs.get(programIndex);
			List<CourseSpec> fullCurriculum = new ArrayList<>(FoundationCatalog.forFaculty(template.key()));
			fullCurriculum.addAll(programEntry.specialization());

			StudyProgram program = studyProgramRepository.save(StudyProgram.builder()
					.programCode(template.code() + "-P" + (programIndex + 1))
					.name(programEntry.name())
					.description("The " + programEntry.name() + " program prepares students for professional and "
							+ "research careers through a four-year curriculum combining theoretical foundations with "
							+ "practical and project-based work.")
					.programDirector(teacherPool.get(programIndex % teacherPool.size()))
					.faculty(faculty)
					.build());

			List<Course> courses = courseRepository.saveAll(buildCourses(program, fullCurriculum, studyYears,
					teacherPool, globalCourseCounter, template.code()));
			courseCount += courses.size();

			teachingMaterialRepository.saveAll(buildTextbooks(courses));
			classScheduleRepository.saveAll(buildClassSchedules(courses, teacherPool, allocator));
			ExamInfrastructure examInfrastructure = buildExamPeriods(courses);
			examPeriodRepository.saveAll(examInfrastructure.periods());
			courseAnnouncementRepository.saveAll(buildCourseAnnouncements(courses));

			List<Student> students = studentRepository.saveAll(
					buildStudents(program, faculty, courses, studyYears, studentPermission, passwordHash));
			studentCount += students.size();

			studentYearEnrollmentRepository.saveAll(buildEnrollmentHistory(students, courses));
			examAttemptRepository.saveAll(buildExamAttempts(students, courses, examInfrastructure.pastTermsByCourseId()));
			finalThesisRepository.saveAll(buildFinalTheses(students, program, teacherPool));
		}

		return new FacultyResult(studentCount, courseCount);
	}

	private void seedStudentAffairsOffice(Faculty faculty, Permission affairsPermission, String passwordHash) {
		String first = FAKER.name().firstName();
		String last = FAKER.name().lastName();
		studentAffairsOfficeRepository.save(StudentAffairsOffice.builder()
				.username((first + "." + last).toLowerCase())
				.email(SeedSupport.uniqueEmail(first, last, STAFF_DOMAIN))
				.password(passwordHash)
				.firstName(first)
				.lastName(last)
				.permissions(Set.of(affairsPermission))
				.faculty(faculty)
				.build());
	}

	private List<Teacher> buildTeacherPool(int size, University university, Permission teacherPermission, String passwordHash) {
		List<Teacher> teachers = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			String first = FAKER.name().firstName();
			String last = FAKER.name().lastName();
			teachers.add(Teacher.builder()
					.username((first + "." + last).toLowerCase() + indexSequence.getAndIncrement())
					.email(SeedSupport.uniqueEmail(first, last, STAFF_DOMAIN))
					.password(passwordHash)
					.firstName(first)
					.lastName(last)
					.permissions(Set.of(teacherPermission))
					.biography(FAKER.lorem().sentence(15))
					.personalIdNumber(String.valueOf(FAKER.number().numberBetween(100000000, 999999999)))
					.university(university)
					.build());
		}
		return teachers;
	}

	// ---------------------------------------------------------------- courses

	private List<Course> buildCourses(StudyProgram program, List<CourseSpec> curriculum, List<StudyYear> studyYears,
			List<Teacher> teacherPool, AtomicInteger globalCourseCounter, String facultyCode) {
		List<Course> courses = new ArrayList<>();
		for (int i = 0; i < curriculum.size(); i++) {
			CourseSpec spec = curriculum.get(i);
			int year = (i / 8) + 1;
			Teacher teacher = teacherPool.get(globalCourseCounter.getAndIncrement() % teacherPool.size());
			courses.add(Course.builder()
					.courseCode(program.getProgramCode() + "-C" + (i + 1))
					.name(spec.name())
					.ects(spec.ects())
					.syllabus(SeedSupport.buildSyllabusJson(spec.name()))
					.description(SeedSupport.buildDescription(spec.name(), facultyKeyFromCode(facultyCode)))
					.startDate(SeedSupport.toDate(COURSE_START))
					.endDate(SeedSupport.toDate(COURSE_END))
					.teacher(teacher)
					.studyProgram(program)
					.studyYear(studyYears.get(year - 1))
					.build());
		}
		return courses;
	}

	private FacultyKey facultyKeyFromCode(String code) {
		for (FacultyCatalog.FacultyTemplate t : FacultyCatalog.FACULTIES) {
			if (t.code().equals(code)) {
				return t.key();
			}
		}
		throw new IllegalStateException("Unknown faculty code " + code);
	}

	private List<TeachingMaterial> buildTextbooks(List<Course> courses) {
		List<TeachingMaterial> materials = new ArrayList<>();
		for (Course course : courses) {
			for (int i = 0; i < 2; i++) {
				int quantity = SeedSupport.between(10, 50);
				materials.add(TeachingMaterial.builder()
						.title(SeedSupport.randomTextbookTitle(course.getName()))
						.authors(FAKER.name().fullName())
						.publicationYear(String.valueOf(SeedSupport.between(2015, 2024)))
						.publisher(SeedSupport.randomPublisher())
						.description(SeedSupport.randomTextbookDescription(course.getName()))
						.pageCount(SeedSupport.between(220, 600))
						.quantity(quantity)
						.issuedQuantity(SeedSupport.between(0, quantity))
						.outcome(Outcome.POSITIVE)
						.course(course)
						.build());
			}
		}
		return materials;
	}

	private List<ClassSchedule> buildClassSchedules(List<Course> courses, List<Teacher> teacherPool, ClassroomAllocator allocator) {
		List<ClassSchedule> schedules = new ArrayList<>();
		for (Course course : courses) {
			ClassroomAllocator.Slot lecture = allocator.nextFreeSlot();
			schedules.add(toClassSchedule(course, lecture, ClassType.LECTURE));

			ClassroomAllocator.Slot second = allocator.nextFreeSlot();
			ClassType secondType = SeedSupport.chance(0.5) ? ClassType.EXERCISE : ClassType.LAB;
			schedules.add(toClassSchedule(course, second, secondType));
		}
		return schedules;
	}

	private ClassSchedule toClassSchedule(Course course, ClassroomAllocator.Slot slot, ClassType type) {
		return ClassSchedule.builder()
				.course(course)
				.teacher(course.getTeacher())
				.dayOfWeek(slot.dayOfWeek())
				.startTime(slot.startTime())
				.endTime(slot.endTime())
				.classroom(slot.classroom())
				.type(type)
				.build();
	}

	private record ExamInfrastructure(List<ExamPeriod> periods, Map<Long, ExamPeriodTerm[]> pastTermsByCourseId) {
	}

	/**
	 * {@code ExamPeriodTerm} uses Lombok's id-only equals/hashCode, so two transient
	 * (unsaved, id == null) terms compare equal to each other - a plain HashSet would
	 * silently collapse them into one element. An identity-based set sidesteps that.
	 */
	private static Set<ExamPeriodTerm> newTermSet() {
		return Collections.newSetFromMap(new IdentityHashMap<>());
	}

	private ExamInfrastructure buildExamPeriods(List<Course> courses) {
		List<ExamPeriod> periods = new ArrayList<>();
		Map<Long, ExamPeriodTerm[]> pastTermsByCourseId = new HashMap<>();
		for (Course course : courses) {
			String examRoomA = course.getCourseCode() + "-EXAM-A";
			String examRoomB = course.getCourseCode() + "-EXAM-B";

			ExamPeriodTerm termA = ExamPeriodTerm.builder().dateTime(PAST_TERM_1).classroom(examRoomA).maxStudents(40).build();
			ExamPeriodTerm termB = ExamPeriodTerm.builder().dateTime(PAST_TERM_2).classroom(examRoomB).maxStudents(40).build();
			Set<ExamPeriodTerm> pastTerms = newTermSet();
			pastTerms.add(termA);
			pastTerms.add(termB);
			ExamPeriod pastPeriod = ExamPeriod.builder()
					.name("January 2026 Exam Session")
					.startDate(PAST_PERIOD_START)
					.endDate(PAST_PERIOD_END)
					.course(course)
					.createdBy(course.getTeacher())
					.build();
			termA.setExamPeriod(pastPeriod);
			termB.setExamPeriod(pastPeriod);
			pastPeriod.setTerms(pastTerms);
			periods.add(pastPeriod);
			pastTermsByCourseId.put(course.getId(), new ExamPeriodTerm[] { termA, termB });

			ExamPeriodTerm currentTerm = ExamPeriodTerm.builder().dateTime(CURRENT_TERM_1)
					.classroom(course.getCourseCode() + "-EXAM-C").maxStudents(40).build();
			Set<ExamPeriodTerm> currentTerms = newTermSet();
			currentTerms.add(currentTerm);
			ExamPeriod currentPeriod = ExamPeriod.builder()
					.name("June 2026 Exam Session")
					.startDate(CURRENT_PERIOD_START)
					.endDate(CURRENT_PERIOD_END)
					.course(course)
					.createdBy(course.getTeacher())
					.build();
			currentTerm.setExamPeriod(currentPeriod);
			currentPeriod.setTerms(currentTerms);
			periods.add(currentPeriod);
		}
		return new ExamInfrastructure(periods, pastTermsByCourseId);
	}

	private List<CourseAnnouncement> buildCourseAnnouncements(List<Course> courses) {
		List<CourseAnnouncement> announcements = new ArrayList<>();
		for (Course course : courses) {
			if (!SeedSupport.chance(0.4)) {
				continue;
			}
			announcements.add(CourseAnnouncement.builder()
					.title("Update: " + course.getName())
					.content("Please check the latest information regarding " + course.getName()
							+ ". Office hours and consultation schedule have been updated for this term.")
					.date(LocalDateTime.of(2026, 6, 1, 12, 0))
					.startDate(SeedSupport.toDate(LocalDate.of(2026, 6, 1)))
					.endDate(SeedSupport.toDate(LocalDate.of(2026, 9, 1)))
					.course(course)
					.build());
		}
		return announcements;
	}

	// ---------------------------------------------------------------- students

	private List<Student> buildStudents(StudyProgram program, Faculty faculty, List<Course> courses,
			List<StudyYear> studyYears, Permission studentPermission, String passwordHash) {
		List<Student> students = new ArrayList<>();
		for (int currentYear = 1; currentYear <= 4; currentYear++) {
			int countForYear = YEAR_DISTRIBUTION[currentYear - 1];
			int enrollmentYear = 2025 - (currentYear - 1);
			for (int i = 0; i < countForYear; i++) {
				String first = FAKER.name().firstName();
				String last = FAKER.name().lastName();

				Set<StudyYear> yearsSoFar = new HashSet<>();
				for (int y = 1; y <= currentYear; y++) {
					yearsSoFar.add(studyYears.get(y - 1));
				}

				Set<Course> coursesSoFar = new HashSet<>();
				for (int y = 1; y <= currentYear; y++) {
					coursesSoFar.addAll(coursesForYear(courses, y));
				}

				int sequence = indexSequence.getAndIncrement();
				students.add(Student.builder()
						.username((first + "." + last).toLowerCase() + sequence)
						.email(SeedSupport.uniqueEmail(first, last, STUDENT_DOMAIN))
						.password(passwordHash)
						.firstName(first)
						.lastName(last)
						.permissions(Set.of(studentPermission))
						.indexNumber(enrollmentYear + "/" + String.format("%04d", sequence))
						.faculty(faculty)
						.studyYears(yearsSoFar)
						.courses(coursesSoFar)
						.build());
			}
		}
		return students;
	}

	private List<Course> coursesForYear(List<Course> orderedCourses, int year) {
		int from = (year - 1) * 8;
		return orderedCourses.subList(from, from + 8);
	}

	private List<StudentYearEnrollment> buildEnrollmentHistory(List<Student> students, List<Course> courses) {
		List<StudentYearEnrollment> enrollments = new ArrayList<>();
		for (Student student : students) {
			int currentYear = student.getStudyYears().size();
			StudyProgram program = courses.get(0).getStudyProgram();
			for (StudyYear studyYear : student.getStudyYears()) {
				int yearsBeforeNow = currentYear - studyYear.getYear();
				LocalDate enrollmentDate = LocalDate.of(2025 - yearsBeforeNow, 9, 1);
				enrollments.add(StudentYearEnrollment.builder()
						.enrollmentDate(SeedSupport.toSqlDate(enrollmentDate))
						.studyYear(studyYear)
						.student(student)
						.studyProgram(program)
						.build());
			}
		}
		return enrollments;
	}

	private List<ExamAttempt> buildExamAttempts(List<Student> students, List<Course> courses,
			Map<Long, ExamPeriodTerm[]> pastTermsByCourseId) {
		List<ExamAttempt> attempts = new ArrayList<>();
		for (Student student : students) {
			int currentYear = student.getStudyYears().size();
			if (currentYear <= 1) {
				continue;
			}
			for (int y = 1; y < currentYear; y++) {
				for (Course course : coursesForYear(courses, y)) {
					attempts.addAll(buildAttemptsForCourse(student, course, pastTermsByCourseId.get(course.getId())));
				}
			}
		}
		return attempts;
	}

	private List<ExamAttempt> buildAttemptsForCourse(Student student, Course course, ExamPeriodTerm[] pastTerms) {
		List<ExamAttempt> attempts = new ArrayList<>();
		if (SeedSupport.chance(0.85)) {
			attempts.add(passingAttempt(student, course, pastTerms[0]));
		} else {
			attempts.add(failingAttempt(student, course, pastTerms[0]));
			attempts.add(passingAttempt(student, course, pastTerms[1]));
		}
		return attempts;
	}

	private ExamAttempt passingAttempt(Student student, Course course, ExamPeriodTerm term) {
		LocalDateTime when = term.getDateTime();
		return ExamAttempt.builder()
				.points(SeedSupport.between(51, 100))
				.finalGrade(SeedSupport.between(6, 10))
				.startTime(when)
				.endTime(when.plusMinutes(90))
				.course(course)
				.student(student)
				.teacher(course.getTeacher())
				.examPeriodTerm(term)
				.build();
	}

	private ExamAttempt failingAttempt(Student student, Course course, ExamPeriodTerm term) {
		LocalDateTime when = term.getDateTime();
		return ExamAttempt.builder()
				.points(SeedSupport.between(20, 50))
				.finalGrade(5)
				.startTime(when)
				.endTime(when.plusMinutes(90))
				.note("Did not meet the minimum passing threshold; retake recommended.")
				.course(course)
				.student(student)
				.teacher(course.getTeacher())
				.examPeriodTerm(term)
				.build();
	}

	private List<FinalThesis> buildFinalTheses(List<Student> students, StudyProgram program, List<Teacher> teacherPool) {
		List<FinalThesis> theses = new ArrayList<>();
		for (Student student : students) {
			if (student.getStudyYears().size() < 4 || !SeedSupport.chance(0.3)) {
				continue;
			}
			theses.add(FinalThesis.builder()
					.topic("Research Project in " + program.getName() + ": " + FAKER.book().title())
					.student(student)
					.mentor(SeedSupport.pick(teacherPool))
					.build());
		}
		return theses;
	}
}
