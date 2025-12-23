import { Link, useNavigate } from 'react-router-dom';
import { getCurrentUser, logout } from '../services/authService';

const Navbar = () => {
    const user = getCurrentUser();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar" style={{ display: 'flex', gap: '20px', padding: '10px', background: '#f4f4f4', marginBottom: '20px' }}>
            <Link to="/books">Каталог книг</Link>

            {user ? (
                <div className="nav-right" style={{ display: 'flex', gap: '15px', alignItems: 'center', marginLeft: 'auto' }}>

                    {/* --- ДОБАВЛЯЕМ ЭТОТ БЛОК --- */}
                    {(user.role === 'ADMIN' || user.role === 'LIBRARIAN') && (
                        <Link to="/admin/users" style={{ fontWeight: 'bold', color: 'darkblue' }}>
                            Управление пользователями
                        </Link>
                    )}
                    {/* --------------------------- */}

                    <span>Вы вошли как: <b>{user.sub}</b></span>
                    <button onClick={handleLogout}>Выйти</button>
                </div>
            ) : (
                <div style={{ marginLeft: 'auto' }}>
                    <Link to="/login">Войти</Link>
                </div>
            )}
        </nav>
    );
};

export default Navbar;