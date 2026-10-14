import { useEffect, useState } from 'react';
import Dropdown from '../components/Dropdown';
import { ErrorMessage, Loading } from '../components/LoadingError';
import useOptions from '../hooks/useOptions';
import api from '../services/api';

export default function Profile({ role }) {
  const [profile, setProfile] = useState(null);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const { options } = useOptions();
  const endpoint = role === 'student' ? '/students/me' : '/companies/me';
  useEffect(() => { api.get(endpoint).then((res) => setProfile(res.data)).catch((err) => setError(err.message)); }, [endpoint]);
  if (error) return <ErrorMessage message={error} />;
  if (!profile) return <Loading />;
  const fields = role === 'student'
    ? ['fullName', 'phone', 'college', 'university', 'department', 'degree', 'graduationYear', 'cgpa', 'backlogs', 'skills', 'certifications', 'projects']
    : ['fullName', 'companyName', 'website', 'industry', 'description', 'contactPerson', 'contactNumber', 'location'];
  const save = async (event) => {
    event.preventDefault();
    setMessage('');
    setError('');
    try {
      const { data } = await api.put(endpoint, profile);
      setProfile(data);
      setMessage('Profile saved');
    } catch (err) {
      setError(err.message);
    }
  };
  return (
    <>
      <h1 className="page-title">Profile</h1>
      <ErrorMessage message={error} />
      {message ? <div className="alert alert-success">{message}</div> : null}
      <form className="surface form-grid" onSubmit={save}>
        {fields.map((field) => (
          <ProfileField
            key={field}
            field={field}
            value={profile[field]}
            options={options}
            onChange={(value) => setProfile({ ...profile, [field]: value })}
          />
        ))}
        <button className="btn btn-primary">Save profile</button>
      </form>
      {role === 'student' ? <ResumeUploader profile={profile} onUpload={async () => {
        const { data } = await api.get(endpoint);
        setProfile(data);
      }} /> : null}
    </>
  );
}

function ProfileField({ field, value, options, onChange }) {
  const label = field.replace(/([A-Z])/g, ' $1');
  const dropdowns = {
    department: options?.departments,
    degree: options?.degrees,
    graduationYear: options?.graduationYears,
    backlogs: options?.backlogs
  };
  if (dropdowns[field]) {
    return (
      <Dropdown
        label={label}
        options={dropdowns[field]}
        value={value == null ? '' : String(value)}
        placeholder={`Choose ${label.toLowerCase()}`}
        onChange={(next) => onChange(['graduationYear', 'backlogs'].includes(field) ? Number(next) : next)}
        persistKey={`spms_profile_${field}`}
      />
    );
  }
  return (
    <label className="form-label">
      {label}
      <input className="form-control" value={value ?? ''} onChange={(event) => onChange(event.target.value)} />
    </label>
  );
}

function ResumeUploader({ profile, onUpload }) {
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [uploading, setUploading] = useState(false);

  const upload = async (event) => {
    const file = event.target.files?.[0];
    if (!file) return;
    setError('');
    setMessage('');
    setUploading(true);
    try {
      const data = new FormData();
      data.append('file', file);
      await api.post('/students/me/resume', data);
      setMessage('Resume uploaded successfully.');
      onUpload?.();
    } catch (err) {
      setError(err.message);
    } finally {
      setUploading(false);
    }
  };

  const viewResume = async () => {
    try {
      const response = await api.get('/students/me/resume', { responseType: 'blob' });
      const blobUrl = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
      window.open(blobUrl, '_blank');
    } catch (err) {
      setError('Could not view resume: ' + err.message);
    }
  };

  return (
    <div className="surface mt-4">
      <h2>Resume Management</h2>
      <p className="text-secondary small">Upload your resume in PDF format (maximum 5 MB).</p>
      {profile?.resumeUploaded ? (
        <div className="alert alert-success d-flex align-items-center justify-content-between flex-wrap gap-2 mb-3">
          <div>
            <strong>Resume on file:</strong> {profile.resumeFileName || 'resume.pdf'}
            {profile.resumeUploadedAt ? (
              <div className="small text-secondary">
                Uploaded: {new Date(profile.resumeUploadedAt).toLocaleString()}
              </div>
            ) : null}
          </div>
          <button type="button" className="btn btn-primary btn-sm" onClick={viewResume}>
            View Current Resume
          </button>
        </div>
      ) : (
        <div className="alert alert-warning mb-3">No resume uploaded yet. Companies require a resume to review applications.</div>
      )}
      <label className="form-label w-100">
        {profile?.resumeUploaded ? 'Replace Resume (PDF only)' : 'Upload Resume (PDF only)'}
        <input className="form-control" type="file" accept="application/pdf" disabled={uploading} onChange={upload} />
      </label>
      {uploading ? <p className="text-secondary mt-2">Uploading resume...</p> : null}
      {message ? <p className="text-success mt-2">{message}</p> : null}
      {error ? <p className="text-danger mt-2">{error}</p> : null}
    </div>
  );
}
