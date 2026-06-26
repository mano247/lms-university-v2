import { Course } from './academic/predmet';
import { Notification } from './obavestenje';
import { Teacher } from './users/nastavnik';
import { Student } from './users/student';

export interface ExamAttempt {
    id?: number;
    points?: number;
    finalGrade?: number;
    startTime?: Date;
    endTime?: Date;
    note?: string;
    course?: Course;
    student?: Student;
    notification?: Notification;
    teacher?: Teacher;
}
