import { Link } from 'react-router-dom';

export default function NotFound() {
  return (
    <main className="public-page">
      <div className="surface">
        <h1>Page not found</h1>
        <p className="text-secondary">The page you opened does not exist.</p>
        <Link className="btn btn-primary" to="/">Go home</Link>
      </div>
    </main>
  );
}
