import { Navigate } from 'react-router-dom';
import { getCurrentUser } from '../services/authService';

const ProtectedRoute = ({ children, allowedRoles }) => {
    const user = getCurrentUser();

    // 1. Если пользователь вообще не залогинен
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    // 2. Если залогинен, но роль не совпадает (если роли переданы)
    if (allowedRoles && !allowedRoles.includes(user.role)) {
        return <Navigate to="/books" replace />;
    }

    return children;
};

export default ProtectedRoute;
