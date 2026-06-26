import { Course } from '../academic/course';
import { University } from '../academic/university';
import { ExamAttempt } from '../exam-attempt';
import { RegisteredUser } from './registered-user';

export interface Teacher extends RegisteredUser {
    biography: string;
    personalIdNumber: string;
    university: University;
    courses: Course[];
    examAttempts: ExamAttempt[];
}
