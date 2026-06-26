import { TeachingType } from "./teaching-type";

export interface TeacherAssignment {
    id?: number;
    brojCasova: number;
    teacher_id: number;
    tip_nastave: TeachingType;
}
