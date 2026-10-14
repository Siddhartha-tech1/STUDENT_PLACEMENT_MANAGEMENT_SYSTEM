import { BriefcaseBusiness, Building2, FileText, LayoutDashboard, LogOut, User, Users } from 'lucide-react';
import { useState } from 'react';
import { Link, NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const links = {
  STUDENT: [
    ['Dashboard', '/student/dashboard', LayoutDashboard],
    ['Profile', '/student/profile', User],
    ['Drives', '/drives', BriefcaseBusiness],
    ['Applications', '/student/applications', FileText],
    ['Interviews', '/student/interviews', Users]
  ],
  COMPANY: [
    ['Dashboard', '/company/dashboard', LayoutDashboard],
    ['Profile', '/company/profile', Building2],
    ['Drives', '/company/drives', BriefcaseBusiness],
    ['Applicants', '/company/applicants', Users],
    ['Interviews', '/company/interviews', FileText]
  ],
  ADMIN: [
    ['Dashboard', '/admin/dashboard', LayoutDashboard],
    ['Users', '/admin/users', Users],
    ['Drives', '/admin/drives', BriefcaseBusiness],
    ['Applications', '/admin/applications', FileText]
  ]
};

export default function DashboardLayout() {
  const { user, logout } = useAuth();
  const [open, setOpen] = useState(false);
  const nav = links[user?.role] || [];
  return (
    <div className="app-shell">
      <button className="dashboard-menu-button" type="button" onClick={() => setOpen((current) => !current)}>Menu</button>
      <aside className={`sidebar ${open ? 'open' : ''}`}>
        <Link className="brand" to="/">SPMS</Link>
        <nav className="nav flex-column gap-1">
          {nav.map(([label, path, Icon]) => (
            <NavLink key={path} to={path} className="nav-link">
              <Icon size={18} /> {label}
            </NavLink>
          ))}
        </nav>
        <button className="btn btn-outline-light mt-auto" onClick={logout}>
          <LogOut size={18} /> Logout
        </button>
      </aside>
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
