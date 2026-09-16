import { useState } from 'react';
import Logo from './components/Logo';
import CustomersList from './components/CustomersList';
import CreateAccountForm from './components/CreateAccountForm';
import TransferForm from './components/TransferForm';
import TransactionHistory from './components/TransactionHistory';

const TABS = [
  { id: 'clientes', label: 'Clientes' },
  { id: 'nueva-cuenta', label: 'Nueva cuenta' },
  { id: 'transferir', label: 'Transferir' },
  { id: 'historial', label: 'Historial' },
];

function App() {
  const [activeTab, setActiveTab] = useState('clientes');

  return (
    <div className="app">
      <header className="app-header">
        <Logo size={44} />
        <h1>
          ban<span className="brand-accent">LOCO</span>mbia
        </h1>
      </header>

      <nav className="tabs">
        {TABS.map((tab) => (
          <button
            key={tab.id}
            className={tab.id === activeTab ? 'tab tab-active' : 'tab'}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.label}
          </button>
        ))}
      </nav>

      <main className="app-main">
        {activeTab === 'clientes' && <CustomersList />}
        {activeTab === 'nueva-cuenta' && <CreateAccountForm />}
        {activeTab === 'transferir' && <TransferForm />}
        {activeTab === 'historial' && <TransactionHistory />}
      </main>
    </div>
  );
}

export default App;
