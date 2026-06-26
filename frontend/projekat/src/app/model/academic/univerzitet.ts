import { Rectorate } from '../rektorat';

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
