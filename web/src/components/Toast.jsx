import React from 'react';
import { Sparkles } from 'lucide-react';

export default function Toast({ message, onClose }) {
  if (!message) return null;

  return (
    <div className="toast">
      <Sparkles size={18} color="#78d1ff" />
      <span>{message}</span>
    </div>
  );
}
