import React from 'react';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  icon?: React.ReactNode;
}

export const Input: React.FC<InputProps> = ({
  label,
  error,
  icon,
  className = '',
  id,
  ...props
}) => {
  const inputId = id || `input-${Math.random().toString(36).substr(2, 5)}`;

  return (
    <div className="input-group">
      {label && (
        <label htmlFor={inputId} className="input-label">
          {label}
        </label>
      )}
      <div className="input-wrapper">
        {icon && <span className="input-icon-left">{icon}</span>}
        <input
          id={inputId}
          className={`input-field ${icon ? 'input-with-icon-left' : ''} ${
            error ? 'input-error' : ''
          } ${className}`}
          {...props}
        />
      </div>
      {error && <span className="input-error-message">{error}</span>}

      <style>{`
        .input-error {
          border-color: var(--color-error) !important;
        }
        .input-error:focus {
          box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1) !important;
        }
        .input-error-message {
          font-size: 0.8rem;
          color: var(--color-error);
          font-weight: 500;
          margin-top: 2px;
        }
      `}</style>
    </div>
  );
};
