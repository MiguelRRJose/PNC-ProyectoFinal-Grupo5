import React, { useState, useEffect } from 'react';
import { User, LogOut, BookOpen, Bell, Mail, Smartphone } from 'lucide-react';
import { User as UserType } from '../models/User';
import { ApiService, NotificationItem } from '../services/apiService';

interface NavbarProps {
  currentUser: UserType | null;
  onLogout: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ currentUser, onLogout }) => {
  const [notifications, setNotifications] = useState<NotificationItem[]>([]);
  const [showNotifications, setShowNotifications] = useState(false);

  const loadNotifications = () => {
    if (currentUser) {
      const data = ApiService.getNotifications(currentUser.id);
      setNotifications(data);
    }
  };

  useEffect(() => {
    loadNotifications();
    // Poll every 3 seconds for new notifications dynamically
    const interval = setInterval(loadNotifications, 3000);
    return () => clearInterval(interval);
  }, [currentUser]);

  const handleMarkAsRead = () => {
    if (currentUser) {
      ApiService.markNotificationsAsRead(currentUser.id);
      loadNotifications();
    }
  };

  const getRoleBadge = (role?: string) => {
    switch (role) {
      case 'admin':
        return <span className="role-badge badge-admin">Admin</span>;
      case 'instructor':
        return <span className="role-badge badge-instructor">Instructor</span>;
      default:
        return <span className="role-badge badge-student">Común</span>;
    }
  };

  const getNotifIcon = (type: string) => {
    switch (type) {
      case 'email':
        return <Mail size={14} color="#6366f1" />;
      case 'sms':
        return <Smartphone size={14} color="#10b981" />;
      default:
        return <Bell size={14} color="#3b52ef" />;
    }
  };

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <header className="navbar-header">
      <div className="navbar-logo">
        <div className="logo-icon">
          <BookOpen size={22} color="#ffffff" />
        </div>
        <span className="logo-text">Educa<span style={{ color: 'var(--color-primary)' }}>Net</span></span>
      </div>

      {currentUser && (
        <div className="navbar-profile">
          {/* Notifications Center */}
          <div className="notifications-wrapper" style={{ position: 'relative' }}>
            <button
              className="notif-bell-btn"
              onClick={() => {
                setShowNotifications(!showNotifications);
                if (!showNotifications) handleMarkAsRead();
              }}
              style={{
                cursor: 'pointer',
                color: 'var(--color-text-secondary)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                padding: '8px',
                borderRadius: '8px',
                backgroundColor: 'transparent',
                position: 'relative'
              }}
              title="Notificaciones"
            >
              <Bell size={20} />
              {unreadCount > 0 && (
                <span className="unread-dot" style={{
                  position: 'absolute',
                  top: '4px',
                  right: '4px',
                  width: '8px',
                  height: '8px',
                  borderRadius: '50%',
                  backgroundColor: 'var(--color-error)',
                  border: '1px solid #fff'
                }} />
              )}
            </button>

            {showNotifications && (
              <div className="notif-dropdown card" style={{
                position: 'absolute',
                top: '45px',
                right: '0',
                width: '320px',
                maxHeight: '380px',
                overflowY: 'auto',
                zIndex: 50,
                padding: '16px',
                boxShadow: 'var(--shadow-lg)',
                backgroundColor: '#ffffff'
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px', borderBottom: '1px solid var(--border-color)', paddingBottom: '8px' }}>
                  <span style={{ fontSize: '0.85rem', fontWeight: '700' }}>Notificaciones del Sistema</span>
                  <button onClick={loadNotifications} style={{ fontSize: '0.75rem', color: 'var(--color-primary)', cursor: 'pointer' }}>Actualizar</button>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                  {notifications.map(n => (
                    <div key={n.id} style={{
                      display: 'flex',
                      gap: '10px',
                      padding: '8px',
                      borderRadius: '8px',
                      backgroundColor: n.read ? '#fff' : 'var(--color-primary-light)',
                      border: '1px solid #f1f5f9'
                    }}>
                      <div style={{ marginTop: '2px' }}>{getNotifIcon(n.type)}</div>
                      <div style={{ display: 'flex', flexDirection: 'column', gap: '2px' }}>
                        <span style={{ fontSize: '0.75rem', color: 'var(--color-text-primary)', lineHeight: '1.3' }}>{n.text}</span>
                        <span style={{ fontSize: '0.65rem', color: 'var(--color-text-muted)' }}>{new Date(n.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                      </div>
                    </div>
                  ))}
                  {notifications.length === 0 && (
                    <p style={{ textAlign: 'center', fontSize: '0.8rem', color: 'var(--color-text-muted)', padding: '16px' }}>No hay notificaciones.</p>
                  )}
                </div>
              </div>
            )}
          </div>

          <div className="user-info">
            <div className="user-details">
              <span className="user-name">{currentUser.name}</span>
              {getRoleBadge(currentUser.role)}
            </div>
            <div className="avatar-circle">
              {currentUser.avatarUrl ? (
                <img src={currentUser.avatarUrl} alt={currentUser.name} />
              ) : (
                <User size={18} color="var(--color-text-secondary)" />
              )}
            </div>
          </div>

          <button className="logout-btn" onClick={onLogout} title="Cerrar Sesión">
            <LogOut size={18} />
          </button>
        </div>
      )}

      <style>{`
        .navbar-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          height: 70px;
          background-color: var(--color-bg-card);
          border-bottom: 1px solid var(--border-color);
          padding: 0 32px;
          position: sticky;
          top: 0;
          z-index: 10;
          box-shadow: var(--shadow-sm);
        }
        .navbar-logo {
          display: flex;
          align-items: center;
          gap: 10px;
        }
        .logo-icon {
          background-color: var(--color-primary);
          padding: 8px;
          border-radius: var(--border-radius-md);
          display: flex;
          align-items: center;
          justify-content: center;
        }
        .logo-text {
          font-family: var(--font-family-title);
          font-size: 1.4rem;
          font-weight: 800;
          color: var(--color-text-primary);
          letter-spacing: -0.03em;
        }
        .navbar-profile {
          display: flex;
          align-items: center;
          gap: 20px;
        }
        .user-info {
          display: flex;
          align-items: center;
          gap: 12px;
        }
        .user-details {
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          gap: 2px;
        }
        .user-name {
          font-size: 0.9rem;
          font-weight: 600;
          color: var(--color-text-primary);
        }
        .role-badge {
          font-size: 0.7rem;
          font-weight: 700;
          padding: 2px 8px;
          border-radius: var(--border-radius-full);
          text-transform: uppercase;
        }
        .badge-admin {
          background-color: var(--color-error-light);
          color: var(--color-error);
        }
        .badge-instructor {
          background-color: var(--color-primary-light);
          color: var(--color-primary);
        }
        .badge-student {
          background-color: var(--color-success-light);
          color: var(--color-success);
        }
        .avatar-circle {
          width: 36px;
          height: 36px;
          border-radius: var(--border-radius-full);
          background-color: var(--color-bg-app);
          border: 1px solid var(--border-color);
          display: flex;
          align-items: center;
          justify-content: center;
          overflow: hidden;
        }
        .avatar-circle img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        .logout-btn {
          cursor: pointer;
          color: var(--color-text-secondary);
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 8px;
          border-radius: var(--border-radius-md);
          transition: all var(--transition-fast);
        }
        .logout-btn:hover {
          color: var(--color-error);
          background-color: var(--color-error-light);
        }
      `}</style>
    </header>
  );
};
