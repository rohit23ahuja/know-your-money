import React, { useState } from 'react';
import axios from 'axios';
import { Upload, Loader, AlertCircle, CheckCircle2 } from 'lucide-react';

const UploadStatement = () => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [statementType, setStatementType] = useState('Credit Card');
  const [reProcess, setReProcess] = useState('false');
  const [loading, setLoading] = useState(false);
  const [successResponse, setSuccessResponse] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files?.[0] || null);
    setSuccessResponse(null);
    setErrorMessage('');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage('');
    setSuccessResponse(null);

    if (!selectedFile) {
      setErrorMessage('Please select a statement file to upload.');
      return;
    }

    const token = localStorage.getItem('authToken');
    if (!token) {
      setErrorMessage('Authentication token is missing. Please sign in again.');
      return;
    }

    const formData = new FormData();
    formData.append('uploadedStatement', selectedFile);
    formData.append(
      'processStatementRequest',
      new Blob(
        [JSON.stringify({ statementType, reProcess: reProcess === 'true' })],
        { type: 'application/json' }
      )
    );

    setLoading(true);
    try {
      const response = await axios.post('/statement/process', formData, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setSuccessResponse(response.data);
    } catch (err) {
      setErrorMessage(err.response?.data?.message || 'Failed to process statement. Please try again.');
      console.error('Process statement error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-full bg-slate-50 p-4 md:p-10">
      <div className="max-w-3xl mx-auto bg-white rounded-xl shadow-md border border-slate-200 p-8">
        <div className="flex items-center gap-3 mb-4">
          <Upload size={28} className="text-blue-600" />
          <div>
            <h1 className="text-3xl font-bold text-slate-800">Upload Statement</h1>
            <p className="text-slate-500">Upload your bank or credit card statement to process transactions.</p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {errorMessage && (
            <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700 flex items-start gap-2">
              <AlertCircle size={18} className="mt-0.5" />
              <span>{errorMessage}</span>
            </div>
          )}

          {successResponse && (
            <div className="rounded-xl border border-green-200 bg-green-50 p-4 text-sm text-slate-800">
              <div className="flex items-center gap-2 font-semibold text-green-700 mb-3">
                <CheckCircle2 size={18} />
                Statement processed successfully
              </div>
              <div className="grid gap-2 sm:grid-cols-2">
                <div className="rounded-lg bg-white p-3 border border-slate-200">
                  <p className="text-xs uppercase tracking-wide text-slate-500">Statement File ID</p>
                  <p className="text-slate-900 font-semibold">{successResponse.statementFileId}</p>
                </div>
                <div className="rounded-lg bg-white p-3 border border-slate-200">
                  <p className="text-xs uppercase tracking-wide text-slate-500">Rows Count</p>
                  <p className="text-slate-900 font-semibold">{successResponse.statementRowsCount}</p>
                </div>
                <div className="rounded-lg bg-white p-3 border border-slate-200">
                  <p className="text-xs uppercase tracking-wide text-slate-500">Parsed Transactions</p>
                  <p className="text-slate-900 font-semibold">{successResponse.parsedTransactionsCount}</p>
                </div>
                <div className="rounded-lg bg-white p-3 border border-slate-200">
                  <p className="text-xs uppercase tracking-wide text-slate-500">Categorized Transactions</p>
                  <p className="text-slate-900 font-semibold">{successResponse.categorizedTransactionsCount}</p>
                </div>
              </div>
            </div>
          )}

          <div>
            <label htmlFor="statementFile" className="block text-sm font-semibold text-slate-700 mb-2">
              Statement File
            </label>
            <input
              id="statementFile"
              type="file"
              accept=".pdf,.csv,.xls,.xlsx"
              onChange={handleFileChange}
              className="w-full text-slate-700 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-600 file:text-white file:hover:bg-blue-700"
            />
          </div>

          <div>
            <label htmlFor="statementType" className="block text-sm font-semibold text-slate-700 mb-2">
              Statement Type
            </label>
            <select
              id="statementType"
              value={statementType}
              onChange={(e) => setStatementType(e.target.value)}
              className="w-full rounded-lg border border-slate-300 px-4 py-2 bg-white text-slate-900 focus:ring-2 focus:ring-blue-500 outline-none"
            >
              <option value="Credit Card">Credit Card</option>
              <option value="Bank Account">Bank Account</option>
            </select>
          </div>

          <div>
            <p className="block text-sm font-semibold text-slate-700 mb-2">Re-process</p>
            <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
              <label className="inline-flex items-center gap-2 text-slate-700">
                <input
                  type="radio"
                  name="re-process"
                  value="true"
                  checked={reProcess === 'true'}
                  onChange={(e) => setReProcess(e.target.value)}
                  className="accent-blue-600"
                />
                True
              </label>
              <label className="inline-flex items-center gap-2 text-slate-700">
                <input
                  type="radio"
                  name="re-process"
                  value="false"
                  checked={reProcess === 'false'}
                  onChange={(e) => setReProcess(e.target.value)}
                  className="accent-blue-600"
                />
                False
              </label>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full inline-flex items-center justify-center gap-2 rounded-lg bg-blue-600 px-4 py-3 text-white font-semibold hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-blue-400"
          >
            {loading && <Loader size={18} className="animate-spin" />}
            {loading ? 'Processing...' : 'Process Statement'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default UploadStatement;
