import { Course } from '../academic/predmet';
import { University } from '../academic/univerzitet';
import { ExamAttempt } from '../polaganje';
import { RegisteredUser } from './registrovaniKorisnik';

export interface Teacher extends RegisteredUser {
    biography: string;
    personalIdNumber: string;
    university: University;
    courses: Course[];
    examAttempts: ExamAttempt[];
}
