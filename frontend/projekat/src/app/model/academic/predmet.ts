import { Notification } from '../obavestenje';
import { ExamAttempt } from '../polaganje';
import { Teacher } from '../users/nastavnik';
import { Student } from '../users/student';
import { CourseMaterial } from './nastavniMaterijal';

export interface Course {
    id?: number;
    courseCode: string;
    syllabus: string;
    name: string;
    ects: number;
    startDate: Date;
    endDate: Date;
    description: string;
    teachingMaterials: CourseMaterial[];
    examAttempts: ExamAttempt[];
    teacher?: Teacher;
    students: Student[];
    announcements: Notification[];
    studyProgram: any;
}
