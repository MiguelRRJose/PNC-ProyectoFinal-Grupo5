import { useState, useEffect } from 'react';
import { ApiService, LessonQuestion } from '../services/apiService';
import { Course, Lesson, Module, QuizQuestion } from '../models/Course';
import { User, StudentProfile } from '../models/User';
import { Certificate } from '../models/Certificate';

interface UseCourseDetailControllerProps {
  courseId: string;
  currentUser: User;
  onUpdateUser: (user: User) => void;
  onBack: () => void;
}

export const useCourseDetailController = ({
  courseId,
  currentUser,
  onUpdateUser,
  onBack
}: UseCourseDetailControllerProps) => {
  const [course, setCourse] = useState<Course | null>(null);
  const [activeLesson, setActiveLesson] = useState<Lesson | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [progressPercent, setProgressPercent] = useState(0);

  // Stripe & Purchase State
  const [couponCode, setCouponCode] = useState('');
  const [appliedDiscount, setAppliedDiscount] = useState<number>(0); // discount percentage
  const [couponError, setCouponError] = useState('');
  const [couponSuccess, setCouponSuccess] = useState('');
  const [isPurchasing, setIsPurchasing] = useState(false);
  
  // Custom Payment Success Modal state (to replace alert())
  const [showPaymentSuccessModal, setShowPaymentSuccessModal] = useState(false);

  // Quiz States
  const [quizAnswers, setQuizAnswers] = useState<Record<string, number>>({});
  const [quizSubmitted, setQuizSubmitted] = useState(false);
  const [quizScore, setQuizScore] = useState<number | null>(null);
  const [quizPassed, setQuizPassed] = useState<boolean | null>(null);

  // Automatic Certificate Celebration Modal
  const [newCertificate, setNewCertificate] = useState<Certificate | null>(null);

  // --- NUEVO: Q&A en Lecciones ---
  const [lessonQuestions, setLessonQuestions] = useState<LessonQuestion[]>([]);
  const [questionInputText, setQuestionInputText] = useState('');

  // Curriculum Editor States (Admins & Instructors)
  const [showAddModuleModal, setShowAddModuleModal] = useState(false);
  const [newModuleTitle, setNewModuleTitle] = useState('');
  
  const [showAddLessonModal, setShowAddLessonModal] = useState(false);
  const [targetModuleIdForLesson, setTargetModuleIdForLesson] = useState<string | null>(null);
  const [newLessonTitle, setNewLessonTitle] = useState('');
  const [newLessonType, setNewLessonType] = useState<'video' | 'pdf' | 'quiz'>('video');
  const [newLessonUrl, setNewLessonUrl] = useState('');
  const [newLessonDuration, setNewLessonDuration] = useState(10);

  // Quiz builder states inside Lesson modal
  const [quizQuestionsList, setQuizQuestionsList] = useState<QuizQuestion[]>([]);
  const [currentQuestionText, setCurrentQuestionText] = useState('');
  const [currentQuestionOptions, setCurrentQuestionOptions] = useState<string[]>(['', '', '', '']);
  const [currentQuestionCorrectIndex, setCurrentQuestionCorrectIndex] = useState(0);

  // Load Course Details & Student Progress
  const loadCourseData = async () => {
    setIsLoading(true);
    try {
      const data = await ApiService.getCourseById(courseId);
      setCourse(data);

      // Set first lesson as active by default if not set
      if (!activeLesson && data.modules.length > 0 && data.modules[0].lessons.length > 0) {
        setActiveLesson(data.modules[0].lessons[0]);
      } else if (activeLesson) {
        // Sync active lesson data
        const updatedActive = data.modules
          .flatMap(m => m.lessons)
          .find(l => l.id === activeLesson.id);
        if (updatedActive) setActiveLesson(updatedActive);
      }

      // Calculate progress percent if student
      if (currentUser.role === 'student') {
        const student = currentUser as StudentProfile;
        const lessonsIds = data.modules.flatMap(m => m.lessons.map(l => l.id));
        const completed = lessonsIds.filter(id => student.completedLessons?.includes(id));
        setProgressPercent(lessonsIds.length > 0 ? Math.round((completed.length / lessonsIds.length) * 100) : 0);
      }
    } catch (err) {
      console.error(err);
      onBack();
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadCourseData();
  }, [courseId, currentUser]);

  // Clean quiz state and load Q&A when active lesson changes
  useEffect(() => {
    setQuizAnswers({});
    setQuizSubmitted(false);
    setQuizScore(null);
    setQuizPassed(null);

    if (activeLesson) {
      // Cargar Q&A de la lección
      setLessonQuestions(ApiService.getQuestionsForLesson(activeLesson.id));
      
      // Registrar lección accedida recientemente en historial (Usuario Común)
      if (currentUser.role === 'student' && course) {
        ApiService.recordLessonAccess(currentUser.id, course.id, activeLesson.id, activeLesson.title, course.title);
        // Sincronizar el historial localmente en la app para reflejar el widget
        const users: User[] = JSON.parse(localStorage.getItem('users') || '[]');
        const updated = users.find(u => u.id === currentUser.id);
        if (updated) onUpdateUser(updated);
      }
    }
  }, [activeLesson]);

  // Coupon application logic
  const handleApplyCoupon = async (e: React.FormEvent) => {
    e.preventDefault();
    setCouponError('');
    setCouponSuccess('');
    if (!couponCode.trim()) return;

    try {
      const coupon = await ApiService.applyCoupon(couponCode);
      setAppliedDiscount(coupon.discountPercentage);
      setCouponSuccess(`¡Cupón aplicado con éxito! Descuento del ${coupon.discountPercentage}%`);
    } catch (err: any) {
      setCouponError(err.message || 'Error al aplicar cupón.');
      setAppliedDiscount(0);
    }
  };

  // Stripe checkout simulation
  const handleCheckout = async () => {
    setIsPurchasing(true);
    try {
      const updatedUser = await ApiService.purchaseCourse(currentUser.id, courseId, couponCode);
      onUpdateUser(updatedUser);
      setShowPaymentSuccessModal(true);
    } catch (err: any) {
      alert(err.message || 'Error en el pago.');
    } finally {
      setIsPurchasing(false);
    }
  };

  const handleClosePaymentSuccess = () => {
    setShowPaymentSuccessModal(false);
    loadCourseData();
  };

  // Mark lesson as completed
  const handleToggleLessonComplete = async (lessonId: string, isCompleted: boolean) => {
    if (currentUser.role !== 'student') return;
    try {
      const result = await ApiService.updateProgress(currentUser.id, courseId, lessonId, isCompleted);
      
      // Actualizar usuario en App Context
      const updatedUser = { ...currentUser, completedLessons: result.completedLessons } as StudentProfile;
      onUpdateUser(updatedUser);

      // Actualizar progreso local
      setProgressPercent(result.progressPercent);

      // Si se generó un certificado automático, gatillar modal de celebración
      if (result.certificateGenerated) {
        setNewCertificate(result.certificateGenerated);
      }
    } catch (err) {
      console.error(err);
    }
  };

  // Quiz submission logic
  const handleSelectQuizAnswer = (questionId: string, optionIndex: number) => {
    setQuizAnswers(prev => ({
      ...prev,
      [questionId]: optionIndex
    }));
  };

  const handleSubmitQuiz = () => {
    if (!activeLesson || !activeLesson.quizQuestions) return;

    const questions = activeLesson.quizQuestions;
    let correctCount = 0;

    questions.forEach(q => {
      if (quizAnswers[q.id] === q.correctAnswerIndex) {
        correctCount++;
      }
    });

    const score = Math.round((correctCount / questions.length) * 100);
    const passed = score >= 70; // 70% to pass

    setQuizScore(score);
    setQuizPassed(passed);
    setQuizSubmitted(true);

    if (passed) {
      // Mark quiz lesson as completed automatically if passed!
      handleToggleLessonComplete(activeLesson.id, true);
    }
  };

  // --- NUEVO: Q&A Acción ---
  const handleAskQuestion = (e: React.FormEvent) => {
    e.preventDefault();
    if (!activeLesson || !questionInputText.trim()) return;

    const newQ = ApiService.addQuestion(currentUser.id, currentUser.name, activeLesson.id, questionInputText);
    setLessonQuestions(prev => [...prev, newQ]);
    setQuestionInputText('');
  };

  // --- CURRICULUM EDITING ACTIONS ---

  const handleAddModule = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newModuleTitle.trim() || !course) return;

    const courses: Course[] = JSON.parse(localStorage.getItem('courses') || '[]');
    const courseIndex = courses.findIndex(c => c.id === course.id);
    if (courseIndex === -1) return;

    const newModule: Module = {
      id: `mod-${Date.now()}`,
      courseId: course.id,
      title: newModuleTitle,
      orderIndex: course.modules.length + 1,
      lessons: []
    };

    courses[courseIndex].modules.push(newModule);
    localStorage.setItem('courses', JSON.stringify(courses));
    
    // Log Audit
    const auditLogs = JSON.parse(localStorage.getItem('audit_logs') || '[]');
    auditLogs.unshift({
      id: `log-${Date.now()}`,
      userId: currentUser.id,
      userEmail: currentUser.email,
      action: "MODULE_ADDED",
      details: `Modulo creado: "${newModuleTitle}" para el curso "${course.title}"`,
      timestamp: new Date().toISOString()
    });
    localStorage.setItem('audit_logs', JSON.stringify(auditLogs));

    setNewModuleTitle('');
    setShowAddModuleModal(false);
    loadCourseData();
  };

  const handleAddQuizQuestion = () => {
    if (!currentQuestionText.trim()) return;
    
    const newQuestion: QuizQuestion = {
      id: `q-${Date.now()}-${quizQuestionsList.length}`,
      question: currentQuestionText,
      options: [...currentQuestionOptions],
      correctAnswerIndex: currentQuestionCorrectIndex
    };

    setQuizQuestionsList(prev => [...prev, newQuestion]);
    
    // Reset inputs
    setCurrentQuestionText('');
    setCurrentQuestionOptions(['', '', '', '']);
    setCurrentQuestionCorrectIndex(0);
  };

  const handleRemoveQuizQuestion = (idx: number) => {
    setQuizQuestionsList(prev => prev.filter((_, i) => i !== idx));
  };

  const handleAddLesson = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newLessonTitle.trim() || !course || !targetModuleIdForLesson) return;

    const courses: Course[] = JSON.parse(localStorage.getItem('courses') || '[]');
    const courseIndex = courses.findIndex(c => c.id === course.id);
    if (courseIndex === -1) return;

    const targetMod = courses[courseIndex].modules.find(m => m.id === targetModuleIdForLesson);
    if (!targetMod) return;

    const newLesson: Lesson = {
      id: `les-${Date.now()}`,
      moduleId: targetModuleIdForLesson,
      title: newLessonTitle,
      type: newLessonType,
      durationMinutes: Number(newLessonDuration),
      orderIndex: targetMod.lessons.length + 1
    };

    if (newLessonType === 'video') {
      newLesson.contentUrl = newLessonUrl.trim() || "https://www.w3schools.com/html/mov_bbb.mp4";
    } else if (newLessonType === 'pdf') {
      newLesson.contentUrl = newLessonUrl.trim() || "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
    } else if (newLessonType === 'quiz') {
      newLesson.quizQuestions = [...quizQuestionsList];
    }

    targetMod.lessons.push(newLesson);
    localStorage.setItem('courses', JSON.stringify(courses));

    // Audit Log
    const auditLogs = JSON.parse(localStorage.getItem('audit_logs') || '[]');
    auditLogs.unshift({
      id: `log-${Date.now()}`,
      userId: currentUser.id,
      userEmail: currentUser.email,
      action: "LESSON_ADDED",
      details: `Leccion creada: "${newLessonTitle}" (Tipo: ${newLessonType}) en curso "${course.title}"`,
      timestamp: new Date().toISOString()
    });
    localStorage.setItem('audit_logs', JSON.stringify(auditLogs));

    // Reset States
    setNewLessonTitle('');
    setNewLessonUrl('');
    setNewLessonType('video');
    setNewLessonDuration(10);
    setQuizQuestionsList([]);
    setTargetModuleIdForLesson(null);
    setShowAddLessonModal(false);
    
    loadCourseData();
  };

  const handleOpenAddLessonModal = (moduleId: string) => {
    setTargetModuleIdForLesson(moduleId);
    setShowAddLessonModal(true);
  };

  // Helper check
  const isEnrolled = () => {
    if (currentUser.role !== 'student') return false;
    return (currentUser as StudentProfile).enrolledCourses?.includes(courseId) || false;
  };

  const isCompleted = (lessonId: string) => {
    if (currentUser.role !== 'student') return false;
    return (currentUser as StudentProfile).completedLessons?.includes(lessonId) || false;
  };

  return {
    course,
    activeLesson,
    setActiveLesson,
    isLoading,
    progressPercent,
    couponCode,
    setCouponCode,
    appliedDiscount,
    couponError,
    couponSuccess,
    isPurchasing,
    showPaymentSuccessModal,
    handleClosePaymentSuccess,
    quizAnswers,
    quizSubmitted,
    quizScore,
    quizPassed,
    newCertificate,
    setNewCertificate,
    handleApplyCoupon,
    handleCheckout,
    handleToggleLessonComplete,
    handleSelectQuizAnswer,
    handleSubmitQuiz,
    isEnrolled,
    isCompleted,
    
    // Q&A
    lessonQuestions,
    questionInputText,
    setQuestionInputText,
    handleAskQuestion,
    
    // Curriculum editing states
    showAddModuleModal,
    setShowAddModuleModal,
    newModuleTitle,
    setNewModuleTitle,
    handleAddModule,
    
    showAddLessonModal,
    setShowAddLessonModal,
    newLessonTitle,
    setNewLessonTitle,
    newLessonType,
    setNewLessonType,
    newLessonUrl,
    setNewLessonUrl,
    newLessonDuration,
    setNewLessonDuration,
    handleOpenAddLessonModal,
    handleAddLesson,
    
    // Quiz builders
    quizQuestionsList,
    currentQuestionText,
    setCurrentQuestionText,
    currentQuestionOptions,
    setCurrentQuestionOptions,
    currentQuestionCorrectIndex,
    setCurrentQuestionCorrectIndex,
    handleAddQuizQuestion,
    handleRemoveQuizQuestion
  };
};
