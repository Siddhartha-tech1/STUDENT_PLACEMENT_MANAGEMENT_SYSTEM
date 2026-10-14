import { useEffect, useState } from 'react';
import Dropdown from '../components/Dropdown';
import { ErrorMessage } from '../components/LoadingError';
import useOptions from '../hooks/useOptions';
import api from '../services/api';
import { DriveCard } from './Drives';

const blank = {
  jobTitle: '', jobDescription: '', location: '', employmentType: '', salaryPackage: '', minimumCgpa: '',
  eligibleDepartments: '', eligibleGraduationYears: '', maxBacklogs: 0, requiredSkills: '', applicationDeadline: '',
  recruitmentProcess: '', status: ''
};

export default function CompanyDrives() {
  const [drives, setDrives] = useState([]);
  const [form, setForm] = useState(blank);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const { options } = useOptions();

  const load = () => api.get('/companies/me/drives').then((res) => setDrives(res.data || []));
  useEffect(() => { load().catch((err) => setError(err.message)); }, []);

  useEffect(() => {
    if (!options || form.employmentType || form.status) return;
    setForm((current) => ({
      ...current,
      employmentType: options.employmentTypes?.[0]?.value || '',
      status: options.driveStatuses?.find((option) => option.value === 'OPEN')?.value || options.driveStatuses?.[0]?.value || ''
    }));
  }, [options, form.employmentType, form.status]);

  const save = async (event) => {
    event.preventDefault();
    setError('');
    setMessage('');
    try {
      const payload = {
        ...form,
        status: form.status || null,
        salaryPackage: Number(form.salaryPackage || 0),
        minimumCgpa: Number(form.minimumCgpa || 0),
        maxBacklogs: Number(form.maxBacklogs || 0)
      };
      if (editingId) {
        await api.put(`/placement-drives/${editingId}`, payload);
        setMessage('Placement drive updated successfully.');
      } else {
        await api.post('/placement-drives', payload);
        setMessage('Placement drive created successfully.');
      }
      setEditingId(null);
      setForm({
        ...blank,
        employmentType: options?.employmentTypes?.[0]?.value || '',
        status: options?.driveStatuses?.find((option) => option.value === 'OPEN')?.value || options?.driveStatuses?.[0]?.value || ''
      });
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  const startEdit = (drive) => {
    setEditingId(drive.id);
    setForm({
      jobTitle: drive.jobTitle || '',
      jobDescription: drive.jobDescription || '',
      location: drive.location || '',
      employmentType: drive.employmentType || '',
      salaryPackage: drive.salaryPackage ?? '',
      minimumCgpa: drive.minimumCgpa ?? '',
      eligibleDepartments: drive.eligibleDepartments || '',
      eligibleGraduationYears: drive.eligibleGraduationYears || '',
      maxBacklogs: drive.maxBacklogs ?? 0,
      requiredSkills: drive.requiredSkills || '',
      applicationDeadline: drive.applicationDeadline || '',
      recruitmentProcess: drive.recruitmentProcess || '',
      status: drive.status || 'OPEN'
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setForm(blank);
  };

  const remove = async (id) => {
    if (!window.confirm('Are you sure you want to delete this placement drive?')) return;
    setError('');
    try {
      await api.delete(`/placement-drives/${id}`);
      setMessage('Placement drive removed.');
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <>
      <h1 className="page-title">{editingId ? 'Edit Placement Drive' : 'Manage Placement Drives'}</h1>
      <ErrorMessage message={error} />
      {message ? <div className="alert alert-success">{message}</div> : null}
      <form className="surface form-grid" onSubmit={save}>
        {Object.keys(blank).map((field) => (
          <DriveField
            key={field}
            field={field}
            value={form[field]}
            options={options}
            onChange={(value) => setForm({ ...form, [field]: value })}
          />
        ))}
        <div className="d-flex gap-2 align-items-center">
          <button className="btn btn-primary">{editingId ? 'Update drive' : 'Create drive'}</button>
          {editingId ? (
            <button type="button" className="btn btn-secondary-soft" onClick={cancelEdit}>
              Cancel
            </button>
          ) : null}
        </div>
      </form>
      <div className="drive-grid mt-4">
        {drives.map((drive) => (
          <div key={drive.id} className="surface drive-card">
            <DriveCard drive={drive} />
            <div className="d-flex gap-2 mt-3 pt-2 border-top border-secondary-subtle">
              <button type="button" className="btn btn-secondary-soft btn-sm" onClick={() => startEdit(drive)}>
                Edit
              </button>
              <button type="button" className="btn btn-outline-danger btn-sm" onClick={() => remove(drive.id)}>
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

function DriveField({ field, value, options, onChange }) {
  const label = field.replace(/([A-Z])/g, ' $1');
  const dropdowns = {
    employmentType: options?.employmentTypes,
    eligibleDepartments: options?.departments,
    eligibleGraduationYears: options?.graduationYears,
    maxBacklogs: options?.backlogs,
    status: options?.driveStatuses
  };
  if (dropdowns[field]) {
    return (
      <Dropdown
        label={label}
        options={dropdowns[field]}
        value={value == null ? '' : String(value)}
        placeholder={`Choose ${label.toLowerCase()}`}
        onChange={(next) => onChange(field === 'maxBacklogs' ? Number(next) : next)}
        persistKey={`spms_drive_${field}`}
      />
    );
  }
  return (
    <label className="form-label">
      {label}
      <input
        className="form-control"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        required={['jobTitle', 'jobDescription', 'applicationDeadline'].includes(field)}
        type={field === 'applicationDeadline' ? 'date' : 'text'}
      />
    </label>
  );
}
