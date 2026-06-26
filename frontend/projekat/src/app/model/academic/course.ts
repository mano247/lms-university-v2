import { Notification } from '../announcement';
import { ExamAttempt } from '../exam-attempt';
import { Teacher } from '../users/teacher';
import { Student } from '../users/student';
import { CourseMaterial } from './teaching-material';

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
