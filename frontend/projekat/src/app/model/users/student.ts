import { RegisteredUser } from './registered-user';
import { Faculty } from '../academic/faculty';

export interface Student extends RegisteredUser {
    userType?: string;
    indexNumber?: string;
    faculty?: Faculty;
}
