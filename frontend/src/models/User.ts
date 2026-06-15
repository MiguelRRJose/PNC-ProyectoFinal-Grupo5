export type UserRole = 'student' | 'instructor' | 'admin';

export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
  avatarUrl?: string;
  failedAttempts: number;
  blockedUntil?: string | null; // ISO string representing when they are unblocked
  createdAt: string;
}

export interface StudentProfile extends User {
  enrolledCourses: string[]; // List of courseIds
  completedLessons: string[]; // List of lessonIds
  wishlist: string[]; // List of courseIds
  certificates: string[]; // List of certificateIds
}

export interface InstructorProfile extends User {
  bio?: string;
  revenue: number; // Income report
  commissionRate: number; // Commission percentage of the platform (e.g. 0.05)
}
