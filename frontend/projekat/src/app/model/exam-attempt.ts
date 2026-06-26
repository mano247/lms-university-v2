import { Course } from './academic/course';
import { Notification } from './announcement';
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
    notification?: Notification;
    teacher?: Teacher;
}
