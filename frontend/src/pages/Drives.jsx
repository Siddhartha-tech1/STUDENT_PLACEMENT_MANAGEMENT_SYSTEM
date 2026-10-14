import { Search } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Dropdown from '../components/Dropdown';
import StatusBadge from '../components/StatusBadge';
import { EmptyState, ErrorMessage, Loading } from '../components/LoadingError';
import useOptions from '../hooks/useOptions';
import api from '../services/api';

export default function Drives() {
  const [query, setQuery] = useState('');
  const [status, setStatus] = useState(() => localStorage.getItem('spms_drive_status') || 'OPEN');
  const [page, setPage] = useState(null);
  const [error, setError] = useState('');
  const { options } = useOptions();
  const load = () => api.get('/placement-drives', { params: { q: query, status: status || undefined, size: 20 } })
    .then((res) => setPage(res.data)).catch((err) => setError(err.message));
  useEffect(() => { load(); }, []);
  return (
    <main className="public-page">
      <h1 className="page-title">Placement Drives</h1>
      <div className="toolbar">
        <Search size={18} />
        <input className="form-control" placeholder="Search company, role, skills, or location" value={query} onChange={(event) => setQuery(event.target.value)} onKeyDown={(event) => event.key === 'Enter' && load()} />
        <Dropdown
          options={options?.driveStatuses || []}
          value={status}
          placeholder="Drive status"
          defaultOption={{ value: '', label: 'All statuses', description: 'Show every placement drive' }}
          onChange={setStatus}
          persistKey="spms_drive_status"
        />
        <button className="btn btn-primary" onClick={load}>Search</button>
      </div>
      <ErrorMessage message={error} />
      {!page ? <Loading /> : page.content?.length ? (
        <div className="drive-grid">
          {page.content.map((drive) => <DriveCard key={drive.id} drive={drive} />)}
        </div>
      ) : <EmptyState text="No placement drives found." />}
    </main>
  );
}

export function DriveCard({ drive }) {
  return (
    <Link to={`/drives/${drive.id}`} className="drive-card">
      <div className="d-flex justify-content-between gap-3">
        <div>
          <p className="text-secondary mb-1">{drive.companyName}</p>
          <h2>{drive.jobTitle}</h2>
        </div>
        <StatusBadge value={drive.status} />
      </div>
      <p>{drive.location || 'Location not specified'} · {drive.employmentType || 'Role type not specified'}</p>
      <p className="text-secondary mb-0">Min CGPA {drive.minimumCgpa || 'Any'} · Deadline {drive.applicationDeadline}</p>
    </Link>
  );
}
