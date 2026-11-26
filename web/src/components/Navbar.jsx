import React from 'react';
import { Search, PlusCircle, Sparkles, UserCheck, LayoutDashboard, RotateCcw } from 'lucide-react';

export default function Navbar({ activeTab, setActiveTab, onReportClick, onResetClick }) {
  return (
    <header className="navbar">
      <div className="navbar-content">
        <div className="brand" onClick={() => setActiveTab('dashboard')} style={{ cursor: 'pointer' }}>
          <div className="brand-icon">
            <Search size={22} />
          </div>
          <div>
            <div>FindIt Campus</div>
            <div style={{ fontSize: '0.72rem', color: '#64748b', fontWeight: 600, marginTop: '-3px' }}>
              Campus Lost & Found Platform
            </div>
          </div>
        </div>

        <nav className="nav-links">
          <button
            className={`nav-btn ${activeTab === 'dashboard' ? 'active' : ''}`}
            onClick={() => setActiveTab('dashboard')}
          >
            <LayoutDashboard size={18} />
            <span>Dashboard</span>
          </button>

          <button
            className={`nav-btn ${activeTab === 'browse' ? 'active' : ''}`}
            onClick={() => setActiveTab('browse')}
          >
            <Search size={18} />
            <span>Browse Items</span>
          </button>

          <button
            className={`nav-btn ${activeTab === 'matching' ? 'active' : ''}`}
            onClick={() => setActiveTab('matching')}
          >
            <Sparkles size={18} />
            <span>Smart Match</span>
          </button>

          <button
            className={`nav-btn ${activeTab === 'my-reports' ? 'active' : ''}`}
            onClick={() => setActiveTab('my-reports')}
          >
            <UserCheck size={18} />
            <span>My Reports</span>
          </button>
        </nav>

        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <button className="btn-outline" onClick={onResetClick} title="Reset sample data">
            <RotateCcw size={16} />
            <span style={{ fontSize: '0.82rem' }}>Reset Data</span>
          </button>

          <button className="btn-primary" onClick={onReportClick}>
            <PlusCircle size={18} />
            <span>Report Item</span>
          </button>
        </div>
      </div>
    </header>
  );
}
