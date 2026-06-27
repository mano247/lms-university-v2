# University of Ashford — LMS

A full-stack Learning Management System built as a university team project, now undergoing a complete professional upgrade for portfolio/CV purposes.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 20 (standalone, no NgModules) |
| UI Library | Angular Material + custom design system |
| Backend | Java 21 · Spring Boot 3.x |
| Auth | Spring Security + JWT (in-memory token) |
| Database | MySQL 8 (`lms_v2`) |
| Build | Angular CLI / Maven |

---

## Architecture highlights

- **Standalone components** — no NgModules anywhere; all imports are per-component
- **Lazy-loaded routes** — every route uses `loadComponent()`, initial bundle is ~96 KB
- **Design system** — CSS custom properties: navy `#002444`, gold `#ffddb1`, Merriweather/Inter/JetBrains Mono
- **In-memory JWT** — access token never touches `localStorage`; proactive expiry timer auto-logs out the user
- **Global 401 interceptor** — all HTTP errors funnel through a functional `HttpInterceptorFn`
- **Role guards** — `authGuard` + `roleGuard` protect every authenticated route
- **Fully responsive** — mobile-first, breakpoints at 480 / 600 / 768 / 900 / 1024px

---

## Roles & access

| Role | Portal path | Permission constant |
|---|---|---|
| Student | `/eStudent` | `STUDENT_PERMISSION` |
| Teacher / Assistant | `/eTeacher` | `TEACHER_PERMISSION` |
| Student Affairs | `/eOffice` | `STUDENT_AFFAIRS_PERMISSION` |
| Administrator | `/eAdmin` | `ADMINISTRATOR_PERMISSION` |

Default test password for all seeded accounts: **`Password123!`**

---

## Running locally

### Prerequisites

- Node.js 20+ and npm
- Java 21
- MySQL 8 with a database named `lms_v2`

### Backend

```bash
cd backend
./mvnw spring-boot:run
# Starts on http://localhost:8080
```

### Frontend

```bash
cd frontend/projekat
npm install
npx ng serve
# Opens on http://localhost:4200
```

---

## Public pages (no login required)

| Route | Description |
|---|---|
| `/` | University home |
| `/faculties` | All faculties |
| `/faculty/:code` | Faculty detail + study programs |
| `/faculty/:code/study-program/:code` | Program detail with course list |
| `/faculty/:code/.../course/:code` | Course detail |
| `/enrollment` | Enrollment information |
| `/contact` | Contact form |
| `/rectorate` | Rectorate information |
| `/all-announcements` | Global announcements |

---

## Known issues (in-progress)

- Document export (XML/PDF) from student affairs portal currently broken — backend bug
- CRUD forms for Faculty and Study Program use plain-text for relational fields (dean, director) instead of a proper dropdown populated from the DB
- Exam period administration and timetable management not yet implemented
- Cascade delete leaves orphan foreign keys — backend fix pending

---

## Project phases

| # | Phase | Status |
|---|---|---|
| 1-9 | Backend: analysis, refactor, security, bugs, new modules | In progress |
| 10-13 | Frontend: analysis, translation, PrimeNG→Material migration, Angular upgrade | Done |
| 14 | Frontend redesign + responsive | Done |
| 15 | Frontend security (XSS, JWT hardening) | Done |
| 16-18 | Docker, CI/CD, deployment, testing, docs | Pending |
