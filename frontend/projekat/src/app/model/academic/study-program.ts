import { Faculty } from './faculty';
import { Teacher } from '../users/teacher';

export interface StudyProgram {
    id?: number;
    programCode: string;
    name: string;
    description: string;
    programDirector?: Teacher;
    faculty: Faculty;
    courses?: string[];
}
