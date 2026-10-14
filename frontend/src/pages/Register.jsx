import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Dropdown from '../components/Dropdown';
import { ErrorMessage } from '../components/LoadingError';
import { useAuth } from '../context/AuthContext';
import useOptions from '../hooks/useOptions';
import { AuthCard, Input } from './Login';

export default function Register() {
  const { role } = useParams();
  const navigate = useNavigate();
  const { register } = useAuth();
  const isCompany = role === 'company';
  const { options } = useOptions();
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    fullName: '', email: '', password: '', studentId: '', department: '', graduationYear: new Date().getFullYear() + 1,
    companyName: '', contactPerson: '', contactNumber: ''
  });

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await register({ ...form, role: isCompany ? 'COMPANY' : 'STUDENT' });
      navigate(isCompany ? '/company/dashboard' : '/student/dashboard');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <AuthCard title={isCompany ? 'Company Registration' : 'Student Registration'}>
      <ErrorMessage message={error} />
      <form onSubmit={submit} className="vstack gap-3">
        <Input label="Full name" value={form.fullName} onChange={(fullName) => setForm({ ...form, fullName })} />
        <Input label="Email" type="email" value={form.email} onChange={(email) => setForm({ ...form, email })} />
        <Input label="Password" type="password" value={form.password} onChange={(password) => setForm({ ...form, password })} />
        {isCompany ? (
          <>
            <Input label="Company name" value={form.companyName} onChange={(companyName) => setForm({ ...form, companyName })} />
            <Input label="Contact person" value={form.contactPerson} onChange={(contactPerson) => setForm({ ...form, contactPerson })} required={false} />
            <Input label="Contact number" value={form.contactNumber} onChange={(contactNumber) => setForm({ ...form, contactNumber })} required={false} />
          </>
        ) : (
          <>
            <Input label="Student ID / Roll number" value={form.studentId} onChange={(studentId) => setForm({ ...form, studentId })} />
            <Dropdown label="Department" options={options?.departments || []} value={form.department} placeholder="Choose department" onChange={(department) => setForm({ ...form, department })} persistKey="spms_register_department" />
            <Dropdown label="Graduation year" options={options?.graduationYears || []} value={String(form.graduationYear)} placeholder="Choose graduation year" onChange={(graduationYear) => setForm({ ...form, graduationYear: Number(graduationYear) })} persistKey="spms_register_graduation_year" />
          </>
        )}
        <button className="btn btn-primary">Create account</button>
      </form>
    </AuthCard>
  );
}
