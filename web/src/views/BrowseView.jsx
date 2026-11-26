import React, { useState } from 'react';
import ItemCard from '../components/ItemCard';
import { Search, Filter, RefreshCw } from 'lucide-react';
import { CATEGORIES, LOCATIONS } from '../data/sampleData';

export default function BrowseView({ items, onItemClick }) {
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('ALL');
  const [type, setType] = useState('ALL');
  const [location, setLocation] = useState('ALL');
  const [status, setStatus] = useState('ALL');

  const filteredItems = items.filter(item => {
    const matchesSearch = !search ||
      item.title.toLowerCase().includes(search.toLowerCase()) ||
      item.description.toLowerCase().includes(search.toLowerCase()) ||
      item.location.toLowerCase().includes(search.toLowerCase());

    const matchesCategory = category === 'ALL' || item.category === category;
    const matchesType = type === 'ALL' || item.type === type;
    const matchesLocation = location === 'ALL' || item.location === location;
    const matchesStatus = status === 'ALL' || item.status === status;

    return matchesSearch && matchesCategory && matchesType && matchesLocation && matchesStatus;
  });

  const resetFilters = () => {
    setSearch('');
    setCategory('ALL');
    setType('ALL');
    setLocation('ALL');
    setStatus('ALL');
  };

  return (
    <div>
      <div style={{ marginBottom: '1.5rem' }}>
        <h2 style={{ fontSize: '1.6rem', fontWeight: 800, marginBottom: '0.25rem' }}>Browse Campus Catalog</h2>
        <p style={{ color: '#64748b' }}>Filter lost and found reports by building, category, and status.</p>
      </div>

      {/* Advanced Filter Bar */}
      <div style={{ background: '#ffffff', padding: '1.25rem', borderRadius: '20px', border: '1px solid #e2e8f0', marginBottom: '1.5rem', boxShadow: '0 2px 8px rgba(0,0,0,0.03)' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', marginBottom: '1rem' }}>
          <div>
            <label className="form-label">Search Keywords</label>
            <div className="search-box">
              <Search className="search-icon" size={16} />
              <input
                type="text"
                className="search-input"
                style={{ padding: '0.55rem 1rem 0.55rem 2.4rem', fontSize: '0.9rem' }}
                placeholder="Search items..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
          </div>

          <div>
            <label className="form-label">Report Type</label>
            <select className="form-select" value={type} onChange={(e) => setType(e.target.value)}>
              <option value="ALL">All Types (Lost & Found)</option>
              <option value="LOST">Lost Items Only</option>
              <option value="FOUND">Found Items Only</option>
            </select>
          </div>

          <div>
            <label className="form-label">Category</label>
            <select className="form-select" value={category} onChange={(e) => setCategory(e.target.value)}>
              <option value="ALL">All Categories</option>
              {CATEGORIES.map(cat => <option key={cat} value={cat}>{cat}</option>)}
            </select>
          </div>

          <div>
            <label className="form-label">Location</label>
            <select className="form-select" value={location} onChange={(e) => setLocation(e.target.value)}>
              <option value="ALL">All Campus Locations</option>
              {LOCATIONS.map(loc => <option key={loc} value={loc}>{loc}</option>)}
            </select>
          </div>

          <div>
            <label className="form-label">Listing Status</label>
            <select className="form-select" value={status} onChange={(e) => setStatus(e.target.value)}>
              <option value="ALL">All Statuses</option>
              <option value="OPEN">Open Only</option>
              <option value="CLAIMED">Claimed / Returned</option>
            </select>
          </div>
        </div>

        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <button className="btn-outline" style={{ fontSize: '0.85rem' }} onClick={resetFilters}>
            <RefreshCw size={14} />
            <span>Reset All Filters</span>
          </button>
        </div>
      </div>

      {/* Grid */}
      {filteredItems.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '4rem', background: '#ffffff', borderRadius: '24px', border: '1px solid #e2e8f0' }}>
          <Filter size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700 }}>No items match selected filters</h3>
          <p style={{ color: '#64748b', marginTop: '0.5rem' }}>Try clearing or adjusting filter criteria.</p>
        </div>
      ) : (
        <div className="items-grid">
          {filteredItems.map(item => (
            <ItemCard key={item.id} item={item} onClick={() => onItemClick(item)} />
          ))}
        </div>
      )}
    </div>
  );
}
