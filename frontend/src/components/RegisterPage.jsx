import React, { useState } from 'react';
import { authApi } from '../api/axios';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    // Поля должны СТРОГО совпадать с твоим JSON
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        firstName: '',
        lastName: ''
    });
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            // Отправляем именно тот объект, который ждет бэкенд
            await authApi.post('/auth/register', formData);
            alert('Регистрация успешна!');
            navigate('/login');
        } catch (error) {
            console.error("Детали ошибки:", error.response?.data);
            alert('Ошибка регистрации: ' + (error.response?.data?.message || 'Некорректные данные'));
        }
    };

    return (
        <div className="auth-container">
            <h2>Регистрация</h2>
            <form onSubmit={handleRegister}>
                <input
                    placeholder="Логин"
                    onChange={(e) => setFormData({...formData, username: e.target.value})}
                    required
                />
                <input
                    type="password"
                    placeholder="Пароль"
                    onChange={(e) => setFormData({...formData, password: e.target.value})}
                    required
                />
                <input
                    placeholder="Имя"
                    onChange={(e) => setFormData({...formData, firstName: e.target.value})}
                    required
                />
                <input
                    placeholder="Фамилия"
                    onChange={(e) => setFormData({...formData, lastName: e.target.value})}
                    required
                />
                <button type="submit">Создать аккаунт</button>
            </form>
        </div>
    );
};

export default RegisterPage;
