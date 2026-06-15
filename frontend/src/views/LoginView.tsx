import React, { useState } from 'react';
import { Mail, Lock, BookOpen } from 'lucide-react';
import { useLoginController } from '../controllers/useLoginController';
import { Input } from '../components/Input';
import { Button } from '../components/Button';
import { User } from '../models/User';

interface LoginViewProps {
  onLoginSuccess: (user: User) => void;
}

export const LoginView: React.FC<LoginViewProps> = ({ onLoginSuccess }) => {
  const {
    email,
    setEmail,
    password,
    setPassword,
    errors,
    isLoading,
    handleLogin,
    showGoogleModal,
    setShowGoogleModal,
    handleGoogleLogin
  } = useLoginController({ onLoginSuccess });

  // Custom typing for Google simulator
  const [customGoogleEmail, setCustomGoogleEmail] = useState('');
  const [customGoogleName, setCustomGoogleName] = useState('');

  return (
    <div className="login-page">
      <div className="login-card-container">
        <div className="login-header">
          <div className="logo-badge">
            <BookOpen size={28} color="#ffffff" />
          </div>
          <h1>EducaNet</h1>
          <p className="login-subtitle">Programación N-Capas UCA</p>
        </div>

        <form onSubmit={handleLogin} className="login-form">
          {errors.general && (
            <div className="alert alert-error">
              <span>{errors.general}</span>
            </div>
          )}

          <Input
            label="Correo Electrónico"
            type="email"
            placeholder="ejemplo@mail.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            error={errors.email}
            icon={<Mail size={18} />}
          />

          <Input
            label="Contraseña"
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={errors.password}
            icon={<Lock size={18} />}
          />

          <Button type="submit" isLoading={isLoading} className="login-btn">
            Iniciar Sesión
          </Button>

          {/* Separador para Google */}
          <div className="divider-container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '12px 0', gap: '10px' }}>
            <span style={{ height: '1px', flex: '1', backgroundColor: 'var(--border-color)' }}></span>
            <span style={{ fontSize: '0.75rem', color: 'var(--color-text-secondary)', fontWeight: '500' }}>O BIEN</span>
            <span style={{ height: '1px', flex: '1', backgroundColor: 'var(--border-color)' }}></span>
          </div>

          {/* Google Sign In Button */}
          <button
            type="button"
            className="google-btn"
            onClick={() => setShowGoogleModal(true)}
            style={{
              width: '100%',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '10px',
              padding: '12px',
              border: '1px solid var(--border-color)',
              borderRadius: 'var(--border-radius-md)',
              backgroundColor: '#ffffff',
              cursor: 'pointer',
              fontWeight: '600',
              color: '#475569',
              transition: 'all var(--transition-fast)'
            }}
          >
            {/* Google SVG Icon */}
            <svg width="18" height="18" viewBox="0 0 18 18">
              <path d="M17.64 9.2c0-.63-.06-1.25-.16-1.84H9v3.47h4.84c-.21 1.12-.84 2.07-1.79 2.7v2.24h2.9c1.69-1.55 2.69-3.84 2.69-6.57zm-8.64 8.8c2.43 0 4.47-.8 5.96-2.18l-2.9-2.24c-.8.54-1.84.87-3.06.87-2.35 0-4.35-1.59-5.06-3.73H.9v2.3C2.39 16.03 5.48 18 9 18z" fill="#34A853"/>
              <path d="M3.94 10.92c-.18-.54-.28-1.12-.28-1.72s.1-1.18.28-1.72V5.18H.9C.33 6.34 0 7.63 0 9s.33 2.66.9 3.82l3.04-2.3z" fill="#FBBC05"/>
              <path d="M9 3.58c1.32 0 2.5.45 3.44 1.35L15 2.4C13.46.99 11.43 0 9 0 5.48 0 2.39 1.97.9 4.98l3.04 2.3c.71-2.14 2.71-3.7 5.06-3.7z" fill="#EA4335"/>
            </svg>
            <span>Iniciar sesión con Google</span>
          </button>
        </form>

        <div className="test-accounts-section">
          <h3>Cuentas de Prueba:</h3>
          <div className="accounts-grid">
            <div className="account-item" onClick={() => { setEmail('orlando@mail.com'); setPassword('orlando123'); }}>
              <span className="badge badge-student">Estudiante</span>
              <span className="account-email">orlando@mail.com / orlando123</span>
            </div>
            <div className="account-item" onClick={() => { setEmail('luisa.arevalo@uca.edu.sv'); setPassword('luisa123'); }}>
              <span className="badge badge-instructor">Instructor</span>
              <span className="account-email">luisa.arevalo@uca.edu.sv / luisa123</span>
            </div>
            <div className="account-item" onClick={() => { setEmail('admin@uca.edu.sv'); setPassword('admin123'); }}>
              <span className="badge badge-admin">Admin</span>
              <span className="account-email">admin@uca.edu.sv / admin123</span>
            </div>
          </div>
        </div>
      </div>

      {/* POPUP SIMULADO DE SELECCIÓN DE CUENTAS DE GOOGLE (GOOGLE ACCOUNT CHOOSER) */}
      {showGoogleModal && (
        <div className="modal-overlay" onClick={() => setShowGoogleModal(false)}>
          <div className="modal-card" style={{ maxWidth: '400px', padding: '28px' }} onClick={(e) => e.stopPropagation()}>
            <div style={{ textAlign: 'center', marginBottom: '20px' }}>
              <svg width="24" height="24" viewBox="0 0 24 24" style={{ margin: '0 auto 8px auto', display: 'block' }}>
                <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
                <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
                <path d="M5.84 14.1c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.08H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.92l3.66-2.82z" fill="#FBBC05"/>
                <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.08l3.66 2.82c.87-2.6 3.3-4.52 6.16-4.52z" fill="#EA4335"/>
              </svg>
              <h3 style={{ borderBottom: 'none', paddingBottom: '0', fontSize: '1.2rem', marginBottom: '4px' }}>Inicia sesión con Google</h3>
              <p style={{ fontSize: '0.85rem', color: 'var(--color-text-secondary)' }}>para continuar en EducaNet</p>
            </div>

            <div className="google-accounts-list" style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              {/* Opción 1: Orlando pre-inscripto */}
              <button
                className="google-account-option"
                onClick={() => handleGoogleLogin('orlando@mail.com', 'Orlando Rivas')}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '12px',
                  padding: '12px',
                  border: '1px solid var(--border-color)',
                  borderRadius: '10px',
                  cursor: 'pointer',
                  width: '100%',
                  textAlign: 'left',
                  backgroundColor: '#fff'
                }}
              >
                <div style={{ width: '32px', height: '32px', borderRadius: '50%', backgroundColor: 'var(--color-primary-light)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 'bold', fontSize: '0.9rem' }}>OR</div>
                <div style={{ display: 'flex', flexDirection: 'column' }}>
                  <span style={{ fontSize: '0.85rem', fontWeight: '600' }}>Orlando Rivas</span>
                  <span style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>orlando@mail.com</span>
                </div>
              </button>

              {/* Opción 2: Nueva cuenta de Google simulada */}
              <button
                className="google-account-option"
                onClick={() => handleGoogleLogin('estudiante.google@gmail.com', 'Carlos Gómez')}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '12px',
                  padding: '12px',
                  border: '1px solid var(--border-color)',
                  borderRadius: '10px',
                  cursor: 'pointer',
                  width: '100%',
                  textAlign: 'left',
                  backgroundColor: '#fff'
                }}
              >
                <div style={{ width: '32px', height: '32px', borderRadius: '50%', backgroundColor: '#fef2f2', color: '#ef4444', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 'bold', fontSize: '0.9rem' }}>CG</div>
                <div style={{ display: 'flex', flexDirection: 'column' }}>
                  <span style={{ fontSize: '0.85rem', fontWeight: '600' }}>Carlos Gómez</span>
                  <span style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>estudiante.google@gmail.com</span>
                </div>
              </button>

              {/* Opción 3: Formulario para ingresar cualquier cuenta de Google */}
              <div style={{ borderTop: '1px solid var(--border-color)', marginTop: '12px', paddingTop: '16px' }}>
                <p style={{ fontSize: '0.8rem', fontWeight: '600', color: 'var(--color-text-secondary)', marginBottom: '8px' }}>Usar otra cuenta de Google:</p>
                <Input
                  placeholder="Nombre Completo"
                  value={customGoogleName}
                  onChange={(e) => setCustomGoogleName(e.target.value)}
                  style={{ marginBottom: '8px' }}
                />
                <Input
                  placeholder="correo@gmail.com"
                  type="email"
                  value={customGoogleEmail}
                  onChange={(e) => setCustomGoogleEmail(e.target.value)}
                />
                <Button
                  onClick={() => handleGoogleLogin(customGoogleEmail, customGoogleName)}
                  disabled={!customGoogleEmail.trim() || !customGoogleName.trim()}
                  style={{ width: '100%', marginTop: '8px', fontSize: '0.85rem' }}
                >
                  Registrarse e Iniciar Sesión con Google
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}

      <style>{`
        .login-page {
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          background: linear-gradient(135deg, #f8fafc 0%, #eff1fe 100%);
          padding: 20px;
        }
        .login-card-container {
          background-color: var(--color-bg-card);
          border-radius: var(--border-radius-lg);
          border: 1px solid var(--border-color);
          box-shadow: var(--shadow-lg), 0 20px 40px -15px rgba(59, 82, 239, 0.08);
          padding: 40px;
          width: 100%;
          max-width: 480px;
          display: flex;
          flex-direction: column;
          gap: 28px;
          animation: fadeUp 0.6s cubic-bezier(0.16, 1, 0.3, 1);
        }
        @keyframes fadeUp {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
        .login-header {
          text-align: center;
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 12px;
        }
        .logo-badge {
          background-color: var(--color-primary);
          padding: 12px;
          border-radius: var(--border-radius-lg);
          display: flex;
          align-items: center;
          justify-content: center;
          box-shadow: 0 8px 24px -6px rgba(59, 82, 239, 0.4);
        }
        .login-header h1 {
          font-size: 2rem;
          font-weight: 800;
          color: var(--color-text-primary);
          letter-spacing: -0.04em;
        }
        .login-subtitle {
          font-size: 0.9rem;
          color: var(--color-text-secondary);
          font-weight: 500;
        }
        .login-form {
          display: flex;
          flex-direction: column;
          gap: 8px;
        }
        .alert {
          padding: 12px 16px;
          border-radius: var(--border-radius-md);
          font-size: 0.85rem;
          font-weight: 500;
          line-height: 1.4;
          margin-bottom: 12px;
        }
        .alert-error {
          background-color: var(--color-error-light);
          color: var(--color-error);
          border: 1px solid rgba(239, 68, 68, 0.2);
        }
        .login-btn {
          width: 100%;
          padding: 12px;
          font-size: 1rem;
          margin-top: 10px;
        }
        .google-btn:hover {
          background-color: #f8fafc !important;
          border-color: var(--color-primary) !important;
        }
        .google-account-option:hover {
          border-color: var(--color-primary) !important;
          background-color: var(--color-primary-light) !important;
        }
        .test-accounts-section {
          border-top: 1px dashed var(--border-color);
          padding-top: 20px;
        }
        .test-accounts-section h3 {
          font-size: 0.85rem;
          font-weight: 600;
          color: var(--color-text-secondary);
          margin-bottom: 12px;
        }
        .accounts-grid {
          display: flex;
          flex-direction: column;
          gap: 8px;
        }
        .account-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 8px 12px;
          border: 1px solid var(--border-color);
          border-radius: var(--border-radius-md);
          cursor: pointer;
          transition: all var(--transition-fast);
          background-color: var(--color-bg-app);
        }
        .account-item:hover {
          border-color: var(--color-primary);
          background-color: var(--color-primary-light);
        }
        .account-email {
          font-family: monospace;
          font-size: 0.8rem;
          color: var(--color-text-secondary);
        }
        .badge {
          font-size: 0.65rem;
          font-weight: 700;
          padding: 2px 6px;
          border-radius: var(--border-radius-full);
          text-transform: uppercase;
          min-width: 75px;
          text-align: center;
        }
        .badge-student {
          background-color: var(--color-success-light);
          color: var(--color-success);
        }
        .badge-instructor {
          background-color: var(--color-primary-light);
          color: var(--color-primary);
        }
        .badge-admin {
          background-color: var(--color-error-light);
          color: var(--color-error);
        }
      `}</style>
    </div>
  );
};
