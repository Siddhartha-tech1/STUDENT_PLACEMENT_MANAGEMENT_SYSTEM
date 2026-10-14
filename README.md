# Student Placement Management System

A full-stack college placement project with a React/Vite frontend and a Spring Boot backend. Students can maintain profiles, upload resumes, browse placement drives, check eligibility, apply for jobs, and track status. Companies can create drives, review applicants, update statuses, and schedule interviews. Administrators can monitor users, drives, applications, and placement statistics.

## Technology Stack

- Frontend: React, Vite, JavaScript, Bootstrap 5, React Router, Axios, Lucide React
- Backend: Java 21, Spring Boot, Spring Web, Spring Data MongoDB, Spring Security, JWT, Maven
- Database: MongoDB
- Testing: JUnit, Spring Boot Test, MockMvc-ready setup

## Project Structure

```text
frontend/   React Vite application
backend/    Spring Boot REST API
README.md   Setup and usage guide
```

## Prerequisites

- Java 21
- Maven 3.9+ or Maven Wrapper
- Node.js 20+ and npm
- MongoDB Community Server or a MongoDB Atlas cluster

## Database Setup

Copy `backend/.env.example` to your local environment values. Do not commit real secrets.

Required backend variables:

- `MONGODB_URI` (must include a database name, for example `mongodb://localhost:27017/student_placement_db`)
- `JWT_SECRET`
- `ADMIN_EMAIL`
- `ADMIN_PASSWORD`
- `RESUME_STORAGE_DIR`

The first administrator is created only when `ADMIN_EMAIL` and `ADMIN_PASSWORD` are supplied and the email does not already exist.

## Run The Backend

From `backend/` on Windows PowerShell:

For a local MongoDB instance:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

The backend automatically reads `backend/.env` on startup. Environment variables set in PowerShell take priority over values in the file.

The API runs at `http://localhost:8080/api`.

## Run The Frontend

From `frontend/`:

```powershell
npm install
npm run dev
```

The frontend runs at `http://localhost:5173`.

## Deploy on Render

This repository includes a `render.yaml` Blueprint that creates two services:

- `spms-api`: Spring Boot API built from `backend/Dockerfile`
- `spms-frontend`: React/Vite static site with an SPA rewrite rule

In Render, choose **New > Blueprint** and select this repository. Render will request the values marked as secrets. Set them as follows:

- `MONGODB_URI`: your MongoDB Atlas URI, including the database name
- `JWT_SECRET`: allow Render to generate it
- `ADMIN_EMAIL` and `ADMIN_PASSWORD`: optional initial administrator credentials
- `VITE_API_BASE_URL`: `https://<your-api-service>.onrender.com/api`
- `CORS_ALLOWED_ORIGINS`: `https://<your-frontend-site>.onrender.com`

Because the frontend's Vite variables are embedded at build time, save `VITE_API_BASE_URL` with a rebuild after the API has its Render URL. Save `CORS_ALLOWED_ORIGINS` and redeploy the API after the frontend has its URL. Keep the Atlas database user credentials only in Render's environment settings, never in Git.

## Build And Test

Backend:

```powershell
cd backend
mvn test
```

Frontend:

```powershell
cd frontend
npm install
npm test
npm run build
```

## Main REST APIs

- `POST /api/auth/register` — register student or company
- `POST /api/auth/login` — authenticate and return JWT token
- `GET /api/students/me` — fetch student profile
- `PUT /api/students/me` — update student profile
- `POST /api/students/me/resume` — upload PDF resume
- `GET /api/students/me/resume` — download student resume
- `GET /api/companies/me` — fetch company profile
- `PUT /api/companies/me` — update company profile
- `GET /api/companies/me/drives` — fetch drives owned by authenticated company
- `GET /api/placement-drives` — search and browse placement drives
- `POST /api/placement-drives` — create placement drive
- `GET /api/placement-drives/{id}/eligibility` — check student eligibility
- `POST /api/applications` — apply to placement drive
- `GET /api/applications/my` — fetch authenticated student applications
- `GET /api/applications/drive/{id}` — fetch applicants for owned drive
- `PATCH /api/applications/{id}/status` — update application status
- `GET /api/applications/{id}/resume` — authorized resume download
- `POST /api/interviews` — schedule interview
- `GET /api/interviews/my` — fetch user interview schedules
- `GET /api/admin/dashboard` — admin placement metrics & statistics

## Security Notes

- Passwords are hashed with BCrypt.
- JWT is required for protected APIs.
- Public registration cannot create admin accounts.
- Resume uploads accept PDF content only and are stored outside public frontend folders.
- Students, companies, and admins are authorized separately on the backend.
- Company users can manage only their own drives and applicants.

## Common Setup Issues

- MongoDB connection fails: confirm `MONGODB_URI` includes a database name and that your local MongoDB server or Atlas IP-access list permits the connection.
- Frontend cannot reach backend: confirm backend is on port `8080` and `VITE_API_BASE_URL` is correct.
- Admin user not created: set `ADMIN_EMAIL` and `ADMIN_PASSWORD` before the first backend startup.
- Resume upload fails: upload a valid PDF under 5 MB and ensure `RESUME_STORAGE_DIR` is writable.
