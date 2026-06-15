import { User, StudentProfile, InstructorProfile } from '../models/User';
import { Course, Module, Lesson, Review, Coupon } from '../models/Course';
import { Certificate, CertificateFormat } from '../models/Certificate';
import { CertificateGeneratorFactory } from './certificateStrategies';

// --- AUDIT LOG SCHEMA ---
export interface AuditLog {
  id: string;
  userId?: string;
  userEmail?: string;
  action: string;
  details: string;
  timestamp: string;
}

// --- LESSON QUESTION SCHEMA (Q&A) ---
export interface LessonQuestion {
  id: string;
  studentId: string;
  studentName: string;
  questionText: string;
  createdAt: string;
  answerText?: string;
  answeredBy?: string;
  answeredAt?: string;
}

// --- NOTIFICATION SCHEMA ---
export interface NotificationItem {
  id: string;
  userId: string;
  text: string;
  type: 'email' | 'sms' | 'ui';
  read: boolean;
  timestamp: string;
}

// --- INITIAL MOCK DATA ---
const INITIAL_USERS: (StudentProfile | InstructorProfile | User | any)[] = [
  {
    id: "std-01",
    name: "Orlando Rivas",
    email: "orlando@mail.com",
    role: "student",
    failedAttempts: 0,
    blockedUntil: null,
    createdAt: new Date().toISOString(),
    enrolledCourses: ["course-1"], // Pre-inscribed in .NET course
    completedLessons: ["lesson-1-1"],
    wishlist: [],
    certificates: [],
    lessonHistory: [], // Recently accessed lessons: { courseId, courseTitle, lessonId, lessonTitle, accessedAt }[]
    bio: "Estudiante de Ingeniería de Sistemas UCA. Interesado en arquitectura N-Capas.",
    isActive: true
  },
  {
    id: "inst-01",
    name: "Ing. Luisa Arévalo",
    email: "luisa.arevalo@uca.edu.sv",
    role: "instructor",
    failedAttempts: 0,
    blockedUntil: null,
    createdAt: new Date().toISOString(),
    bio: "Docente de Ingeniería y Arquitectura en la UCA. Especialista en Arquitectura de Software N-Capas.",
    revenue: 1250.00,
    commissionRate: 0.05,
    isActive: true
  },
  {
    id: "adm-01",
    name: "Administrador UCA",
    email: "admin@uca.edu.sv",
    role: "admin",
    failedAttempts: 0,
    blockedUntil: null,
    createdAt: new Date().toISOString(),
    isActive: true
  }
];

const INITIAL_COUPONS: Coupon[] = [
  {
    id: "coup-50",
    code: "NCAPAS50",
    discountPercentage: 50,
    expirationDate: "2027-12-31T23:59:59Z",
    maxUses: 10,
    usedCount: 3,
    isActive: true
  },
  {
    id: "coup-100",
    code: "BECA100",
    discountPercentage: 100,
    expirationDate: "2027-12-31T23:59:59Z",
    maxUses: 5,
    usedCount: 1,
    isActive: true
  }
];

const INITIAL_COURSES: Course[] = [
  {
    id: "course-1",
    title: "Arquitectura N-Capas y SOLID con .NET Core",
    description: "Domina el diseño de software empresarial usando arquitectura N-Capas, principios SOLID, patrones de diseño clásicos y Entity Framework Core.",
    price: 49.99,
    category: "Software Engineering",
    durationHours: 15,
    instructorId: "inst-01",
    instructorName: "Ing. Luisa Arévalo",
    imageUrl: "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=600&q=80",
    rating: 4.8,
    created_at: new Date(Date.now() - 30 * 24 * 3600 * 1000).toISOString(), // 30 days ago
    isActive: true, // For enabling/disabling
    reviews: [
      {
        id: "rev-1",
        studentId: "std-01",
        studentName: "Orlando Rivas",
        rating: 5,
        comment: "Excelente curso, explica de manera muy clara los patrones de diseño y la arquitectura limpia.",
        createdAt: new Date().toISOString()
      }
    ],
    modules: [
      {
        id: "mod-1-1",
        courseId: "course-1",
        title: "Módulo 1: Introducción a la Arquitectura N-Capas",
        orderIndex: 1,
        lessons: [
          {
            id: "lesson-1-1",
            moduleId: "mod-1-1",
            title: "1.1 Conceptos Básicos y Separación de Responsabilidades",
            type: "video",
            contentUrl: "https://www.w3schools.com/html/mov_bbb.mp4",
            durationMinutes: 12,
            orderIndex: 1
          },
          {
            id: "lesson-1-2",
            moduleId: "mod-1-1",
            title: "1.2 Arquitectura N-Capas vs Clean Architecture",
            type: "pdf",
            contentUrl: "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
            durationMinutes: 8,
            orderIndex: 2
          }
        ]
      },
      {
        id: "mod-1-2",
        courseId: "course-1",
        title: "Módulo 2: Cuestionario de Nivelación",
        orderIndex: 2,
        lessons: [
          {
            id: "lesson-1-3",
            moduleId: "mod-1-2",
            title: "2.1 Quiz Evaluado de Patrones de Diseño",
            type: "quiz",
            durationMinutes: 10,
            orderIndex: 1,
            quizQuestions: [
              {
                id: "q-1",
                question: "¿Cuál patrón de diseño soluciona el acoplamiento directo al cambiar formatos de certificados sin alterar el núcleo del sistema?",
                options: ["Singleton Pattern", "Strategy Pattern", "Decorator Pattern", "Facade Pattern"],
                correctAnswerIndex: 1
              },
              {
                id: "q-2",
                question: "¿Cuál es la función principal de la Capa de Acceso a Datos (DAL)?",
                options: ["Manejar el ruteo HTTP", "Renderizar vistas de usuario", "Interactuar directamente con la base de datos", "Definir políticas de cobro"],
                correctAnswerIndex: 2
              }
            ]
          }
        ]
      }
    ]
  },
  {
    id: "course-2",
    title: "Master en React y TypeScript Avanzado",
    description: "Aprende a construir aplicaciones frontend extremadamente rápidas, tipadas y aplicando patrones de diseño avanzados como MVC, Hooks y renderizado optimizado.",
    price: 39.99,
    category: "Web Development",
    durationHours: 20,
    instructorId: "inst-01",
    instructorName: "Ing. Luisa Arévalo",
    imageUrl: "https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80",
    rating: 4.6,
    created_at: new Date(Date.now() - 15 * 24 * 3600 * 1000).toISOString(), // 15 days ago
    isActive: true,
    reviews: [],
    modules: [
      {
        id: "mod-2-1",
        courseId: "course-2",
        title: "Módulo 1: Fundamentos de TypeScript en React",
        orderIndex: 1,
        lessons: [
          {
            id: "lesson-2-1",
            moduleId: "mod-2-1",
            title: "1.1 Configurando Vite, React y TS Config",
            type: "video",
            contentUrl: "https://www.w3schools.com/html/movie.mp4",
            durationMinutes: 15,
            orderIndex: 1
          }
        ]
      }
    ]
  }
];

const INITIAL_QUESTIONS: LessonQuestion[] = [
  {
    id: "ques-1",
    studentId: "std-01",
    studentName: "Orlando Rivas",
    questionText: "¿Por qué en una arquitectura N-Capas la capa de negocio no debería hacer referencias a librerías de UI?",
    createdAt: new Date(Date.now() - 2 * 24 * 3600 * 1000).toISOString(),
    answerText: "Para garantizar la independencia tecnológica y el reuso. Si la capa de negocio tuviese referencias a UI, no podría ser expuesta como API o utilizada en otra interfaz.",
    answeredBy: "inst-01",
    answeredAt: new Date(Date.now() - 1 * 24 * 3600 * 1000).toISOString()
  }
];

// --- SEED LOCAL STORAGE ---
const initStorage = () => {
  if (!localStorage.getItem('users')) {
    localStorage.setItem('users', JSON.stringify(INITIAL_USERS));
  }
  if (!localStorage.getItem('courses')) {
    localStorage.setItem('courses', JSON.stringify(INITIAL_COURSES));
  }
  if (!localStorage.getItem('coupons')) {
    localStorage.setItem('coupons', JSON.stringify(INITIAL_COUPONS));
  }
  if (!localStorage.getItem('certificates')) {
    localStorage.setItem('certificates', JSON.stringify([]));
  }
  if (!localStorage.getItem('audit_logs')) {
    localStorage.setItem('audit_logs', JSON.stringify([]));
  }
  if (!localStorage.getItem('lesson_questions')) {
    localStorage.setItem('lesson_questions', JSON.stringify(INITIAL_QUESTIONS));
  }
  if (!localStorage.getItem('notifications')) {
    localStorage.setItem('notifications', JSON.stringify([]));
  }
};

initStorage();

// --- DATA ACCESS LAYER (SIMULADO EN LOCAL STORAGE) ---
const getData = <T>(key: string): T => {
  return JSON.parse(localStorage.getItem(key) || '[]') as T;
};

const saveData = <T>(key: string, data: T): void => {
  localStorage.setItem(key, JSON.stringify(data));
};

// Logger de auditoría
const addAuditLog = (userId: string | undefined, userEmail: string | undefined, action: string, details: string) => {
  const logs = getData<AuditLog[]>('audit_logs');
  const newLog: AuditLog = {
    id: `log-${Date.now()}-${Math.random().toString(36).substr(2, 5)}`,
    userId,
    userEmail,
    action,
    details,
    timestamp: new Date().toISOString()
  };
  logs.unshift(newLog);
  saveData('audit_logs', logs);
};

// Disparador de Notificaciones
const sendNotification = (userId: string, text: string, type: 'email' | 'sms' | 'ui' = 'ui') => {
  const notifs = getData<NotificationItem[]>('notifications');
  const newNotif: NotificationItem = {
    id: `notif-${Date.now()}-${Math.random().toString(36).substr(2, 5)}`,
    userId,
    text,
    type,
    read: false,
    timestamp: new Date().toISOString()
  };
  notifs.unshift(newNotif);
  saveData('notifications', notifs);
  console.log(`[SISTEMA - NOTIFICACIÓN ${type.toUpperCase()}] Enviada a usuario ${userId}: "${text}"`);
};

// --- SERVICES & LOGIC (CONTROLADORES / SERVICIOS) ---
export const ApiService = {
  // 1. Autenticación y Bloqueo de Cuentas
  login: async (email: string, password: string): Promise<User> => {
    await new Promise(resolve => setTimeout(resolve, 800));

    const users = getData<any[]>('users');
    const userIndex = users.findIndex(u => u.email.toLowerCase() === email.toLowerCase());

    if (userIndex === -1) {
      addAuditLog(undefined, email, "LOGIN_FAILED", "Intento de login con correo inexistente.");
      throw new Error("Credenciales inválidas. Por favor verifique.");
    }

    const user = users[userIndex];

    if (!user.isActive) {
      addAuditLog(user.id, user.email, "LOGIN_BLOCKED", "Intento de acceso en cuenta deshabilitada por administración.");
      throw new Error("Esta cuenta ha sido deshabilitada temporalmente por el Administrador.");
    }

    // Verificar bloqueo temporal
    if (user.blockedUntil) {
      const blockTime = new Date(user.blockedUntil).getTime();
      const now = new Date().getTime();
      if (now < blockTime) {
        const remainingSec = Math.round((blockTime - now) / 1000);
        addAuditLog(user.id, user.email, "LOGIN_BLOCKED", `Intento de acceso en cuenta bloqueada. Quedan ${remainingSec}s.`);
        throw new Error(`Esta cuenta se encuentra temporalmente bloqueada debido a múltiples intentos fallidos. Intente de nuevo en ${remainingSec} segundos.`);
      } else {
        user.blockedUntil = null;
        user.failedAttempts = 0;
      }
    }

    let isCorrect = false;
    if (user.email === 'orlando@mail.com' && password === 'orlando123') isCorrect = true;
    if (user.email === 'luisa.arevalo@uca.edu.sv' && password === 'luisa123') isCorrect = true;
    if (user.email === 'admin@uca.edu.sv' && password === 'admin123') isCorrect = true;
    // Cuentas creadas dinámicamente usan su contraseña ingresada
    if (user.password && user.password === password) isCorrect = true;

    if (!isCorrect) {
      user.failedAttempts += 1;
      addAuditLog(user.id, user.email, "LOGIN_FAILED", `Contraseña incorrecta. Intento fallido #${user.failedAttempts}`);

      if (user.failedAttempts >= 5) {
        const blockDurationMs = 30000;
        user.blockedUntil = new Date(Date.now() + blockDurationMs).toISOString();
        saveData('users', users);
        addAuditLog(user.id, user.email, "ACCOUNT_LOCKED", "Cuenta bloqueada temporalmente por el Sistema tras 5 intentos fallidos.");
        
        // Notificación de seguridad (Simulada por Email y SMS)
        sendNotification(user.id, "Tu cuenta ha sido bloqueada por seguridad tras 5 intentos fallidos.", 'email');
        sendNotification(user.id, "EducaNet: Cuenta bloqueada temporalmente.", 'sms');
        
        throw new Error("Has excedido el número de intentos permitidos. Tu cuenta ha sido bloqueada temporalmente por 30 segundos.");
      }

      saveData('users', users);
      throw new Error(`Credenciales inválidas. Te quedan ${5 - user.failedAttempts} intentos.`);
    }

    user.failedAttempts = 0;
    user.blockedUntil = null;
    saveData('users', users);

    addAuditLog(user.id, user.email, "LOGIN_SUCCESS", "Inicio de sesión exitoso.");
    return user;
  },

  // 1B. Google Login Simulator
  loginWithGoogle: async (email: string, name: string): Promise<User> => {
    await new Promise(resolve => setTimeout(resolve, 1000));

    const users = getData<any[]>('users');
    let user = users.find(u => u.email.toLowerCase() === email.toLowerCase());

    if (!user) {
      // Registro automático al ingresar con Google
      user = {
        id: `google-${Date.now()}`,
        name: name,
        email: email,
        role: "student",
        failedAttempts: 0,
        blockedUntil: null,
        createdAt: new Date().toISOString(),
        enrolledCourses: [],
        completedLessons: [],
        wishlist: [],
        certificates: [],
        lessonHistory: [],
        bio: "Estudiante registrado vía Google.",
        isActive: true,
        avatarUrl: `https://api.dicebear.com/7.x/adventurer/svg?seed=${encodeURIComponent(name)}`
      };
      users.push(user);
      saveData('users', users);
      
      addAuditLog(user.id, user.email, "GOOGLE_REGISTER", "Usuario registrado exitosamente a través de Google.");
      sendNotification(user.id, "¡Bienvenido a EducaNet! Te has registrado con tu cuenta de Google.", 'ui');
      sendNotification(user.id, "Bienvenido a EducaNet. Empieza a aprender ya.", 'email');
    } else {
      if (!user.isActive) {
        throw new Error("Esta cuenta vinculada a Google ha sido deshabilitada por el Administrador.");
      }
      addAuditLog(user.id, user.email, "GOOGLE_LOGIN", "Inicio de sesión con cuenta de Google.");
    }

    return user;
  },

  // 2. Gestión de Usuarios (Admin: CRUD de Cuentas, Habilitar/Deshabilitar, Asignar Roles)
  getUsers: (): User[] => {
    return getData<User[]>('users');
  },

  createUserByAdmin: async (adminId: string, userData: any): Promise<User> => {
    const users = getData<any[]>('users');
    const emailExists = users.some(u => u.email.toLowerCase() === userData.email.toLowerCase());
    if (emailExists) throw new Error("El correo electrónico ya está registrado.");

    const newUser: any = {
      id: `usr-${Date.now()}`,
      name: userData.name,
      email: userData.email,
      role: userData.role,
      password: userData.password || "12345", // contraseña default si no la define
      failedAttempts: 0,
      blockedUntil: null,
      createdAt: new Date().toISOString(),
      isActive: true,
      avatarUrl: `https://api.dicebear.com/7.x/adventurer/svg?seed=${encodeURIComponent(userData.name)}`
    };

    if (userData.role === 'student') {
      newUser.enrolledCourses = [];
      newUser.completedLessons = [];
      newUser.wishlist = [];
      newUser.certificates = [];
      newUser.lessonHistory = [];
      newUser.bio = "Usuario común registrado.";
    } else if (userData.role === 'instructor') {
      newUser.bio = "Instructor acreditado.";
      newUser.revenue = 0;
      newUser.commissionRate = 0.05;
    }

    users.push(newUser);
    saveData('users', users);

    const admin = users.find(u => u.id === adminId);
    addAuditLog(adminId, admin?.email, "USER_CREATED_BY_ADMIN", `Usuario creado por Admin: "${newUser.name}" (Rol: ${newUser.role})`);
    
    // Notificación al nuevo usuario
    sendNotification(newUser.id, `Tu cuenta de EducaNet ha sido creada por administración. Rol asignado: ${newUser.role}`, 'email');
    return newUser;
  },

  updateUserByAdmin: async (adminId: string, userId: string, userData: any): Promise<User> => {
    const users = getData<any[]>('users');
    const idx = users.findIndex(u => u.id === userId);
    if (idx === -1) throw new Error("Usuario no encontrado.");

    const oldRole = users[idx].role;
    users[idx] = {
      ...users[idx],
      name: userData.name,
      email: userData.email,
      role: userData.role,
      isActive: userData.isActive !== undefined ? userData.isActive : users[idx].isActive
    };

    if (userData.password) {
      users[idx].password = userData.password;
    }

    saveData('users', users);

    const admin = users.find(u => u.id === adminId);
    addAuditLog(adminId, admin?.email, "USER_UPDATED_BY_ADMIN", `Usuario modificado por Admin: "${users[idx].name}". Rol anterior: ${oldRole} -> Rol nuevo: ${users[idx].role}`);
    
    // Notificar al usuario afectado
    if (oldRole !== userData.role) {
      sendNotification(userId, `Tu rol ha sido cambiado a: ${userData.role} por el administrador.`, 'ui');
    }
    return users[idx];
  },

  deleteUserByAdmin: async (adminId: string, userId: string): Promise<void> => {
    if (adminId === userId) throw new Error("No puedes eliminar tu propia cuenta de administrador.");
    
    const users = getData<any[]>('users');
    const user = users.find(u => u.id === userId);
    if (!user) throw new Error("Usuario no encontrado.");

    const filtered = users.filter(u => u.id !== userId);
    saveData('users', filtered);

    const admin = users.find(u => u.id === adminId);
    addAuditLog(adminId, admin?.email, "USER_DELETED_BY_ADMIN", `Usuario eliminado permanentemente por Admin: "${user.name}" (${user.email})`);
    
    // Simular que el sistema notifica el cierre por email
    console.log(`[SISTEMA - NOTIFICACIÓN EMAIL] Enviada a ${user.email}: "Tu cuenta de EducaNet ha sido eliminada por administración."`);
  },

  // 2B. Borrar cuenta propia (Estudiante / Usuario Común)
  deleteOwnAccount: async (userId: string): Promise<void> => {
    const users = getData<any[]>('users');
    const user = users.find(u => u.id === userId);
    if (!user) throw new Error("Usuario no encontrado.");

    const filtered = users.filter(u => u.id !== userId);
    saveData('users', filtered);

    addAuditLog(userId, user.email, "ACCOUNT_SELF_DELETED", "El usuario eliminó su propia cuenta de perfil de forma definitiva.");
  },

  // 3. Gestión de Cursos (Activar/Desactivar, Editar y Borrar)
  getCourses: async (search?: string, category?: string, maxPrice?: number): Promise<Course[]> => {
    const courses = getData<Course[]>('courses');
    return courses.filter(course => {
      const matchSearch = search ? (course.title.toLowerCase().includes(search.toLowerCase()) || course.description.toLowerCase().includes(search.toLowerCase())) : true;
      const matchCategory = category ? course.category === category : true;
      const matchPrice = maxPrice !== undefined ? course.price <= maxPrice : true;
      return matchSearch && matchCategory && matchPrice;
    });
  },

  getCourseById: async (id: string): Promise<Course> => {
    const courses = getData<Course[]>('courses');
    const course = courses.find(c => c.id === id);
    if (!course) throw new Error("Curso no encontrado.");
    return course;
  },

  createCourse: async (courseData: Partial<Course>, instructorId: string): Promise<Course> => {
    const courses = getData<Course[]>('courses');
    const users = getData<User[]>('users');
    const instructor = users.find(u => u.id === instructorId);

    const newCourse: Course = {
      id: `course-${Date.now()}`,
      title: courseData.title || "Curso sin Título",
      description: courseData.description || "",
      price: courseData.price || 0.00,
      category: courseData.category || "General",
      durationHours: courseData.durationHours || 1,
      instructorId,
      instructorName: instructor ? instructor.name : "Instructor",
      imageUrl: courseData.imageUrl || "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=600&q=80",
      rating: 5.0,
      created_at: new Date().toISOString(),
      isActive: true,
      reviews: [],
      modules: courseData.modules || []
    };

    courses.push(newCourse);
    saveData('courses', courses);

    addAuditLog(instructorId, instructor?.email, "COURSE_CREATED", `Curso creado: "${newCourse.title}"`);
    return newCourse;
  },

  updateCourse: async (courseId: string, courseData: Partial<Course>, userId: string): Promise<Course> => {
    const courses = getData<Course[]>('courses');
    const idx = courses.findIndex(c => c.id === courseId);
    if (idx === -1) throw new Error("Curso no encontrado.");

    const user = getData<User[]>('users').find(u => u.id === userId);
    if (user?.role !== 'admin' && courses[idx].instructorId !== userId) {
      throw new Error("No tienes permisos para modificar este curso.");
    }

    courses[idx] = {
      ...courses[idx],
      title: courseData.title || courses[idx].title,
      description: courseData.description || courses[idx].description,
      price: courseData.price !== undefined ? courseData.price : courses[idx].price,
      category: courseData.category || courses[idx].category,
      imageUrl: courseData.imageUrl || courses[idx].imageUrl,
      isActive: courseData.isActive !== undefined ? courseData.isActive : courses[idx].isActive
    };

    saveData('courses', courses);
    addAuditLog(userId, user?.email, "COURSE_UPDATED", `Curso "${courses[idx].title}" modificado.`);
    return courses[idx];
  },

  deleteCourse: async (courseId: string, userId: string): Promise<void> => {
    const courses = getData<Course[]>('courses');
    const course = courses.find(c => c.id === courseId);
    if (!course) throw new Error("Curso no encontrado.");

    const user = getData<User[]>('users').find(u => u.id === userId);
    if (user?.role !== 'admin' && course.instructorId !== userId) {
      throw new Error("No tienes permisos para borrar este curso.");
    }

    const filtered = courses.filter(c => c.id !== courseId);
    saveData('courses', filtered);

    addAuditLog(userId, user?.email, "COURSE_DELETED", `Curso "${course.title}" eliminado.`);
  },

  // 4. Gestión de Cupones (Instructor & Admin: CRUD de Cupones)
  getCoupons: (): Coupon[] => {
    return getData<Coupon[]>('coupons');
  },

  createCoupon: (couponData: Partial<Coupon>, userId: string): Coupon => {
    const coupons = getData<Coupon[]>('coupons');
    const codeExists = coupons.some(c => c.code.toUpperCase() === couponData.code?.toUpperCase());
    if (codeExists) throw new Error("Ya existe un cupón registrado con ese código.");

    const newCoupon: Coupon = {
      id: `coup-${Date.now()}`,
      code: couponData.code?.toUpperCase() || "CUPON",
      discountPercentage: couponData.discountPercentage || 10,
      expirationDate: couponData.expirationDate || new Date(Date.now() + 30 * 24 * 3600 * 1000).toISOString(),
      maxUses: couponData.maxUses || 5,
      usedCount: 0,
      isActive: true
    };

    coupons.push(newCoupon);
    saveData('coupons', coupons);

    const user = getData<User[]>('users').find(u => u.id === userId);
    addAuditLog(userId, user?.email, "COUPON_CREATED", `Cupón de descuento creado: "${newCoupon.code}" (${newCoupon.discountPercentage}% desc.)`);
    return newCoupon;
  },

  deleteCoupon: (id: string, userId: string): void => {
    const coupons = getData<Coupon[]>('coupons');
    const coupon = coupons.find(c => c.id === id);
    if (!coupon) throw new Error("Cupón no encontrado.");

    const filtered = coupons.filter(c => c.id !== id);
    saveData('coupons', filtered);

    const user = getData<User[]>('users').find(u => u.id === userId);
    addAuditLog(userId, user?.email, "COUPON_DELETED", `Cupón de descuento eliminado: "${coupon.code}"`);
  },

  // 5. Compra y Darse de Baja (Desmatricular)
  applyCoupon: async (code: string): Promise<Coupon> => {
    const coupons = getData<Coupon[]>('coupons');
    const coupon = coupons.find(c => c.code.toUpperCase() === code.toUpperCase() && c.isActive);

    if (!coupon) throw new Error("El cupón no es válido o ha expirado.");
    if (new Date(coupon.expirationDate).getTime() < Date.now()) {
      throw new Error("El cupón ha expirado.");
    }
    if (coupon.usedCount >= coupon.maxUses) {
      throw new Error("El cupón ha alcanzado el límite máximo de usos.");
    }

    return coupon;
  },

  purchaseCourse: async (studentId: string, courseId: string, couponCode?: string): Promise<StudentProfile> => {
    await new Promise(resolve => setTimeout(resolve, 1500));

    const users = getData<any[]>('users');
    const courses = getData<Course[]>('courses');
    const coupons = getData<Coupon[]>('coupons');

    const userIndex = users.findIndex(u => u.id === studentId);
    const course = courses.find(c => c.id === courseId);

    if (userIndex === -1 || !course) throw new Error("Usuario o Curso no encontrado.");
    const user = users[userIndex] as StudentProfile;

    if (user.enrolledCourses.includes(courseId)) {
      throw new Error("Ya estás inscrito en este curso.");
    }

    let finalPrice = course.price;
    if (couponCode) {
      const couponIndex = coupons.findIndex(c => c.code.toUpperCase() === couponCode.toUpperCase() && c.isActive);
      if (couponIndex !== -1) {
        const coupon = coupons[couponIndex];
        finalPrice = Math.max(0, finalPrice - (finalPrice * (coupon.discountPercentage / 100)));
        coupon.usedCount += 1;
        saveData('coupons', coupons);
      }
    }

    // Inscribir curso
    user.enrolledCourses.push(courseId);
    saveData('users', users);

    // Sumar ganancias al Instructor
    const instructorIndex = users.findIndex(u => u.id === course.instructorId);
    if (instructorIndex !== -1) {
      const instructor = users[instructorIndex] as InstructorProfile;
      const earnings = finalPrice * (1 - (instructor.commissionRate || 0.05));
      instructor.revenue = Number((instructor.revenue + earnings).toFixed(2));
      saveData('users', users);
    }

    addAuditLog(studentId, user.email, "COURSE_PURCHASED", `Acceso concedido al curso "${course.title}". Total pagado: $${finalPrice.toFixed(2)}. Transacción Stripe: ch_${Date.now()}`);
    
    // Notificación del sistema (UI, SMS, Email)
    sendNotification(studentId, `¡Inscripción exitosa a "${course.title}"! Pago de $${finalPrice.toFixed(2)} verificado.`, 'ui');
    sendNotification(studentId, `Tu pago por el curso "${course.title}" ha sido verificado.`, 'email');
    sendNotification(studentId, `EducaNet: Inscripción completada.`, 'sms');

    return user;
  },

  unenrollCourse: async (studentId: string, courseId: string): Promise<StudentProfile> => {
    const users = getData<any[]>('users');
    const userIndex = users.findIndex(u => u.id === studentId);
    const courses = getData<Course[]>('courses');
    const course = courses.find(c => c.id === courseId);

    if (userIndex === -1 || !course) throw new Error("Usuario o Curso no encontrado.");
    const user = users[userIndex] as StudentProfile;

    // Remover curso de la inscripción
    user.enrolledCourses = user.enrolledCourses.filter(id => id !== courseId);
    
    // Remover lecciones completadas pertenecientes a este curso
    const courseLessonsIds = course.modules.flatMap(m => m.lessons.map(l => l.id));
    user.completedLessons = user.completedLessons.filter(id => !courseLessonsIds.includes(id));
    
    // Remover certificados asociados a este curso
    const certificates = getData<Certificate[]>('certificates');
    const remainingCerts = certificates.filter(c => !(c.studentId === studentId && c.courseId === courseId));
    saveData('certificates', remainingCerts);
    
    user.certificates = user.certificates.filter(certId => remainingCerts.some(c => c.id === certId));

    saveData('users', users);

    addAuditLog(studentId, user.email, "COURSE_UNENROLLED", `El usuario se dio de baja del curso: "${course.title}" (se revoca el acceso).`);
    sendNotification(studentId, `Te has dado de baja del curso: "${course.title}".`, 'ui');
    sendNotification(studentId, `Baja confirmada del curso "${course.title}". Se revoca acceso.`, 'email');

    return user;
  },

  // 6. Historial de Navegación y Progreso
  recordLessonAccess: (studentId: string, courseId: string, lessonId: string, lessonTitle: string, courseTitle: string) => {
    const users = getData<any[]>('users');
    const userIdx = users.findIndex(u => u.id === studentId);
    if (userIdx === -1) return;

    const user = users[userIdx];
    if (!user.lessonHistory) user.lessonHistory = [];

    // Remover si ya existía para mandarlo al tope
    user.lessonHistory = user.lessonHistory.filter((h: any) => h.lessonId !== lessonId);
    
    // Agregar al inicio
    user.lessonHistory.unshift({
      courseId,
      courseTitle,
      lessonId,
      lessonTitle,
      accessedAt: new Date().toISOString()
    });

    // Limitar historial a los últimos 5
    if (user.lessonHistory.length > 5) {
      user.lessonHistory = user.lessonHistory.slice(0, 5);
    }

    saveData('users', users);
  },

  updateProgress: async (studentId: string, courseId: string, lessonId: string, isCompleted: boolean): Promise<{ completedLessons: string[]; progressPercent: number; certificateGenerated?: Certificate }> => {
    const users = getData<any[]>('users');
    const courses = getData<Course[]>('courses');

    const userIndex = users.findIndex(u => u.id === studentId);
    const course = courses.find(c => c.id === courseId);

    if (userIndex === -1 || !course) throw new Error("Usuario o Curso no encontrado.");
    const user = users[userIndex] as StudentProfile;

    if (isCompleted) {
      if (!user.completedLessons.includes(lessonId)) {
        user.completedLessons.push(lessonId);
      }
    } else {
      user.completedLessons = user.completedLessons.filter(id => id !== lessonId);
    }

    const courseLessonsIds = course.modules.flatMap(m => m.lessons.map(l => l.id));
    const completedCourseLessons = courseLessonsIds.filter(id => user.completedLessons.includes(id));
    const progressPercent = Math.round((completedCourseLessons.length / courseLessonsIds.length) * 100);

    let certificateGenerated: Certificate | undefined;

    if (progressPercent === 100) {
      const certificates = getData<Certificate[]>('certificates');
      const hasCertificate = certificates.some(cert => cert.studentId === studentId && cert.courseId === courseId);

      if (!hasCertificate) {
        const format: CertificateFormat = 'Image';
        const uniqueHash = `cert-${Date.now()}-${Math.random().toString(36).substr(2, 9).toUpperCase()}`;
        const issueDate = new Date().toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' });

        const generator = CertificateGeneratorFactory.getGenerator(format);
        const certUrl = generator.generate({
          studentName: user.name,
          courseTitle: course.title,
          issuedAt: issueDate,
          verificationHash: uniqueHash
        });

        const newCert: Certificate = {
          id: uniqueHash,
          studentId,
          studentName: user.name,
          courseId,
          courseTitle: course.title,
          issuedAt: new Date().toISOString(),
          formatType: format,
          certificateUrl: certUrl,
          verificationHash: uniqueHash
        };

        certificates.push(newCert);
        saveData('certificates', certificates);

        user.certificates.push(newCert.id);
        
        addAuditLog(studentId, user.email, "CERTIFICATE_GENERATED", `Diploma generado automáticamente para "${course.title}".`);
        
        // Notificaciones del Sistema
        sendNotification(studentId, `¡Felicidades! Has completado "${course.title}" y tu certificado digital ya está disponible para descarga.`, 'ui');
        sendNotification(studentId, `Certificado de EducaNet emitido para "${course.title}".`, 'email');
        
        certificateGenerated = newCert;
      }
    }

    saveData('users', users);

    return {
      completedLessons: user.completedLessons,
      progressPercent,
      certificateGenerated
    };
  },

  generateCertificateManual: async (studentId: string, courseId: string, format: CertificateFormat): Promise<Certificate> => {
    const users = getData<any[]>('users');
    const courses = getData<Course[]>('courses');
    const certificates = getData<Certificate[]>('certificates');

    const user = users.find(u => u.id === studentId) as StudentProfile;
    const course = courses.find(c => c.id === courseId);

    if (!user || !course) throw new Error("Datos incorrectos.");

    const uniqueHash = `cert-${Date.now()}-${Math.random().toString(36).substr(2, 9).toUpperCase()}`;
    const issueDate = new Date().toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' });

    const generator = CertificateGeneratorFactory.getGenerator(format);
    const certUrl = generator.generate({
      studentName: user.name,
      courseTitle: course.title,
      issuedAt: issueDate,
      verificationHash: uniqueHash
    });

    const newCert: Certificate = {
      id: uniqueHash,
      studentId,
      studentName: user.name,
      courseId,
      courseTitle: course.title,
      issuedAt: new Date().toISOString(),
      formatType: format,
      certificateUrl: certUrl,
      verificationHash: uniqueHash
    };

    certificates.push(newCert);
    saveData('certificates', certificates);

    if (!user.certificates.includes(newCert.id)) {
      user.certificates.push(newCert.id);
      saveData('users', users);
    }

    addAuditLog(studentId, user.email, "CERTIFICATE_REGENERATED", `Certificado regenerado en formato ${format} para "${course.title}".`);
    return newCert;
  },

  // 7. Preguntas y Respuestas (Q&A en Lecciones)
  getQuestionsForLesson: (lessonId: string): LessonQuestion[] => {
    const questions = getData<LessonQuestion[]>('lesson_questions');
    return questions.filter(q => q.id.startsWith(`q-${lessonId}`) || q.id === `ques-1`); // ques-1 es global mock para probar
  },

  addQuestion: (studentId: string, studentName: string, lessonId: string, text: string): LessonQuestion => {
    const questions = getData<LessonQuestion[]>('lesson_questions');
    const newQ: LessonQuestion = {
      id: `q-${lessonId}-${Date.now()}`,
      studentId,
      studentName,
      questionText: text,
      createdAt: new Date().toISOString()
    };
    questions.push(newQ);
    saveData('lesson_questions', questions);

    const user = getData<User[]>('users').find(u => u.id === studentId);
    addAuditLog(studentId, user?.email, "QUESTION_ASKED", `El usuario común hizo una pregunta en la lección: "${text.substring(0, 30)}..."`);
    return newQ;
  },

  answerQuestion: (instructorId: string, instructorName: string, questionId: string, answerText: string): void => {
    const questions = getData<LessonQuestion[]>('lesson_questions');
    const idx = questions.findIndex(q => q.id === questionId);
    if (idx === -1) throw new Error("Pregunta no encontrada.");

    questions[idx].answerText = answerText;
    questions[idx].answeredBy = instructorId;
    questions[idx].answeredAt = new Date().toISOString();
    saveData('lesson_questions', questions);

    const user = getData<User[]>('users').find(u => u.id === instructorId);
    addAuditLog(instructorId, user?.email, "QUESTION_ANSWERED", `El instructor respondió a la pregunta con ID: ${questionId}`);
    
    // Notificación en la UI del estudiante que preguntó
    sendNotification(questions[idx].studentId, `El Instructor ${instructorName} ha respondido a tu pregunta en clase.`, 'ui');
    sendNotification(questions[idx].studentId, `Tu pregunta en EducaNet ha sido respondida.`, 'email');
  },

  // 8. Reseñas y Calificación
  addReview: (studentId: string, studentName: string, courseId: string, rating: number, comment: string): Course => {
    const courses = getData<Course[]>('courses');
    const idx = courses.findIndex(c => c.id === courseId);
    if (idx === -1) throw new Error("Curso no encontrado.");

    const reviews = courses[idx].reviews || [];
    
    // Evitar reseñas duplicadas del mismo alumno
    const exists = reviews.some(r => r.studentId === studentId);
    if (exists) throw new Error("Ya has calificado este curso previamente.");

    const newRev: Review = {
      id: `rev-${Date.now()}`,
      studentId,
      studentName,
      rating,
      comment,
      createdAt: new Date().toISOString()
    };

    reviews.push(newRev);
    courses[idx].reviews = reviews;
    
    // Recalcular promedio
    const totalRating = reviews.reduce((sum, r) => sum + r.rating, 0);
    courses[idx].rating = Number((totalRating / reviews.length).toFixed(1));

    saveData('courses', courses);

    const student = getData<User[]>('users').find(u => u.id === studentId);
    addAuditLog(studentId, student?.email, "REVIEW_SUBMITTED", `El estudiante dejó una reseña del curso "${courses[idx].title}" con ${rating} estrellas.`);
    
    // Notificar al instructor del curso
    sendNotification(courses[idx].instructorId, `El estudiante ${studentName} ha dejado una reseña de ${rating} estrellas en tu curso "${courses[idx].title}".`, 'ui');

    return courses[idx];
  },

  // 9. Reportes Financieros y de Auditoría (Admin Dashboard)
  getFinancialSummary: () => {
    const courses = getData<Course[]>('courses');
    const users = getData<any[]>('users');
    const certificates = getData<Certificate[]>('certificates');
    const logs = getData<AuditLog[]>('audit_logs');
    
    // Filtrar compras en el log de auditoría
    const purchaseLogs = logs.filter(l => l.action === "COURSE_PURCHASED");
    
    let totalSales = 0;
    let totalAdminCommissions = 0;
    
    const transactions = purchaseLogs.map(l => {
      // Intentar extraer el precio cobrado del mensaje de auditoría
      // "Acceso concedido al curso X. Total pagado: $Y. Transacción..."
      const priceRegex = /\$(\d+\.\d+)/;
      const match = l.details.match(priceRegex);
      const paidAmount = match ? parseFloat(match[1]) : 0;
      
      totalSales += paidAmount;
      const commission = paidAmount * 0.05;
      totalAdminCommissions += commission;
      
      return {
        id: l.id,
        timestamp: l.timestamp,
        userEmail: l.userEmail,
        courseTitle: l.details.includes('"') ? l.details.split('"')[1] : "Curso",
        amountPaid: paidAmount,
        commissionCollected: commission,
        stripeChargeId: l.details.split('Stripe: ')[1] || 'simulada'
      };
    });

    const instructorsCount = users.filter(u => u.role === 'instructor').length;
    const studentsCount = users.filter(u => u.role === 'student').length;

    return {
      totalSales: Number(totalSales.toFixed(2)),
      totalAdminCommissions: Number(totalAdminCommissions.toFixed(2)),
      instructorsCount,
      studentsCount,
      coursesCount: courses.length,
      certificatesCount: certificates.length,
      transactions
    };
  },

  // 10. Notificaciones
  getNotifications: (userId: string): NotificationItem[] => {
    const notifs = getData<NotificationItem[]>('notifications');
    return notifs.filter(n => n.userId === userId);
  },

  markNotificationsAsRead: (userId: string) => {
    const notifs = getData<NotificationItem[]>('notifications');
    notifs.forEach(n => {
      if (n.userId === userId) n.read = true;
    });
    saveData('notifications', notifs);
  }
};
