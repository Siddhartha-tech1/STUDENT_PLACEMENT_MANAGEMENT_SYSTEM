import { useEffect, useState } from 'react';
import api from '../services/api';

let cachedOptions = null;

const item = (value, label, description) => ({ value, label, description, disabled: false });

// Registration must remain usable while the backend is starting or temporarily unavailable.
const fallbackOptions = {
  departments: [
    item('Computer Science', 'Computer Science', 'CSE and related programs'),
    item('Information Technology', 'Information Technology', 'IT and software programs'),
    item('Electronics', 'Electronics', 'ECE and electronics programs'),
    item('Electrical', 'Electrical', 'Electrical engineering'),
    item('Mechanical', 'Mechanical', 'Mechanical engineering'),
    item('Civil', 'Civil', 'Civil engineering'),
    item('Business Administration', 'Business Administration', 'Management programs'),
    item('All', 'All departments', 'Open to every department')
  ],
  graduationYears: Array.from({ length: 8 }, (_, index) => {
    const year = String(new Date().getFullYear() - 1 + index);
    return item(year, year, year === String(new Date().getFullYear()) ? 'Current graduating batch' : 'Graduation batch');
  })
};

export default function useOptions() {
  const [options, setOptions] = useState(cachedOptions || fallbackOptions);
  const [error, setError] = useState('');

  useEffect(() => {
    if (cachedOptions) return;
    api.get('/options')
      .then((res) => {
        cachedOptions = res.data;
        setOptions(res.data);
      })
      .catch((err) => {
        setOptions(fallbackOptions);
        setError(err.message);
      });
  }, []);

  return { options, error };
}
