import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Search } from 'lucide-react';
import { parseCategories } from './utils/formatters';

const Dashboard = ({ token }) => {
  const [transactions, setTransactions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [searchLoading, setSearchLoading] = useState(false);
  const [error, setError] = useState('');
  const [statementOptions, setStatementOptions] = useState([]);
  const [customerOptions, setCustomerOptions] = useState([]);
  const [selectedStatements, setSelectedStatements] = useState([]);
  const [selectedCustomers, setSelectedCustomers] = useState([]);
  const [descriptionFilter, setDescriptionFilter] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [dateFrom, setDateFrom] = useState('');
  const [dateTo, setDateTo] = useState('');

  useEffect(() => {
    const fetchFilters = async () => {
      try {
        const [statementRes, customerRes] = await Promise.all([
          axios.get('/statement-year-month', {
            headers: { Authorization: `Bearer ${token}` }
          }),
          axios.get('/customer-name', {
            headers: { Authorization: `Bearer ${token}` }
          })
        ]);

        setStatementOptions(
          statementRes.data.map((item) => ({
            id: item.id,
            value: item.statementYearMonth
              ? item.statementYearMonth.toString().substring(0, 7)
              : ''
          }))
        );
        setCustomerOptions(customerRes.data || []);
      } catch (err) {
        console.error('Failed to load search filters:', err);
      }
    };

    const fetchTransactions = async () => {
      setLoading(true);
      setError('');
      try {
        const response = await axios.get('/transactions', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setTransactions(response.data);
      } catch (err) {
        setError('Failed to load transactions. Please try again.');
        console.error('API Error:', err);
      } finally {
        setLoading(false);
      }
    };

    if (token) {
      fetchFilters();
      fetchTransactions();
    }
  }, [token]);

  const buildSearchParams = () => {
    const params = {};

    if (selectedStatements.length > 0) {
      params.statementYearMonth = selectedStatements;
    }
    if (selectedCustomers.length > 0) {
      params.customerName = selectedCustomers;
    }
    if (descriptionFilter.trim()) {
      params.description = descriptionFilter.trim();
    }
    if (categoryFilter.trim()) {
      params.category = categoryFilter.trim();
    }
    if (dateFrom) {
      params.txnDateFrom = dateFrom;
    }
    if (dateTo) {
      params.txnDateTo = dateTo;
    }

    return params;
  };

  const handleSearch = async () => {
    setSearchLoading(true);
    setError('');
    try {
      const response = await axios.get('/transactions/search', {
        headers: {
          Authorization: `Bearer ${token}`
        },
        params: buildSearchParams()
      });
      setTransactions(response.data);
    } catch (err) {
      setError('Failed to search transactions. Please try again.');
      console.error('Search API Error:', err);
    } finally {
      setSearchLoading(false);
    }
  };

  const handleMultiSelect = (event, setter) => {
    const values = Array.from(event.target.selectedOptions, (option) => option.value);
    setter(values);
  };

  const filteredData = transactions.filter(
    (t) =>
      t.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      t.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      t.transactionCategorization.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalAmount = filteredData.reduce((sum, txn) => {
    const amt = txn.debitCredit === 'Cr' ? -txn.amt : txn.amt;
    return sum + amt;
  }, 0);

  const totalRewards = filteredData.reduce((sum, txn) => sum + txn.rewards, 0);

  return (
    <div className="min-h-[calc(100vh-4rem)] bg-slate-50 text-slate-900">
      <div className="max-w-full mx-auto bg-white rounded-xl shadow-md border border-slate-200 overflow-hidden">
        {/* Header Section */}
        <div className="p-6 border-b border-slate-100 flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
              <span>📊</span>
              Expense Dashboard
            </h1>
          </div>
          <div className="relative">
            <Search size={18} className="absolute left-3 top-2.5 text-slate-400" />
            <input
              type="text"
              placeholder="Search descriptions..."
              className="pl-10 pr-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none w-full md:w-80 bg-slate-50"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {/* Search Panel */}
        <div className="p-6 border-b border-slate-100">
          <div className="grid gap-4 xl:grid-cols-4 lg:grid-cols-3 md:grid-cols-2">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Statement</label>
              <select
                multiple
                value={selectedStatements}
                onChange={(e) => handleMultiSelect(e, setSelectedStatements)}
                className="w-full h-28 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
              >
                {statementOptions.map((option) => (
                  <option key={option.id} value={option.value}>
                    {option.value}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Account Holder</label>
              <select
                multiple
                value={selectedCustomers}
                onChange={(e) => handleMultiSelect(e, setSelectedCustomers)}
                className="w-full h-28 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
              >
                {customerOptions.map((customer) => (
                  <option key={customer} value={customer}>
                    {customer}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Description</label>
              <input
                type="text"
                value={descriptionFilter}
                onChange={(e) => setDescriptionFilter(e.target.value)}
                placeholder="Description"
                className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Category</label>
              <input
                type="text"
                value={categoryFilter}
                onChange={(e) => setCategoryFilter(e.target.value)}
                placeholder="Category"
                className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Transaction Date From</label>
              <input
                type="date"
                value={dateFrom}
                onChange={(e) => setDateFrom(e.target.value)}
                className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Transaction Date To</label>
              <input
                type="date"
                value={dateTo}
                onChange={(e) => setDateTo(e.target.value)}
                className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
              />
            </div>

            <div className="flex items-end">
              <button
                onClick={handleSearch}
                disabled={searchLoading}
                className="w-full rounded-lg bg-blue-600 px-4 py-3 text-white font-semibold hover:bg-blue-700 transition-colors disabled:cursor-not-allowed disabled:opacity-60"
              >
                {searchLoading ? 'Searching...' : 'Search'}
              </button>
            </div>
          </div>
        </div>

        {/* Error State */}
        {error && (
          <div className="bg-red-50 border-b border-red-200 p-4 text-red-700 text-sm">
            {error}
          </div>
        )}

        {/* Loading State */}
        {loading && (
          <div className="p-12 text-center text-slate-500">
            <div className="inline-block border-4 border-slate-200 border-t-blue-600 rounded-full w-8 h-8 animate-spin"></div>
            <p className="mt-4">Loading transactions...</p>
          </div>
        )}

        {/* Grid/Table */}
        {!loading && !error && (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse table-fixed">
              <colgroup>
                <col style={{ width: '12%' }} />
                <col style={{ width: '22%' }} />
                <col style={{ width: '15%' }} />
                <col style={{ width: '12%' }} />
                <col style={{ width: '12%' }} />
                <col style={{ width: '27%' }} />
              </colgroup>
              <thead className="bg-slate-50 text-slate-500 text-xs font-semibold uppercase">
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
                  <tr
                    key={txn.id}
                    className={`transition-colors ${
                      txn.debitCredit === 'Cr'
                        ? 'bg-green-50/50 hover:bg-green-100/50'
                        : 'hover:bg-blue-50/50'
                    }`}
                  >
                    <td className="px-4 py-3 text-sm text-slate-500">{txn.txnDateTime}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">
                      <div className="flex items-center gap-2">
                        {txn.debitCredit === 'Cr' && (
                          <span className="px-2 py-1 bg-green-600 text-white text-[10px] font-bold rounded">
                            CREDIT
                          </span>
                        )}
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
              <tfoot className="bg-slate-100 font-semibold">
                <tr>
                  <td colSpan="3" className="px-4 py-3 text-sm text-slate-800">Total</td>
                  <td className="px-4 py-3 font-bold text-slate-900">₹{totalAmount.toLocaleString('en-IN')}</td>
                  <td className="px-4 py-3 font-bold text-slate-900">{totalRewards}</td>
                  <td></td>
                </tr>
              </tfoot>
            </table>
          </div>
        )}

        {/* Empty State */}
        {!loading && !error && filteredData.length === 0 && (
          <div className="p-12 text-center text-slate-500">
            <p>No transactions found</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
