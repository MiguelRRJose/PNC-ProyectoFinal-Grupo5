import React, { useState, useEffect } from 'react';
import { LoginView } from './views/LoginView';
import { DashboardView } from './views/DashboardView';
import { CourseDetailView } from './views/CourseDetailView';
import { Navbar } from './components/Navbar';
import { User } from './models/User';

export const App: React.FC = () => {
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [selectedCourseId, setSelectedCourseId] = useState<string | null>(null);
  const [isInitializing, setIsInitializing] = useState(true);

  // Intentar cargar la sesión guardada desde localStorage al arrancar
  useEffect(() => {
    const savedSession = localStorage.getItem('current_user_session');
    if (savedSession) {
      try {
        const user = JSON.parse(savedSession) as User;
        setCurrentUser(user);
      } catch (err) {
        console.error("Error cargando sesión previa:", err);
        localStorage.removeItem('current_user_session');
      }
    }
    setIsInitializing(false);
  }, []);

  const handleLoginSuccess = (user: User) => {
    setCurrentUser(user);
    localStorage.setItem('current_user_session', JSON.stringify(user));
  };

  const handleLogout = () => {
    // Registrar cierre de sesión en auditoría
    if (currentUser) {
      const allLogs = JSON.parse(localStorage.getItem('audit_logs') || '[]');
      const newLog = {
        id: `log-${Date.now()}`,
        userId: currentUser.id,
        userEmail: currentUser.email,
        action: "LOGOUT_SUCCESS",
        details: "El usuario cerró sesión voluntariamente.",
        timestamp: new Date().toISOString()
      };
      allLogs.unshift(newLog);
      localStorage.setItem('audit_logs', JSON.stringify(allLogs));
    }

    setCurrentUser(null);
    setSelectedCourseId(null);
    localStorage.removeItem('current_user_session');
  };

  const handleUpdateUser = (updatedUser: User) => {
    setCurrentUser(updatedUser);
    localStorage.setItem('current_user_session', JSON.stringify(updatedUser));
    
    // Sincronizar en la lista global de usuarios en localStorage
    const users: User[] = JSON.parse(localStorage.getItem('users') || '[]');
    const idx = users.findIndex(u => u.id === updatedUser.id);
    if (idx !== -1) {
      users[idx] = updatedUser;
      localStorage.setItem('users', JSON.stringify(users));
    }
  };

  if (isInitializing) {
    return (
      <div className="app-loader">
        <div className="loader"></div>
        <p>Iniciando EducaNet...</p>
        <style>{`
          .app-loader {
            height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 16px;
            background-color: #f8fafc;
            color: #475569;
            font-family: 'Inter', sans-serif;
          }
          .loader {
            width: 36px;
            height: 36px;
            border: 3px solid #e2e8f0;
            border-top-color: #3b52ef;
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
          }
          @keyframes spin {
            to { transform: rotate(360deg); }
          }
        `}</style>
      </div>
    );
  }

  // Enrutador dinámico limpio basado en estado (Arquitectura Modular)
  if (!currentUser) {
    return <LoginView onLoginSuccess={handleLoginSuccess} />;
  }
  if (selectedCourseId) {
    return (
      <div className="app-content-wrapper">
        <Navbar currentUser={currentUser} onLogout={handleLogout} />
        <div style={{ padding: '32px', maxWidth: '1400px', margin: '0 auto', width: '100%' }}>
          <CourseDetailView
            courseId={selectedCourseId}
            currentUser={currentUser}
            onUpdateUser={handleUpdateUser}
            onBack={() => setSelectedCourseId(null)}
          />
        </div>
      </div>
    );
  }

  return (
    <DashboardView
      currentUser={currentUser}
      onLogout={handleLogout}
      onUpdateUser={handleUpdateUser}
      onSelectCourse={setSelectedCourseId}
    />
  );
};

export default App;
