const variants = {
  APPLIED: 'secondary',
  UNDER_REVIEW: 'info',
  SHORTLISTED: 'primary',
  INTERVIEW_SCHEDULED: 'warning',
  SELECTED: 'success',
  REJECTED: 'danger',
  OPEN: 'success',
  CLOSED: 'secondary',
  DRAFT: 'warning',
  CANCELLED: 'danger'
};

export default function StatusBadge({ value }) {
  const label = String(value || '').replaceAll('_', ' ');
  return <span className={`badge text-bg-${variants[value] || 'secondary'} rounded-pill px-2 py-1`}>{label}</span>;
}
