import { Course } from './academic/course';
import { Teacher } from './users/teacher';
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
    teacher?: Teacher;
}
