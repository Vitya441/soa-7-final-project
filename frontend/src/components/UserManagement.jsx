import React, { useEffect, useState, useCallback } from 'react';
import { authApi } from '../api/axios';

const UserManagement = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);

    // Состояние для формы создания
    const [newUser, setNewUser] = useState({
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        role: 'LIBRARIAN' // По умолчанию создаем библиотекаря
    });

    const fetchUsers = useCallback(async () => {
        setLoading(true);
        try {
            const response = await authApi.get('/users');
            setUsers(response.data);
        } catch (error) {
            console.error("Ошибка загрузки пользователей", error);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    const handleCreateUser = async (e) => {
        e.preventDefault();
        try {
            await authApi.post('/users', newUser);
            alert("Пользователь успешно создан!");
            setNewUser({ username: '', password: '', firstName: '', lastName: '', role: 'LIBRARIAN' }); // Очистка
            fetchUsers(); // Обновление списка
        } catch (error) {
            alert("Ошибка создания: " + (error.response?.data?.message || "Проверьте права доступа"));
        }
    };

    const deleteUser = async (id) => {
        if (window.confirm("Удалить этого пользователя?")) {
            try {
                await authApi.delete(`/users/${id}`);
                fetchUsers();
            } catch (error) {
                alert("Ошибка при удалении");
            }
        }
    };

    return (
        <div className="admin-container">
            <h2>Панель управления пользователями</h2>

            {/* Форма создания */}
            <section style={{ marginBottom: '30px', padding: '20px', border: '1px solid #ddd', borderRadius: '8px' }}>
                <h3>Добавить нового сотрудника/пользователя</h3>
                <form onSubmit={handleCreateUser} style={{ display: 'grid', gap: '10px', maxWidth: '400px' }}>
                    <input placeholder="Username" value={newUser.username} onChange={e => setNewUser({...newUser, username: e.target.value})} required />
                    <input type="password" placeholder="Password" value={newUser.password} onChange={e => setNewUser({...newUser, password: e.target.value})} required />
                    <input placeholder="Имя" value={newUser.firstName} onChange={e => setNewUser({...newUser, firstName: e.target.value})} required />
                    <input placeholder="Фамилия" value={newUser.lastName} onChange={e => setNewUser({...newUser, lastName: e.target.value})} required />
                    <select value={newUser.role} onChange={e => setNewUser({...newUser, role: e.target.value})}>
                        <option value="READER">Читатель</option>
                        <option value="LIBRARIAN">Библиотекарь</option>
                        <option value="ADMIN">Администратор</option>
                    </select>
                    <button type="submit" style={{ padding: '10px', background: '#28a745', color: 'white', border: 'none', borderRadius: '4px' }}>
                        Создать пользователя
                    </button>
                </form>
            </section>

            {/* Таблица пользователей */}
            <h3>Список всех пользователей</h3>
            {loading ? <p>Загрузка...</p> : (
                <table border="1" style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
                    <thead style={{ background: '#f8f9fa' }}>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Имя Фамилия</th>
                        <th>Роль</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map(user => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.username}</td>
                            <td>{user.firstName} {user.lastName}</td>
                            <td>{user.role}</td>
                            <td>
                                <button
                                    onClick={() => deleteUser(user.id)}
                                    style={{ color: 'white', background: '#dc3545', border: 'none', padding: '5px 10px', borderRadius: '4px', cursor: 'pointer' }}
                                >
                                    Удалить
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default UserManagement;
