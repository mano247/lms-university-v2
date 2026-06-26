import { University } from './academic/university';

export interface Rectorate {
    id?: number;
    name: string;
    contact: string;
    image: string;
    address: string;
    rectorName: string;
    universities?: University[];
}
