import React from 'react';
import { Star, Clock, Heart, BookOpen } from 'lucide-react';
import { Course } from '../models/Course';

interface CourseCardProps {
  course: Course;
  isEnrolled: boolean;
  progressPercent?: number;
  isInWishlist: boolean;
  onSelect: (courseId: string) => void;
  onToggleWishlist?: (courseId: string) => void;
  showWishlistBtn?: boolean;
}

export const CourseCard: React.FC<CourseCardProps> = ({
  course,
  isEnrolled,
  progressPercent = 0,
  isInWishlist,
  onSelect,
  onToggleWishlist,
  showWishlistBtn = true
}) => {
  return (
    <div className="card card-hover course-card">
      <div className="course-card-image">
        <img src={course.imageUrl} alt={course.title} />
        <span className="course-category-badge">{course.category}</span>
        
        {showWishlistBtn && onToggleWishlist && (
          <button
            className={`wishlist-toggle-btn ${isInWishlist ? 'active-wishlist' : ''}`}
            onClick={(e) => {
              e.stopPropagation();
              onToggleWishlist(course.id);
            }}
            title={isInWishlist ? 'Quitar de Favoritos' : 'Guardar en Favoritos'}
          >
            <Heart size={18} fill={isInWishlist ? 'var(--color-error)' : 'none'} />
          </button>
        )}
      </div>

      <div className="course-card-content">
        <h3 className="course-title" onClick={() => onSelect(course.id)}>
          {course.title}
        </h3>
        
        <p className="course-instructor">Por {course.instructorName}</p>

        <div className="course-rating">
          <div className="stars">
            {Array.from({ length: 5 }).map((_, i) => (
              <Star
                key={i}
                size={14}
                fill={i < Math.round(course.rating) ? 'var(--color-accent)' : 'none'}
                color="var(--color-accent)"
              />
            ))}
          </div>
          <span className="rating-number">{course.rating.toFixed(1)}</span>
        </div>

        <p className="course-description">{course.description}</p>

        <div className="course-meta">
          <span className="meta-item">
            <Clock size={14} />
            <span>{course.durationHours}h de clase</span>
          </span>
          <span className="meta-item">
            <BookOpen size={14} />
            <span>{course.modules.length} módulos</span>
          </span>
        </div>

        {isEnrolled ? (
          <div className="progress-section">
            <div className="progress-bar-container">
              <div
                className="progress-bar-fill"
                style={{ width: `${progressPercent}%` }}
              />
            </div>
            <div className="progress-text">
              <span>Progreso</span>
              <span className="progress-percentage">{progressPercent}%</span>
            </div>
          </div>
        ) : (
          <div className="price-section">
            <span className="price-label">Precio</span>
            <span className="price-value">${course.price.toFixed(2)}</span>
          </div>
        )}

        <button className="btn btn-primary btn-full-width" onClick={() => onSelect(course.id)}>
          {isEnrolled ? 'Continuar Aprendiendo' : 'Ver Detalles'}
        </button>
      </div>

      <style>{`
        .course-card {
          display: flex;
          flex-direction: column;
          padding: 0;
          overflow: hidden;
        }
        .course-card-image {
          position: relative;
          height: 180px;
          width: 100%;
          overflow: hidden;
        }
        .course-card-image img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          transition: transform var(--transition-normal);
        }
        .course-card:hover .course-card-image img {
          transform: scale(1.05);
        }
        .course-category-badge {
          position: absolute;
          bottom: 12px;
          left: 12px;
          background-color: rgba(15, 23, 42, 0.75);
          color: #ffffff;
          padding: 4px 10px;
          border-radius: var(--border-radius-sm);
          font-size: 0.75rem;
          font-weight: 600;
          backdrop-filter: blur(4px);
        }
        .wishlist-toggle-btn {
          position: absolute;
          top: 12px;
          right: 12px;
          background-color: #ffffff;
          color: var(--color-text-secondary);
          padding: 8px;
          border-radius: var(--border-radius-full);
          display: flex;
          align-items: center;
          justify-content: center;
          cursor: pointer;
          box-shadow: var(--shadow-sm);
          transition: all var(--transition-fast);
        }
        .wishlist-toggle-btn:hover {
          transform: scale(1.1);
          color: var(--color-error);
        }
        .active-wishlist {
          color: var(--color-error) !important;
        }
        .course-card-content {
          padding: 20px;
          display: flex;
          flex-direction: column;
          gap: 12px;
          flex: 1;
        }
        .course-title {
          font-size: 1.1rem;
          line-height: 1.4;
          font-weight: 700;
          cursor: pointer;
          transition: color var(--transition-fast);
        }
        .course-title:hover {
          color: var(--color-primary);
        }
        .course-instructor {
          font-size: 0.85rem;
          color: var(--color-text-secondary);
          margin-top: -6px;
        }
        .course-rating {
          display: flex;
          align-items: center;
          gap: 6px;
        }
        .stars {
          display: flex;
          gap: 2px;
        }
        .rating-number {
          font-size: 0.85rem;
          font-weight: 600;
          color: var(--color-text-primary);
        }
        .course-description {
          font-size: 0.875rem;
          color: var(--color-text-secondary);
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          line-height: 1.5;
        }
        .course-meta {
          display: flex;
          align-items: center;
          gap: 16px;
          padding-top: 4px;
          border-top: 1px solid var(--border-color);
        }
        .meta-item {
          display: flex;
          align-items: center;
          gap: 6px;
          font-size: 0.8rem;
          color: var(--color-text-secondary);
        }
        .price-section {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding: 8px 0;
        }
        .price-label {
          font-size: 0.85rem;
          color: var(--color-text-secondary);
        }
        .price-value {
          font-size: 1.25rem;
          font-weight: 700;
          color: var(--color-primary);
        }
        .progress-section {
          display: flex;
          flex-direction: column;
          gap: 6px;
          padding: 8px 0;
        }
        .progress-bar-container {
          height: 6px;
          width: 100%;
          background-color: var(--border-color);
          border-radius: var(--border-radius-full);
          overflow: hidden;
        }
        .progress-bar-fill {
          height: 100%;
          background-color: var(--color-success);
          border-radius: var(--border-radius-full);
          transition: width 0.5s ease-out;
        }
        .progress-text {
          display: flex;
          align-items: center;
          justify-content: space-between;
          font-size: 0.8rem;
          color: var(--color-text-secondary);
          font-weight: 500;
        }
        .progress-percentage {
          font-weight: 700;
          color: var(--color-success);
        }
        .btn-full-width {
          width: 100%;
          margin-top: auto;
        }
      `}</style>
    </div>
  );
};
