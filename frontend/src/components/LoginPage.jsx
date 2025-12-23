import React, { useState } from 'react';
import { login } from '../services/authService';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const user = await login(credentials);
            // Если админ — в панель управления, если нет — в каталог
            if (user.role === 'ADMIN' || user.role === 'LIBRARIAN') {
                navigate('/admin/users');
            } else {
                navigate('/books');
            }
        } catch (error) {
            alert("Ошибка входа: " + error.response?.data || error.message);
        }
    };

    return (
        <div className="login-container">
            <h2>Вход в библиотеку</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Логин"
                    onChange={(e) => setCredentials({...credentials, username: e.target.value})}
                />
                <input
                    type="password"
                    placeholder="Пароль"
                    onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                />
                <button type="submit">Войти</button>

                <div style={{ marginTop: '15px', textAlign: 'center' }}>
                    <span>Нет аккаунта? </span>
                    <button
                        type="button"
                        onClick={() => navigate('/register')}
                        style={{ background: 'none', border: 'none', color: '#007bff', cursor: 'pointer', textDecoration: 'underline' }}
                    >
                        Зарегистрироваться
                    </button>
                </div>
            </form>
        </div>
    );
};

export default LoginPage;
