import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Добавили это для переходов
import { coreApi } from '../api/axios';
import { getCurrentUser } from '../services/authService';

const BookList = () => {
    const [books, setBooks] = useState([]);
    const [error, setError] = useState(null);
    const user = getCurrentUser();
    const navigate = useNavigate(); // Создаем "навигатор"

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const response = await coreApi.get('/books');
                setBooks(response.data);
            } catch (err) {
                console.error("Ошибка при получении книг:", err);
                setError("Не удалось загрузить книги. Проверьте Core Service.");
            }
        };
        fetchBooks();
    }, []);

    const handleReserve = async (bookId) => {
        try {
            await coreApi.post(`/reservations?bookId=${bookId}`);
            alert("Книга успешно забронирована!");
            const updated = await coreApi.get('/books');
            setBooks(updated.data);
        } catch (err) {
            alert("Ошибка: " + (err.response?.data?.message || "Нет в наличии"));
        }
    };

    if (error) return <div style={{color: 'red'}}>{error}</div>;

    return (
        <div>
            <h2>Каталог книг</h2>
            <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
                gap: '20px'
            }}>
                {books.length === 0 ? (
                    <p>Книг пока нет...</p>
                ) : (
                    books.map(book => (
                        <div key={book.id} style={{
                            border: '1px solid #ccc',
                            padding: '15px',
                            borderRadius: '8px',
                            boxShadow: '2px 2px 10px rgba(0,0,0,0.1)'
                        }}>
                            <h3>{book.title}</h3>
                            <p>Автор: <b>{book.author}</b></p>
                            <p>Доступно копий: {book.availableCopies}</p>

                            <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                                {/* Кнопка перехода на страницу конкретной книги */}
                                <button
                                    onClick={() => navigate(`/books/${book.id}`)}
                                    style={{ padding: '8px 12px', cursor: 'pointer', backgroundColor: '#6c757d', color: 'white', border: 'none', borderRadius: '4px' }}
                                >
                                    Подробнее
                                </button>

                                {/* Кнопка только для Читателя */}
                                {user?.role === 'READER' && (
                                    <button
                                        onClick={() => handleReserve(book.id)}
                                        disabled={book.availableCopies === 0}
                                        style={{
                                            padding: '8px 12px',
                                            cursor: book.availableCopies > 0 ? 'pointer' : 'not-allowed',
                                            backgroundColor: book.availableCopies > 0 ? '#28a745' : '#ccc',
                                            color: 'white',
                                            border: 'none',
                                            borderRadius: '4px'
                                        }}
                                    >
                                        {book.availableCopies > 0 ? "Забронировать" : "Разобрали"}
                                    </button>
                                )}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default BookList;