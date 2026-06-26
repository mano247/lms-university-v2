import { Faculty } from './faculty';

export interface StudyProgram {
    id?: number;
    programCode: string;
    description: string;
    name: string;
    programDirector: any;
    faculty: Faculty;
    courses: any[];
}
