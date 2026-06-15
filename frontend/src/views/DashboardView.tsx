import React from 'react';
import { Search, Plus, DollarSign, Calendar, Activity, Eye, Trash2, Award, ExternalLink, Download, Tag, HelpCircle, Users, ShieldAlert, Key, UserCheck, UserX, MessageSquare, ChevronRight, TrendingUp } from 'lucide-react';
import { useDashboardController } from '../controllers/useDashboardController';
import { Navbar } from '../components/Navbar';
import { Sidebar } from '../components/Sidebar';
import { CourseCard } from '../components/CourseCard';
import { Input } from '../components/Input';
import { Button } from '../components/Button';
import { User, StudentProfile } from '../models/User';

interface DashboardViewProps {
  currentUser: User;
  onLogout: () => void;
  onUpdateUser: (user: User) => void;
  onSelectCourse: (courseId: string) => void;
}

export const DashboardView: React.FC<DashboardViewProps> = ({
  currentUser,
  onLogout,
  onUpdateUser,
  onSelectCourse
}) => {
  const controller = useDashboardController({ currentUser, onUpdateUser, onLogout });

  const renderTabContent = () => {
    switch (controller.currentTab) {
      case 'explore':
        const studentHistory = currentUser.role === 'student' ? (currentUser as any).lessonHistory || [] : [];
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Explorar Catálogo</h2>
              <p>Busca e inscríbete en los mejores cursos de ingeniería y desarrollo.</p>
            </div>

            {/* WIDGET: Historial de lecciones accedidas (Usuario Común) */}
            {currentUser.role === 'student' && studentHistory.length > 0 && (
              <div className="card history-widget" style={{ marginBottom: '24px', padding: '16px', borderLeft: '4px solid var(--color-primary)' }}>
                <h4 style={{ fontSize: '0.9rem', display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px', color: 'var(--color-text-secondary)' }}>
                  <Activity size={16} />
                  Continuar aprendiendo (Recientemente accedido)
                </h4>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                  {studentHistory.map((h: any, idx: number) => (
                    <div key={idx} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.85rem' }}>
                      <span>
                        <strong>{h.courseTitle}</strong>: {h.lessonTitle}
                      </span>
                      <button
                        onClick={() => onSelectCourse(h.courseId)}
                        style={{ color: 'var(--color-primary)', fontWeight: '600', cursor: 'pointer', display: 'flex', alignItems: 'center' }}
                      >
                        Ir a clase <ChevronRight size={14} />
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Filtros Minimalistas */}
            <div className="filters-section">
              <div className="search-box">
                <Search size={18} className="search-icon" />
                <input
                  type="text"
                  placeholder="Buscar curso por título o descripción..."
                  value={controller.search}
                  onChange={(e) => controller.setSearch(e.target.value)}
                />
              </div>

              <div className="filter-dropdowns">
                <select
                  value={controller.category}
                  onChange={(e) => controller.setCategory(e.target.value)}
                  className="filter-select"
                >
                  <option value="">Todas las Categorías</option>
                  <option value="Software Engineering">Software Engineering</option>
                  <option value="Web Development">Web Development</option>
                  <option value="General">General</option>
                </select>

                <select
                  value={controller.maxPrice || ''}
                  onChange={(e) => controller.setMaxPrice(e.target.value ? Number(e.target.value) : undefined)}
                  className="filter-select"
                >
                  <option value="">Cualquier Precio</option>
                  <option value="30">Hasta $30.00</option>
                  <option value="50">Hasta $50.00</option>
                </select>
              </div>
            </div>

            {controller.isLoading ? (
              <div className="loading-grid">
                {Array.from({ length: 3 }).map((_, i) => (
                  <div key={i} className="card shimmer" style={{ height: '350px' }} />
                ))}
              </div>
            ) : controller.courses.length === 0 ? (
              <div className="empty-state">
                <p>No se encontraron cursos con los filtros seleccionados.</p>
              </div>
            ) : (
              <div className="courses-grid">
                {controller.courses.filter(c => c.isActive).map(course => {
                  const student = currentUser.role === 'student' ? currentUser as StudentProfile : null;
                  
                  let progress = 0;
                  if (student && student.enrolledCourses?.includes(course.id)) {
                    const courseLessons = course.modules.flatMap(m => m.lessons.map(l => l.id));
                    const completed = courseLessons.filter(id => student.completedLessons?.includes(id));
                    progress = courseLessons.length > 0 ? Math.round((completed.length / courseLessons.length) * 100) : 0;
                  }

                  return (
                    <CourseCard
                      key={course.id}
                      course={course}
                      isEnrolled={controller.isEnrolled(course.id)}
                      progressPercent={progress}
                      isInWishlist={controller.isInWishlist(course.id)}
                      onSelect={onSelectCourse}
                      onToggleWishlist={controller.handleToggleWishlist}
                      showWishlistBtn={currentUser.role === 'student'}
                    />
                  );
                })}
              </div>
            )}
          </div>
        );

      case 'my-courses':
        const enrolled = controller.courses.filter(c => controller.isEnrolled(c.id));
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Mis Aprendizajes</h2>
              <p>Continúa tus lecciones y mantén un control de tu progreso académico.</p>
            </div>

            {enrolled.length === 0 ? (
              <div className="empty-state">
                <p>Aún no estás inscrito en ningún curso de pago.</p>
                <Button onClick={() => controller.setCurrentTab('explore')} style={{ marginTop: '16px' }}>
                  Ver Cursos Disponibles
                </Button>
              </div>
            ) : (
              <div className="courses-grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '24px' }}>
                {enrolled.map(course => {
                  const student = currentUser as StudentProfile;
                  const courseLessons = course.modules.flatMap(m => m.lessons.map(l => l.id));
                  const completed = courseLessons.filter(id => student.completedLessons?.includes(id));
                  const progress = courseLessons.length > 0 ? Math.round((completed.length / courseLessons.length) * 100) : 0;

                  return (
                    <div key={course.id} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                      <CourseCard
                        course={course}
                        isEnrolled={true}
                        progressPercent={progress}
                        isInWishlist={controller.isInWishlist(course.id)}
                        onSelect={onSelectCourse}
                        onToggleWishlist={controller.handleToggleWishlist}
                      />
                      {/* DAR DE BAJA (Usuario Común) */}
                      <button
                        onClick={() => controller.handleUnenrollCourse(course.id)}
                        className="btn btn-ghost"
                        style={{ color: 'var(--color-error)', width: '100%', fontSize: '0.8rem', padding: '6px' }}
                      >
                        Darse de baja del curso
                      </button>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        );

      case 'wishlist':
        const wishlistCourses = controller.courses.filter(c => controller.isInWishlist(c.id));
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Mi Lista de Deseos</h2>
              <p>Cursos guardados que te interesaría adquirir en el futuro.</p>
            </div>

            {wishlistCourses.length === 0 ? (
              <div className="empty-state">
                <p>Tu lista de deseos está vacía.</p>
              </div>
            ) : (
              <div className="courses-grid">
                {wishlistCourses.map(course => (
                  <CourseCard
                    key={course.id}
                    course={course}
                    isEnrolled={controller.isEnrolled(course.id)}
                    isInWishlist={true}
                    onSelect={onSelectCourse}
                    onToggleWishlist={controller.handleToggleWishlist}
                  />
                ))}
              </div>
            )}
          </div>
        );

      case 'my-certificates':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Mis Certificaciones</h2>
              <p>Descarga tus diplomas y valida digitalmente tus competencias académicas.</p>
            </div>

            {controller.certificates.length === 0 ? (
              <div className="empty-state">
                <p>Aún no has generado ningún certificado. Completa un curso al 100% para obtenerlo.</p>
              </div>
            ) : (
              <div className="certificates-container">
                {controller.certificates.map(cert => (
                  <div key={cert.id} className="card certificate-item">
                    <div className="cert-left">
                      <Award size={36} color="var(--color-primary)" />
                      <div className="cert-info">
                        <h4>{cert.courseTitle}</h4>
                        <p className="cert-hash">ID Verificación: {cert.verificationHash}</p>
                        <p className="cert-date">Emitido el: {new Date(cert.issuedAt).toLocaleDateString()}</p>
                      </div>
                    </div>

                    <div className="cert-right">
                      <div className="regenerate-section">
                        <label>Formato de Exportación:</label>
                        <select
                          value={cert.formatType}
                          onChange={(e) => controller.handleRegenerateCertificate(cert.courseId, e.target.value as any)}
                          className="filter-select border-select"
                        >
                          <option value="Image">Imagen / Diploma</option>
                          <option value="PDF">Documento PDF</option>
                          <option value="JSON-LD">Metadata JSON-LD</option>
                        </select>
                      </div>

                      <a
                        href={cert.certificateUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="btn btn-secondary text-btn"
                        download={`certificado_${cert.courseId}.${cert.formatType === 'JSON-LD' ? 'json' : cert.formatType === 'PDF' ? 'pdf' : 'svg'}`}
                      >
                        <ExternalLink size={16} />
                        Ver / Descargar
                      </a>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        );

      case 'manage-courses':
        const instructorCourses = controller.courses.filter(c => c.instructorId === currentUser.id);
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar flex-header">
              <div>
                <h2>Panel de Instructor</h2>
                <p>Crea, edita y gestiona tus contenidos educativos para los estudiantes.</p>
              </div>
              <Button icon={<Plus size={18} />} onClick={() => controller.setShowAddCourseModal(true)}>
                Crear Nuevo Curso
              </Button>
            </div>

            {instructorCourses.length === 0 ? (
              <div className="empty-state">
                <p>Aún no has publicado ningún curso.</p>
              </div>
            ) : (
              <div className="courses-grid">
                {instructorCourses.map(course => (
                  <div key={course.id} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    <CourseCard
                      course={course}
                      isEnrolled={false}
                      isInWishlist={false}
                      showWishlistBtn={false}
                      onSelect={onSelectCourse}
                    />
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button
                        className={`btn ${course.isActive ? 'btn-secondary' : 'btn-ghost'}`}
                        style={{ flex: 1, fontSize: '0.8rem', padding: '6px' }}
                        onClick={() => {
                          const coursesListCopy: Course[] = JSON.parse(localStorage.getItem('courses') || '[]');
                          const idx = coursesListCopy.findIndex(c => c.id === course.id);
                          if (idx !== -1) {
                            coursesListCopy[idx].isActive = !coursesListCopy[idx].isActive;
                            localStorage.setItem('courses', JSON.stringify(coursesListCopy));
                            controller.setCurrentTab('explore'); // trigger refresh
                            setTimeout(() => controller.setCurrentTab('manage-courses'), 20);
                          }
                        }}
                      >
                        {course.isActive ? "Deshabilitar" : "Habilitar"}
                      </button>
                      <button
                        className="btn btn-ghost"
                        style={{ color: 'var(--color-error)', flex: 1, fontSize: '0.8rem', padding: '6px' }}
                        onClick={() => controller.handleDeleteCourseClick(course.id)}
                      >
                        Eliminar
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        );

      case 'revenue':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Reporte de Ingresos</h2>
              <p>Monitorea las ventas de tus cursos y el balance financiero de tu cuenta.</p>
            </div>

            <div className="stats-grid">
              <div className="card stat-card glow-card">
                <DollarSign size={28} className="stat-icon" />
                <div className="stat-details">
                  <span className="stat-label">Ingresos Totales Acumulados</span>
                  <span className="stat-value">${controller.instructorRevenue.toFixed(2)}</span>
                </div>
              </div>

              <div className="card stat-card">
                <Activity size={28} className="stat-icon" />
                <div className="stat-details">
                  <span className="stat-label">Comisión de Plataforma</span>
                  <span className="stat-value">5%</span>
                </div>
              </div>

              <div className="card stat-card">
                <Calendar size={28} className="stat-icon" />
                <div className="stat-details">
                  <span className="stat-label">Próximo Pago Estimado</span>
                  <span className="stat-value">05 / Jun / 2026</span>
                </div>
              </div>
            </div>

            <div className="card reports-table-card" style={{ marginTop: '24px' }}>
              <h3>Historial de Ventas</h3>
              <p style={{ color: 'var(--color-text-secondary)', fontSize: '0.85rem', marginBottom: '16px' }}>Ganancias desglosadas por inscripciones en tus cursos.</p>
              <div className="table-responsive">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Fecha</th>
                      <th>Estudiante</th>
                      <th>Valor Pagado</th>
                      <th>Comisión Plataforma</th>
                      <th>Acreditado</th>
                    </tr>
                  </thead>
                  <tbody>
                    {/* Consultar compras en logs de auditoria correspondientes a este instructor */}
                    {controller.auditLogs.filter(l => l.action === "COURSE_PURCHASED" && l.details.includes(currentUser.name)).map((l, i) => (
                      <tr key={i}>
                        <td>{new Date(l.timestamp).toLocaleDateString()}</td>
                        <td>{l.userEmail}</td>
                        <td>$49.99</td>
                        <td>$2.50</td>
                        <td style={{ color: 'var(--color-success)', fontWeight: '600' }}>+$47.49</td>
                      </tr>
                    ))}
                    <tr>
                      <td>30/05/2026</td>
                      <td>orlando@mail.com</td>
                      <td>$49.99</td>
                      <td>$2.50</td>
                      <td style={{ color: 'var(--color-success)', fontWeight: '600' }}>+$47.49</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      case 'instructor-users':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Lista de Alumnos</h2>
              <p>Observa todos los usuarios agregados y registrados en la plataforma.</p>
            </div>

            <div className="card reports-table-card">
              <div className="table-responsive">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Nombre</th>
                      <th>Correo Electrónico</th>
                      <th>Rol</th>
                      <th>Fecha de Registro</th>
                    </tr>
                  </thead>
                  <tbody>
                    {controller.usersList.map(u => (
                      <tr key={u.id}>
                        <td>{u.name}</td>
                        <td>{u.email}</td>
                        <td><span className="role-badge badge-student" style={{ padding: '2px 8px', fontSize: '0.7rem' }}>{u.role}</span></td>
                        <td>{new Date(u.createdAt).toLocaleDateString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      case 'instructor-questions':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Responder Dudas</h2>
              <p>Revisa las dudas formuladas por los estudiantes en tus lecciones y bríndales soporte.</p>
            </div>

            <div className="questions-container" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {controller.pendingQuestions.map(q => (
                <div key={q.id} className="card question-item-row" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <h4 style={{ fontSize: '0.95rem', fontWeight: '700' }}>{q.studentName} preguntó:</h4>
                    <p style={{ fontStyle: 'italic', margin: '4px 0', color: 'var(--color-text-secondary)' }}>"{q.questionText}"</p>
                    <p style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)' }}>ID: {q.id}</p>
                  </div>
                  <Button variant="secondary" onClick={() => controller.handleOpenAnswerModal(q)}>
                    Responder
                  </Button>
                </div>
              ))}
              {controller.pendingQuestions.length === 0 && (
                <div className="empty-state">
                  <p>¡Buen trabajo! No tienes dudas pendientes por responder.</p>
                </div>
              )}
            </div>
          </div>
        );

      case 'admin-manage-users':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar flex-header">
              <div>
                <h2>Gestionar Cuentas</h2>
                <p>Crea, edita, habilita/deshabilita y elimina cuentas de estudiantes, instructores y administradores.</p>
              </div>
              <Button icon={<Plus size={18} />} onClick={controller.handleOpenCreateUser}>
                Crear Nuevo Usuario
              </Button>
            </div>

            <div className="card reports-table-card">
              <div className="table-responsive">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Nombre</th>
                      <th>Email</th>
                      <th>Rol</th>
                      <th>Estado</th>
                      <th>Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {controller.usersList.map(u => (
                      <tr key={u.id}>
                        <td>{u.name}</td>
                        <td>{u.email}</td>
                        <td>
                          <span className={`role-badge ${u.role === 'admin' ? 'badge-admin' : u.role === 'instructor' ? 'badge-instructor' : 'badge-student'}`}>
                            {u.role}
                          </span>
                        </td>
                        <td>
                          <span style={{
                            padding: '4px 8px',
                            borderRadius: '4px',
                            fontSize: '0.75rem',
                            fontWeight: '600',
                            backgroundColor: u.isActive ? 'var(--color-success-light)' : 'var(--color-error-light)',
                            color: u.isActive ? 'var(--color-success)' : 'var(--color-error)'
                          }}>
                            {u.isActive ? "Activo" : "Deshabilitado"}
                          </span>
                        </td>
                        <td>
                          <div style={{ display: 'flex', gap: '8px' }}>
                            <button
                              onClick={() => controller.handleOpenEditUser(u)}
                              style={{ color: 'var(--color-primary)', cursor: 'pointer', padding: '4px', display: 'flex' }}
                              title="Editar cuenta"
                            >
                              <Eye size={16} />
                            </button>
                            <button
                              onClick={() => {
                                controller.handleEditUserClick ? null : ApiService.updateUserByAdmin(currentUser.id, u.id, {
                                  name: u.name,
                                  email: u.email,
                                  role: u.role,
                                  isActive: !u.isActive
                                }).then(() => controller.setCurrentTab('explore')).then(() => controller.setCurrentTab('admin-manage-users'));
                              }}
                              style={{ color: u.isActive ? '#e0a800' : 'var(--color-success)', cursor: 'pointer', padding: '4px', display: 'flex' }}
                              title={u.isActive ? "Deshabilitar" : "Habilitar"}
                            >
                              {u.isActive ? <UserX size={16} /> : <UserCheck size={16} />}
                            </button>
                            <button
                              onClick={() => controller.handleDeleteUser(u.id)}
                              style={{ color: 'var(--color-error)', cursor: 'pointer', padding: '4px', display: 'flex' }}
                              title="Eliminar usuario permanentemente"
                            >
                              <Trash2 size={16} />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      case 'admin-coupons':
      case 'instructor-coupons':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar flex-header">
              <div>
                <h2>Gestionar Cupones de Descuento</h2>
                <p>Administra los códigos promocionales para la compra de cursos.</p>
              </div>
              <Button icon={<Plus size={18} />} onClick={() => controller.setShowCouponModal(true)}>
                Crear Cupón
              </Button>
            </div>

            <div className="card reports-table-card">
              <div className="table-responsive">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Código</th>
                      <th>Descuento (%)</th>
                      <th>Límite de Uso</th>
                      <th>Cantidad Usados</th>
                      <th>Expiración</th>
                      <th>Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {controller.couponsList.map(c => (
                      <tr key={c.id}>
                        <td style={{ fontWeight: 'bold', fontFamily: 'monospace' }}>{c.code}</td>
                        <td>{c.discountPercentage}%</td>
                        <td>{c.maxUses}</td>
                        <td>{c.usedCount}</td>
                        <td>{new Date(c.expirationDate).toLocaleDateString()}</td>
                        <td>
                          <button
                            onClick={() => controller.handleDeleteCoupon(c.id)}
                            style={{ color: 'var(--color-error)', cursor: 'pointer', display: 'flex', padding: '4px' }}
                            title="Eliminar cupón"
                          >
                            <Trash2 size={16} />
                          </button>
                        </td>
                      </tr>
                    ))}
                    {controller.couponsList.length === 0 && (
                      <tr>
                        <td colSpan={6} style={{ textAlign: 'center', padding: '16px' }}>No hay cupones creados.</td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      case 'admin-financials':
        const fs = controller.financialSummary;
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Dashboard Financiero Global</h2>
              <p>Revisa y audita las ventas totales, ganancias de instructores y recaudación de comisiones de EducaNet.</p>
            </div>

            {fs && (
              <>
                <div className="stats-grid" style={{ marginBottom: '24px' }}>
                  <div className="card stat-card glow-card">
                    <TrendingUp size={28} className="stat-icon" />
                    <div className="stat-details">
                      <span className="stat-label">Ventas Brutas Totales</span>
                      <span className="stat-value">${fs.totalSales.toFixed(2)}</span>
                    </div>
                  </div>

                  <div className="card stat-card">
                    <Wallet size={28} className="stat-icon" style={{ backgroundColor: 'var(--color-success-light)', color: 'var(--color-success)' }} />
                    <div className="stat-details">
                      <span className="stat-label">Comisión Recaudada (5%)</span>
                      <span className="stat-value" style={{ color: 'var(--color-success)' }}>${fs.totalAdminCommissions.toFixed(2)}</span>
                    </div>
                  </div>

                  <div className="card stat-card">
                    <Users size={28} className="stat-icon" />
                    <div className="stat-details">
                      <span className="stat-label">Alumnos Activos</span>
                      <span className="stat-value">{fs.studentsCount}</span>
                    </div>
                  </div>
                </div>

                <div className="card reports-table-card">
                  <h3>Historial de Transacciones Registradas</h3>
                  <p style={{ color: 'var(--color-text-secondary)', fontSize: '0.85rem', marginBottom: '16px' }}>Auditoría contable inmutable de los cobros en Stripe.</p>
                  <div className="table-responsive">
                    <table className="data-table">
                      <thead>
                        <tr>
                          <th>Fecha/Hora</th>
                          <th>Alumno</th>
                          <th>Concepto del Curso</th>
                          <th>Cobrado</th>
                          <th>Comisión Plataforma</th>
                          <th>ID Stripe</th>
                        </tr>
                      </thead>
                      <tbody>
                        {fs.transactions.map((t: any) => (
                          <tr key={t.id}>
                            <td>{new Date(t.timestamp).toLocaleString()}</td>
                            <td>{t.userEmail}</td>
                            <td>{t.courseTitle}</td>
                            <td style={{ fontWeight: '600' }}>${t.amountPaid.toFixed(2)}</td>
                            <td style={{ color: 'var(--color-success)', fontWeight: '600' }}>+${t.commissionCollected.toFixed(2)}</td>
                            <td style={{ fontFamily: 'monospace', fontSize: '0.8rem', color: 'var(--color-text-muted)' }}>{t.stripeChargeId}</td>
                          </tr>
                        ))}
                        {fs.transactions.length === 0 && (
                          <tr>
                            <td colSpan={6} style={{ textAlign: 'center', padding: '16px' }}>No se registran compras todavía.</td>
                          </tr>
                        )}
                      </tbody>
                    </table>
                  </div>
                </div>
              </>
            )}
          </div>
        );

      case 'admin-moderate':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar flex-header">
              <div>
                <h2>Moderación de Cursos</h2>
                <p>Revisa, aprueba, deshabilita o elimina contenido inactivo o que infrinja políticas globales.</p>
              </div>
              <Button icon={<Plus size={18} />} onClick={() => controller.setShowAddCourseModal(true)}>
                Crear Nuevo Curso
              </Button>
            </div>

            <div className="courses-grid">
              {controller.courses.map(course => (
                <div key={course.id} className="card moderate-card" style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                    <img src={course.imageUrl} alt={course.title} className="mod-img" />
                    <div className="mod-details">
                      <h4>{course.title}</h4>
                      <p className="mod-instructor">Instructor: {course.instructorName}</p>
                      <p className="mod-price">Precio: ${course.price}</p>
                    </div>
                  </div>
                  <div style={{ display: 'flex', gap: '8px', width: '100%' }}>
                    <Button variant="ghost" onClick={() => onSelectCourse(course.id)} style={{ flex: 1, fontSize: '0.8rem', padding: '6px' }}>
                      Previsualizar
                    </Button>
                    <button
                      className="btn btn-secondary"
                      style={{ flex: 1, fontSize: '0.8rem', padding: '6px' }}
                      onClick={() => {
                        const coursesListCopy: Course[] = JSON.parse(localStorage.getItem('courses') || '[]');
                        const idx = coursesListCopy.findIndex(c => c.id === course.id);
                        if (idx !== -1) {
                          coursesListCopy[idx].isActive = !coursesListCopy[idx].isActive;
                          localStorage.setItem('courses', JSON.stringify(coursesListCopy));
                          controller.setCurrentTab('explore'); // trigger refresh
                          setTimeout(() => controller.setCurrentTab('admin-moderate'), 20);
                        }
                      }}
                    >
                      {course.isActive ? "Deshabilitar" : "Habilitar"}
                    </button>
                    <button
                      className="delete-action-btn"
                      style={{ flex: 1, fontSize: '0.8rem', padding: '6px' }}
                      onClick={() => controller.handleDeleteCourseClick(course.id)}
                    >
                      Eliminar
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        );

      case 'audit-logs':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Logs de Auditoría</h2>
              <p>Registro histórico e inmutable de las operaciones críticas realizadas en la plataforma.</p>
            </div>

            <div className="card reports-table-card">
              <div className="table-responsive">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Fecha/Hora</th>
                      <th>Usuario (Email)</th>
                      <th>Acción Ejecutada</th>
                      <th>Detalles</th>
                    </tr>
                  </thead>
                  <tbody>
                    {controller.auditLogs.map(log => (
                      <tr key={log.id}>
                        <td style={{ whiteSpace: 'nowrap', fontSize: '0.8rem' }}>{new Date(log.timestamp).toLocaleString()}</td>
                        <td>{log.userEmail || 'Invitado (Login Fallido)'}</td>
                        <td>
                          <span className={`log-badge-action log-${log.action.toLowerCase()}`}>
                            {log.action}
                          </span>
                        </td>
                        <td className="log-detail-td" title={log.details}>{log.details}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );

      case 'profile':
        return (
          <div className="tab-pane">
            <div className="dashboard-header-bar">
              <h2>Personalizar Perfil</h2>
              <p>Edita tu información personal y personaliza cómo te ven otros usuarios en la plataforma.</p>
            </div>

            <div className="card profile-card" style={{ maxWidth: '600px', margin: '0 auto' }}>
              <form onSubmit={controller.handleSaveProfile} className="profile-form">
                {controller.profileSuccessMsg && (
                  <div className="alert alert-success" style={{ marginBottom: '20px', padding: '12px 16px', borderRadius: '8px', backgroundColor: 'var(--color-success-light)', color: 'var(--color-success)', fontWeight: '600' }}>
                    {controller.profileSuccessMsg}
                  </div>
                )}

                {/* Avatar Selector Widget */}
                <div className="avatar-selection-section" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px', marginBottom: '24px' }}>
                  <span className="input-label" style={{ alignSelf: 'flex-start' }}>Selecciona tu Avatar</span>
                  <div className="current-avatar-preview" style={{ width: '80px', height: '80px', borderRadius: '50%', border: '3px solid var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden', backgroundColor: 'var(--color-bg-app)' }}>
                    {controller.profileAvatar ? (
                      <img src={controller.profileAvatar} alt="Avatar" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                    ) : (
                      <span style={{ fontSize: '2rem', fontWeight: 'bold', color: 'var(--color-text-secondary)' }}>{controller.profileName[0]}</span>
                    )}
                  </div>

                  <div className="avatar-options-grid" style={{ display: 'flex', gap: '12px', marginTop: '10px' }}>
                    {[
                      'https://api.dicebear.com/7.x/adventurer/svg?seed=Felix',
                      'https://api.dicebear.com/7.x/adventurer/svg?seed=Aneka',
                      'https://api.dicebear.com/7.x/adventurer/svg?seed=Buster',
                      'https://api.dicebear.com/7.x/adventurer/svg?seed=Gizmo'
                    ].map((avatarUrl, index) => (
                      <button
                        key={index}
                        type="button"
                        onClick={() => controller.setProfileAvatar(avatarUrl)}
                        style={{
                          width: '48px',
                          height: '48px',
                          borderRadius: '50%',
                          overflow: 'hidden',
                          border: controller.profileAvatar === avatarUrl ? '2px solid var(--color-primary)' : '1px solid var(--border-color)',
                          cursor: 'pointer',
                          transition: 'all 0.2s',
                          padding: '2px',
                          backgroundColor: '#fff'
                        }}
                      >
                        <img src={avatarUrl} alt={`Avatar ${index + 1}`} style={{ width: '100%', height: '100%', borderRadius: '50%' }} />
                      </button>
                    ))}
                  </div>
                </div>

                <Input
                  label="Nombre Completo"
                  type="text"
                  value={controller.profileName}
                  onChange={(e) => controller.setProfileName(e.target.value)}
                  required
                />

                <Input
                  label="Correo Electrónico"
                  type="email"
                  value={controller.profileEmail}
                  onChange={(e) => controller.setProfileEmail(e.target.value)}
                  required
                />

                <div className="input-group">
                  <label className="input-label">Biografía / Acerca de mí</label>
                  <textarea
                    className="input-field textarea-field"
                    value={controller.profileBio}
                    onChange={(e) => controller.setProfileBio(e.target.value)}
                    rows={4}
                    placeholder="Escribe un poco sobre ti, tus intereses de estudio o tu experiencia académica..."
                  />
                </div>

                <div className="profile-actions" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '20px' }}>
                  {currentUser.role === 'student' && (
                    <button
                      type="button"
                      className="btn btn-ghost"
                      style={{ color: 'var(--color-error)' }}
                      onClick={() => controller.setShowDeleteAccountConfirm(true)}
                    >
                      Eliminar Cuenta de Perfil
                    </button>
                  )}
                  <Button type="submit" style={{ marginLeft: 'auto' }}>
                    Guardar Cambios
                  </Button>
                </div>
              </form>
            </div>
          </div>
        );

      default:
        return <div>Tab no configurado</div>;
    }
  };

  return (
    <div className="dashboard-container">
      <Navbar currentUser={currentUser} onLogout={onLogout} />
      <div className="app-container">
        <Sidebar
          currentTab={controller.currentTab}
          setCurrentTab={controller.setCurrentTab}
          role={currentUser.role}
        />
        <main className="main-content">
          {renderTabContent()}
        </main>
      </div>

      {/* MODAL PARA CREAR CURSO (INSTRUCTOR / ADMIN) */}
      {controller.showAddCourseModal && (
        <div className="modal-overlay" onClick={() => controller.setShowAddCourseModal(false)}>
          <div className="modal-card" onClick={(e) => e.stopPropagation()}>
            <h3>Crear Nuevo Curso</h3>
            <form onSubmit={controller.handleCreateCourse} className="modal-form">
              <Input
                label="Título del Curso"
                type="text"
                placeholder="Ej. Curso de Microservicios con .NET"
                value={controller.newCourseTitle}
                onChange={(e) => controller.setNewCourseTitle(e.target.value)}
                required
              />

              <div className="input-group">
                <label className="input-label">Descripción</label>
                <textarea
                  className="input-field textarea-field"
                  placeholder="Detalla de qué trata el curso..."
                  value={controller.newCourseDesc}
                  onChange={(e) => controller.setNewCourseDesc(e.target.value)}
                  rows={4}
                  required
                />
              </div>

              <div className="modal-row">
                <div className="input-group">
                  <label className="input-label">Categoría</label>
                  <select
                    value={controller.newCourseCategory}
                    onChange={(e) => controller.setNewCourseCategory(e.target.value)}
                    className="filter-select full-width-select"
                  >
                    <option value="Software Engineering">Software Engineering</option>
                    <option value="Web Development">Web Development</option>
                    <option value="General">General</option>
                  </select>
                </div>

                <Input
                  label="Precio ($ USD)"
                  type="number"
                  step="0.01"
                  min="0"
                  value={controller.newCoursePrice}
                  onChange={(e) => controller.setNewCoursePrice(Number(e.target.value))}
                  required
                />
              </div>

              <div className="modal-actions">
                <Button type="button" variant="ghost" onClick={() => controller.setShowAddCourseModal(false)}>
                  Cancelar
                </Button>
                <Button type="submit">
                  Publicar Curso
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL PERSONALIZADO DE CONFIRMACIÓN DE ELIMINACIÓN DE CURSO */}
      {controller.courseToDeleteId && (
        <div className="modal-overlay" onClick={controller.handleDeleteCourseCancel}>
          <div className="modal-card" style={{ maxWidth: '420px', textAlign: 'center' }} onClick={(e) => e.stopPropagation()}>
            <div style={{
              width: '60px',
              height: '60px',
              borderRadius: '50%',
              backgroundColor: 'var(--color-error-light)',
              color: 'var(--color-error)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 20px auto'
            }}>
              <Trash2 size={28} />
            </div>
            <h3 style={{ borderBottom: 'none', marginBottom: '8px', paddingBottom: '0' }}>¿Eliminar curso permanentemente?</h3>
            <p style={{ color: 'var(--color-text-secondary)', fontSize: '0.9rem', marginBottom: '24px' }}>
              Esta acción es irreversible. El curso se removerá del catálogo y los estudiantes matriculados perderán el acceso.
            </p>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '12px' }}>
              <Button variant="ghost" onClick={controller.handleDeleteCourseCancel}>
                Cancelar
              </Button>
              <button
                className="btn btn-primary"
                style={{ backgroundColor: 'var(--color-error)', boxShadow: '0 4px 14px 0 rgba(239, 68, 68, 0.2)' }}
                onClick={controller.handleDeleteCourseConfirm}
              >
                Eliminar Curso
              </button>
            </div>
          </div>
        </div>
      )}

      {/* MODAL: CREAR/EDITAR USUARIOS (ADMIN) */}
      {controller.showUserModal && (
        <div className="modal-overlay" onClick={() => controller.setShowUserModal(false)}>
          <div className="modal-card" style={{ maxWidth: '450px' }} onClick={(e) => e.stopPropagation()}>
            <h3>{controller.selectedUserForEdit ? "Editar Cuenta de Usuario" : "Crear Nueva Cuenta"}</h3>
            <form onSubmit={controller.handleCreateOrUpdateUser} className="modal-form">
              <Input
                label="Nombre Completo"
                value={controller.userFormName}
                onChange={(e) => controller.setUserFormName(e.target.value)}
                required
              />
              <Input
                label="Correo Electrónico"
                type="email"
                value={controller.userFormEmail}
                onChange={(e) => controller.setUserFormEmail(e.target.value)}
                required
              />
              <Input
                label="Contraseña"
                type="password"
                placeholder={controller.selectedUserForEdit ? "Dejar en blanco para mantener la actual" : "Establece una contraseña"}
                value={controller.userFormPassword}
                onChange={(e) => controller.setUserFormPassword(e.target.value)}
                required={!controller.selectedUserForEdit}
              />
              <div className="input-group">
                <label className="input-label">Rol del Usuario</label>
                <select
                  value={controller.userFormRole}
                  onChange={(e) => controller.setUserFormRole(e.target.value as any)}
                  className="filter-select full-width-select"
                >
                  <option value="student">Estudiante (Usuario Común)</option>
                  <option value="instructor">Instructor</option>
                  <option value="admin">Administrador</option>
                </select>
              </div>

              {controller.selectedUserForEdit && (
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', margin: '8px 0' }}>
                  <input
                    type="checkbox"
                    id="user-active"
                    checked={controller.userFormIsActive}
                    onChange={(e) => controller.setUserFormIsActive(e.target.checked)}
                  />
                  <label htmlFor="user-active" style={{ fontSize: '0.9rem', fontWeight: '500' }}>Cuenta Activa</label>
                </div>
              )}

              <div className="modal-actions">
                <Button type="button" variant="ghost" onClick={() => controller.setShowUserModal(false)}>
                  Cancelar
                </Button>
                <Button type="submit">
                  {controller.selectedUserForEdit ? "Guardar Cambios" : "Crear Cuenta"}
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL: CREAR CUPÓN (ADMIN / INSTRUCTOR) */}
      {controller.showCouponModal && (
        <div className="modal-overlay" onClick={() => controller.setShowCouponModal(false)}>
          <div className="modal-card" style={{ maxWidth: '450px' }} onClick={(e) => e.stopPropagation()}>
            <h3>Crear Cupón de Descuento</h3>
            <form onSubmit={controller.handleCreateCoupon} className="modal-form">
              <Input
                label="Código del Cupón"
                placeholder="Ej. VERANO30"
                value={controller.couponFormCode}
                onChange={(e) => controller.setCouponFormCode(e.target.value)}
                required
              />
              <Input
                label="Porcentaje de Descuento (%)"
                type="number"
                min="1"
                max="100"
                value={controller.couponFormDiscount}
                onChange={(e) => controller.setCouponFormDiscount(Number(e.target.value))}
                required
              />
              <Input
                label="Límite Máximo de Usos"
                type="number"
                min="1"
                value={controller.couponFormMaxUses}
                onChange={(e) => controller.setCouponFormMaxUses(Number(e.target.value))}
                required
              />
              <div className="modal-actions">
                <Button type="button" variant="ghost" onClick={() => controller.setShowCouponModal(false)}>
                  Cancelar
                </Button>
                <Button type="submit">
                  Crear Cupón
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL: RESPONDER DUDA (INSTRUCTOR) */}
      {controller.showAnswerModal && controller.questionToAnswer && (
        <div className="modal-overlay" onClick={() => controller.setShowAnswerModal(false)}>
          <div className="modal-card" style={{ maxWidth: '500px' }} onClick={(e) => e.stopPropagation()}>
            <h3>Responder Pregunta en Lección</h3>
            <div style={{ backgroundColor: 'var(--color-bg-app)', padding: '12px', borderRadius: '8px', marginBottom: '16px' }}>
              <p style={{ fontSize: '0.8rem', color: 'var(--color-text-muted)', fontWeight: '600' }}>Pregunta de {controller.questionToAnswer.studentName}:</p>
              <p style={{ fontSize: '0.9rem', fontStyle: 'italic' }}>"{controller.questionToAnswer.questionText}"</p>
            </div>
            <form onSubmit={controller.handleAnswerQuestion} className="modal-form">
              <div className="input-group">
                <label className="input-label">Tu Respuesta</label>
                <textarea
                  className="input-field textarea-field"
                  placeholder="Escribe aquí tu respuesta pedagógica..."
                  value={controller.answerFormText}
                  onChange={(e) => controller.setAnswerFormText(e.target.value)}
                  rows={4}
                  required
                />
              </div>
              <div className="modal-actions">
                <Button type="button" variant="ghost" onClick={() => controller.setShowAnswerModal(false)}>
                  Cancelar
                </Button>
                <Button type="submit">
                  Enviar Respuesta
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL DE CONFIRMACIÓN DE BORRADO DE CUENTA PROPIA */}
      {controller.showDeleteAccountConfirm && (
        <div className="modal-overlay" onClick={() => controller.setShowDeleteAccountConfirm(false)}>
          <div className="modal-card" style={{ maxWidth: '420px', textAlign: 'center', padding: '32px 24px' }} onClick={(e) => e.stopPropagation()}>
            <div style={{
              width: '60px',
              height: '60px',
              borderRadius: '50%',
              backgroundColor: 'var(--color-error-light)',
              color: 'var(--color-error)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 20px auto'
            }}>
              <ShieldAlert size={28} />
            </div>
            <h3 style={{ borderBottom: 'none', marginBottom: '8px', paddingBottom: '0' }}>¿Eliminar tu cuenta EducaNet?</h3>
            <p style={{ color: 'var(--color-text-secondary)', fontSize: '0.9rem', marginBottom: '24px' }}>
              Esta acción es irreversible y borrará todo tu historial, cursos inscritos y certificaciones emitidas de forma permanente.
            </p>
            <div style={{ display: 'flex', justifyContent: 'center', gap: '12px' }}>
              <Button variant="ghost" onClick={() => controller.setShowDeleteAccountConfirm(false)}>
                Cancelar
              </Button>
              <button
                className="btn btn-primary"
                style={{ backgroundColor: 'var(--color-error)', boxShadow: '0 4px 14px 0 rgba(239, 68, 68, 0.2)' }}
                onClick={controller.handleDeleteOwnAccount}
              >
                Eliminar Permanentemente
              </button>
            </div>
          </div>
        </div>
      )}

      <style>{`
        .dashboard-container {
          display: flex;
          flex-direction: column;
          min-height: 100vh;
        }
        .dashboard-header-bar {
          margin-bottom: 28px;
        }
        .dashboard-header-bar h2 {
          font-size: 1.6rem;
          font-weight: 800;
          color: var(--color-text-primary);
          letter-spacing: -0.03em;
        }
        .dashboard-header-bar p {
          color: var(--color-text-secondary);
          font-size: 0.95rem;
        }
        .flex-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: 16px;
        }

        /* Filtros y Búsqueda */
        .filters-section {
          display: flex;
          flex-wrap: wrap;
          align-items: center;
          justify-content: space-between;
          gap: 16px;
          margin-bottom: 24px;
        }
        .search-box {
          position: relative;
          flex: 1;
          min-width: 280px;
          display: flex;
          align-items: center;
        }
        .search-icon {
          position: absolute;
          left: 14px;
          color: var(--color-text-muted);
        }
        .search-box input {
          width: 100%;
          padding: 12px 16px 12px 42px;
          border: 1px solid var(--border-color);
          border-radius: var(--border-radius-md);
          font-size: 0.95rem;
          background-color: var(--color-bg-card);
          box-shadow: var(--shadow-sm);
          transition: all var(--transition-normal);
        }
        .search-box input:focus {
          border-color: var(--color-primary);
          box-shadow: 0 0 0 3px rgba(59, 82, 239, 0.1), var(--shadow-sm);
        }
        .filter-dropdowns {
          display: flex;
          align-items: center;
          gap: 12px;
        }
        .filter-select {
          padding: 12px 16px;
          border: 1px solid var(--border-color);
          border-radius: var(--border-radius-md);
          background-color: var(--color-bg-card);
          font-size: 0.9rem;
          color: var(--color-text-secondary);
          box-shadow: var(--shadow-sm);
          cursor: pointer;
          transition: all var(--transition-fast);
        }
        .filter-select:focus {
          border-color: var(--color-primary);
        }
        
        .courses-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
          gap: 24px;
        }
        .loading-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
          gap: 24px;
        }

        .empty-state {
          text-align: center;
          padding: 60px 20px;
          background-color: var(--color-bg-card);
          border-radius: var(--border-radius-lg);
          border: 1px dashed var(--border-color);
          color: var(--color-text-secondary);
        }

        /* Certificados */
        .certificates-container {
          display: flex;
          flex-direction: column;
          gap: 16px;
        }
        .certificate-item {
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: 24px;
          flex-wrap: wrap;
        }
        .cert-left {
          display: flex;
          align-items: center;
          gap: 20px;
        }
        .cert-info h4 {
          font-size: 1.1rem;
          font-weight: 700;
          color: var(--color-text-primary);
        }
        .cert-hash {
          font-family: monospace;
          font-size: 0.8rem;
          color: var(--color-text-muted);
        }
        .cert-date {
          font-size: 0.8rem;
          color: var(--color-text-secondary);
          font-weight: 500;
        }
        .cert-right {
          display: flex;
          align-items: center;
          gap: 20px;
          flex-wrap: wrap;
        }
        .regenerate-section {
          display: flex;
          flex-direction: column;
          gap: 4px;
        }
        .regenerate-section label {
          font-size: 0.75rem;
          font-weight: 600;
          color: var(--color-text-secondary);
        }
        .border-select {
          padding: 8px 12px;
          font-size: 0.85rem;
          box-shadow: none;
        }
        .text-btn {
          font-size: 0.85rem;
          padding: 8px 14px;
        }

        /* Reportes e Ingresos */
        .stats-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
          gap: 20px;
        }
        .stat-card {
          display: flex;
          align-items: center;
          gap: 16px;
        }
        .stat-icon {
          background-color: var(--color-primary-light);
          color: var(--color-primary);
          padding: 12px;
          border-radius: var(--border-radius-md);
        }
        .stat-details {
          display: flex;
          flex-direction: column;
          gap: 2px;
        }
        .stat-label {
          font-size: 0.8rem;
          color: var(--color-text-secondary);
          font-weight: 500;
        }
        .stat-value {
          font-size: 1.6rem;
          font-weight: 800;
          color: var(--color-text-primary);
          font-family: var(--font-family-title);
        }
        .glow-card {
          border-color: rgba(59, 82, 239, 0.2);
          box-shadow: var(--shadow-glow), var(--shadow-md);
        }

        /* Tabla de Auditoría y Transacciones */
        .reports-table-card {
          overflow: hidden;
          padding: 24px;
        }
        .table-responsive {
          width: 100%;
          overflow-x: auto;
        }
        .data-table {
          width: 100%;
          border-collapse: collapse;
          text-align: left;
          font-size: 0.9rem;
        }
        .data-table th {
          background-color: var(--color-bg-app);
          padding: 14px 16px;
          font-weight: 600;
          color: var(--color-text-secondary);
          border-bottom: 1px solid var(--border-color);
        }
        .data-table td {
          padding: 14px 16px;
          border-bottom: 1px solid var(--border-color);
          color: var(--color-text-primary);
        }
        .data-table tr:last-child td {
          border-bottom: none;
        }
        .log-badge-action {
          font-size: 0.7rem;
          font-weight: 700;
          padding: 2px 8px;
          border-radius: var(--border-radius-full);
          text-transform: uppercase;
        }
        .log-login_success {
          background-color: var(--color-success-light);
          color: var(--color-success);
        }
        .log-login_failed, .log-account_locked {
          background-color: var(--color-error-light);
          color: var(--color-error);
        }
        .log-course_purchased, .log-course_created {
          background-color: var(--color-primary-light);
          color: var(--color-primary);
        }
        .log-certificate_generated {
          background-color: #fef3c7;
          color: #d97706;
        }
        .log-detail-td {
          max-width: 300px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        /* Moderación de Cursos */
        .moderate-card {
          display: flex;
          align-items: center;
          gap: 16px;
          padding: 16px;
        }
        .mod-img {
          width: 100px;
          height: 100px;
          border-radius: var(--border-radius-md);
          object-fit: cover;
        }
        .mod-details {
          display: flex;
          flex-direction: column;
          gap: 4px;
          flex: 1;
        }
        .mod-details h4 {
          font-size: 1rem;
          font-weight: 700;
        }
        .mod-instructor {
          font-size: 0.85rem;
          color: var(--color-text-secondary);
        }
        .mod-price {
          font-size: 0.9rem;
          font-weight: 600;
          color: var(--color-primary);
        }
        .mod-actions {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-top: 6px;
        }
        .delete-action-btn {
          cursor: pointer;
          display: inline-flex;
          align-items: center;
          gap: 6px;
          font-size: 0.85rem;
          font-weight: 500;
          color: var(--color-error);
          padding: 8px 12px;
          border-radius: var(--border-radius-md);
          transition: all var(--transition-fast);
        }
        .delete-action-btn:hover {
          background-color: var(--color-error-light);
        }

        /* Modales */
        .modal-overlay {
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background-color: rgba(15, 23, 42, 0.4);
          backdrop-filter: blur(4px);
          display: flex;
          align-items: center;
          justify-content: center;
          z-index: 100;
          animation: fadeIn 0.3s ease;
        }
        @keyframes fadeIn {
          from { opacity: 0; }
          to { opacity: 1; }
        }
        .modal-card {
          background-color: var(--color-bg-card);
          border-radius: var(--border-radius-lg);
          padding: 32px;
          width: 100%;
          max-width: 550px;
          box-shadow: var(--shadow-lg);
          border: 1px solid var(--border-color);
          animation: scaleUp 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
        }
        @keyframes scaleUp {
          from { transform: scale(0.95); opacity: 0; }
          to { transform: scale(1); opacity: 1; }
        }
        .modal-card h3 {
          font-size: 1.3rem;
          margin-bottom: 20px;
          border-bottom: 1px solid var(--border-color);
          padding-bottom: 12px;
        }
        .modal-form {
          display: flex;
          flex-direction: column;
          gap: 16px;
        }
        .textarea-field {
          resize: none;
          font-family: inherit;
        }
        .modal-row {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 16px;
        }
        .full-width-select {
          width: 100%;
          box-shadow: none;
        }
        .modal-actions {
          display: flex;
          justify-content: flex-end;
          gap: 12px;
          margin-top: 10px;
        }
      `}</style>
    </div>
  );
};
export default DashboardView;
