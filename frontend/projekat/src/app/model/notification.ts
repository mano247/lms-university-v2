import { Course } from './academic/course';

export interface Notification {
    id?: number;
    postedAt?: Date;
    content: string;
    title: string;
    image?: string;
    startDate?: Date;
    endDate?: Date;
    course?: Course;
}
