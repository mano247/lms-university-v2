import { ExamAttempt } from '../exam-attempt';
import { Teacher } from '../users/teacher';
import { CourseMaterial } from './course-material';
import { StudyProgram } from './study-program';

export interface Course {
    id?: number;
    courseCode: string;
    syllabus?: string;
    name: string;
    ects?: number;
    teacher?: Teacher;
    startDate?: Date;
    endDate?: Date;
    description?: string;
    studyProgram?: StudyProgram;
    teachingMaterials?: CourseMaterial[];
    examAttempts?: ExamAttempt[];
}
