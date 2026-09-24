# Ecole - School Management System

A comprehensive microservices-based school management application built with Spring Boot, Angular 17, and Docker.

## 🚀 Features

### Backend (Spring Boot Microservices)
- **Eureka Server** - Service discovery and registration
- **API Gateway** - Single entry point with routing and load balancing
- **Config Server** - Centralized configuration management
- **Auth Service** - Authentication and authorization with JWT and Google OAuth
- **School Service** - School and teacher management with email notifications

### Frontend (Angular 17)
- Modern, responsive UI with Material Design
- JWT-based authentication
- Google OAuth integration
- Role-based access control
- Real-time notifications

### Database
- PostgreSQL for persistent data storage
- Separate databases for auth and school services

### Infrastructure
- Docker Compose for container orchestration
- Nginx for frontend serving and API proxying

## 📁 Project Structure

```
ecole/
├── backend/
│   ├── eureka-server/       # Service Discovery
│   ├── gateway/            # API Gateway
│   ├── config-server/      # Configuration Server
│   ├── auth-service/       # Authentication Service
│   └── school-service/     # School & Teacher Service
├── frontend/
│   └── ecole-frontend/     # Angular 17 Application
├── docker-compose.yml      # Docker Compose Configuration
├── .gitignore
└── README.md
```

## 🛠️ Prerequisites

- Java 17+
- Node.js 18+
- npm or yarn
- Docker & Docker Compose
- PostgreSQL

## 🚀 Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository:
```bash
git clone https://github.com/cstoeckel-dev/ecole.git
cd ecole
```

2. Create environment file:
```bash
cp .env.example .env
# Edit .env with your configuration
```

3. Start all services:
```bash
docker-compose up -d
```

4. Access the application:
- Frontend: http://localhost:4200
- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- Config Server: http://localhost:8888

### Manual Setup

#### Backend

1. Build and run each microservice:
```bash
# Eureka Server
cd backend/eureka-server
mvn spring-boot:run

# Config Server
cd backend/config-server
mvn spring-boot:run

# Gateway
cd backend/gateway
mvn spring-boot:run

# Auth Service
cd backend/auth-service
mvn spring-boot:run

# School Service
cd backend/school-service
mvn spring-boot:run
```

#### Frontend

1. Install dependencies and start:
```bash
cd frontend/ecole-frontend
npm install
ng serve
```

## 🔐 Default Super Admin

- **Email:** superadmin@ecole.com
- **Password:** SuperAdmin123!

## 🌐 API Endpoints

### Auth Service (Port: 9001)
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - User logout
- `GET /api/auth/google` - Get Google OAuth URL
- `GET /api/auth/google/callback` - Google OAuth callback
- `GET /api/auth/me` - Get current user

### School Service (Port: 9002)
- `GET /api/schools` - List all schools
- `GET /api/schools/{id}` - Get school by ID
- `POST /api/schools` - Create new school
- `PUT /api/schools/{id}` - Update school
- `DELETE /api/schools/{id}` - Delete school
- `POST /api/schools/{id}/approve` - Approve school
- `POST /api/schools/{id}/reject` - Reject school
- `POST /api/schools/{id}/logo` - Upload school logo
- `GET /api/schools/{id}/teachers` - Get school teachers
- `GET /api/schools/stats` - Get school statistics

### Teacher Service (Port: 9002)
- `GET /api/teachers` - List all teachers
- `GET /api/teachers/{id}` - Get teacher by ID
- `POST /api/teachers` - Create new teacher
- `PUT /api/teachers/{id}` - Update teacher
- `DELETE /api/teachers/{id}` - Delete teacher
- `POST /api/teachers/{id}/approve` - Approve teacher
- `POST /api/teachers/{id}/reject` - Reject teacher
- `POST /api/teachers/{id}/profile-picture` - Upload profile picture
- `GET /api/teachers/{id}/qualifications` - Get teacher qualifications
- `GET /api/teachers/{id}/experiences` - Get teacher experiences
- `GET /api/teachers/{id}/documents` - Get teacher documents
- `GET /api/teachers/stats` - Get teacher statistics

## 📊 Database Schema

### Auth Service Database (ecole_auth)
- users
- authorities
- refresh_tokens

### School Service Database (ecole_school)
- schools
- teachers
- school_documents
- teacher_documents
- teacher_qualifications
- teacher_experiences

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the root directory:

```env
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=ecole
DB_USERNAME=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=your-super-secret-key
JWT_EXPIRATION=86400000

# Google OAuth
GOOGLE_CLIENT_ID=your-client-id
GOOGLE_CLIENT_SECRET=your-client-secret
GOOGLE_REDIRECT_URI=http://localhost:4200/login/google

# Email
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-email-password
```

## 🐳 Docker Commands

### Build all images:
```bash
docker-compose build
```

### Start all services:
```bash
docker-compose up -d
```

### Stop all services:
```bash
docker-compose down
```

### View logs:
```bash
docker-compose logs -f
```

### View specific service logs:
```bash
docker-compose logs -f ecole-auth
docker-compose logs -f ecole-school
docker-compose logs -f ecole-frontend
```

## 📝 Running Tests

### Backend Tests
```bash
cd backend/auth-service
mvn test

cd backend/school-service
mvn test
```

### Frontend Tests
```bash
cd frontend/ecole-frontend
ng test
```

## 📚 Technologies Used

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Hibernate
- Netflix Eureka
- Spring Cloud Gateway
- Spring Cloud Config
- OpenFeign
- Lombok
- JJWT

### Frontend
- Angular 17
- TypeScript
- RxJS
- Angular Material
- Bootstrap 5
- ngx-toastr
- SweetAlert2
- ngx-spinner
- ngx-cookie-service
- Chart.js
- Font Awesome

### Infrastructure
- Docker
- Docker Compose
- Nginx

## 🎯 Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Frontend (Angular 17)                      │
│                         Port: 4200                              │
└─────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                              │
│                         Port: 8080                              │
└─────────────────────────────────────────────────────────────┘
                                 │
    ┌────────────────────────┬────────────────────────────────┐
    │                        │                                    │
    ▼                        ▼                                    ▼
┌─────────────┐    ┌─────────────────┐            ┌─────────────────┐
│ Auth Service │    │ School Service   │            │ Config Server   │
│ Port: 9001   │    │ Port: 9002       │            │ Port: 8888       │
└─────────────┘    └─────────────────┘            └─────────────────┘
    │                        │                                    │
    ▼                        ▼                                    ▼
┌─────────────┐    ┌─────────────────┐            ┌─────────────────┐
│  PostgreSQL  │    │   PostgreSQL     │            │   Eureka Server  │
│  ecole_auth │    │  ecole_school    │            │   Port: 8761      │
└─────────────┘    └─────────────────┘            └─────────────────┘
```

## 📞 Support

For any issues or questions, please open an issue on GitHub.

## 📄 License

This project is licensed under the MIT License.

## 🙏 Acknowledgments

- Spring Boot Team
- Angular Team
- Docker Team
- All open-source contributors
