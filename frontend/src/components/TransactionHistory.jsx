import { useState } from 'react';
import api from '../api/api';

function TransactionHistory() {
  const [accountNumber, setAccountNumber] = useState('');
  const [transactions, setTransactions] = useState(null);
  const [error, setError] = useState(null);

  const handleSearch = async (e) => {
    e.preventDefault();
    setError(null);
    setTransactions(null);
    try {
      const response = await api.get(`/transactions/${accountNumber}`);
      setTransactions(response.data);
    } catch {
      setError('No se pudo obtener el historial de esa cuenta.');
    }
  };

  return (
    <div className="card">
      <h2>Historial de transacciones</h2>
      <form onSubmit={handleSearch} className="form form-inline">
        <label>
          Número de cuenta
          <input value={accountNumber} onChange={(e) => setAccountNumber(e.target.value)} required />
        </label>
        <button type="submit">Buscar</button>
      </form>
      {error && <p className="message-error">{error}</p>}
      {transactions && (
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Origen</th>
              <th>Destino</th>
              <th>Monto</th>
              <th>Fecha</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((tx) => (
              <tr key={tx.id}>
                <td>{tx.id}</td>
                <td>{tx.senderAccountNumber}</td>
                <td>{tx.receiverAccountNumber}</td>
                <td>${Number(tx.amount).toFixed(2)}</td>
                <td>{tx.timestamp}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {transactions && transactions.length === 0 && <p className="muted">Esta cuenta no tiene transacciones.</p>}
    </div>
  );
}

export default TransactionHistory;
