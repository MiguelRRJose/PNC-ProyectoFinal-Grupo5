import React from 'react';
import { BookOpen, Compass, Award, ClipboardList, PlusCircle, BarChart3, Shield, Heart, User, Users, Tag, HelpCircle, Wallet } from 'lucide-react';
import { UserRole } from '../models/User';

interface SidebarProps {
  currentTab: string;
  setCurrentTab: (tab: string) => void;
  role: UserRole;
}

export const Sidebar: React.FC<SidebarProps> = ({ currentTab, setCurrentTab, role }) => {
  const getMenuItems = () => {
    const items = [
      { id: 'explore', label: 'Explorar Cursos', icon: <Compass size={18} /> }
    ];

    if (role === 'student') {
      items.push(
        { id: 'my-courses', label: 'Mis Cursos', icon: <BookOpen size={18} /> },
        { id: 'wishlist', label: 'Lista de Deseos', icon: <Heart size={18} /> },
        { id: 'my-certificates', label: 'Certificados', icon: <Award size={18} /> }
      );
    } else if (role === 'instructor') {
      items.push(
        { id: 'manage-courses', label: 'Gestionar Cursos', icon: <PlusCircle size={18} /> },
        { id: 'instructor-coupons', label: 'Cupones Descuento', icon: <Tag size={18} /> },
        { id: 'instructor-questions', label: 'Responder Dudas', icon: <HelpCircle size={18} /> },
        { id: 'revenue', label: 'Mis Ingresos', icon: <BarChart3 size={18} /> },
        { id: 'instructor-users', label: 'Lista de Alumnos', icon: <Users size={18} /> }
      );
    } else if (role === 'admin') {
      items.push(
        { id: 'admin-moderate', label: 'Moderar Cursos', icon: <Shield size={18} /> },
        { id: 'admin-manage-users', label: 'Gestionar Cuentas', icon: <Users size={18} /> },
        { id: 'admin-coupons', label: 'Gestionar Cupones', icon: <Tag size={18} /> },
        { id: 'admin-financials', label: 'Finanzas Globales', icon: <Wallet size={18} /> },
        { id: 'audit-logs', label: 'Auditoría Logs', icon: <ClipboardList size={18} /> }
      );
    }

    // Perfil visible para todos los roles
    items.push({ id: 'profile', label: 'Mi Perfil', icon: <User size={18} /> });

    return items;
  };

  return (
    <aside className="sidebar-aside">
      <nav className="sidebar-nav">
        {getMenuItems().map(item => (
          <button
            key={item.id}
            className={`nav-item-btn ${currentTab === item.id ? 'nav-item-active' : ''}`}
            onClick={() => setCurrentTab(item.id)}
          >
            <span className="nav-item-icon">{item.icon}</span>
            <span className="nav-item-label">{item.label}</span>
          </button>
        ))}
      </nav>

      <div className="sidebar-footer">
        <p className="footer-text">Programación N-Capas</p>
        <p className="footer-subtext">UCA - Proyecto 1</p>
      </div>

      <style>{`
        .sidebar-aside {
          width: 250px;
          background-color: var(--color-bg-aside);
          border-right: 1px solid var(--border-color);
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          padding: 24px 16px;
          min-height: calc(100vh - 70px);
          position: sticky;
          top: 70px;
        }
        .sidebar-nav {
          display: flex;
          flex-direction: column;
          gap: 6px;
        }
        .nav-item-btn {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 12px 16px;
          border-radius: var(--border-radius-md);
          color: var(--color-text-secondary);
          font-weight: 500;
          font-size: 0.95rem;
          cursor: pointer;
          transition: all var(--transition-fast);
          text-align: left;
        }
        .nav-item-btn:hover {
          color: var(--color-primary);
          background-color: var(--color-primary-light);
        }
        .nav-item-active {
          color: var(--color-primary) !important;
          background-color: var(--color-primary-light) !important;
          font-weight: 600;
        }
        .nav-item-icon {
          display: flex;
          align-items: center;
        }
        .sidebar-footer {
          border-top: 1px solid var(--border-color);
          padding-top: 16px;
          text-align: center;
        }
        .footer-text {
          font-size: 0.8rem;
          font-weight: 600;
          color: var(--color-text-secondary);
        }
        .footer-subtext {
          font-size: 0.7rem;
          color: var(--color-text-muted);
        }
      `}</style>
    </aside>
  );
};
