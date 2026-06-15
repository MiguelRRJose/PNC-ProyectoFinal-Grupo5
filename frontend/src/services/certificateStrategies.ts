import { CertificateFormat } from '../models/Certificate';

export interface CertificateData {
  studentName: string;
  courseTitle: string;
  issuedAt: string;
  verificationHash: string;
}

// 1. Interfaz común (Strategy)
export interface ICertificateGenerator {
  generate(data: CertificateData): string;
}

// 2. Estrategia PDF
export class PdfCertificateGenerator implements ICertificateGenerator {
  generate(data: CertificateData): string {
    // Simulamos la generación de un documento PDF regresando un "Data URI"
    console.log(`Generando Certificado PDF para ${data.studentName}...`);
    return `data:application/pdf;base64,JVBERi0xLjQKJ...[CERTIFICADO_PDF_PARA_${encodeURIComponent(data.studentName)}_${data.verificationHash}]`;
  }
}

// 3. Estrategia JSON-LD (Metadata semántica)
export class JsonLdCertificateGenerator implements ICertificateGenerator {
  generate(data: CertificateData): string {
    // Simulamos metadatos JSON-LD estructurados para verificación digital
    console.log(`Generando Certificado JSON-LD para ${data.studentName}...`);
    const jsonLd = {
      "@context": "https://w3id.org/credentials/v1",
      "type": ["VerifiableCredential", "CourseCompletionCertificate"],
      "issuer": "https://educanet.uca.edu.sv",
      "issuanceDate": data.issuedAt,
      "credentialSubject": {
        "student": data.studentName,
        "course": data.courseTitle,
        "hash": data.verificationHash
      }
    };
    return `data:application/json;charset=utf-8,${encodeURIComponent(JSON.stringify(jsonLd, null, 2))}`;
  }
}

// 4. Estrategia Imagen (Diploma Visual)
export class ImageCertificateGenerator implements ICertificateGenerator {
  generate(data: CertificateData): string {
    // Simulamos la generación de una imagen SVG/PNG
    console.log(`Generando Certificado Imagen para ${data.studentName}...`);
    const svg = `
      <svg width="800" height="600" xmlns="http://www.w3.org/2000/svg" style="font-family:sans-serif; background:#fff; border:10px solid #3b52ef;">
        <rect width="780" height="580" x="10" y="10" fill="none" stroke="#6366f1" stroke-width="2"/>
        <text x="400" y="120" text-anchor="middle" font-size="40" font-weight="bold" fill="#0f172a">DIPLOMA DE EXCELENCIA</text>
        <text x="400" y="200" text-anchor="middle" font-size="20" fill="#64748b">Otorgado con orgullo a</text>
        <text x="400" y="270" text-anchor="middle" font-size="32" font-weight="bold" fill="#3b52ef">${data.studentName}</text>
        <text x="400" y="340" text-anchor="middle" font-size="18" fill="#64748b">por haber completado satisfactoriamente el curso</text>
        <text x="400" y="390" text-anchor="middle" font-size="24" font-weight="bold" fill="#0f172a">"${data.courseTitle}"</text>
        <text x="400" y="470" text-anchor="middle" font-size="14" fill="#94a3b8">Fecha de emisión: ${data.issuedAt}</text>
        <text x="400" y="510" text-anchor="middle" font-size="12" fill="#94a3b8">Código de Verificación: ${data.verificationHash}</text>
      </svg>
    `;
    return `data:image/svg+xml;utf8,${encodeURIComponent(svg.trim())}`;
  }
}

// 5. Fábrica de Estrategias (Factory Method Pattern)
export class CertificateGeneratorFactory {
  static getGenerator(format: CertificateFormat): ICertificateGenerator {
    switch (format) {
      case 'PDF':
        return new PdfCertificateGenerator();
      case 'JSON-LD':
        return new JsonLdCertificateGenerator();
      case 'Image':
        return new ImageCertificateGenerator();
      default:
        throw new Error(`Formato de certificado no soportado: ${format}`);
    }
  }
}
