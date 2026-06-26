import { Permission } from '../permission';

export interface RegisteredUser {
    id?: number;
    username: string;
    password: string;
    email: string;
    firstName: string;
    lastName: string;
    permissions: Permission[];
}
