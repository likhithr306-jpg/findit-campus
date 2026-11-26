import React from 'react';

export default function StatCard({ title, count, icon: Icon, bg, color }) {
  return (
    <div className="stat-card">
      <div className="stat-icon" style={{ background: bg, color: color }}>
        <Icon size={24} />
      </div>
      <div>
        <div className="stat-val" style={{ color: color }}>{count}</div>
        <div className="stat-lbl">{title}</div>
      </div>
    </div>
  );
}
