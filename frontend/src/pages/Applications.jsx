import { useEffect, useState } from 'react';
import StatusBadge from '../components/StatusBadge';
import { EmptyState, ErrorMessage, Loading } from '../components/LoadingError';
import Dropdown from '../components/Dropdown';
import useOptions from '../hooks/useOptions';
import api from '../services/api';

export default function Applications() {
  const [items, setItems] = useState(null);
  const [error, setError] = useState('');
  useEffect(() => { api.get('/applications/my').then((res) => setItems(res.data)).catch((err) => setError(err.message)); }, []);
  if (error) return <ErrorMessage message={error} />;
  if (!items) return <Loading />;
  return (
    <>
      <h1 className="page-title">My Applications</h1>
      {items.length ? <ApplicationTable items={items} /> : <EmptyState text="You have not applied to any placement drives yet." />}
    </>
  );
}

export function ApplicationTable({ items, onStatus, onScheduleInterview }) {
  const downloadResume = async (applicationId) => {
    try {
      const response = await api.get(`/applications/${applicationId}/resume`, { responseType: 'blob' });
      const blobUrl = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
      window.open(blobUrl, '_blank');
    } catch (err) {
      alert('Could not load resume: ' + err.message);
    }
  };

  return (
    <div className="table-responsive surface">
      <table className="table table-hover align-middle">
        <thead>
          <tr>
            <th>Company</th>
            <th>Role</th>
            <th>Student</th>
            <th>Status</th>
            <th>Interview</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.id}>
              <td>{item.companyName}</td>
              <td>{item.jobTitle}</td>
              <td>
                <div>{item.studentName || '-'}</div>
                {item.studentDepartment ? <small className="text-secondary">{item.studentDepartment}</small> : null}
              </td>
              <td><StatusBadge value={item.status} /></td>
              <td>
                {item.interview ? (
                  <div>
                    <strong>{item.interview.interviewRound}</strong>
                    <div className="small text-secondary">
                      {new Date(item.interview.interviewDateTime).toLocaleString()} · {item.interview.mode}
                    </div>
                    {item.interview.locationOrMeetingDetails ? (
                      <div className="small text-truncate" style={{ maxWidth: '180px' }}>
                        {item.interview.locationOrMeetingDetails}
                      </div>
                    ) : null}
                  </div>
                ) : (
                  <span className="text-secondary">-</span>
                )}
              </td>
              <td>
                <div className="d-flex align-items-center gap-2 flex-wrap">
                  {onStatus ? <StatusEditor item={item} onStatus={onStatus} /> : null}
                  <button
                    type="button"
                    className="btn btn-secondary-soft btn-sm"
                    onClick={() => downloadResume(item.id)}
                    title="View candidate resume"
                  >
                    Resume
                  </button>
                  {onScheduleInterview ? (
                    <button
                      type="button"
                      className="btn btn-primary btn-sm"
                      onClick={() => onScheduleInterview(item)}
                    >
                      {item.interview ? 'Reschedule' : 'Interview'}
                    </button>
                  ) : null}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function StatusEditor({ item, onStatus }) {
  const { options } = useOptions();
  return (
    <Dropdown
      className="compact-dropdown"
      options={options?.applicationStatuses || []}
      value={item.status}
      placeholder="Status"
      onChange={(status) => onStatus(item.id, status)}
    />
  );
}
