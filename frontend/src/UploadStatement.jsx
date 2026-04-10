import React from 'react';
import { Upload } from 'lucide-react';

const UploadStatement = () => {
  return (
    <div className="min-h-full bg-slate-50 p-4 md:p-10">
      <div className="max-w-6xl mx-auto bg-white rounded-xl shadow-md border border-slate-200 p-8">
        <div className="flex items-center gap-3 mb-2">
          <Upload size={28} className="text-blue-600" />
          <h1 className="text-3xl font-bold text-slate-800">Upload Statement</h1>
        </div>
        <p className="text-slate-500 mt-4">
          Upload your bank or credit card statements here. This feature will help you process and categorize your transactions.
        </p>
      </div>
    </div>
  );
};

export default UploadStatement;
