import { University } from './university';
import { Teacher } from '../users/teacher';

export interface Faculty {
    id?: number;
    facultyCode: string;
    name: string;
    contact: string;
    description: string;
    dean?: Teacher;
    image: string;
    address: string;
    university?: University;
}
