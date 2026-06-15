export type CertificateFormat = 'PDF' | 'JSON-LD' | 'Image';

export interface Certificate {
  id: string;
  studentId: string;
  studentName: string;
  courseId: string;
  courseTitle: string;
  issuedAt: string;
  formatType: CertificateFormat;
  certificateUrl: string;
  verificationHash: string; // Anti-fraud unique identifier
}
