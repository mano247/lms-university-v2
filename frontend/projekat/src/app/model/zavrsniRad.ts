import { Teacher } from './users/nastavnik';
import { Student } from './users/student';

export interface Thesis {
    id?: number;
    topic: string;
    link: string;
    student: Student;
    professor: Teacher;
}
