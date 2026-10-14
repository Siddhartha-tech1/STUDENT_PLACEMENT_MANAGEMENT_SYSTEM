import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('spms_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.message
      || (error.code === 'ERR_NETWORK' ? 'Cannot connect to the backend. Start it on http://localhost:8080 and verify MongoDB access.' : error.message)
      || 'Request failed';
    return Promise.reject(new Error(message));
  }
);

export default api;
