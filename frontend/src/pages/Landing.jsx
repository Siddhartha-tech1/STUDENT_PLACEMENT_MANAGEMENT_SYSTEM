import { ArrowRight, BookOpen, BriefcaseBusiness, Building2, ClipboardCheck, GraduationCap, Menu, ShieldCheck, Sparkles, X } from 'lucide-react';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Dropdown from '../components/Dropdown';

const productOptions = [
  {
    label: 'Portals',
    options: [
      { value: '/register/student', label: 'Student Portal', description: 'Profiles, resumes, eligibility and applications', icon: GraduationCap },
      { value: '/register/company', label: 'Recruiter Workspace', description: 'Drives, applicants, shortlists and interviews', icon: Building2 },
      { value: '/login', label: 'Admin Console', description: 'Users, applications and placement reports', icon: ShieldCheck }
    ]
  }
];

const resourceOptions = [
  {
    label: 'Resources',
    options: [
      { value: '/drives', label: 'Browse Drives', description: 'Explore live campus placement drives', icon: BriefcaseBusiness },
      { value: '/login', label: 'Sign In', description: 'Login to student, company or admin portal', icon: BookOpen }
    ]
  }
];

export default function Landing() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const navigate = useNavigate();

  return (
    <div className="site-page">
      <nav className="site-nav">
        <Link className="site-logo" to="/">
          <span>CB</span> CareerBridge
        </Link>
        <button className="mobile-menu-button" type="button" aria-label="Toggle menu" onClick={() => setMobileOpen((open) => !open)}>
          {mobileOpen ? <X size={22} /> : <Menu size={22} />}
        </button>
        <div className={`site-nav-links ${mobileOpen ? 'open' : ''}`}>
          <Dropdown
            className="nav-dropdown"
            options={productOptions}
            value=""
            placeholder="Portals"
            onChange={(path) => path && navigate(path)}
          />
          <Link to="/drives">Placement Drives</Link>
          <Dropdown
            className="nav-dropdown"
            options={resourceOptions}
            value=""
            placeholder="Explore"
            onChange={(path) => path && navigate(path)}
          />
        </div>
        <Link className="btn btn-primary nav-cta" to="/login">Sign In</Link>
      </nav>
      <section className="hero">
        <div>
          <p className="eyebrow"><Sparkles size={16} /> Campus hiring, simplified</p>
          <h1>Manage campus placements with clarity</h1>
          <p className="lead">CareerBridge helps students, recruiters, and placement officers handle drives, applications, interviews, and selections from one focused workspace.</p>
          <div className="hero-actions">
            <Link className="btn btn-primary" to="/register/student">Get Started <ArrowRight size={18} /></Link>
            <Link className="btn btn-secondary-soft" to="/drives">Browse drives</Link>
          </div>
        </div>
      </section>
      <section className="product-section reveal">
        <div className="section-heading">
          <p className="eyebrow">Role-based access</p>
          <h2>Separate portals for students, companies, and admins</h2>
        </div>
        <div className="product-grid">
          <ProductCard icon={GraduationCap} title="Student Portal" text="Build profiles, upload resumes, check eligibility and apply with confidence." />
          <ProductCard icon={Building2} title="Recruiter Workspace" text="Create drives, review applicants, schedule interviews and update outcomes." />
          <ProductCard icon={ShieldCheck} title="Admin Console" text="Manage users, monitor applications and measure placement performance." />
        </div>
      </section>
      <section className="use-case-section reveal" id="workflow">
        <div className="section-heading">
          <p className="eyebrow">Placement workflow</p>
          <h2>Move each drive from opening to final selection</h2>
        </div>
        <div className="workflow-strip">
          <WorkflowStep number="01" title="Create drive" text="Company or admin adds job role, eligibility, location, deadline and interview details." />
          <WorkflowStep number="02" title="Apply and review" text="Students apply through their portal while recruiters shortlist suitable candidates." />
          <WorkflowStep number="03" title="Track result" text="Interview status and final selection updates stay visible to the right users." />
        </div>
      </section>
      <section className="news-section reveal">
        <div className="section-heading inline">
          <div>
            <p className="eyebrow">Daily work area</p>
            <h2>Useful actions from the home page</h2>
          </div>
          <Link to="/drives">View all</Link>
        </div>
        <div className="news-list">
          <News label="Students" category="Apply" title="Browse active placement drives and submit applications before deadline" />
          <News label="Companies" category="Review" title="Check applicants, shortlist candidates and update interview status" />
          <News label="Admins" category="Monitor" title="Track users, applications and placement progress from live records" />
        </div>
      </section>
      <section className="final-cta reveal" id="registration">
        <p className="eyebrow"><ClipboardCheck size={16} /> Ready to work</p>
        <h2>Open the right portal and continue placement work</h2>
        <div className="d-flex justify-content-center gap-3 flex-wrap">
          <Link className="btn btn-primary" to="/register/student">Register as Student</Link>
          <Link className="btn btn-secondary-soft" to="/register/company">Register Company</Link>
          <Link className="btn btn-secondary-soft" to="/login">Admin Login</Link>
        </div>
      </section>
      <footer className="site-footer" id="support">
        <div>
          <strong>CareerBridge</strong>
          <p>Student Placement Management System for organized campus hiring.</p>
        </div>
        <div>
          <h3>Portals</h3>
          <Link to="/drives">Drives</Link>
          <Link to="/register/student">Students</Link>
          <Link to="/register/company">Companies</Link>
        </div>
        <div>
          <h3>Resources</h3>
          <Link to="/login">Login</Link>
          <a href="#workflow">Workflow</a>
          <a href="#registration">Registration</a>
        </div>
        <div>
          <h3>Admin</h3>
          <Link to="/admin/dashboard">Dashboard</Link>
          <Link to="/admin/users">Users</Link>
          <Link to="/admin/applications">Applications</Link>
        </div>
      </footer>
    </div>
  );
}

function ProductCard({ icon: Icon, title, text }) {
  return (
    <article className="product-card">
      <div className="card-visual"><Icon size={42} /></div>
      <h3>{title}</h3>
      <p>{text}</p>
    </article>
  );
}

function WorkflowStep({ number, title, text }) {
  return (
    <article className="use-card workflow-step">
      <div className="thumb">{number}</div>
      <h3>{title}</h3>
      <p>{text}</p>
    </article>
  );
}

function News({ label, category, title }) {
  return (
    <article className="news-item">
      <span>{label}</span>
      <strong>{category}</strong>
      <h3>{title}</h3>
      <a href="#registration">Open portal</a>
    </article>
  );
}
