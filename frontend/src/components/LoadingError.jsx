export function Loading({ text = 'Loading...' }) {
  return <div className="surface p-4 text-secondary">{text}</div>;
}

export function ErrorMessage({ message }) {
  return message ? <div className="alert alert-danger">{message}</div> : null;
}

export function EmptyState({ text }) {
  return <div className="surface p-4 text-secondary">{text}</div>;
}
