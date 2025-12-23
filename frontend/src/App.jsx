import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import Navbar from './components/Navbar.jsx';
import LoginPage from './components/LoginPage.jsx';
import RegisterPage from './components/RegisterPage.jsx';
import UserManagement from './components/UserManagement.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import BookList from "./components/BookList.jsx";
import BookDetail from "./components/BookDetail.jsx"; // Обязательный импорт!

function App() {
    return (
        <Router>
            <Navbar/>
            <div style={{padding: '20px', maxWidth: '1200px', margin: '0 auto'}}>
                <Routes>
                    <Route path="/login" element={<LoginPage/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>

                    <Route path="/books" element={
                        <ProtectedRoute>
                            <BookList/>
                        </ProtectedRoute>
                    }/>

                    <Route path="/books/:id" element={
                        <ProtectedRoute>
                            <BookDetail/>
                        </ProtectedRoute>
                    }/>

                    <Route path="/admin/users" element={
                        <ProtectedRoute allowedRoles={['ADMIN', 'LIBRARIAN']}>
                            <UserManagement/>
                        </ProtectedRoute>
                    }/>

                    <Route path="/" element={<Navigate to="/books" replace/>}/>
                    <Route path="*" element={<h2>404: Страница не найдена</h2>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
