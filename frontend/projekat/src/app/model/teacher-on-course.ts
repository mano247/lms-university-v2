import { TeachingType } from './tipNastave';

export interface TeacherOnCourse {
    id?: number;
    hoursCount: number;
    teacherId: number;
    teachingType: TeachingType;
}
