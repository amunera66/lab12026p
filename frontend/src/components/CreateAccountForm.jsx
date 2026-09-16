import { useState } from 'react';
import api from '../api/api';

const initialForm = {
  firstName: '',
  lastName: '',
  accountNumber: '',
  balance: '',
};

function CreateAccountForm() {
  const [form, setForm] = useState(initialForm);
  const [status, setStatus] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus(null);
    try {
      const response = await api.post('/customers', {
        firstName: form.firstName,
        lastName: form.lastName,
        accountNumber: form.accountNumber,
        balance: parseFloat(form.balance),
      });
      setStatus({ type: 'success', message: `Cliente creado: ${response.data.firstName} ${response.data.lastName} (cuenta ${response.data.accountNumber})` });
      setForm(initialForm);
    } catch (error) {
      const message = typeof error.response?.data === 'string'
        ? error.response.data
        : error.response?.data?.message || 'No se pudo crear el cliente.';
      setStatus({ type: 'error', message });
    }
  };

  return (
    <div className="card">
      <h2>Nueva cuenta</h2>
      <form onSubmit={handleSubmit} className="form">
        <label>
          Nombre
          <input name="firstName" value={form.firstName} onChange={handleChange} required />
        </label>
        <label>
          Apellido
          <input name="lastName" value={form.lastName} onChange={handleChange} required />
        </label>
        <label>
          Número de cuenta
          <input name="accountNumber" value={form.accountNumber} onChange={handleChange} required />
        </label>
        <label>
          Saldo inicial
          <input name="balance" type="number" step="0.01" value={form.balance} onChange={handleChange} required />
        </label>
        <button type="submit">Crear cuenta</button>
      </form>
      {status && (
        <p className={status.type === 'success' ? 'message-success' : 'message-error'}>
          {status.message}
        </p>
      )}
    </div>
  );
}

export default CreateAccountForm;
