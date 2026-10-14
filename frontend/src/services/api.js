import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'https://spms-api-jilp.onrender.com/api'
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('spms_token');

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.message ||
      (error.code === 'ERR_NETWORK'
        ? 'Cannot connect to the deployed backend. Please check the backend service and MongoDB connection.'
        : error.message) ||
      'Request failed';

    return Promise.reject(new Error(message));
  }
);

export default api;