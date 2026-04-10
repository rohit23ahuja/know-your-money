import React from 'react';
import { Menu } from 'lucide-react';
import Sidebar from './Sidebar';

const MainLayout = ({ token, onLogout, sidebarOpen, onToggleSidebar, currentPage, onNavigate, children }) => {
  return (
    <div className="min-h-screen bg-slate-50">
      {/* Header */}
      <header className="fixed top-0 w-full h-16 bg-white border-b border-slate-200 flex items-center justify-between px-6 z-50 shadow-sm">
        <div className="flex items-center gap-3">
          <button
            onClick={onToggleSidebar}
            className="p-2 hover:bg-slate-100 rounded-lg transition-colors"
            aria-label="Toggle sidebar"
          >
            <Menu size={24} className="text-slate-700" />
          </button>
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center text-white font-bold text-sm">
              💰
            </div>
            <h1 className="text-xl font-bold text-slate-800">Know Your Money</h1>
          </div>
        </div>

        <button
          onClick={onLogout}
          className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors font-medium"
        >
          Logout
        </button>
      </header>

      {/* Sidebar */}
      <Sidebar isOpen={sidebarOpen} currentPage={currentPage} onNavigate={onNavigate} />

      {/* Main Content */}
      <main className={`pt-16 transition-all duration-300 ${sidebarOpen ? 'md:ml-48' : ''}`}>
        {children}
      </main>
    </div>
  );
};

export default MainLayout;
