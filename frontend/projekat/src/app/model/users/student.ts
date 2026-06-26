import { Course } from '../academic/course';
import { StudyYear } from '../study-year';
import { ExamAttempt } from '../exam-attempt';
import { RegisteredUser } from './registered-user';

export interface Student extends RegisteredUser {
    indexNumber: string;
    studyYears: StudyYear[];
    examAttempts: ExamAttempt[];
    courses: Course[];
}
