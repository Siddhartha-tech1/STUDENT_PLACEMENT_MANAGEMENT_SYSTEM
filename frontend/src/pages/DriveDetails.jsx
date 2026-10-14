import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import StatusBadge from '../components/StatusBadge';
import { ErrorMessage, Loading } from '../components/LoadingError';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

export default function DriveDetails() {
  const { id } = useParams();
  const { user } = useAuth();
  const [drive, setDrive] = useState(null);
  const [eligibility, setEligibility] = useState(null);
  const [coverLetter, setCoverLetter] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  useEffect(() => {
    api.get(`/placement-drives/${id}`).then((res) => setDrive(res.data)).catch((err) => setError(err.message));
    if (user?.role === 'STUDENT') api.get(`/placement-drives/${id}/eligibility`).then((res) => setEligibility(res.data)).catch(() => {});
  }, [id, user?.role]);
  const apply = async () => {
    setError('');
    setMessage('');
    try {
      await api.post('/applications', { placementDriveId: Number(id), coverLetter });
      setMessage('Application submitted');
    } catch (err) {
      setError(err.message);
    }
  };
  if (error && !drive) return <ErrorMessage message={error} />;
  if (!drive) return <Loading />;
  return (
    <main className="public-page">
      <Link to="/drives" className="text-secondary">Back to drives</Link>
      <div className="surface mt-3">
        <div className="d-flex justify-content-between align-items-start gap-3">
          <div>
            <p className="text-secondary">{drive.companyName}</p>
            <h1>{drive.jobTitle}</h1>
          </div>
          <StatusBadge value={drive.status} />
        </div>
        <p>{drive.jobDescription}</p>
        <div className="details-grid">
          <span>Location <strong>{drive.location || 'Not specified'}</strong></span>
          <span>Employment <strong>{drive.employmentType || 'Not specified'}</strong></span>
          <span>CTC <strong>{drive.salaryPackage || 'Not specified'}</strong></span>
          <span>Minimum CGPA <strong>{drive.minimumCgpa || 'Any'}</strong></span>
          <span>Departments <strong>{drive.eligibleDepartments || 'All'}</strong></span>
          <span>Years <strong>{drive.eligibleGraduationYears || 'All'}</strong></span>
          <span>Max backlogs <strong>{drive.maxBacklogs ?? 0}</strong></span>
          <span>Deadline <strong>{drive.applicationDeadline}</strong></span>
        </div>
        <h2>Recruitment process</h2>
        <p>{drive.recruitmentProcess || 'Process details will be shared by the recruiter.'}</p>
        {user?.role === 'STUDENT' ? (
          <div className="apply-box">
            {eligibility ? <div className={`alert ${eligibility.eligible ? 'alert-success' : 'alert-warning'}`}>{eligibility.reason}</div> : null}
            <textarea className="form-control" rows="3" placeholder="Optional cover letter" value={coverLetter} onChange={(event) => setCoverLetter(event.target.value)} />
            <button className="btn btn-primary mt-2" disabled={eligibility && !eligibility.eligible} onClick={apply}>Apply</button>
          </div>
        ) : null}
        <ErrorMessage message={error} />
        {message ? <div className="alert alert-success mt-3">{message}</div> : null}
      </div>
    </main>
  );
}
