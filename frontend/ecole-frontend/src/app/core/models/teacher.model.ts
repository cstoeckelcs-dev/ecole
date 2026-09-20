export interface Teacher {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dateOfBirth: Date;
  gender: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  nationalId: string;
  qualification: string;
  specialization: string;
  yearsOfExperience: number;
  emergencyContactName: string;
  emergencyContactPhone: string;
  emergencyContactRelationship: string;
  profilePicture?: string;
  biography: string;
  isActive?: boolean;
  isApproved?: boolean;
  rejectionReason?: string;
  status?: TeacherStatus;
  schoolId?: number;
  schoolName?: string;
}

export enum TeacherStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  ON_LEAVE = 'ON_LEAVE',
  RETIRED = 'RETIRED',
  PENDING = 'PENDING'
}

export interface TeacherRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dateOfBirth: Date;
  gender: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  nationalId: string;
  qualification: string;
  specialization: string;
  yearsOfExperience: number;
  emergencyContactName: string;
  emergencyContactPhone: string;
  emergencyContactRelationship: string;
  biography: string;
  schoolId: number;
}

export interface TeacherResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dateOfBirth: Date;
  gender: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  nationalId: string;
  qualification: string;
  specialization: string;
  yearsOfExperience: number;
  emergencyContactName: string;
  emergencyContactPhone: string;
  emergencyContactRelationship: string;
  profilePicture?: string;
  biography: string;
  isActive?: boolean;
  isApproved?: boolean;
  rejectionReason?: string;
  status?: TeacherStatus;
  schoolId?: number;
  schoolName?: string;
}

export interface TeacherStats {
  totalTeachers: number;
  approvedTeachers: number;
  pendingTeachers: number;
  activeTeachers: number;
}

export interface TeacherSearchParams {
  schoolId?: number;
  specialization?: string;
  isApproved?: boolean;
  isActive?: boolean;
  page?: number;
  size?: number;
}
