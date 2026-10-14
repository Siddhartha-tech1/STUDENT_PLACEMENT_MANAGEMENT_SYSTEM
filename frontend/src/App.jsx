import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import DashboardLayout from './layouts/DashboardLayout';
import { useAuth } from './context/AuthContext';
import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';
import Drives from './pages/Drives';
import DriveDetails from './pages/DriveDetails';
import Dashboard from './pages/Dashboard';
import Profile from './pages/Profile';
import Applications from './pages/Applications';
import CompanyDrives from './pages/CompanyDrives';
import Applicants from './pages/Applicants';
import Interviews from './pages/Interviews';
import AdminLists from './pages/AdminLists';
import Unauthorized from './pages/Unauthorized';
import NotFound from './pages/NotFound';

function Protected({ roles, children }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (roles && !roles.includes(user.role)) return <Navigate to="/unauthorized" replace />;
  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register/:role" element={<Register />} />
        <Route path="/drives" element={<Drives />} />
        <Route path="/drives/:id" element={<DriveDetails />} />
        <Route path="/unauthorized" element={<Unauthorized />} />
        <Route element={<Protected><DashboardLayout /></Protected>}>
          <Route path="/student/dashboard" element={<Protected roles={['STUDENT']}><Dashboard role="student" /></Protected>} />
          <Route path="/student/profile" element={<Protected roles={['STUDENT']}><Profile role="student" /></Protected>} />
          <Route path="/student/applications" element={<Protected roles={['STUDENT']}><Applications /></Protected>} />
          <Route path="/student/interviews" element={<Protected roles={['STUDENT']}><Interviews /></Protected>} />
          <Route path="/company/dashboard" element={<Protected roles={['COMPANY']}><Dashboard role="company" /></Protected>} />
          <Route path="/company/profile" element={<Protected roles={['COMPANY']}><Profile role="company" /></Protected>} />
          <Route path="/company/drives" element={<Protected roles={['COMPANY']}><CompanyDrives /></Protected>} />
          <Route path="/company/applicants" element={<Protected roles={['COMPANY']}><Applicants /></Protected>} />
          <Route path="/company/interviews" element={<Protected roles={['COMPANY']}><Interviews /></Protected>} />
          <Route path="/admin/dashboard" element={<Protected roles={['ADMIN']}><Dashboard role="admin" /></Protected>} />
          <Route path="/admin/users" element={<Protected roles={['ADMIN']}><AdminLists type="users" /></Protected>} />
          <Route path="/admin/drives" element={<Protected roles={['ADMIN']}><AdminLists type="drives" /></Protected>} />
          <Route path="/admin/applications" element={<Protected roles={['ADMIN']}><AdminLists type="applications" /></Protected>} />
        </Route>
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}
