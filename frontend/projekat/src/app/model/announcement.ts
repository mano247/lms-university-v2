import { Course } from './academic/course';

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
