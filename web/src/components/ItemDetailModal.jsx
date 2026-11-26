import React, { useState } from 'react';
import { X, MapPin, Tag, Clock, User, Mail, CheckCircle2, RotateCcw, Trash2 } from 'lucide-react';

export default function ItemDetailModal({ item, onClose, onUpdateStatus, onDeleteItem }) {
  const [showContact, setShowContact] = useState(false);

  if (!item) return null;

  const isLost = item.type === 'LOST';
  const isResolved = item.status === 'CLAIMED' || item.status === 'RETURNED';

  const formattedDate = new Date(item.dateReported).toLocaleString('en-US', {
    weekday: 'short',
    month: 'short',
    day: 'numeric',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-card" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
            <span className={`badge ${isLost ? 'badge-lost' : 'badge-found'}`} style={{ position: 'static' }}>
              {item.type} REPORT
            </span>
            <span className={`status-badge ${isResolved ? 'status-claimed' : 'status-open'}`} style={{ position: 'static' }}>
              {item.status}
            </span>
          </div>

          <button
            onClick={onClose}
            style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#64748b' }}
          >
            <X size={22} />
          </button>
        </div>

        <div className="modal-body">
          <div style={{ height: '220px', borderRadius: '16px', overflow: 'hidden', marginBottom: '1.25rem' }}>
            <img
              src={item.image || 'https://images.unsplash.com/photo-1584438784894-089d6a62b8fa?auto=format&fit=crop&w=600&q=80'}
              alt={item.title}
              style={{ width: '100%', height: '100%', objectFit: 'cover' }}
              onError={(e) => {
                e.target.onerror = null;
                e.target.src = 'https://images.unsplash.com/photo-1584438784894-089d6a62b8fa?auto=format&fit=crop&w=600&q=80';
              }}
            />
          </div>

          <h2 style={{ fontSize: '1.4rem', fontWeight: 800, marginBottom: '0.5rem' }}>{item.title}</h2>
          <p style={{ color: '#475569', marginBottom: '1.25rem', lineHeight: '1.6' }}>{item.description}</p>

          <div style={{ background: '#f8fafc', padding: '1rem', borderRadius: '14px', marginBottom: '1.25rem', display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem', border: '1px solid #e2e8f0' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
              <MapPin size={16} color="#006689" />
              <div>
                <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>LOCATION</div>
                <div style={{ fontWeight: 700 }}>{item.location}</div>
              </div>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
              <Tag size={16} color="#4e616d" />
              <div>
                <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>CATEGORY</div>
                <div style={{ fontWeight: 700 }}>{item.category}</div>
              </div>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
              <User size={16} color="#6b5778" />
              <div>
                <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>REPORTED BY</div>
                <div style={{ fontWeight: 700 }}>{item.reporterName || 'Campus Member'}</div>
              </div>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem' }}>
              <Clock size={16} color="#0288d1" />
              <div>
                <div style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>DATE & TIME</div>
                <div style={{ fontWeight: 700 }}>{formattedDate}</div>
              </div>
            </div>
          </div>

          {/* Contact Box */}
          {showContact && (
            <div style={{ background: '#e0f2fe', padding: '1rem', borderRadius: '14px', marginBottom: '1.25rem', border: '1px solid #bae6fd' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontWeight: 700, color: '#0369a1', marginBottom: '0.25rem' }}>
                <Mail size={18} />
                <span>Contact Details:</span>
              </div>
              <div style={{ fontSize: '1.05rem', fontWeight: 800, color: '#0c4a6e' }}>{item.contactInfo}</div>
            </div>
          )}

          {/* Action Buttons */}
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.75rem', marginTop: '1rem' }}>
            <button className="btn-primary" style={{ flex: 1 }} onClick={() => setShowContact(!showContact)}>
              <Mail size={18} />
              <span>{showContact ? 'Hide Contact Info' : 'Contact Reporter'}</span>
            </button>

            {!isResolved ? (
              <button
                className="btn-outline"
                style={{ background: '#e8f5e9', borderColor: '#81c784', color: '#2e7d32', fontWeight: 700 }}
                onClick={() => onUpdateStatus(item.id, isLost ? 'CLAIMED' : 'RETURNED')}
              >
                <CheckCircle2 size={18} />
                <span>Mark as {isLost ? 'Claimed' : 'Returned'}</span>
              </button>
            ) : (
              <button
                className="btn-outline"
                onClick={() => onUpdateStatus(item.id, 'OPEN')}
              >
                <RotateCcw size={18} />
                <span>Re-open Listing</span>
              </button>
            )}

            {onDeleteItem && (
              <button
                className="btn-outline"
                style={{ borderColor: '#fca5a5', color: '#dc2626' }}
                onClick={() => onDeleteItem(item.id)}
              >
                <Trash2 size={18} />
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
