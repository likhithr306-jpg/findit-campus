import React, { useState } from 'react';
import ItemCard from '../components/ItemCard';
import { UserCheck, PlusCircle } from 'lucide-react';

export default function MyReportsView({ items, onItemClick, onReportClick }) {
  const [filter, setFilter] = useState('ALL');

  const myItems = items.filter(item => {
    if (filter === 'OPEN') return item.status === 'OPEN';
    if (filter === 'RESOLVED') return item.status === 'CLAIMED' || item.status === 'RETURNED';
    return true;
  });

  return (
    <div>
      <div style={{ marginBottom: '1.5rem', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h2 style={{ fontSize: '1.6rem', fontWeight: 800 }}>My Reported Items</h2>
          <p style={{ color: '#64748b' }}>Manage your active lost and found listings.</p>
        </div>

        <button className="btn-primary" onClick={onReportClick}>
          <PlusCircle size={18} />
          <span>Post New Report</span>
        </button>
      </div>

      {/* Filter Tabs */}
      <div className="chip-group" style={{ marginBottom: '1.5rem' }}>
        <button
          className={`chip ${filter === 'ALL' ? 'active' : ''}`}
          onClick={() => setFilter('ALL')}
        >
          All Reports ({items.length})
        </button>
        <button
          className={`chip ${filter === 'OPEN' ? 'active' : ''}`}
          onClick={() => setFilter('OPEN')}
        >
          Open ({items.filter(i => i.status === 'OPEN').length})
        </button>
        <button
          className={`chip ${filter === 'RESOLVED' ? 'active' : ''}`}
          onClick={() => setFilter('RESOLVED')}
        >
          Resolved ({items.filter(i => i.status !== 'OPEN').length})
        </button>
      </div>

      {/* Grid */}
      {myItems.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '4rem', background: '#ffffff', borderRadius: '24px', border: '1px solid #e2e8f0' }}>
          <UserCheck size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700 }}>No reports found under this view</h3>
          <p style={{ color: '#64748b', marginTop: '0.5rem', marginBottom: '1.5rem' }}>Create a lost or found report to manage it here.</p>
          <button className="btn-primary" style={{ margin: '0 auto' }} onClick={onReportClick}>
            <PlusCircle size={18} />
            <span>Submit Report</span>
          </button>
        </div>
      ) : (
        <div className="items-grid">
          {myItems.map(item => (
            <ItemCard key={item.id} item={item} onClick={() => onItemClick(item)} />
          ))}
        </div>
      )}
    </div>
  );
}
