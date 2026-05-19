import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { parseCategories } from './utils/formatters';

const Dashboard = ({ token }) => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchLoading, setSearchLoading] = useState(false);
  const [error, setError] = useState('');
  const [statementOptions, setStatementOptions] = useState([]);
  const [customerOptions, setCustomerOptions] = useState([]);
  const [selectedStatementId, setSelectedStatementId] = useState('');
  const [selectedCustomer, setSelectedCustomer] = useState('');
  const [descriptionFilter, setDescriptionFilter] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [dateFrom, setDateFrom] = useState('');
  const [dateTo, setDateTo] = useState('');

  const resetFilters = () => {
    setSelectedStatementId('');
    setSelectedCustomer('');
    setDescriptionFilter('');
    setCategoryFilter('');
    setDateFrom('');
    setDateTo('');
  };

  const toTitleCase = (value) =>
    value
      ? value
          .toLowerCase()
          .split(' ')
          .filter((part) => part)
          .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
          .join(' ')
      : '';

  const fetchTransactions = async ({
    statementFileId = selectedStatementId,
    accountHolder = selectedCustomer,
    description = descriptionFilter,
    category = categoryFilter,
    transactionDateFrom = dateFrom,
    transactionDateTo = dateTo
  } = {}) => {
    setSearchLoading(true);
    setError('');
    try {
      const params = {};
      if (statementFileId) params.statementFileId = statementFileId;
      if (accountHolder) params.accountHolder = accountHolder;
      if (description.trim()) params.description = description.trim();
      if (category.trim()) params.category = category.trim();
      if (transactionDateFrom) params.transactionDateFrom = transactionDateFrom;
      if (transactionDateTo) params.transactionDateTo = transactionDateTo;

      const response = await axios.get('/transactions/search', {
        headers: {
          Authorization: `Bearer ${token}`
        },
        params
      });
      setTransactions(response.data?.content || []);
    } catch (err) {
      setError('Failed to search transactions. Please try again.');
      console.error('Search API Error:', err);
    } finally {
      setSearchLoading(false);
    }
  };

  useEffect(() => {
    const fetchFilters = async () => {
      setLoading(true);
      setError('');
      try {
        const [statementRes, customerRes] = await Promise.all([
          axios.get('/statement-year-month', {
            headers: { Authorization: `Bearer ${token}` }
          }),
          axios.get('/customer-name', {
            headers: { Authorization: `Bearer ${token}` }
          })
        ]);

        const statementItems = (statementRes.data || [])
          .map((item) => ({
            id: item.id,
            value: item.statementYearMonth ? item.statementYearMonth.toString().substring(0, 7) : ''
          }))
          .filter((option) => option.value)
          .sort((a, b) => b.value.localeCompare(a.value));

        const customerItems = (customerRes.data || []).map((customer) => ({
          value: customer,
          label: toTitleCase(customer)
        }));

        setStatementOptions(statementItems);
        setCustomerOptions(customerItems);

        const mostRecent = statementItems[0];
        if (mostRecent) {
          setSelectedStatementId(mostRecent.id);
          await fetchTransactions({ statementFileId: mostRecent.id });
        } else {
          setTransactions([]);
        }
      } catch (err) {
        console.error('Failed to load search filters:', err);
        setError('Failed to load filters. Please refresh the page.');
      } finally {
        setLoading(false);
      }
    };

    if (token) {
      fetchFilters();
    }
  }, [token]);

  const buildSearchParams = () => {
    const params = {};

    if (selectedStatementId) {
      params.statementFileId = selectedStatementId;
    }
    if (selectedCustomer) {
      params.accountHolder = selectedCustomer;
    }
    if (descriptionFilter.trim()) {
      params.description = descriptionFilter.trim();
    }
    if (categoryFilter.trim()) {
      params.category = categoryFilter.trim();
    }
    if (dateFrom) {
      params.transactionDateFrom = dateFrom;
    }
    if (dateTo) {
      params.transactionDateTo = dateTo;
    }

    return params;
  };

  const handleSearch = async () => {
    await fetchTransactions();
  };

  const filteredData = transactions;

  const totalAmount = filteredData.reduce((sum, txn) => {
    const amt = txn.debitCredit === 'Cr' ? -txn.amt : txn.amt;
    return sum + amt;
  }, 0);

  const totalRewards = filteredData.reduce((sum, txn) => sum + txn.rewards, 0);

  return (
    <div className="min-h-[calc(100vh-4rem)] bg-slate-50 text-slate-900">
      <div className="max-w-full mx-auto bg-white rounded-xl shadow-md border border-slate-200 overflow-hidden">
        {/* Search Panel */}
        <div className="p-6 border-b border-slate-100">
          <div className="grid gap-4 xl:grid-cols-2 lg:grid-cols-1">
            <div className="grid gap-4 md:grid-cols-2">
              <div className="flex items-center gap-3">
                <span className="text-sm font-medium text-slate-700 min-w-[120px]">Statement</span>
                <select
                  value={selectedStatementId || ''}
                  onChange={(e) => setSelectedStatementId(Number(e.target.value))}
                  className="flex-1 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
                >
                  <option value="" disabled>
                    Select
                  </option>
                  {statementOptions.map((option) => (
                    <option key={option.id} value={option.id}>
                      {option.value}
                    </option>
                  ))}
                </select>
              </div>

              <div className="flex items-center gap-3">
                <span className="text-sm font-medium text-slate-700 min-w-[120px]">Account Holder</span>
                <select
                  value={selectedCustomer || ''}
                  onChange={(e) => setSelectedCustomer(e.target.value)}
                  className="flex-1 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
                >
                  <option value="" disabled>
                    Select
                  </option>
                  {customerOptions.map((customer) => (
                    <option key={customer.value} value={customer.value}>
                      {customer.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <input
                  type="text"
                  value={descriptionFilter}
                  onChange={(e) => setDescriptionFilter(e.target.value)}
                  placeholder="Search Description"
                  className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-500 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
                />
              </div>
              <div>
                <input
                  type="text"
                  value={categoryFilter}
                  onChange={(e) => setCategoryFilter(e.target.value)}
                  placeholder="Search Categories"
                  className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-500 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
                />
              </div>
            </div>

            <div className="grid gap-4 md:grid-cols-2">
              <div className="flex items-center gap-3">
                <span className="text-sm font-medium text-slate-700">Transaction Date From</span>
                <input
                  type="date"
                  value={dateFrom}
                  onChange={(e) => setDateFrom(e.target.value)}
                  className="flex-1 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
                />
              </div>
              <div className="flex items-center gap-3">
                <span className="text-sm font-medium text-slate-700">Transaction Date To</span>
                <input
                  type="date"
                  value={dateTo}
                  onChange={(e) => setDateTo(e.target.value)}
                  className="flex-1 rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 focus:border-blue-400 focus:ring-2 focus:ring-blue-200 outline-none"
                />
              </div>
            </div>

            <div className="flex items-end justify-end gap-3">
              <button
                type="button"
                onClick={resetFilters}
                className="inline-flex rounded-lg border border-slate-300 bg-white px-4 py-3 text-slate-700 font-semibold hover:bg-slate-50 transition-colors"
              >
                Reset
              </button>
              <button
                onClick={handleSearch}
                disabled={searchLoading}
                className="inline-flex rounded-lg bg-blue-600 px-6 py-3 text-white font-semibold hover:bg-blue-700 transition-colors disabled:cursor-not-allowed disabled:opacity-60"
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
