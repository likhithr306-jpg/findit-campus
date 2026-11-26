import React, { useState } from 'react';
import MatchPairCard from '../components/MatchPairCard';
import { computeSmartMatches } from '../utils/matchingEngine';
import { Sparkles, Sliders, RefreshCw, CheckCircle2 } from 'lucide-react';

export default function MatchingView({ items, onViewItem, onUpdateStatus }) {
  const [minConfidence, setMinConfidence] = useState(0.35);
  const [recalcTrigger, setRecalcTrigger] = useState(0);

  const rawMatches = computeSmartMatches(items);
  const filteredMatches = rawMatches.filter(m => m.confidenceScore >= minConfidence);

  const handleConfirmMatch = (matchId, lostId, foundId) => {
    onUpdateStatus(lostId, 'CLAIMED');
    onUpdateStatus(foundId, 'RETURNED');
  };

  const handleRejectMatch = (matchId) => {
    setRecalcTrigger(prev => prev + 1);
  };

  return (
    <div>
      <div style={{ marginBottom: '1.5rem', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h2 style={{ fontSize: '1.6rem', fontWeight: 800, display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Sparkles color="#006689" />
            Smart Match Analytics
          </h2>
          <p style={{ color: '#64748b' }}>Automated pair matching comparing category, location proximity, title keywords, and report time.</p>
        </div>

        <button className="btn-outline" onClick={() => setRecalcTrigger(prev => prev + 1)}>
          <RefreshCw size={16} />
          <span>Re-run Matching Engine</span>
        </button>
      </div>

      {/* Threshold Slider Card */}
      <div style={{ background: '#ffffff', padding: '1.25rem', borderRadius: '20px', border: '1px solid #e2e8f0', marginBottom: '1.5rem', boxShadow: '0 2px 8px rgba(0,0,0,0.03)' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontWeight: 700, fontSize: '0.95rem' }}>
            <Sliders size={18} color="#006689" />
            <span>Minimum Match Confidence Threshold</span>
          </div>
          <span style={{ fontSize: '1.1rem', fontWeight: 800, color: '#006689' }}>
            {Math.round(minConfidence * 100)}%
          </span>
        </div>

        <input
          type="range"
          min="0.25"
          max="0.90"
          step="0.05"
          value={minConfidence}
          onChange={(e) => setMinConfidence(parseFloat(e.target.value))}
          style={{ width: '100%', accentColor: '#006689', cursor: 'pointer' }}
        />
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.75rem', color: '#64748b', marginTop: '0.25rem' }}>
          <span>25% (Broad Search)</span>
          <span>50% (Balanced)</span>
          <span>90% (Strict Exact Match)</span>
        </div>
      </div>

      {/* Match List */}
      {filteredMatches.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '4rem', background: '#ffffff', borderRadius: '24px', border: '1px solid #e2e8f0' }}>
          <CheckCircle2 size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700 }}>No match pairs found above {Math.round(minConfidence * 100)}% threshold</h3>
          <p style={{ color: '#64748b', marginTop: '0.5rem' }}>Lower the confidence threshold slider or add new lost/found reports.</p>
        </div>
      ) : (
        <div>
          {filteredMatches.map(match => (
            <MatchPairCard
              key={match.id}
              match={match}
              onConfirm={handleConfirmMatch}
              onReject={handleRejectMatch}
              onViewItem={onViewItem}
            />
          ))}
        </div>
      )}
    </div>
  );
}
