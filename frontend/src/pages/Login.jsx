import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { ErrorMessage } from '../components/LoadingError';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await login(form.email, form.password);
      const saved = JSON.parse(localStorage.getItem('spms_user'));
      navigate(`/${saved.role.toLowerCase()}/dashboard`);
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <AuthCard title="Login">
      <ErrorMessage message={error} />
      <form onSubmit={submit} className="vstack gap-3">
        <Input label="Email" type="email" value={form.email} onChange={(email) => setForm({ ...form, email })} />
        <Input label="Password" type="password" value={form.password} onChange={(password) => setForm({ ...form, password })} />
        <button className="btn btn-primary">Login</button>
      </form>
      <div className="small text-secondary mt-3">
        New here? <Link to="/register/student">Register as student</Link> or <Link to="/register/company">company</Link>.
      </div>
    </AuthCard>
  );
}

export function AuthCard({ title, children }) {
  return (
    <div className="auth-page">
      <div className="auth-card">
        <Link to="/" className="brand text-decoration-none">SPMS</Link>
        <h1>{title}</h1>
        {children}
      </div>
    </div>
  );
}

export function Input({ label, value, onChange, type = 'text', as = 'input', required = true }) {
  const Comp = as;
  return (
    <label className="form-label">
      {label}
      <Comp
        className="form-control"
        type={type}
        value={value ?? ''}
        required={required}
        onChange={(event) => onChange(event.target.value)}
      />
    </label>
  );
}
