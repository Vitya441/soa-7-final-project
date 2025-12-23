import axios from 'axios';

const API_AUTH_URL = 'http://localhost:8080/api/v1'
const API_CORE_URL = 'http://localhost:8081'

export const authApi = axios.create({
    baseURL: API_AUTH_URL,
});

export const coreApi = axios.create({
    baseURL: API_CORE_URL,
});

const authInterceptor = (config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}

authApi.interceptors.request.use(authInterceptor)
coreApi.interceptors.request.use(authInterceptor)
