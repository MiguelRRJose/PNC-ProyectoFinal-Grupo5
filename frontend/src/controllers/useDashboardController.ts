import { useState, useEffect } from 'react';
import { ApiService, AuditLog, LessonQuestion } from '../services/apiService';
import { Course, Coupon } from '../models/Course';
import { User, StudentProfile, InstructorProfile } from '../models/User';
import { Certificate } from '../models/Certificate';

interface UseDashboardControllerProps {
  currentUser: User;
  onUpdateUser: (user: User) => void;
  onLogout: () => void;
}

export const useDashboardController = ({ currentUser, onUpdateUser, onLogout }: UseDashboardControllerProps) => {
  // Navigation & UI Tab
  const [currentTab, setCurrentTab] = useState('explore');
  const [isLoading, setIsLoading] = useState(false);

  // Search & Filters
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [maxPrice, setMaxPrice] = useState<number | undefined>(undefined);
  const [courses, setCourses] = useState<Course[]>([]);

  // Admin Audit Logs & Moderation
  const [auditLogs, setAuditLogs] = useState<AuditLog[]>([]);

  // Deletion Modal state (to replace native confirm)
  const [courseToDeleteId, setCourseToDeleteId] = useState<string | null>(null);

  // Instructor Forms & Reports
  const [instructorRevenue, setInstructorRevenue] = useState(0);
  const [newCourseTitle, setNewCourseTitle] = useState('');
  const [newCourseDesc, setNewCourseDesc] = useState('');
  const [newCourseCategory, setNewCourseCategory] = useState('Software Engineering');
  const [newCoursePrice, setNewCoursePrice] = useState(29.99);
  const [showAddCourseModal, setShowAddCourseModal] = useState(false);

  // Student Certificates
  const [certificates, setCertificates] = useState<Certificate[]>([]);

  // Student/User Profile Personalization Form State
  const [profileName, setProfileName] = useState(currentUser.name);
  const [profileEmail, setProfileEmail] = useState(currentUser.email);
  const [profileBio, setProfileBio] = useState('');
  const [profileAvatar, setProfileAvatar] = useState(currentUser.avatarUrl || '');
  const [profileSuccessMsg, setProfileSuccessMsg] = useState('');
  const [showDeleteAccountConfirm, setShowDeleteAccountConfirm] = useState(false);

  // --- NUEVO: GESTIÓN DE USUARIOS (ADMIN) ---
  const [usersList, setUsersList] = useState<User[]>([]);
  const [showUserModal, setShowUserModal] = useState(false);
  const [selectedUserForEdit, setSelectedUserForEdit] = useState<User | null>(null);
  const [userFormName, setUserFormName] = useState('');
  const [userFormEmail, setUserFormEmail] = useState('');
  const [userFormPassword, setUserFormPassword] = useState('');
  const [userFormRole, setUserFormRole] = useState<'student' | 'instructor' | 'admin'>('student');
  const [userFormIsActive, setUserFormIsActive] = useState(true);

  // --- NUEVO: GESTIÓN DE CUPONES (ADMIN / INSTRUCTOR) ---
  const [couponsList, setCouponsList] = useState<Coupon[]>([]);
  const [showCouponModal, setShowCouponModal] = useState(false);
  const [couponFormCode, setCouponFormCode] = useState('');
  const [couponFormDiscount, setCouponFormDiscount] = useState(15);
  const [couponFormMaxUses, setCouponFormMaxUses] = useState(10);
  const [couponFormExpiry, setCouponFormExpiry] = useState('2026-12-31T23:59:59Z');

  // --- NUEVO: DASHBOARD FINANCIERO (ADMIN) ---
  const [financialSummary, setFinancialSummary] = useState<any>(null);

  // --- NUEVO: RESPONDER PREGUNTAS (INSTRUCTOR) ---
  const [pendingQuestions, setPendingQuestions] = useState<LessonQuestion[]>([]);
  const [showAnswerModal, setShowAnswerModal] = useState(false);
  const [questionToAnswer, setQuestionToAnswer] = useState<LessonQuestion | null>(null);
  const [answerFormText, setAnswerFormText] = useState('');

  // Synchronize profile state when user changes
  useEffect(() => {
    setProfileName(currentUser.name);
    setProfileEmail(currentUser.email);
    setProfileAvatar(currentUser.avatarUrl || '');
    
    const student = currentUser as any;
    if (currentUser.role === 'student') {
      setProfileBio(student.bio || 'Estudiante entusiasmado de la UCA.');
    } else if (currentUser.role === 'instructor') {
      setProfileBio(student.bio || 'Instructor de EducaNet.');
    } else {
      setProfileBio(student.bio || 'Administrador del sistema.');
    }
  }, [currentUser]);

  // Fetch Courses (triggered when search/filters or tab changes)
  const fetchCourses = async () => {
    setIsLoading(true);
    try {
      const data = await ApiService.getCourses(search, category, maxPrice);
      setCourses(data);
    } catch (err) {
      console.error("Error al cargar cursos:", err);
    } finally {
      setIsLoading(false);
    }
  };

  // Load contextual data depending on the tab
  useEffect(() => {
    fetchCourses();
    
    if (currentTab === 'audit-logs' && currentUser.role === 'admin') {
      ApiService.getAuditLogs().then(setAuditLogs);
    }

    if (currentTab === 'my-certificates' && currentUser.role === 'student') {
      const allCerts: Certificate[] = JSON.parse(localStorage.getItem('certificates') || '[]');
      const userCerts = allCerts.filter(c => c.studentId === currentUser.id);
      setCertificates(userCerts);
    }

    // Cargar cupones
    if (currentTab === 'admin-coupons' || currentTab === 'instructor-coupons') {
      setCouponsList(ApiService.getCoupons());
    }

    // Cargar usuarios para el Admin
    if (currentTab === 'admin-manage-users' || currentTab === 'instructor-users') {
      setUsersList(ApiService.getUsers());
    }

    // Cargar dashboard financiero
    if (currentTab === 'admin-financials' && currentUser.role === 'admin') {
      setFinancialSummary(ApiService.getFinancialSummary());
    }

    // Cargar preguntas pendientes de responder para Instructores
    if (currentTab === 'instructor-questions' && currentUser.role === 'instructor') {
      const allQuestions: LessonQuestion[] = JSON.parse(localStorage.getItem('lesson_questions') || '[]');
      const myCourses = courses.filter(c => c.instructorId === currentUser.id).map(c => c.id);
      
      // Filtrar preguntas que pertenecen a los cursos del instructor y no tienen respuesta aún
      const filtered = allQuestions.filter(q => {
        // En nuestro simulador, q.id empieza por q-lessonId
        // ques-1 es una pregunta mock que mostramos a todos
        return !q.answerText;
      });
      setPendingQuestions(filtered);
    }

    if (currentUser.role === 'instructor') {
      const users: User[] = JSON.parse(localStorage.getItem('users') || '[]');
      const inst = users.find(u => u.id === currentUser.id) as InstructorProfile;
      if (inst) setInstructorRevenue(inst.revenue);
    }
  }, [currentTab, search, category, maxPrice]);

  // Wishlist Action
  const handleToggleWishlist = async (courseId: string) => {
    if (currentUser.role !== 'student') return;
    try {
      const updatedWishlist = await ApiService.toggleWishlist(currentUser.id, courseId);
      const updatedUser = { ...currentUser, wishlist: updatedWishlist };
      onUpdateUser(updatedUser);
    } catch (err) {
      console.error(err);
    }
  };

  // Create Course Action (Instructor / Admin)
  const handleCreateCourse = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newCourseTitle.trim()) return;

    setIsLoading(true);
    try {
      const mockModules = [
        {
          id: `mod-${Date.now()}-1`,
          courseId: '',
          title: "Módulo 1: Introducción y Configuración",
          orderIndex: 1,
          lessons: [
            {
              id: `les-${Date.now()}-1`,
              moduleId: `mod-${Date.now()}-1`,
              title: "1.1 Lección Inicial y Conceptos Básicos",
              type: "video" as const,
              contentUrl: "https://www.w3schools.com/html/mov_bbb.mp4",
              durationMinutes: 10,
              orderIndex: 1
            }
          ]
        }
      ];

      await ApiService.createCourse(
        {
          title: newCourseTitle,
          description: newCourseDesc,
          category: newCourseCategory,
          price: Number(newCoursePrice),
          durationHours: 8,
          modules: mockModules
        },
        currentUser.id
      );

      setNewCourseTitle('');
      setNewCourseDesc('');
      setNewCoursePrice(29.99);
      setShowAddCourseModal(false);

      fetchCourses();
    } catch (err) {
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  // Delete Course Action
  const handleDeleteCourseClick = (courseId: string) => {
    setCourseToDeleteId(courseId);
  };

  const handleDeleteCourseConfirm = async () => {
    if (!courseToDeleteId) return;
    try {
      await ApiService.deleteCourse(courseToDeleteId, currentUser.id);
      setCourseToDeleteId(null);
      fetchCourses();
      
      if (currentTab === 'admin-financials') {
        setFinancialSummary(ApiService.getFinancialSummary());
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleDeleteCourseCancel = () => {
    setCourseToDeleteId(null);
  };

  // Save profile personalization
  const handleSaveProfile = (e: React.FormEvent) => {
    e.preventDefault();
    setProfileSuccessMsg('');

    if (!profileName.trim()) return;

    const updatedUser = {
      ...currentUser,
      name: profileName,
      email: profileEmail,
      avatarUrl: profileAvatar,
      bio: profileBio
    } as any;

    onUpdateUser(updatedUser);
    setProfileSuccessMsg('¡Perfil actualizado con éxito!');
    setTimeout(() => setProfileSuccessMsg(''), 4000);
  };

  // Delete Own Account (Usuario Común)
  const handleDeleteOwnAccount = async () => {
    try {
      await ApiService.deleteOwnAccount(currentUser.id);
      setShowDeleteAccountConfirm(false);
      onLogout();
      alert("Tu cuenta ha sido eliminada con éxito del sistema. Te extrañaremos.");
    } catch (err: any) {
      alert(err.message || "Error al eliminar cuenta.");
    }
  };

  // Darse de baja de un curso (Unenroll)
  const handleUnenrollCourse = async (courseId: string) => {
    if (!confirm("¿Deseas darte de baja de este curso? Perderás el acceso y todo tu progreso se borrará de forma inmediata.")) return;

    try {
      const updatedUser = await ApiService.unenrollCourse(currentUser.id, courseId);
      onUpdateUser(updatedUser);
      fetchCourses();
    } catch (err: any) {
      alert(err.message);
    }
  };

  // --- GESTIÓN DE USUARIOS (ADMIN CRUD) ---
  const handleOpenCreateUser = () => {
    setSelectedUserForEdit(null);
    setUserFormName('');
    setUserFormEmail('');
    setUserFormPassword('');
    setUserFormRole('student');
    setUserFormIsActive(true);
    setShowUserModal(true);
  };

  const handleOpenEditUser = (user: User) => {
    const usr = user as any;
    setSelectedUserForEdit(user);
    setUserFormName(user.name);
    setUserFormEmail(user.email);
    setUserFormPassword(usr.password || '');
    setUserFormRole(user.role);
    setUserFormIsActive(user.isActive);
    setShowUserModal(true);
  };

  const handleCreateOrUpdateUser = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (selectedUserForEdit) {
        await ApiService.updateUserByAdmin(currentUser.id, selectedUserForEdit.id, {
          name: userFormName,
          email: userFormEmail,
          role: userFormRole,
          password: userFormPassword,
          isActive: userFormIsActive
        });
      } else {
        await ApiService.createUserByAdmin(currentUser.id, {
          name: userFormName,
          email: userFormEmail,
          role: userFormRole,
          password: userFormPassword
        });
      }
      setShowUserModal(false);
      setUsersList(ApiService.getUsers());
    } catch (err: any) {
      alert(err.message || "Error al procesar usuario.");
    }
  };

  const handleDeleteUser = async (userId: string) => {
    if (!confirm("¿Está seguro de que desea eliminar permanentemente a este usuario?")) return;
    try {
      await ApiService.deleteUserByAdmin(currentUser.id, userId);
      setUsersList(ApiService.getUsers());
      
      // Actualizar financieros si corresponde
      if (currentTab === 'admin-financials') {
        setFinancialSummary(ApiService.getFinancialSummary());
      }
    } catch (err: any) {
      alert(err.message);
    }
  };

  // --- GESTIÓN DE CUPONES (CRUD ADMIN/INSTRUCTOR) ---
  const handleCreateCoupon = (e: React.FormEvent) => {
    e.preventDefault();
    if (!couponFormCode.trim()) return;

    try {
      ApiService.createCoupon({
        code: couponFormCode,
        discountPercentage: Number(couponFormDiscount),
        maxUses: Number(couponFormMaxUses),
        expirationDate: couponFormExpiry
      }, currentUser.id);

      setCouponFormCode('');
      setShowCouponModal(false);
      setCouponsList(ApiService.getCoupons());
    } catch (err: any) {
      alert(err.message || "Error al crear cupón.");
    }
  };

  const handleDeleteCoupon = (id: string) => {
    if (!confirm("¿Eliminar este cupón de descuento?")) return;
    ApiService.deleteCoupon(id, currentUser.id);
    setCouponsList(ApiService.getCoupons());
  };

  // --- RESPONDER PREGUNTAS (INSTRUCTOR) ---
  const handleOpenAnswerModal = (question: LessonQuestion) => {
    setQuestionToAnswer(question);
    setAnswerFormText('');
    setShowAnswerModal(true);
  };

  const handleAnswerQuestion = (e: React.FormEvent) => {
    e.preventDefault();
    if (!questionToAnswer || !answerFormText.trim()) return;

    try {
      ApiService.answerQuestion(currentUser.id, currentUser.name, questionToAnswer.id, answerFormText);
      setShowAnswerModal(false);
      
      // Recargar preguntas pendientes
      const allQuestions: LessonQuestion[] = JSON.parse(localStorage.getItem('lesson_questions') || '[]');
      const filtered = allQuestions.filter(q => !q.answerText);
      setPendingQuestions(filtered);
    } catch (err: any) {
      alert(err.message);
    }
  };

  // Regeneration of Certificate (Strategy Pattern)
  const handleRegenerateCertificate = async (courseId: string, format: 'PDF' | 'JSON-LD' | 'Image') => {
    try {
      setIsLoading(true);
      await ApiService.generateCertificateManual(currentUser.id, courseId, format);
      const allCerts: Certificate[] = JSON.parse(localStorage.getItem('certificates') || '[]');
      const userCerts = allCerts.filter(c => c.studentId === currentUser.id);
      setCertificates(userCerts);
      alert(`Certificado regenerado exitosamente en formato ${format}.`);
    } catch (err: any) {
      alert(`Error al regenerar certificado: ${err.message}`);
    } finally {
      setIsLoading(false);
    }
  };

  const isEnrolled = (courseId: string) => {
    if (currentUser.role !== 'student') return false;
    return (currentUser as StudentProfile).enrolledCourses?.includes(courseId) || false;
  };

  const isInWishlist = (courseId: string) => {
    if (currentUser.role !== 'student') return false;
    return (currentUser as StudentProfile).wishlist?.includes(courseId) || false;
  };

  return {
    currentTab,
    setCurrentTab,
    isLoading,
    courses,
    search,
    setSearch,
    category,
    setCategory,
    maxPrice,
    setMaxPrice,
    auditLogs,
    instructorRevenue,
    showAddCourseModal,
    setShowAddCourseModal,
    newCourseTitle,
    setNewCourseTitle,
    newCourseDesc,
    setNewCourseDesc,
    newCourseCategory,
    setNewCourseCategory,
    newCoursePrice,
    setNewCoursePrice,
    certificates,
    handleToggleWishlist,
    handleCreateCourse,
    courseToDeleteId,
    handleDeleteCourseClick,
    handleDeleteCourseConfirm,
    handleDeleteCourseCancel,
    
    // Perfil
    profileName,
    setProfileName,
    profileEmail,
    setProfileEmail,
    profileBio,
    setProfileBio,
    profileAvatar,
    setProfileAvatar,
    profileSuccessMsg,
    handleSaveProfile,
    showDeleteAccountConfirm,
    setShowDeleteAccountConfirm,
    handleDeleteOwnAccount,
    
    // Unenroll
    handleUnenrollCourse,
    
    // Google Sign-In
    handleGoogleLogin: onLogout,
    
    // Admin CRUD Users
    usersList,
    showUserModal,
    setShowUserModal,
    selectedUserForEdit,
    userFormName,
    setUserFormName,
    userFormEmail,
    setUserFormEmail,
    userFormPassword,
    setUserFormPassword,
    userFormRole,
    setUserFormRole,
    userFormIsActive,
    setUserFormIsActive,
    handleOpenCreateUser,
    handleOpenEditUser,
    handleCreateOrUpdateUser,
    handleDeleteUser,

    // Coupons CRUD
    couponsList,
    showCouponModal,
    setShowCouponModal,
    couponFormCode,
    setCouponFormCode,
    couponFormDiscount,
    setCouponFormDiscount,
    couponFormMaxUses,
    setCouponFormMaxUses,
    couponFormExpiry,
    setCouponFormExpiry,
    handleCreateCoupon,
    handleDeleteCoupon,

    // Admin Financial Dashboard
    financialSummary,

    // Q&A Instructor
    pendingQuestions,
    showAnswerModal,
    setShowAnswerModal,
    questionToAnswer,
    handleOpenAnswerModal,
    answerFormText,
    setAnswerFormText,
    handleAnswerQuestion,

    isEnrolled,
    isInWishlist
  };
};
