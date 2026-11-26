import React from 'react';
import { MapPin, ArrowRightLeft, Check, X, Sparkles } from 'lucide-react';

export default function MatchPairCard({ match, onConfirm, onReject, onViewItem }) {
  const { lostItem, foundItem, confidenceScore, reasons, status } = match;
  const scorePct = Math.round(confidenceScore * 100);
  const isHighMatch = scorePct >= 80;

  return (
    <div style={{ background: '#ffffff', border: '1px solid #e2e8f0', borderRadius: '20px', padding: '1.25rem', marginBottom: '1.25rem', boxShadow: '0 2px 8px rgba(0,0,0,0.04)' }}>
      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <span style={{
            background: isHighMatch ? 'linear-gradient(135deg, #e65100, #ff9800)' : '#006689',
            color: 'white',
            padding: '0.4rem 0.9rem',
            borderRadius: '20px',
            fontWeight: 800,
            fontSize: '0.85rem'
          }}>
            {scorePct}% MATCH
          </span>

          <span style={{
            background: '#f3daff',
            color: '#6b5778',
            padding: '0.35rem 0.75rem',
            borderRadius: '8px',
            fontSize: '0.78rem',
            fontWeight: 700,
            display: 'flex',
            alignItems: 'center',
            gap: '0.3rem'
          }}>
            <Sparkles size={14} />
            AI Algorithm
          </span>
        </div>

        <span style={{ fontWeight: 800, fontSize: '0.85rem', color: status === 'CONFIRMED' ? '#2e7d32' : status === 'REJECTED' ? '#d32f2f' : '#64748b' }}>
          {status}
        </span>
      </div>

      {/* Reasons Callout */}
      <div style={{ background: '#f8fafc', padding: '0.75rem 1rem', borderRadius: '12px', marginBottom: '1rem', border: '1px solid #f1f5f9', fontSize: '0.85rem', color: '#334155', fontWeight: 600 }}>
        💡 {reasons}
      </div>

      {/* Side-by-Side Items */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr auto 1fr', gap: '0.75rem', alignItems: 'center', marginBottom: '1.25rem' }}>
        {/* Lost Item */}
        <div
          onClick={() => onViewItem(lostItem)}
          style={{ background: '#ffebee', padding: '1rem', borderRadius: '14px', border: '1px solid #ffcdd2', cursor: 'pointer' }}
        >
          <span className="badge badge-lost" style={{ position: 'static', marginBottom: '0.4rem', display: 'inline-block' }}>
            LOST ITEM
          </span>
          <h4 style={{ fontSize: '0.98rem', fontWeight: 800, color: '#991b1b', marginBottom: '0.3rem' }}>{lostItem.title}</h4>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.3rem', fontSize: '0.78rem', color: '#7f1d1d' }}>
            <MapPin size={13} />
            <span>{lostItem.location}</span>
          </div>
        </div>

        {/* Center Arrow */}
        <div style={{ width: '36px', height: '36px', background: '#c3e8ff', color: '#001e2c', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <ArrowRightLeft size={18} />
        </div>

        {/* Found Item */}
        <div
          onClick={() => onViewItem(foundItem)}
          style={{ background: '#e8f5e9', padding: '1rem', borderRadius: '14px', border: '1px solid #c8e6c9', cursor: 'pointer' }}
        >
          <span className="badge badge-found" style={{ position: 'static', marginBottom: '0.4rem', display: 'inline-block' }}>
            FOUND ITEM
          </span>
          <h4 style={{ fontSize: '0.98rem', fontWeight: 800, color: '#166534', marginBottom: '0.3rem' }}>{foundItem.title}</h4>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.3rem', fontSize: '0.78rem', color: '#14532d' }}>
            <MapPin size={13} />
            <span>{foundItem.location}</span>
          </div>
        </div>
      </div>

      {/* Actions */}
      {status === 'PENDING' && (
        <div style={{ display: 'flex', gap: '0.75rem' }}>
          <button
            className="btn-outline"
            style={{ flex: 1, borderColor: '#fca5a5', color: '#dc2626' }}
            onClick={() => onReject(match.id)}
          >
            <X size={16} />
            <span>Reject Candidate</span>
          </button>

          <button
            className="btn-primary"
            style={{ flex: 1, background: '#2e7d32' }}
            onClick={() => onConfirm(match.id, lostItem.id, foundItem.id)}
          >
            <Check size={16} />
            <span>Confirm Match Pair</span>
          </button>
        </div>
      )}
    </div>
  );
}
