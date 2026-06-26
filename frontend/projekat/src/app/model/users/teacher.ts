import { RegisteredUser } from './registered-user';

export interface Teacher extends RegisteredUser {
    userType?: string;
    biography?: string;
    personalIdNumber?: string;
}
