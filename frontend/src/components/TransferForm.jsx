import { useState } from 'react';
import api from '../api/api';

const initialForm = {
  senderAccountNumber: '',
  receiverAccountNumber: '',
  amount: '',
};

function TransferForm() {
  const [form, setForm] = useState(initialForm);
  const [result, setResult] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setResult(null);
    try {
      const response = await api.post('/transactions', {
        senderAccountNumber: form.senderAccountNumber,
        receiverAccountNumber: form.receiverAccountNumber,
        amount: parseFloat(form.amount),
      });
      setResult({ type: 'success', data: response.data });
      setForm(initialForm);
    } catch (error) {
      const message = typeof error.response?.data === 'string'
        ? error.response.data
        : 'No se pudo realizar la transferencia.';
      setResult({ type: 'error', message });
    }
  };

  return (
    <div className="card">
      <h2>Transferir</h2>
      <form onSubmit={handleSubmit} className="form">
        <label>
          Cuenta origen
          <input name="senderAccountNumber" value={form.senderAccountNumber} onChange={handleChange} required />
        </label>
        <label>
          Cuenta destino
          <input name="receiverAccountNumber" value={form.receiverAccountNumber} onChange={handleChange} required />
        </label>
        <label>
          Monto
          <input name="amount" type="number" step="0.01" value={form.amount} onChange={handleChange} required />
        </label>
        <button type="submit">Confirmar transferencia</button>
      </form>
      {result?.type === 'success' && (
        <div className="message-success">
          <p>Transferencia realizada correctamente.</p>
          <ul>
            <li>ID: {result.data.id}</li>
            <li>De: {result.data.senderAccountNumber}</li>
            <li>A: {result.data.receiverAccountNumber}</li>
            <li>Monto: {result.data.amount}</li>
            <li>Fecha: {result.data.timestamp}</li>
          </ul>
        </div>
      )}
      {result?.type === 'error' && (
        <p className="message-error">{result.message}</p>
      )}
    </div>
  );
}

export default TransferForm;
