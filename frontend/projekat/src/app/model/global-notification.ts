export interface GlobalNotification {
    id?: number;
    postedAt?: Date;
    content: string;
    title: string;
    image?: string;
    startDate?: Date;
    endDate?: Date;
}
