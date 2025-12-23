import { authApi } from '../api/axios';
import { jwtDecode } from 'jwt-decode';

export const login = async (credentials) => {
    const response = await authApi.post('/auth/login', credentials);
    const token = response.data; // Твой контроллер возвращает String (токен)
    localStorage.setItem('token', token);

    // Декодируем токен, чтобы знать роль и ID пользователя
    const decoded = jwtDecode(token);
    return decoded;
};

export const logout = () => {
    localStorage.removeItem('token');
    window.location.href = '/login';
};

export const getCurrentUser = () => {
    const token = localStorage.getItem('token');
    if (!token) return null;
    return jwtDecode(token);
};
