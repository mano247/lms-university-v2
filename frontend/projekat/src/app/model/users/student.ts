import { Course } from '../academic/predmet';
import { StudyYear } from '../godinaStudija';
import { ExamAttempt } from '../polaganje';
import { RegisteredUser } from './registrovaniKorisnik';

export interface Student extends RegisteredUser {
    indexNumber: string;
    studyYears: StudyYear[];
    examAttempts: ExamAttempt[];
    courses: Course[];
}
