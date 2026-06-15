export type LessonType = 'video' | 'pdf' | 'quiz';

export interface QuizOption {
  text: string;
  isCorrect: boolean;
}

export interface QuizQuestion {
  id: string;
  question: string;
  options: string[];
  correctAnswerIndex: number;
}

export interface Lesson {
  id: string;
  moduleId: string;
  title: string;
  type: LessonType;
  contentUrl?: string; // URL for PDF or Video
  durationMinutes: number;
  orderIndex: number;
  quizQuestions?: QuizQuestion[]; // Only if type is 'quiz'
}

export interface Module {
  id: string;
  courseId: string;
  title: string;
  orderIndex: number;
  lessons: Lesson[];
}

export interface Review {
  id: string;
  studentId: string;
  studentName: string;
  rating: number; // 1 to 5
  comment: string;
  createdAt: string;
}

export interface Coupon {
  id: string;
  code: string;
  discountPercentage: number;
  expirationDate: string;
  maxUses: number;
  usedCount: number;
  isActive: boolean;
}

export interface Course {
  id: string;
  title: string;
  description: string;
  price: number;
  category: string;
  durationHours: number;
  instructorId: string;
  instructorName: string;
  imageUrl: string;
  modules: Module[];
  reviews: Review[];
  rating: number; // Calculated average rating
  created_at: string;
}
