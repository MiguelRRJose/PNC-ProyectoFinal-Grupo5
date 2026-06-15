import React from 'react';
import { ArrowLeft, Play, FileText, HelpCircle, CheckCircle, Lock, ShoppingCart, Award, X, CreditCard, Sparkles, ExternalLink, Download, Plus, Trash2, MessageSquare } from 'lucide-react';
import { useCourseDetailController } from '../controllers/useCourseDetailController';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { User } from '../models/User';

interface CourseDetailViewProps {
  courseId: string;
  currentUser: User;
  onUpdateUser: (user: User) => void;
  onBack: () => void;
}

export const CourseDetailView: React.FC<CourseDetailViewProps> = ({
  courseId,
  currentUser,
  onUpdateUser,
  onBack
}) => {
  const c = useCourseDetailController({ courseId, currentUser, onUpdateUser, onBack });

  if (c.isLoading || !c.course) {
    return (
      <div className="loading-state-view">
        <div className="spinner-loader"></div>
        <p>Cargando detalles del curso...</p>
        <style>{`
          .loading-state-view {
            min-height: 80vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 16px;
            color: var(--color-text-secondary);
          }
          .spinner-loader {
            width: 40px;
            height: 40px;
            border: 4px solid var(--border-color);
            border-top-color: var(--color-primary);
            border-radius: 50%;
            animation: spin 1s linear infinite;
          }
        `}</style>
      </div>
    );
  }

  const enrolled = c.isEnrolled() || currentUser.role !== 'student';
  const priceDiscounted = c.course.price - (c.course.price * (c.appliedDiscount / 100));
  const canEdit = currentUser.role === 'admin' || currentUser.id === c.course.instructorId;

  const getLessonIcon = (type: string) => {
    switch (type) {
      case 'video':
        return <Play size={16} />;
      case 'pdf':
        return <FileText size={16} />;
      default:
        return <HelpCircle size={16} />;
    }
  };

  return (
    <div className="course-detail-page">
      {/* Top Bar Navigation */}
      <div className="back-navigation">
        <button onClick={onBack} className="back-btn">
          <ArrowLeft size={18} />
          <span>Volver al Catálogo</span>
        </button>
      </div>

      <div className="course-layout">
        {/* Left Side: Active Lesson / Stripe Purchase Screen */}
        <div className="course-main-pane">
          {!enrolled ? (
            /* Pantalla de Pago Seguro con Stripe */
            <div className="card purchase-wall-card">
              <div className="purchase-banner">
                <ShoppingCart size={40} color="var(--color-primary)" />
                <h2>Acceso Restringido</h2>
                <p>Estás a un paso de desbloquear "{c.course.title}" y llevar tus habilidades al siguiente nivel.</p>
              </div>

              <div className="purchase-grid">
                {/* Desglose de Precios */}
                <div className="price-details-card">
                  <h3>Resumen de Compra</h3>
                  <div className="price-row">
                    <span>Precio del Curso</span>
                    <span>${c.course.price.toFixed(2)}</span>
                  </div>
                  {c.appliedDiscount > 0 && (
                    <div className="price-row discount-row">
                      <span>Descuento ({c.appliedDiscount}%)</span>
                      <span>-${(c.course.price * (c.appliedDiscount / 100)).toFixed(2)}</span>
                    </div>
                  )}
                  <div className="price-row total-row">
                    <span>Total a Pagar</span>
                    <span className="total-value">${priceDiscounted.toFixed(2)}</span>
                  </div>

                  {/* Formulario de Cupón */}
                  <form onSubmit={c.handleApplyCoupon} className="coupon-form">
                    <Input
                      placeholder="Código de cupón"
                      value={c.couponCode}
                      onChange={(e) => c.setCouponCode(e.target.value)}
                      error={c.couponError}
                    />
                    <Button type="submit" variant="secondary" className="coupon-btn">
                      Aplicar
                    </Button>
                  </form>
                  {c.couponSuccess && <p className="coupon-success-msg">{c.couponSuccess}</p>}
                </div>

                {/* Pasarela Stripe Simulada */}
                <div className="stripe-payment-form">
                  <h3>Pago Seguro con Stripe</h3>
                  <p className="stripe-note">Se requiere una tarjeta válida para procesar la transacción.</p>

                  <div className="stripe-inputs">
                    <Input
                      label="Número de Tarjeta"
                      placeholder="4242 •••• •••• 4242"
                      icon={<CreditCard size={18} />}
                      required
                    />
                    <div className="stripe-row">
                      <Input label="Fecha Expiración" placeholder="MM/AA" required />
                      <Input label="CVC" placeholder="123" maxLength={4} required />
                    </div>
                  </div>

                  <Button
                    onClick={c.handleCheckout}
                    isLoading={c.isPurchasing}
                    className="stripe-submit-btn"
                  >
                    Pagar ${priceDiscounted.toFixed(2)} con Stripe
                  </Button>
                  <p className="stripe-secure-text">🔒 Conexión SSL de 256 bits encriptada por Stripe.</p>
                </div>
              </div>
            </div>
          ) : (
            /* Visualización de la Lección Activa */
            <div className="active-lesson-viewer" style={{ display: 'flex', flexDirection: 'column', gap: '20px', backgroundColor: 'transparent', border: 'none', boxShadow: 'none' }}>
              {c.activeLesson ? (
                <>
                  <div className="card lesson-container" style={{ padding: '0', overflow: 'hidden' }}>
                    <div className="lesson-header-title" style={{ padding: '24px', borderBottom: '1px solid var(--border-color)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <h2 style={{ fontSize: '1.25rem' }}>{c.activeLesson.title}</h2>
                      {currentUser.role === 'student' && (
                        <label className="lesson-complete-checkbox">
                          <input
                            type="checkbox"
                            checked={c.isCompleted(c.activeLesson.id)}
                            onChange={(e) => c.handleToggleLessonComplete(c.activeLesson!.id, e.target.checked)}
                          />
                          <span className="checkbox-custom"></span>
                          <span className="checkbox-label">Marcar como Completada</span>
                        </label>
                      )}
                    </div>

                    {/* Renderizado Condicional por Tipo de Lección */}
                    <div className="lesson-content-box" style={{ padding: '24px' }}>
                      {c.activeLesson.type === 'video' && (
                        <div className="video-player-wrapper">
                          {c.activeLesson.contentUrl ? (
                            <video key={c.activeLesson.id} controls className="lesson-video">
                              <source src={c.activeLesson.contentUrl} type="video/mp4" />
                              Tu navegador no soporta reproducción de vídeo HTML5.
                            </video>
                          ) : (
                            <div className="video-placeholder">
                              <Play size={48} />
                              <p>El recurso del vídeo no está disponible.</p>
                            </div>
                          )}
                        </div>
                      )}

                      {c.activeLesson.type === 'pdf' && (
                        <div className="pdf-viewer-box">
                          <FileText size={48} color="var(--color-primary)" />
                          <h3>Documentación Lectura PDF</h3>
                          <p>Haz clic en el botón inferior para abrir la especificación teórica detallada del módulo.</p>
                          <a
                            href={c.activeLesson.contentUrl}
                            target="_blank"
                            rel="noreferrer"
                            className="btn btn-primary"
                          >
                            <ExternalLink size={16} />
                            Abrir PDF en pestaña
                          </a>
                        </div>
                      )}

                      {c.activeLesson.type === 'quiz' && (
                        <div className="quiz-viewer-box">
                          {!c.quizSubmitted ? (
                            <div className="quiz-questions">
                              <h3>Cuestionario de Evaluación del Módulo</h3>
                              <p className="quiz-subtitle">Responde correctamente las siguientes preguntas. Se requiere 70% o más para aprobar la lección.</p>
                              
                              {c.activeLesson.quizQuestions && c.activeLesson.quizQuestions.length > 0 ? (
                                c.activeLesson.quizQuestions.map((q, qIndex) => (
                                  <div key={q.id} className="quiz-question-item">
                                    <p className="question-title">
                                      <strong>Pregunta {qIndex + 1}:</strong> {q.question}
                                    </p>
                                    <div className="quiz-options-list">
                                      {q.options.map((option, optIndex) => (
                                        <button
                                          key={optIndex}
                                          className={`quiz-option-btn ${c.quizAnswers[q.id] === optIndex ? 'selected-option' : ''}`}
                                          onClick={() => c.handleSelectQuizAnswer(q.id, optIndex)}
                                        >
                                          <span className="option-bullet">{String.fromCharCode(65 + optIndex)})</span>
                                          <span className="option-text">{option}</span>
                                        </button>
                                      ))}
                                    </div>
                                  </div>
                                ))
                              ) : (
                                <p style={{ color: 'var(--color-text-secondary)' }}>Este quiz no contiene preguntas todavía.</p>
                              )}

                              {c.activeLesson.quizQuestions && c.activeLesson.quizQuestions.length > 0 && (
                                <div className="quiz-actions">
                                  <Button
                                    onClick={c.handleSubmitQuiz}
                                    disabled={Object.keys(c.quizAnswers).length < (c.activeLesson.quizQuestions?.length || 0)}
                                    className="submit-quiz-btn"
                                  >
                                    Enviar Respuestas Evaluadas
                                  </Button>
                                </div>
                              )}
                            </div>
                          ) : (
                            <div className="quiz-results card">
                              {c.quizPassed ? (
                                <div className="result-banner pass-banner">
                                  <CheckCircle size={48} color="var(--color-success)" />
                                  <h3>¡Cuestionario Aprobado!</h3>
                                  <p className="result-score">Calificación Obtenida: <strong>{c.quizScore}%</strong></p>
                                  <p className="result-desc">El módulo ha sido completado con éxito. Se ha marcado tu progreso de forma automática.</p>
                                </div>
                              ) : (
                                <div className="result-banner fail-banner">
                                  <X size={48} color="var(--color-error)" />
                                  <h3>Cuestionario Reprobado</h3>
                                  <p className="result-score">Calificación Obtenida: <strong>{c.quizScore}%</strong></p>
                                  <p className="result-desc">Necesitas al menos 70% para aprobar. Repasa las lecciones previas e inténtalo de nuevo.</p>
                                  <Button onClick={() => c.setActiveLesson({ ...c.activeLesson! })} style={{ marginTop: '16px' }}>
                                    Reintentar Cuestionario
                                  </Button>
                                </div>
                              )}
                            </div>
                          )}
                        </div>
                      )}
                    </div>
                  </div>

                  {/* SECCIÓN Q&A (Preguntas y Respuestas) de la Lección */}
                  <div className="card qa-section" style={{ padding: '24px' }}>
                    <h3 style={{ fontSize: '1.1rem', marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px', borderBottom: '1px solid var(--border-color)', paddingBottom: '10px' }}>
                      <MessageSquare size={18} color="var(--color-primary)" />
                      Foro de Dudas y Preguntas de la Lección
                    </h3>

                    {currentUser.role === 'student' && (
                      <form onSubmit={c.handleAskQuestion} style={{ display: 'flex', gap: '10px', marginBottom: '24px' }}>
                        <textarea
                          placeholder="Haz una pregunta sobre esta lección..."
                          value={c.questionInputText}
                          onChange={(e) => c.setQuestionInputText(e.target.value)}
                          style={{
                            flex: 1,
                            padding: '12px',
                            border: '1px solid var(--border-color)',
                            borderRadius: '8px',
                            resize: 'none',
                            height: '60px',
                            fontFamily: 'inherit',
                            fontSize: '0.85rem'
                          }}
                          required
                        />
                        <Button type="submit" style={{ height: '60px', padding: '0 20px' }}>Preguntar</Button>
                      </form>
                    )}

                    <div className="questions-list-wrapper" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                      {c.lessonQuestions.map(q => (
                        <div key={q.id} className="qa-item" style={{ borderBottom: '1px solid #f1f5f9', paddingBottom: '14px' }}>
                          <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.8rem', color: 'var(--color-text-secondary)', marginBottom: '4px' }}>
                            <strong>{q.studentName}</strong>
                            <span>{new Date(q.createdAt).toLocaleDateString()}</span>
                          </div>
                          <p style={{ fontSize: '0.9rem', color: 'var(--color-text-primary)', marginBottom: '8px' }}>{q.questionText}</p>
                          
                          {q.answerText ? (
                            <div className="instructor-answer" style={{
                              marginLeft: '20px',
                              backgroundColor: 'var(--color-primary-light)',
                              padding: '10px 14px',
                              borderRadius: '8px',
                              borderLeft: '3px solid var(--color-primary)'
                            }}>
                              <p style={{ fontSize: '0.75rem', fontWeight: '700', color: 'var(--color-primary)', marginBottom: '2px' }}>
                                Respuesta del Instructor:
                              </p>
                              <p style={{ fontSize: '0.85rem', color: 'var(--color-text-primary)' }}>{q.answerText}</p>
                            </div>
                          ) : (
                            <p style={{ fontSize: '0.75rem', color: 'var(--color-text-muted)', fontStyle: 'italic', marginLeft: '20px' }}>
                              Pendiente de respuesta por el instructor...
                            </p>
                          )}
                        </div>
                      ))}
                      {c.lessonQuestions.length === 0 && (
                        <p style={{ textAlign: 'center', fontSize: '0.85rem', color: 'var(--color-text-muted)', padding: '16px' }}>
                          No hay preguntas registradas en esta lección. ¡Sé el primero en formular una duda!
                        </p>
                      )}
                    </div>
                  </div>
                </>
              ) : (
                <div className="card no-lesson-state" style={{ padding: '60px', textAlign: 'center', color: 'var(--color-text-secondary)' }}>
                  <BookOpen size={48} style={{ margin: '0 auto 16px auto', display: 'block', opacity: 0.5 }} />
                  <p>Selecciona una lección del menú lateral para comenzar a aprender.</p>
                </div>
              )}
            </div>
          )}
        </div>

        {/* Right Side: Modules and Lessons Hierarchy */}
        <aside className="course-curriculum-panel">
          <div className="curriculum-header">
            <h3>Contenido del Curso</h3>
            {currentUser.role === 'student' && (
              <div className="curriculum-progress">
                <div className="progress-label-row">
                  <span>Progreso General</span>
                  <span className="percentage-bold">{c.progressPercent}%</span>
                </div>
                <div className="bar-bg">
                  <div className="bar-fill" style={{ width: `${c.progressPercent}%` }}></div>
                </div>
              </div>
            )}
          </div>

          <div className="modules-list">
            {c.course.modules.map(mod => (
              <div key={mod.id} className="module-accordion-item">
                <div className="module-header-title" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <h4>{mod.title}</h4>
                  {canEdit && (
                    <button
                      onClick={() => c.handleOpenAddLessonModal(mod.id)}
                      title="Agregar Contenido/Quiz"
                      style={{ cursor: 'pointer', color: 'var(--color-primary)', display: 'flex', alignItems: 'center' }}
                    >
                      <Plus size={16} />
                    </button>
                  )}
                </div>

                <div className="lessons-sublist">
                  {mod.lessons.map(lesson => {
                    const active = c.activeLesson?.id === lesson.id;
                    const completed = c.isCompleted(lesson.id);

                    return (
                      <button
                        key={lesson.id}
                        className={`curriculum-lesson-item ${active ? 'active-lesson-item' : ''} ${!enrolled ? 'locked-lesson-item' : ''}`}
                        onClick={() => enrolled && c.setActiveLesson(lesson)}
                        disabled={!enrolled}
                      >
                        <div className="lesson-left-title">
                          <span className="lesson-type-icon">
                            {!enrolled ? <Lock size={14} /> : getLessonIcon(lesson.type)}
                          </span>
                          <span className="lesson-label-txt">{lesson.title}</span>
                        </div>

                        {enrolled && completed && (
                          <span className="completed-check-icon">
                            <CheckCircle size={16} />
                          </span>
                        )}
                      </button>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>

          {canEdit && (
            <div style={{ padding: '16px', borderTop: '1px solid var(--border-color)' }}>
              <Button
                variant="secondary"
                icon={<Plus size={16} />}
                onClick={() => c.setShowAddModuleModal(true)}
                style={{ width: '100%', fontSize: '0.85rem' }}
              >
                Agregar Módulo
              </Button>
            </div>
          )}
        </aside>
      </div>

      {/* MODAL PERSONALIZADO DE PAGO EXITOSO CON STRIPE */}
      {c.showPaymentSuccessModal && (
        <div className="modal-overlay">
          <div className="modal-card" style={{ maxWidth: '420px', textAlign: 'center', padding: '40px 24px' }}>
            <div style={{
              width: '64px',
              height: '64px',
              borderRadius: '50%',
              backgroundColor: 'var(--color-success-light)',
              color: 'var(--color-success)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 20px auto'
            }}>
              <CheckCircle size={32} />
            </div>
            <h3 style={{ borderBottom: 'none', marginBottom: '8px', paddingBottom: '0' }}>¡Pago Procesado con Éxito!</h3>
            <p style={{ color: 'var(--color-text-secondary)', fontSize: '0.9rem', marginBottom: '24px' }}>
              Tu transacción ha sido validada por Stripe. El acceso completo al curso "{c.course.title}" se encuentra ahora desbloqueado.
            </p>
            <Button onClick={c.handleClosePaymentSuccess} style={{ width: '100%' }}>
              Comenzar Curso
            </Button>
          </div>
        </div>
      )}

      {/* POPUP CELEBRACIÓN DE CERTIFICADO (STRATEGY PATTERN VISUAL) */}
      {c.newCertificate && (
        <div className="modal-overlay">
          <div className="modal-card cert-celebration-card" style={{ maxWidth: '600px', textAlign: 'center' }}>
            <div className="sparkle-layer">
              <Sparkles size={40} className="sparkle-icon" />
            </div>

            <Award size={64} className="gold-medal" />
            
            <h2>¡Felicitaciones Académicas!</h2>
            <p className="student-congratulations">
              <strong>{c.newCertificate.studentName}</strong>, has completado exitosamente todas las lecciones y cuestionarios de:
            </p>
            <h3 className="certified-course-title">"{c.newCertificate.courseTitle}"</h3>
            
            <p className="cert-explanation">
              Tu diploma ha sido generado automáticamente de forma inmutable bajo la estrategia de diseño web segura.
            </p>

            <div className="verification-box">
              <p className="code-lbl">Código Único de Registro:</p>
              <p className="code-val">{c.newCertificate.verificationHash}</p>
            </div>

            <div className="cert-modal-actions" style={{ margin: '0 auto' }}>
              <a
                href={c.newCertificate.certificateUrl}
                download={`certificado_${c.newCertificate.courseId}.svg`}
                className="btn btn-primary"
                onClick={() => c.setNewCertificate(null)}
              >
                <Download size={18} />
                Descargar Diploma
              </a>
              <button className="btn btn-ghost" onClick={() => c.setNewCertificate(null)}>
                Cerrar Ventana
              </button>
            </div>
          </div>
        </div>
      )}

      {/* MODAL: AGREGAR MÓDULO (ADMIN / INSTRUCTOR) */}
      {c.showAddModuleModal && (
        <div className="modal-overlay" onClick={() => c.setShowAddModuleModal(false)}>
          <div className="modal-card" onClick={(e) => e.stopPropagation()} style={{ maxWidth: '450px' }}>
            <h3>Agregar Nuevo Módulo</h3>
            <form onSubmit={c.handleAddModule} className="modal-form">
              <Input
                label="Título del Módulo"
                placeholder="Ej. Módulo 3: Pruebas y Buenas Prácticas"
                value={c.newModuleTitle}
                onChange={(e) => c.setNewModuleTitle(e.target.value)}
                required
              />
              <div className="modal-actions">
                <Button type="button" variant="ghost" onClick={() => c.setShowAddModuleModal(false)}>
                  Cancelar
                </Button>
                <Button type="submit">
                  Guardar Módulo
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL: AGREGAR CONTENIDO / LECCIÓN / QUIZ (ADMIN / INSTRUCTOR) */}
      {c.showAddLessonModal && (
        <div className="modal-overlay" onClick={() => c.setShowAddLessonModal(false)}>
          <div className="modal-card" onClick={(e) => e.stopPropagation()} style={{ maxWidth: '600px', maxHeight: '90vh', overflowY: 'auto' }}>
            <h3>Agregar Contenido al Módulo</h3>
            <form onSubmit={c.handleAddLesson} className="modal-form">
              <Input
                label="Título del Contenido"
                placeholder="Ej. 1.3 Implementación Práctica de Strategy"
                value={c.newLessonTitle}
                onChange={(e) => c.setNewLessonTitle(e.target.value)}
                required
              />

              <div className="modal-row">
                <div className="input-group">
                  <label className="input-label">Tipo de Contenido</label>
                  <select
                    value={c.newLessonType}
                    onChange={(e) => c.setNewLessonType(e.target.value as any)}
                    className="filter-select full-width-select"
                  >
                    <option value="video">Vídeo Explicativo</option>
                    <option value="pdf">Lectura PDF</option>
                    <option value="quiz">Quiz Evaluado</option>
                  </select>
                </div>

                <Input
                  label="Duración Estimada (Minutos)"
                  type="number"
                  min="1"
                  value={c.newLessonDuration}
                  onChange={(e) => c.setNewLessonDuration(Number(e.target.value))}
                  required
                />
              </div>

              {c.newLessonType !== 'quiz' ? (
                <Input
                  label="URL del Contenido (Opcional)"
                  placeholder={c.newLessonType === 'video' ? "https://ejemplo.com/video.mp4" : "https://ejemplo.com/documento.pdf"}
                  value={c.newLessonUrl}
                  onChange={(e) => c.setNewLessonUrl(e.target.value)}
                />
              ) : (
                /* Quiz Builder */
                <div className="quiz-builder-section" style={{ border: '1px dashed var(--border-color)', borderRadius: '12px', padding: '16px', backgroundColor: 'var(--color-bg-app)' }}>
                  <h4 style={{ fontSize: '0.9rem', marginBottom: '12px', color: 'var(--color-text-secondary)' }}>Creador de Preguntas del Quiz</h4>
                  
                  <Input
                    label="Pregunta"
                    placeholder="Escribe el enunciado de la pregunta..."
                    value={c.currentQuestionText}
                    onChange={(e) => c.setCurrentQuestionText(e.target.value)}
                  />

                  <div className="quiz-builder-options" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
                    {c.currentQuestionOptions.map((opt, oIdx) => (
                      <Input
                        key={oIdx}
                        label={`Opción ${String.fromCharCode(65 + oIdx)}`}
                        placeholder={`Respuesta ${oIdx + 1}`}
                        value={opt}
                        onChange={(e) => {
                          const updated = [...c.currentQuestionOptions];
                          updated[oIdx] = e.target.value;
                          c.setCurrentQuestionOptions(updated);
                        }}
                      />
                    ))}
                  </div>

                  <div className="input-group" style={{ marginTop: '8px' }}>
                    <label className="input-label">Opción Correcta</label>
                    <select
                      value={c.currentQuestionCorrectIndex}
                      onChange={(e) => c.setCurrentQuestionCorrectIndex(Number(e.target.value))}
                      className="filter-select full-width-select"
                    >
                      <option value={0}>Opción A</option>
                      <option value={1}>Opción B</option>
                      <option value={2}>Opción C</option>
                      <option value={3}>Opción D</option>
                    </select>
                  </div>

                  <Button
                    type="button"
                    variant="secondary"
                    onClick={c.handleAddQuizQuestion}
                    disabled={!c.currentQuestionText.trim() || c.currentQuestionOptions.some(o => !o.trim())}
                    style={{ width: '100%', marginTop: '12px', fontSize: '0.85rem' }}
                  >
                    Agregar Pregunta a la Lista
                  </Button>

                  {/* List of Added Questions */}
                  {c.quizQuestionsList.length > 0 && (
                    <div className="added-questions-list" style={{ marginTop: '16px', borderTop: '1px solid var(--border-color)', paddingTop: '16px' }}>
                      <p style={{ fontSize: '0.85rem', fontWeight: '600', marginBottom: '8px' }}>Preguntas Agregadas ({c.quizQuestionsList.length}):</p>
                      <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                        {c.quizQuestionsList.map((q, idx) => (
                          <div key={idx} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '8px 12px', backgroundColor: '#fff', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
                            <span style={{ fontSize: '0.8rem', color: 'var(--color-text-primary)', textOverflow: 'ellipsis', overflow: 'hidden', whiteSpace: 'nowrap', maxWidth: '80%' }}>
                              {idx + 1}. {q.question}
                            </span>
                            <button
                              type="button"
                              onClick={() => c.handleRemoveQuizQuestion(idx)}
                              style={{ color: 'var(--color-error)', cursor: 'pointer', display: 'flex' }}
                            >
                              <Trash2 size={14} />
                            </button>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              )}

              <div className="modal-actions" style={{ marginTop: '16px' }}>
                <Button type="button" variant="ghost" onClick={() => c.setShowAddLessonModal(false)}>
                  Cancelar
                </Button>
                <Button
                  type="submit"
                  disabled={c.newLessonType === 'quiz' && c.quizQuestionsList.length === 0}
                >
                  Guardar Contenido
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}

      <style>{`
        .course-detail-page {
          display: flex;
          flex-direction: column;
          gap: 20px;
          animation: fadeIn 0.4s ease;
        }
        .back-navigation {
          border-bottom: 1px solid var(--border-color);
          padding-bottom: 16px;
        }
        .back-btn {
          display: flex;
          align-items: center;
          gap: 8px;
          font-weight: 600;
          color: var(--color-text-secondary);
          cursor: pointer;
          transition: color var(--transition-fast);
        }
        .back-btn:hover {
          color: var(--color-primary);
        }
        .course-layout {
          display: grid;
          grid-template-columns: 1fr 320px;
          gap: 24px;
          align-items: start;
        }
        
        .course-main-pane {
          min-height: 500px;
        }

        /* Bloqueo de Compra y Stripe */
        .purchase-wall-card {
          padding: 40px;
          display: flex;
          flex-direction: column;
          gap: 32px;
        }
        .purchase-banner {
          text-align: center;
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 12px;
          max-width: 600px;
          margin: 0 auto;
        }
        .purchase-banner h2 {
          font-size: 1.5rem;
          font-weight: 800;
        }
        .purchase-banner p {
          color: var(--color-text-secondary);
        }
        .purchase-grid {
          display: grid;
          grid-template-columns: 1fr 1.2fr;
          gap: 32px;
          border-top: 1px solid var(--border-color);
          padding-top: 32px;
        }
        .price-details-card {
          background-color: var(--color-bg-app);
          border-radius: var(--border-radius-lg);
          padding: 24px;
          display: flex;
          flex-direction: column;
          gap: 16px;
        }
        .price-details-card h3 {
          font-size: 1.1rem;
          border-bottom: 1px solid var(--border-color);
          padding-bottom: 10px;
        }
        .price-row {
          display: flex;
          justify-content: space-between;
          font-size: 0.95rem;
          color: var(--color-text-secondary);
        }
        .discount-row {
          color: var(--color-error);
          font-weight: 500;
        }
        .total-row {
          border-top: 1px dashed var(--border-color);
          padding-top: 12px;
          font-weight: 700;
          color: var(--color-text-primary);
          font-size: 1.1rem;
        }
        .total-value {
          color: var(--color-primary);
          font-size: 1.3rem;
        }
        .coupon-form {
          display: grid;
          grid-template-columns: 1fr auto;
          gap: 8px;
          align-items: end;
          margin-top: 10px;
        }
        .coupon-form .input-group {
          margin-bottom: 0;
        }
        .coupon-btn {
          height: 44px;
          padding: 0 16px;
        }
        .coupon-success-msg {
          font-size: 0.8rem;
          color: var(--color-success);
          font-weight: 600;
        }
        
        .stripe-payment-form {
          display: flex;
          flex-direction: column;
          gap: 16px;
        }
        .stripe-payment-form h3 {
          font-size: 1.1rem;
        }
        .stripe-note {
          font-size: 0.85rem;
          color: var(--color-text-secondary);
          margin-top: -8px;
        }
        .stripe-inputs {
          display: flex;
          flex-direction: column;
          gap: 4px;
        }
        .stripe-row {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 16px;
        }
        .stripe-submit-btn {
          width: 100%;
          padding: 14px;
          font-size: 1rem;
        }
        .stripe-secure-text {
          font-size: 0.75rem;
          color: var(--color-text-muted);
          text-align: center;
        }

        /* Reproductor de Lecciones */
        .video-player-wrapper {
          width: 100%;
          background-color: #000;
          border-radius: var(--border-radius-md);
          overflow: hidden;
          aspect-ratio: 16 / 9;
          box-shadow: var(--shadow-md);
        }
        .lesson-video {
          width: 100%;
          height: 100%;
          display: block;
        }
        .video-placeholder {
          height: 100%;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          gap: 12px;
          color: var(--color-text-muted);
        }
        .pdf-viewer-box {
          text-align: center;
          padding: 40px 20px;
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 16px;
          border: 1px dashed var(--border-color);
          border-radius: var(--border-radius-md);
        }
        .pdf-viewer-box h3 {
          font-size: 1.15rem;
        }
        .pdf-viewer-box p {
          font-size: 0.9rem;
          color: var(--color-text-secondary);
          max-width: 450px;
          margin-bottom: 8px;
        }

        /* Quizzes */
        .quiz-viewer-box {
          padding: 8px 0;
        }
        .quiz-subtitle {
          font-size: 0.9rem;
          color: var(--color-text-secondary);
          margin-bottom: 24px;
        }
        .quiz-question-item {
          margin-bottom: 28px;
          border-bottom: 1px solid var(--border-color);
          padding-bottom: 24px;
        }
        .quiz-question-item:last-child {
          border-bottom: none;
          padding-bottom: 0;
        }
        .question-title {
          font-size: 1rem;
          color: var(--color-text-primary);
          margin-bottom: 16px;
        }
        .quiz-options-list {
          display: flex;
          flex-direction: column;
          gap: 10px;
        }
        .quiz-option-btn {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 14px 20px;
          border: 1px solid var(--border-color);
          border-radius: var(--border-radius-md);
          background-color: var(--color-bg-app);
          cursor: pointer;
          text-align: left;
          transition: all var(--transition-fast);
          width: 100%;
        }
        .quiz-option-btn:hover {
          border-color: var(--color-primary);
          background-color: var(--color-primary-light);
        }
        .selected-option {
          border-color: var(--color-primary) !important;
          background-color: var(--color-primary-light) !important;
          font-weight: 600;
          color: var(--color-primary);
        }
        .option-bullet {
          font-weight: 700;
          font-size: 0.9rem;
        }
        .option-text {
          font-size: 0.95rem;
        }
        .quiz-actions {
          display: flex;
          justify-content: flex-end;
          margin-top: 16px;
        }
        .submit-quiz-btn {
          padding: 12px 24px;
        }

        .result-banner {
          text-align: center;
          padding: 40px 20px;
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 12px;
        }
        .result-score {
          font-size: 1.2rem;
          color: var(--color-text-primary);
        }
        .result-score strong {
          font-size: 1.6rem;
          color: var(--color-primary);
        }
        .result-desc {
          font-size: 0.9rem;
          color: var(--color-text-secondary);
          max-width: 400px;
        }
        .pass-banner {
          background-color: var(--color-success-light);
          border: 1px solid rgba(16, 185, 129, 0.2);
          border-radius: var(--border-radius-lg);
        }
        .fail-banner {
          background-color: var(--color-error-light);
          border: 1px solid rgba(239, 68, 68, 0.2);
          border-radius: var(--border-radius-lg);
        }

        /* Menú de Módulos (Derecha) */
        .course-curriculum-panel {
          background-color: var(--color-bg-card);
          border-radius: var(--border-radius-lg);
          border: 1px solid var(--border-color);
          box-shadow: var(--shadow-md);
          overflow: hidden;
          position: sticky;
          top: 90px;
        }
        .curriculum-header {
          padding: 20px;
          border-bottom: 1px solid var(--border-color);
          display: flex;
          flex-direction: column;
          gap: 12px;
        }
        .curriculum-header h3 {
          font-size: 1.1rem;
          font-weight: 700;
        }
        .curriculum-progress {
          display: flex;
          flex-direction: column;
          gap: 6px;
        }
        .progress-label-row {
          display: flex;
          justify-content: space-between;
          font-size: 0.8rem;
          color: var(--color-text-secondary);
          font-weight: 500;
        }
        .percentage-bold {
          font-weight: 700;
          color: var(--color-success);
        }
        .bar-bg {
          height: 6px;
          background-color: var(--border-color);
          border-radius: var(--border-radius-full);
          overflow: hidden;
        }
        .bar-fill {
          height: 100%;
          background-color: var(--color-success);
          border-radius: var(--border-radius-full);
          transition: width 0.4s ease;
        }

        .modules-list {
          display: flex;
          flex-direction: column;
        }
        .module-accordion-item {
          border-bottom: 1px solid var(--border-color);
        }
        .module-accordion-item:last-child {
          border-bottom: none;
        }
        .module-header-title {
          background-color: var(--color-bg-app);
          padding: 14px 20px;
        }
        .module-header-title h4 {
          font-size: 0.85rem;
          font-weight: 700;
          color: var(--color-text-secondary);
          text-transform: uppercase;
          letter-spacing: 0.03em;
        }
        
        .lessons-sublist {
          display: flex;
          flex-direction: column;
        }
        .curriculum-lesson-item {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding: 12px 20px;
          border-bottom: 1px solid #f1f5f9;
          cursor: pointer;
          background-color: transparent;
          transition: all var(--transition-fast);
          width: 100%;
          text-align: left;
        }
        .curriculum-lesson-item:last-child {
          border-bottom: none;
        }
        .curriculum-lesson-item:hover:not(:disabled) {
          background-color: #f1f5f9;
        }
        .active-lesson-item {
          background-color: var(--color-primary-light) !important;
          border-left: 3px solid var(--color-primary);
        }
        .active-lesson-item .lesson-label-txt {
          color: var(--color-primary);
          font-weight: 600;
        }
        .locked-lesson-item {
          opacity: 0.6;
          cursor: not-allowed;
        }
        .lesson-left-title {
          display: flex;
          align-items: center;
          gap: 10px;
          flex: 1;
        }
        .lesson-type-icon {
          display: flex;
          align-items: center;
          color: var(--color-text-muted);
        }
        .lesson-label-txt {
          font-size: 0.85rem;
          color: var(--color-text-primary);
          line-height: 1.3;
        }
        .completed-check-icon {
          display: flex;
          align-items: center;
          color: var(--color-success);
        }

        /* Celebración de Certificado */
        .cert-celebration-card {
          position: relative;
          padding: 40px;
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 20px;
        }
        .sparkle-layer {
          position: absolute;
          top: 20px;
          left: 50%;
          transform: translateX(-50%);
          color: var(--color-accent);
          animation: pulse 2s infinite;
        }
        @keyframes pulse {
          0%, 100% { transform: translateX(-50%) scale(1); opacity: 0.8; }
          50% { transform: translateX(-50%) scale(1.2); opacity: 1; }
        }
        .gold-medal {
          color: #fbbf24;
          filter: drop-shadow(0 4px 10px rgba(251, 191, 36, 0.4));
          animation: float 3s ease-in-out infinite;
        }
        @keyframes float {
          0%, 100% { transform: translateY(0); }
          50% { transform: translateY(-8px); }
        }
        .student-congratulations {
          font-size: 1.05rem;
          color: var(--color-text-secondary);
        }
        .certified-course-title {
          font-size: 1.4rem;
          font-weight: 800;
          color: var(--color-primary);
          line-height: 1.3;
        }
        .cert-explanation {
          font-size: 0.85rem;
          color: var(--color-text-secondary);
          max-width: 450px;
        }
        .verification-box {
          background-color: var(--color-bg-app);
          border: 1px solid var(--border-color);
          border-radius: var(--border-radius-md);
          padding: 10px 20px;
        }
        .code-lbl {
          font-size: 0.75rem;
          color: var(--color-text-muted);
          font-weight: 500;
          text-transform: uppercase;
        }
        .code-val {
          font-family: monospace;
          font-size: 0.9rem;
          font-weight: 700;
          color: var(--color-text-primary);
        }
        .cert-modal-actions {
          display: flex;
          flex-direction: column;
          gap: 10px;
          width: 100%;
          max-width: 320px;
        }
      `}</style>
    </div>
  );
};
export default CourseDetailView;
