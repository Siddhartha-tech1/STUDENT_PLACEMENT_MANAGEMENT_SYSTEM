import { BriefcaseBusiness, CheckCircle, ClipboardList, Clock, Users } from 'lucide-react';
import { useEffect, useState } from 'react';
import StatCard from '../components/StatCard';
import { ErrorMessage, Loading } from '../components/LoadingError';
import api from '../services/api';

const labels = {
  student: [['availableDrives', 'Available Drives', BriefcaseBusiness], ['applicationsSubmitted', 'Applications', ClipboardList], ['underReview', 'Under Review', Clock], ['shortlisted', 'Shortlisted', Users], ['upcomingInterviews', 'Interviews', Clock], ['selectedOffers', 'Offers', CheckCircle]],
  company: [['totalDrives', 'Total Drives', BriefcaseBusiness], ['activeDrives', 'Active Drives', CheckCircle], ['totalApplications', 'Applications', ClipboardList], ['awaitingReview', 'Awaiting Review', Clock], ['shortlisted', 'Shortlisted', Users], ['interviewsScheduled', 'Interviews', Clock], ['selected', 'Selected', CheckCircle]],
  admin: [['totalStudents', 'Students', Users], ['totalCompanies', 'Companies', BriefcaseBusiness], ['totalPlacementDrives', 'Drives', ClipboardList], ['openPlacementDrives', 'Open Drives', CheckCircle], ['totalApplications', 'Applications', ClipboardList], ['studentsSelected', 'Selected', CheckCircle], ['placementPercentage', 'Placement %', CheckCircle]]
};

export default function Dashboard({ role }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  useEffect(() => {
    api.get(role === 'admin' ? '/admin/dashboard' : `/${role === 'student' ? 'students' : 'companies'}/me/dashboard`)
      .then((res) => setData(res.data))
      .catch((err) => setError(err.message));
  }, [role]);
  if (error) return <ErrorMessage message={error} />;
  if (!data) return <Loading />;
  return (
    <>
      <h1 className="page-title">{role[0].toUpperCase() + role.slice(1)} Dashboard</h1>
      <div className="stat-grid">
        {labels[role].map(([key, label, Icon]) => <StatCard key={key} icon={Icon} label={label} value={Number(data[key] || 0).toFixed(key.includes('Percentage') ? 1 : 0)} />)}
      </div>
    </>
  );
}
