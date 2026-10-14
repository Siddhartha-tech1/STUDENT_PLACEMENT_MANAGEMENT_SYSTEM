import { useEffect, useState } from 'react';
import Dropdown from '../components/Dropdown';
import { ErrorMessage, EmptyState } from '../components/LoadingError';
import api from '../services/api';
import { ApplicationTable } from './Applications';

export default function Applicants() {
  const [drives, setDrives] = useState([]);
  const [driveId, setDriveId] = useState('');
  const [apps, setApps] = useState([]);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [schedulingApp, setSchedulingApp] = useState(null);
  const [interviewForm, setInterviewForm] = useState({
    interviewRound: 'Technical Round 1',
    interviewDateTime: '',
    mode: 'ONLINE',
    locationOrMeetingDetails: '',
    instructions: ''
  });

  useEffect(() => {
    api.get('/companies/me/drives').then((res) => {
      const items = res.data || [];
      setDrives(items);
      if (items[0]) setDriveId(String(items[0].id));
    }).catch((err) => setError(err.message));
  }, []);

  const loadApps = () => {
    if (driveId) {
      api.get(`/applications/drive/${driveId}`).then((res) => setApps(res.data)).catch((err) => setError(err.message));
    } else {
      setApps([]);
    }
  };

  useEffect(() => {
    loadApps();
  }, [driveId]);

  const status = async (id, value) => {
    setError('');
    try {
      await api.patch(`/applications/${id}/status`, { status: value });
      loadApps();
    } catch (err) {
      setError(err.message);
    }
  };

  const openScheduler = (app) => {
    setSchedulingApp(app);
    setInterviewForm({
      interviewRound: app.interview?.interviewRound || 'Technical Round 1',
      interviewDateTime: app.interview?.interviewDateTime ? app.interview.interviewDateTime.slice(0, 16) : '',
      mode: app.interview?.mode || 'ONLINE',
      locationOrMeetingDetails: app.interview?.locationOrMeetingDetails || '',
      instructions: app.interview?.instructions || ''
    });
  };

  const submitInterview = async (event) => {
    event.preventDefault();
    if (!schedulingApp) return;
    setError('');
    setMessage('');
    try {
      const payload = {
        applicationId: schedulingApp.id,
        interviewRound: interviewForm.interviewRound,
        interviewDateTime: interviewForm.interviewDateTime,
        mode: interviewForm.mode,
        locationOrMeetingDetails: interviewForm.locationOrMeetingDetails,
        instructions: interviewForm.instructions
      };
      if (schedulingApp.interview?.id) {
        await api.put(`/interviews/${schedulingApp.interview.id}`, payload);
        setMessage('Interview schedule updated successfully.');
      } else {
        await api.post('/interviews', payload);
        // Automatically set status to INTERVIEW_SCHEDULED
        await api.patch(`/applications/${schedulingApp.id}/status`, { status: 'INTERVIEW_SCHEDULED' });
        setMessage('Interview scheduled successfully.');
      }
      setSchedulingApp(null);
      loadApps();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <>
      <h1 className="page-title">Applicants</h1>
      <ErrorMessage message={error} />
      {message ? <div className="alert alert-success">{message}</div> : null}

      {schedulingApp ? (
        <div className="surface mb-4 border border-primary p-4">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h3>Schedule Interview for {schedulingApp.studentName}</h3>
            <button type="button" className="btn btn-secondary-soft btn-sm" onClick={() => setSchedulingApp(null)}>Cancel</button>
          </div>
          <form onSubmit={submitInterview} className="form-grid">
            <label className="form-label">
              Interview Round
              <input
                className="form-control"
                required
                value={interviewForm.interviewRound}
                onChange={(e) => setInterviewForm({ ...interviewForm, interviewRound: e.target.value })}
                placeholder="e.g. Technical Round 1, HR Round"
              />
            </label>
            <label className="form-label">
              Date & Time
              <input
                className="form-control"
                type="datetime-local"
                required
                value={interviewForm.interviewDateTime}
                onChange={(e) => setInterviewForm({ ...interviewForm, interviewDateTime: e.target.value })}
              />
            </label>
            <label className="form-label">
              Mode
              <select
                className="form-control"
                value={interviewForm.mode}
                onChange={(e) => setInterviewForm({ ...interviewForm, mode: e.target.value })}
              >
                <option value="ONLINE">ONLINE</option>
                <option value="IN_PERSON">IN_PERSON</option>
              </select>
            </label>
            <label className="form-label">
              Location / Meeting Link
              <input
                className="form-control"
                value={interviewForm.locationOrMeetingDetails}
                onChange={(e) => setInterviewForm({ ...interviewForm, locationOrMeetingDetails: e.target.value })}
                placeholder="e.g. Google Meet URL or Room 402"
              />
            </label>
            <label className="form-label" style={{ gridColumn: '1 / -1' }}>
              Instructions
              <textarea
                className="form-control"
                rows="2"
                value={interviewForm.instructions}
                onChange={(e) => setInterviewForm({ ...interviewForm, instructions: e.target.value })}
                placeholder="e.g. Keep your resume and code samples ready"
              />
            </label>
            <div className="d-flex gap-2">
              <button className="btn btn-primary">Save Schedule</button>
              <button type="button" className="btn btn-secondary-soft" onClick={() => setSchedulingApp(null)}>Cancel</button>
            </div>
          </form>
        </div>
      ) : null}

      {drives.length ? (
        <Dropdown
          className="toolbar-select"
          label="Placement drive"
          options={drives.map((drive) => ({
            value: String(drive.id),
            label: `${drive.companyName} · ${drive.jobTitle}`,
            description: `${drive.status} · ${drive.applicationDeadline}`
          }))}
          value={driveId}
          placeholder="Choose a placement drive"
          onChange={setDriveId}
          persistKey="spms_applicants_drive"
        />
      ) : (
        <EmptyState text="You have not created any placement drives yet." />
      )}

      {apps.length ? (
        <ApplicationTable items={apps} onStatus={status} onScheduleInterview={openScheduler} />
      ) : drives.length ? (
        <EmptyState text="No applicants for this drive yet." />
      ) : null}
    </>
  );
}
