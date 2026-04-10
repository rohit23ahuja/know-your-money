import React, { useState, useEffect } from 'react';
import Login from './Login';
import MainLayout from './MainLayout';
import Dashboard from './Dashboard';
import UploadStatement from './UploadStatement';

const App = () => {
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [currentPage, setCurrentPage] = useState('transactions');

  useEffect(() => {
    // Check if token exists in localStorage on app load
    const storedToken = localStorage.getItem('authToken');
    if (storedToken) {
      setToken(storedToken);
    }
    setLoading(false);
  }, []);

  const handleLoginSuccess = (authToken) => {
    setToken(authToken);
    setCurrentPage('transactions');
    setSidebarOpen(true);
  };

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    setToken(null);
    setCurrentPage('transactions');
    setSidebarOpen(true);
  };

  const handleNavigate = (page) => {
    setCurrentPage(page);
  };

  const handleToggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  const renderContent = () => {
    switch (currentPage) {
      case 'transactions':
        return <Dashboard token={token} />;
      case 'upload':
        return <UploadStatement />;
      default:
        return <Dashboard token={token} />;
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center">
        <div className="inline-block border-4 border-slate-200 border-t-blue-600 rounded-full w-8 h-8 animate-spin"></div>
      </div>
    );
  }

  return token ? (
    <MainLayout
      token={token}
      onLogout={handleLogout}
      sidebarOpen={sidebarOpen}
      onToggleSidebar={handleToggleSidebar}
      currentPage={currentPage}
      onNavigate={handleNavigate}
    >
      {renderContent()}
    </MainLayout>
  ) : (
    <Login onLoginSuccess={handleLoginSuccess} />
  );
};

export default App;