import { useEffect, useState } from 'react';
import api from '../api/api';

function CustomersList() {
  const [customers, setCustomers] = useState([]);
  const [error, setError] = useState(null);

  const loadCustomers = async () => {
    setError(null);
    try {
      const response = await api.get('/customers');
      setCustomers(response.data);
    } catch {
      setError('No se pudo cargar la lista de clientes.');
    }
  };

  useEffect(() => {
    loadCustomers();
  }, []);

  return (
    <div className="card">
      <div className="card-header">
        <h2>Clientes</h2>
        <button type="button" onClick={loadCustomers}>Actualizar</button>
      </div>
      {error && <p className="message-error">{error}</p>}
      <table className="table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Cuenta</th>
            <th>Saldo</th>
          </tr>
        </thead>
        <tbody>
          {customers.map((customer) => (
            <tr key={customer.id}>
              <td>{customer.firstName} {customer.lastName}</td>
              <td>{customer.accountNumber}</td>
              <td>${Number(customer.balance).toFixed(2)}</td>
            </tr>
          ))}
        </tbody>
      </table>
      {customers.length === 0 && !error && <p className="muted">No hay clientes registrados.</p>}
    </div>
  );
}

export default CustomersList;
