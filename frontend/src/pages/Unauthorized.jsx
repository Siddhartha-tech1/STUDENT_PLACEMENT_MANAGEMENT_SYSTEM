import { Link } from 'react-router-dom';

export default function Unauthorized() {
  return (
    <main className="public-page">
      <div className="surface">
        <h1>Access denied</h1>
        <p className="text-secondary">Your account does not have permission to open this page.</p>
        <Link className="btn btn-primary" to="/">Go home</Link>
      </div>
    </main>
  );
}
