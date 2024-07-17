import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000,
});

axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response) {
        return Promise.reject(error.response.data);
      }
      return Promise.reject(error);
    }
);

export default axiosInstance;
