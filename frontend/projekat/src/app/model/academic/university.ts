import { Rectorate } from '../rectorate';

export interface University {
    id?: number;
    name: string;
    foundingDate: Date;
    description: string;
    contact: string;
    image: string;
    address: string;
    rectorate: Rectorate;
}
