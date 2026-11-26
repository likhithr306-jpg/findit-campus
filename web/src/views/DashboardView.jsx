import React, { useState } from 'react';
import StatCard from '../components/StatCard';
import ItemCard from '../components/ItemCard';
import { HelpCircle, Search, CheckCircle, Sparkles, PlusCircle } from 'lucide-react';
import { CATEGORIES } from '../data/sampleData';

export default function DashboardView({ items, matchesCount, onItemClick, onReportClick, onNavigateTab }) {
  const [search, setSearch] = useState('');
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [typeFilter, setTypeFilter] = useState(null);

  const totalLost = items.filter(i => i.type === 'LOST' && i.status === 'OPEN').length;
  const totalFound = items.filter(i => i.type === 'FOUND' && i.status === 'OPEN').length;
  const totalResolved = items.filter(i => i.status === 'CLAIMED' || i.status === 'RETURNED').length;

  const filteredItems = items.filter(item => {
    const matchesSearch = !search ||
      item.title.toLowerCase().includes(search.toLowerCase()) ||
      item.location.toLowerCase().includes(search.toLowerCase()) ||
      item.description.toLowerCase().includes(search.toLowerCase());

    const matchesCategory = !selectedCategory || item.category === selectedCategory;
    const matchesType = !typeFilter || item.type === typeFilter;

    return matchesSearch && matchesCategory && matchesType;
  });

  return (
    <div>
      {/* Hero Banner */}
      <div className="hero-card">
        <h1>Smart Lost & Found Hub</h1>
        <p>Effortlessly report lost belongings, browse found items, and match reported candidates across campus using automated location & similarity tracking.</p>

        <div style={{ display: 'flex', gap: '1rem', marginTop: '1.5rem' }}>
          <button className="btn-primary" style={{ background: 'white', color: '#006689' }} onClick={onReportClick}>
            <PlusCircle size={18} />
            <span>Report Lost / Found Item</span>
          </button>

          <button className="btn-outline" style={{ borderColor: 'rgba(255,255,255,0.4)', color: 'white' }} onClick={() => onNavigateTab('matching')}>
            <Sparkles size={18} />
            <span>View Smart Matches ({matchesCount})</span>
          </button>
        </div>
      </div>

      {/* Stats Row */}
      <div className="stats-grid">
        <StatCard title="Lost Reports" count={totalLost} icon={HelpCircle} bg="#ffebee" color="#d32f2f" />
        <StatCard title="Found Listings" count={totalFound} icon={Search} bg="#e8f5e9" color="#2e7d32" />
        <StatCard title="Returned / Claimed" count={totalResolved} icon={CheckCircle} bg="#d1e5f4" color="#006689" />
        <StatCard title="Smart AI Matches" count={matchesCount} icon={Sparkles} bg="#fff3e0" color="#e65100" />
      </div>

      {/* Filter & Search Bar */}
      <div className="filter-bar">
        <div className="search-box">
          <Search className="search-icon" size={18} />
          <input
            type="text"
            className="search-input"
            placeholder="Search listings by title, location, description..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className="chip-group">
          <button
            className={`chip ${!typeFilter ? 'active' : ''}`}
            onClick={() => setTypeFilter(null)}
          >
            All Types
          </button>
          <button
            className={`chip ${typeFilter === 'LOST' ? 'active' : ''}`}
            onClick={() => setTypeFilter(typeFilter === 'LOST' ? null : 'LOST')}
          >
            Lost Only
          </button>
          <button
            className={`chip ${typeFilter === 'FOUND' ? 'active' : ''}`}
            onClick={() => setTypeFilter(typeFilter === 'FOUND' ? null : 'FOUND')}
          >
            Found Only
          </button>
        </div>
      </div>

      {/* Category Pills */}
      <div className="chip-group" style={{ marginBottom: '1.5rem' }}>
        <button
          className={`chip ${!selectedCategory ? 'active' : ''}`}
          onClick={() => setSelectedCategory(null)}
        >
          All Categories
        </button>
        {CATEGORIES.map(cat => (
          <button
            key={cat}
            className={`chip ${selectedCategory === cat ? 'active' : ''}`}
            onClick={() => setSelectedCategory(selectedCategory === cat ? null : cat)}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* Listings Grid */}
      {filteredItems.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '4rem 2rem', background: '#ffffff', borderRadius: '24px', border: '1px solid #e2e8f0' }}>
          <Search size={48} color="#94a3b8" style={{ marginBottom: '1rem' }} />
          <h3 style={{ fontSize: '1.2rem', fontWeight: 700, marginBottom: '0.5rem' }}>No matching items found</h3>
          <p style={{ color: '#64748b', marginBottom: '1.5rem' }}>Try broadening your search or submit a new report.</p>
          <button className="btn-primary" style={{ margin: '0 auto' }} onClick={onReportClick}>
            <PlusCircle size={18} />
            <span>Submit New Report</span>
          </button>
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
