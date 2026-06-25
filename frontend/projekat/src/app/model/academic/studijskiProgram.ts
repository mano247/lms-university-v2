import { Faculty } from './fakultet';

export interface StudyProgram {
    id?: number;
    programCode: string;
    description: string;
    name: string;
    programDirector: any;
    faculty: Faculty;
    courses: any[];
}
