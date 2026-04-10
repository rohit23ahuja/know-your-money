import React from 'react';
import { ChevronRight } from 'lucide-react';

const Sidebar = ({ isOpen, currentPage, onNavigate }) => {
  const menuItems = [
    { id: 'transactions', label: 'Transactions', icon: '📊' },
    { id: 'upload', label: 'Upload Statement', icon: '📁' }
  ];

  return (
    <div
      className={`fixed left-0 top-16 h-[calc(100vh-4rem)] bg-white border-r border-slate-200 transition-all duration-300 ${
        isOpen ? 'w-48' : 'w-0 -translate-x-48'
      } overflow-hidden z-40`}
    >
      <nav className="p-4 space-y-2">
        {menuItems.map((item) => (
          <button
            key={item.id}
            onClick={() => onNavigate(item.id)}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-colors text-left ${
              currentPage === item.id
                ? 'bg-blue-100 text-blue-700 border-l-4 border-blue-600 font-semibold'
                : 'text-slate-600 hover:bg-slate-50'
            }`}
          >
            <span className="text-lg">{item.icon}</span>
            <span>{item.label}</span>
            {currentPage === item.id && <ChevronRight size={18} className="ml-auto" />}
          </button>
        ))}
      </nav>
    </div>
  );
};

export default Sidebar;
