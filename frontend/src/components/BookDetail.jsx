import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { coreApi } from '../api/axios';
import { getCurrentUser } from '../services/authService';

const BookDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const user = getCurrentUser();

    useEffect(() => {
        coreApi.get(`/books/${id}`)
            .then(res => {
                setBook(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Ошибка загрузки книги:", err);
                setLoading(false);
            });
    }, [id]);

    const handleReserve = async () => {
        try {
            await coreApi.post(`/reservations?bookId=${id}`);
            alert("Книга успешно забронирована!");
            const updated = await coreApi.get(`/books/${id}`);
            setBook(updated.data);
        } catch (err) {
            alert("Ошибка: " + (err.response?.data?.message || "Не удалось забронировать"));
        }
    };

    if (loading) return <div style={{ padding: '20px' }}>Загрузка...</div>;
    if (!book) return <div style={{ padding: '20px' }}>Книга не найдена</div>;

    return (
        <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
            <button
                onClick={() => navigate('/books')}
                style={{ marginBottom: '20px', cursor: 'pointer', padding: '5px 15px' }}
            >
                ← Вернуться в каталог
            </button>

            <div style={{ border: '1px solid #ddd', padding: '30px', borderRadius: '12px', boxShadow: '0 4px 15px rgba(0,0,0,0.1)' }}>
                <h1 style={{ marginTop: 0 }}>{book.title}</h1>
                <h3 style={{ color: '#555' }}>Автор: {book.author}</h3>

                <div style={{ margin: '20px 0' }}>
                    <p><strong>Жанр:</strong> <span style={{ background: '#e9ecef', padding: '3px 8px', borderRadius: '4px' }}>{book.genre}</span></p>
                </div>

                <div style={{ backgroundColor: '#f8f9fa', padding: '15px', borderRadius: '8px', borderLeft: '5px solid #007bff' }}>
                    <p><strong>Доступно для выдачи:</strong> {book.availableCopies} шт.</p>
                    <p><strong>Всего в библиотеке:</strong> {book.totalCopies} шт.</p>
                </div>

                {user?.role === 'READER' && (
                    <button
                        onClick={handleReserve}
                        disabled={book.availableCopies <= 0}
                        style={{
                            marginTop: '30px',
                            padding: '12px 25px',
                            fontSize: '1rem',
                            backgroundColor: book.availableCopies > 0 ? '#28a745' : '#ccc',
                            color: 'white',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: book.availableCopies > 0 ? 'pointer' : 'not-allowed',
                            width: '100%'
                        }}
                    >
                        {book.availableCopies > 0 ? "Забронировать сейчас" : "Нет в наличии"}
                    </button>
                )}

                {(user?.role === 'ADMIN' || user?.role === 'LIBRARIAN') && (
                    <div style={{ marginTop: '20px', padding: '10px', border: '1px dashed #ccc' }}>
                        <p style={{ color: '#666', fontStyle: 'italic', margin: 0 }}>
                            Режим сотрудника: просмотр статистики фонда.
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default BookDetail;
