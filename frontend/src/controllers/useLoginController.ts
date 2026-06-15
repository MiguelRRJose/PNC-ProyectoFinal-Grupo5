import { useState } from 'react';
import { ApiService } from '../services/apiService';
import { User } from '../models/User';

interface UseLoginControllerProps {
  onLoginSuccess: (user: User) => void;
}

export const useLoginController = ({ onLoginSuccess }: UseLoginControllerProps) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState<{ email?: string; password?: string; general?: string }>({});
  const [isLoading, setIsLoading] = useState(false);
  const [showGoogleModal, setShowGoogleModal] = useState(false);

  const validate = () => {
    const newErrors: typeof errors = {};
    if (!email) {
      newErrors.email = 'El correo electrónico es requerido.';
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      newErrors.email = 'Introduce un formato de correo válido.';
    }

    if (!password) {
      newErrors.password = 'La contraseña es requerida.';
    } else if (password.length < 5) {
      newErrors.password = 'La contraseña debe tener al menos 5 caracteres.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors({});

    if (!validate()) return;

    setIsLoading(true);
    try {
      const user = await ApiService.login(email, password);
      onLoginSuccess(user);
    } catch (err: any) {
      setErrors({ general: err.message || 'Error al iniciar sesión.' });
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleLogin = async (googleEmail: string, googleName: string) => {
    setIsLoading(true);
    setErrors({});
    setShowGoogleModal(false);
    try {
      const user = await ApiService.loginWithGoogle(googleEmail, googleName);
      onLoginSuccess(user);
    } catch (err: any) {
      setErrors({ general: err.message || 'Error al iniciar sesión con Google.' });
    } finally {
      setIsLoading(false);
    }
  };

  return {
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
  };
};
