import { University } from './academic/univerzitet';

export interface Rectorate {
    id?: number;
    name: string;
    contact: string;
    image: string;
    address: string;
    universities: University[];
    rectorName: string;
}
