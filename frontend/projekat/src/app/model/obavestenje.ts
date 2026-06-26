import { Course } from './academic/predmet';

export interface Notification {
    id?: number;
    date: Date;
    content: string;
    title: string;
    image: string;
    startDate: Date;
    endDate: Date;
    course?: Course;
}
