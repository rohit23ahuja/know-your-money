import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Search, ReceiptText } from 'lucide-react';
import { parseCategories } from './utils/formatters';

const App = () => {
  const [transactions, setTransactions] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2hpdF9hZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzc1ODA0MTE2LCJleHAiOjE3NzU4OTA1MTZ9.oCkApFkdf8vuR3Hvk7Szha63JWPh8H45tMvWGkryMW8'; // Replace with actual token retrieval logic
    axios
      .get('/transactions', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      .then((res) => setTransactions(res.data))
      .catch((err) => console.error("API Error:", err));
  }, []);

  const filteredData = transactions.filter(t => 
    t.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
    t.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    t.transactionCategorization.toLowerCase().includes(searchTerm.toLowerCase())
  );

return (
  <div className="min-h-screen bg-slate-50 p-4 md:p-10 text-slate-900">
    <div className="max-w-6xl mx-auto bg-white rounded-xl shadow-md border border-slate-200 overflow-hidden">
      {/* Header Section */}
      <div className="p-6 border-b border-slate-100 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
          <span className="p-2 bg-blue-600 rounded-lg text-white">💰</span>
          Expense Dashboard
        </h1>
        <div className="relative">
          <input
            type="text"
            placeholder="Search descriptions..."
            className="pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none w-full md:w-80 bg-slate-50"
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {/* Grid/Table */}
      <div className="overflow-x-auto">
        <table className="w-full text-left border-collapse table-fixed">          <colgroup>
            <col style={{ width: '12%' }} />
            <col style={{ width: '22%' }} />
            <col style={{ width: '15%' }} />
            <col style={{ width: '12%' }} />
            <col style={{ width: '12%' }} />
            <col style={{ width: '27%' }} />
          </colgroup>          <thead className="bg-slate-50 text-slate-500 text-xs font-semibold uppercase">
            <tr>
              <th className="px-4 py-3 whitespace-nowrap">Date</th>
              <th className="px-4 py-3 whitespace-nowrap">Description</th>
              <th className="px-4 py-3 whitespace-nowrap">Account Holder</th>
              <th className="px-4 py-3 whitespace-nowrap">Amount</th>
              <th className="px-4 py-3 whitespace-nowrap">Rewards</th>
              <th className="px-4 py-3 whitespace-nowrap">Categories</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100">
            {filteredData.map((txn) => (
              <tr key={txn.id} className={`transition-colors ${txn.debitCredit === 'Cr' ? 'bg-green-50/50 hover:bg-green-100/50' : 'hover:bg-blue-50/50'}`}>
                <td className="px-4 py-3 text-sm text-slate-500">{txn.txnDateTime}</td>
                <td className="px-4 py-3 font-medium text-slate-800">
                  <div className="flex items-center gap-2">
                    {txn.debitCredit === 'Cr' && <span className="px-2 py-1 bg-green-600 text-white text-[10px] font-bold rounded">CREDIT</span>}
                    {txn.description}
                  </div>
                </td>
                <td className="px-4 py-3 text-sm text-slate-600 font-medium">{txn.customerName}</td>
                <td className={`px-4 py-3 font-bold ${txn.debitCredit === 'Cr' ? 'text-green-700' : 'text-slate-900'}`}>
                  {txn.debitCredit === 'Cr' ? '+' : ''}₹{txn.amt.toLocaleString('en-IN')}
                </td>
                <td className={`px-4 py-3 font-bold ${txn.rewards > 0 ? 'text-green-700' : txn.rewards < 0 ? 'text-red-700' : 'text-slate-500'}`}>
                  {txn.rewards > 0 ? '+' : ''}{txn.rewards}
                </td>
                <td className="px-4 py-3">
                  <div className="flex flex-wrap gap-2">
                    {parseCategories(txn.transactionCategorization).map((cat, idx) => (
                      <span
                        key={idx}
                        className="px-2.5 py-0.5 rounded-full text-[10px] font-bold tracking-wider bg-blue-100 text-blue-700 border border-blue-200"
                      >
                        {cat}
                      </span>
                    ))}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  </div>
);

};

export default App;