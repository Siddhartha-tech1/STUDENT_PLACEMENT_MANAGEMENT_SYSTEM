import { useEffect, useState } from 'react';
import { EmptyState, ErrorMessage } from '../components/LoadingError';
import api from '../services/api';

export default function Interviews() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState('');
  useEffect(() => { api.get('/interviews/my').then((res) => setItems(res.data)).catch((err) => setError(err.message)); }, []);
  return (
    <>
      <h1 className="page-title">Interviews</h1>
      <ErrorMessage message={error} />
      {items.length ? (
        <div className="surface table-responsive">
          <table className="table table-dark">
            <thead><tr><th>Company</th><th>Role</th><th>Student</th><th>Round</th><th>Date</th><th>Mode</th><th>Details</th></tr></thead>
            <tbody>{items.map((item) => <tr key={item.id}><td>{item.companyName}</td><td>{item.jobTitle}</td><td>{item.studentName}</td><td>{item.interviewRound}</td><td>{item.interviewDateTime}</td><td>{item.mode}</td><td>{item.locationOrMeetingDetails}</td></tr>)}</tbody>
          </table>
        </div>
      ) : <EmptyState text="No interviews scheduled." />}
    </>
  );
}
