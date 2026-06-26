import { Teacher } from "./users/teacher";
import { Student } from "./users/student";

export interface Thesis {
    id?: number;
    topic: string;
    link: string;
    student: Student;
    profesor: Teacher;
}
