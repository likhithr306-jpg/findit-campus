import React, { useState } from 'react';
import { CATEGORIES, LOCATIONS } from '../data/sampleData';
import { Send, Upload, AlertCircle } from 'lucide-react';

export default function ReportView({ onSubmitReport }) {
  const [type, setType] = useState('LOST');
  const [title, setTitle] = useState('');
  const [category, setCategory] = useState(CATEGORIES[0]);
  const [location, setLocation] = useState(LOCATIONS[0]);
  const [description, setDescription] = useState('');
  const [contactInfo, setContactInfo] = useState('');
  const [reporterName, setReporterName] = useState('');
  const [image, setImage] = useState('');
  const [errors, setErrors] = useState({});

  const sampleImages = [
    { label: 'Student ID', url: 'https://images.unsplash.com/photo-1578574577315-3fbeb0cecdc2?auto=format&fit=crop&w=600&q=80' },
    { label: 'AirPods', url: 'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?auto=format&fit=crop&w=600&q=80' },
    { label: 'Keys', url: 'https://images.unsplash.com/photo-1582142839970-2b932715694c?auto=format&fit=crop&w=600&q=80' },
    { label: 'Water Bottle', url: 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=600&q=80' },
    { label: 'Wallet', url: 'https://images.unsplash.com/photo-1627123424574-724758594e93?auto=format&fit=crop&w=600&q=80' }
  ];

  const handleSubmit = (e) => {
    e.preventDefault();
    const errs = {};
    if (!title.trim()) errs.title = 'Item title is required';
    if (!contactInfo.trim()) errs.contactInfo = 'Contact details are required';

    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }

    const newItem = {
      id: Date.now(),
      title: title.trim(),
      category,
      type,
      location,
      description: description.trim() || 'No additional description provided.',
      contactInfo: contactInfo.trim(),
      reporterName: reporterName.trim() || 'Campus Member',
      dateReported: Date.now(),
      status: 'OPEN',
      image: image || sampleImages[0].url
    };

    onSubmitReport(newItem);
  };

  return (
    <div style={{ maxWidth: '720px', margin: '0 auto' }}>
      <div style={{ marginBottom: '1.5rem', textAlign: 'center' }}>
        <h2 style={{ fontSize: '1.8rem', fontWeight: 800 }}>Report Campus Listing</h2>
        <p style={{ color: '#64748b' }}>Provide item details to help return lost items to their rightful owners.</p>
      </div>

      {/* Type Toggle */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem', marginBottom: '1.5rem' }}>
        <button
          type="button"
          style={{
            padding: '1rem',
            borderRadius: '16px',
            border: '2px solid',
            borderColor: type === 'LOST' ? '#d32f2f' : '#e2e8f0',
            background: type === 'LOST' ? '#ffebee' : '#ffffff',
            color: type === 'LOST' ? '#d32f2f' : '#64748b',
            fontWeight: 800,
            fontSize: '1rem',
            cursor: 'pointer',
            transition: 'all 0.2s ease'
          }}
          onClick={() => setType('LOST')}
        >
          🔍 I LOST AN ITEM
        </button>

        <button
          type="button"
          style={{
            padding: '1rem',
            borderRadius: '16px',
            border: '2px solid',
            borderColor: type === 'FOUND' ? '#2e7d32' : '#e2e8f0',
            background: type === 'FOUND' ? '#e8f5e9' : '#ffffff',
            color: type === 'FOUND' ? '#2e7d32' : '#64748b',
            fontWeight: 800,
            fontSize: '1rem',
            cursor: 'pointer',
            transition: 'all 0.2s ease'
          }}
          onClick={() => setType('FOUND')}
        >
          🎁 I FOUND AN ITEM
        </button>
      </div>

      {/* Form Card */}
      <form onSubmit={handleSubmit} style={{ background: '#ffffff', padding: '2rem', borderRadius: '24px', border: '1px solid #e2e8f0', boxShadow: '0 4px 12px rgba(0,0,0,0.05)' }}>
        <div className="form-group">
          <label className="form-label">Item Title *</label>
          <input
            type="text"
            className="form-input"
            placeholder="e.g. Student ID Card - Alex, Blue AirPods Pro, Car Keys"
            value={title}
            onChange={(e) => { setTitle(e.target.value); setErrors({ ...errors, title: null }); }}
          />
          {errors.title && <div style={{ color: '#dc2626', fontSize: '0.8rem', marginTop: '0.3rem', display: 'flex', alignItems: 'center', gap: '0.3rem' }}><AlertCircle size={14} /> {errors.title}</div>}
        </div>

        <div className="form-grid">
          <div className="form-group">
            <label className="form-label">Category</label>
            <select className="form-select" value={category} onChange={(e) => setCategory(e.target.value)}>
              {CATEGORIES.map(cat => <option key={cat} value={cat}>{cat}</option>)}
            </select>
          </div>

          <div className="form-group">
            <label className="form-label">Campus Location</label>
            <select className="form-select" value={location} onChange={(e) => setLocation(e.target.value)}>
              {LOCATIONS.map(loc => <option key={loc} value={loc}>{loc}</option>)}
            </select>
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Detailed Description</label>
          <textarea
            className="form-textarea"
            rows="4"
            placeholder="Describe color, brand, stickers, unique scratches, exact room or desk area..."
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          ></textarea>
        </div>

        <div className="form-grid">
          <div className="form-group">
            <label className="form-label">Contact Email or Phone *</label>
            <input
              type="text"
              className="form-input"
              placeholder="student@campus.edu or (555) 019-2834"
              value={contactInfo}
              onChange={(e) => { setContactInfo(e.target.value); setErrors({ ...errors, contactInfo: null }); }}
            />
            {errors.contactInfo && <div style={{ color: '#dc2626', fontSize: '0.8rem', marginTop: '0.3rem', display: 'flex', alignItems: 'center', gap: '0.3rem' }}><AlertCircle size={14} /> {errors.contactInfo}</div>}
          </div>

          <div className="form-group">
            <label className="form-label">Your Name / Department</label>
            <input
              type="text"
              className="form-input"
              placeholder="e.g. Alex Rivera or Library Front Desk"
              value={reporterName}
              onChange={(e) => setReporterName(e.target.value)}
            />
          </div>
        </div>

        {/* Photo Selector */}
        <div className="form-group">
          <label className="form-label">Select Representative Photo</label>
          <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', marginBottom: '0.75rem' }}>
            {sampleImages.map(sample => (
              <button
                key={sample.label}
                type="button"
                className={`chip ${image === sample.url ? 'active' : ''}`}
                onClick={() => setImage(sample.url)}
              >
                📷 {sample.label}
              </button>
            ))}
          </div>

          <input
            type="text"
            className="form-input"
            placeholder="Or paste custom image URL..."
            value={image}
            onChange={(e) => setImage(e.target.value)}
          />
        </div>

        <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', padding: '0.85rem', fontSize: '1rem', marginTop: '1rem' }}>
          <Send size={18} />
          <span>SUBMIT {type} REPORT</span>
        </button>
      </form>
    </div>
  );
}
