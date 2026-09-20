export interface School {
  id?: number;
  name: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  email: string;
  phone: string;
  website: string;
  foundedDate: Date;
  principalName: string;
  principalEmail: string;
  principalPhone: string;
  accreditationNumber: string;
  licenseNumber: string;
  logoUrl?: string;
  description: string;
  isActive?: boolean;
  isApproved?: boolean;
  rejectionReason?: string;
  studentCount?: number;
  teacherCount?: number;
  classroomCount?: number;
  type?: SchoolType;
  level?: SchoolLevel;
}

export enum SchoolType {
  PUBLIC = 'PUBLIC',
  PRIVATE = 'PRIVATE',
  INTERNATIONAL = 'INTERNATIONAL',
  RELIGIOUS = 'RELIGIOUS',
  SPECIAL = 'SPECIAL'
}

export enum SchoolLevel {
  PRESCHOOL = 'PRESCHOOL',
  PRIMARY = 'PRIMARY',
  SECONDARY = 'SECONDARY',
  HIGH_SCHOOL = 'HIGH_SCHOOL',
  VOCATIONAL = 'VOCATIONAL',
  UNIVERSITY = 'UNIVERSITY'
}

export interface SchoolRequest {
  name: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  email: string;
  phone: string;
  website: string;
  foundedDate: Date;
  principalName: string;
  principalEmail: string;
  principalPhone: string;
  accreditationNumber: string;
  licenseNumber: string;
  description: string;
  type?: SchoolType;
  level?: SchoolLevel;
}

export interface SchoolResponse {
  id: number;
  name: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  email: string;
  phone: string;
  website: string;
  foundedDate: Date;
  principalName: string;
  principalEmail: string;
  principalPhone: string;
  accreditationNumber: string;
  licenseNumber: string;
  logoUrl?: string;
  description: string;
  isActive?: boolean;
  isApproved?: boolean;
  rejectionReason?: string;
  studentCount?: number;
  teacherCount?: number;
  classroomCount?: number;
  type?: SchoolType;
  level?: SchoolLevel;
}

export interface SchoolStats {
  totalSchools: number;
  approvedSchools: number;
  pendingSchools: number;
  activeSchools: number;
}

export interface SchoolSearchParams {
  name?: string;
  city?: string;
  country?: string;
  type?: SchoolType;
  level?: SchoolLevel;
  isApproved?: boolean;
  isActive?: boolean;
  page?: number;
  size?: number;
}
