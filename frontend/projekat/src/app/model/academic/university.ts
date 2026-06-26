import { Rectorate } from '../rectorate';

export interface University {
    id?: number;
    name: string;
    foundingDate: any;
    contact: string;
    description: string;
    image: string;
    address: string;
    rectorate?: Rectorate;
}
