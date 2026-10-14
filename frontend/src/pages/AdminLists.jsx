import { useEffect, useState } from 'react';
import { EmptyState, ErrorMessage } from '../components/LoadingError';
import api from '../services/api';

const endpoints = {
  users: '/admin/users',
  drives: '/admin/placement-drives',
  applications: '/admin/applications'
};

export default function AdminLists({ type }) {
  const [items, setItems] = useState([]);
  const [filterText, setFilterText] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const load = () => {
    api.get(endpoints[type])
      .then((res) => setItems(res.data || []))
      .catch((err) => setError(err.message));
  };

  useEffect(() => {
    setFilterText('');
    setError('');
    setMessage('');
    load();
  }, [type]);

  const toggleUserStatus = async (user) => {
    const next = user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    try {
      await api.patch(`/admin/users/${user.id}/status`, null, { params: { status: next } });
      setMessage(`User ${user.fullName} is now ${next}.`);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const deleteUser = async (user) => {
    if (!window.confirm(`Are you sure you want to delete user ${user.fullName} (${user.email})?`)) return;
    try {
      await api.delete(`/admin/users/${user.id}`);
      setMessage(`User ${user.fullName} removed.`);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const updateDriveStatus = async (drive, status) => {
    try {
      await api.patch(`/placement-drives/${drive.id}/status`, null, { params: { status } });
      setMessage(`Drive ${drive.jobTitle} status updated to ${status}.`);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const deleteDrive = async (drive) => {
    if (!window.confirm(`Delete drive ${drive.jobTitle}?`)) return;
    try {
      await api.delete(`/placement-drives/${drive.id}`);
      setMessage(`Drive removed.`);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const filtered = items.filter((item) => {
    if (!filterText) return true;
    const str = JSON.stringify(item).toLowerCase();
    return str.includes(filterText.toLowerCase());
  });

  const keys = items[0]
    ? Object.keys(items[0]).filter((key) => !['passwordHash', 'jobDescription', 'requiredQualifications', 'coverLetter'].includes(key)).slice(0, 8)
    : [];

  return (
    <>
      <div className="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
        <h1 className="page-title mb-0">Admin {type[0].toUpperCase() + type.slice(1)}</h1>
        <div className="d-flex gap-2 align-items-center">
          <input
            className="form-control"
            style={{ maxWidth: '280px' }}
            placeholder={`Filter ${type}...`}
            value={filterText}
            onChange={(e) => setFilterText(e.target.value)}
          />
        </div>
      </div>
      <ErrorMessage message={error} />
      {message ? <div className="alert alert-success">{message}</div> : null}

      {filtered.length ? (
        <div className="surface table-responsive">
          <table className="table table-hover align-middle">
            <thead>
              <tr>
                {keys.map((key) => <th key={key}>{key}</th>)}
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((item, index) => (
                <tr key={item.id || index}>
                  {keys.map((key) => (
                    <td key={key}>
                      {typeof item[key] === 'object' && item[key] !== null
                        ? JSON.stringify(item[key])
                        : String(item[key] ?? '-')}
                    </td>
                  ))}
                  <td>
                    <div className="d-flex gap-1 flex-wrap">
                      {type === 'users' ? (
                        <>
                          <button
                            type="button"
                            className="btn btn-secondary-soft btn-sm"
                            onClick={() => toggleUserStatus(item)}
                          >
                            {item.status === 'ACTIVE' ? 'Deactivate' : 'Activate'}
                          </button>
                          <button
                            type="button"
                            className="btn btn-outline-danger btn-sm"
                            onClick={() => deleteUser(item)}
                          >
                            Delete
                          </button>
                        </>
                      ) : null}
                      {type === 'drives' ? (
                        <>
                          {item.status !== 'CLOSED' ? (
                            <button
                              type="button"
                              className="btn btn-secondary-soft btn-sm"
                              onClick={() => updateDriveStatus(item, 'CLOSED')}
                            >
                              Close
                            </button>
                          ) : (
                            <button
                              type="button"
                              className="btn btn-secondary-soft btn-sm"
                              onClick={() => updateDriveStatus(item, 'OPEN')}
                            >
                              Reopen
                            </button>
                          )}
                          <button
                            type="button"
                            className="btn btn-outline-danger btn-sm"
                            onClick={() => deleteDrive(item)}
                          >
                            Delete
                          </button>
                        </>
                      ) : null}
                      {type === 'applications' ? (
                        <span className="text-secondary small">Managed by recruiter</span>
                      ) : null}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : <EmptyState text={`No ${type} found.`} />}
    </>
  );
}
