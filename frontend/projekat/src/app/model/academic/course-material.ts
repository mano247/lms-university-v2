import { Outcome } from '../outcome';

export interface CourseMaterial {
    id?: number;
    title: string;
    authors: string;
    publicationYear: string;
    publisher: string;
    pageCount: number;
    description: string;
    url: string;
    outcome?: Outcome;
    issuedQuantity?: number;
    quantity?: number;
}
