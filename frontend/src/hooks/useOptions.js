import { useEffect, useState } from 'react';
import api from '../services/api';

let cachedOptions = null;

export default function useOptions() {
  const [options, setOptions] = useState(cachedOptions);
  const [error, setError] = useState('');

  useEffect(() => {
    if (cachedOptions) return;
    api.get('/options')
      .then((res) => {
        cachedOptions = res.data;
        setOptions(res.data);
      })
      .catch((err) => setError(err.message));
  }, []);

  return { options, error };
}
