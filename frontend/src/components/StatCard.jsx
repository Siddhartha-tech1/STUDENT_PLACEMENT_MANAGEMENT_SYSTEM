export default function StatCard({ icon: Icon, label, value }) {
  return (
    <div className="stat-card">
      {Icon ? <Icon size={22} /> : null}
      <div>
        <div className="stat-value">{value ?? 0}</div>
        <div className="text-secondary small">{label}</div>
      </div>
    </div>
  );
}
