import React from 'react';
import { MapPin, Tag, Clock, ChevronRight } from 'lucide-react';

export default function ItemCard({ item, onClick }) {
  const isLost = item.type === 'LOST';
  const isResolved = item.status === 'CLAIMED' || item.status === 'RETURNED';

  const formattedDate = new Date(item.dateReported).toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });

  return (
    <div className="item-card" onClick={onClick} style={{ cursor: 'pointer' }}>
      <div className="item-img-container">
        <img
          src={item.image || 'https://images.unsplash.com/photo-1584438784894-089d6a62b8fa?auto=format&fit=crop&w=600&q=80'}
          alt={item.title}
          className="item-img"
          onError={(e) => {
            e.target.onerror = null;
            e.target.src = 'https://images.unsplash.com/photo-1584438784894-089d6a62b8fa?auto=format&fit=crop&w=600&q=80';
          }}
        />
        <div className={`badge ${isLost ? 'badge-lost' : 'badge-found'}`}>
          {item.type}
        </div>
        <div className={`status-badge ${isResolved ? 'status-claimed' : 'status-open'}`}>
          {item.status === 'CLAIMED' ? '✓ Claimed' : item.status === 'RETURNED' ? '✓ Returned' : '● Open'}
        </div>
      </div>

      <div className="item-body">
        <h3 className="item-title">{item.title}</h3>
        <p className="item-desc">{item.description}</p>

        <div className="item-meta">
          <div className="meta-item">
            <MapPin size={14} color="#006689" />
            <span>{item.location}</span>
          </div>
          <div className="meta-item">
            <Tag size={14} color="#4e616d" />
            <span>{item.category}</span>
          </div>
        </div>

        <div className="item-footer">
          <div className="meta-item" style={{ fontSize: '0.78rem' }}>
            <Clock size={13} />
            <span>{formattedDate}</span>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '0.2rem', color: '#006689', fontWeight: 700, fontSize: '0.85rem' }}>
            <span>View Details</span>
            <ChevronRight size={16} />
          </div>
        </div>
      </div>
    </div>
  );
}
