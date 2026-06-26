import { Notification } from '../obavestenje';
import { ExamAttempt } from '../polaganje';
import { Teacher } from '../users/nastavnik';
import { Student } from '../users/student';
import { CourseMaterial } from './nastavniMaterijal';

export interface StudyYear {
  id?: number;
  yearNumber: number;
}

export interface Course {
    id?: number;
    courseCode: string;
    syllabus: string;
    name: string;
    ects: number;
    startDate: Date;
    endDate: Date;
    description: string;
    studyYear?: StudyYear;
    teachingMaterials: CourseMaterial[];
    examAttempts: ExamAttempt[];
    teacher?: Teacher;
    students: Student[];
    announcements: Notification[];
    studyProgram: any;
}
