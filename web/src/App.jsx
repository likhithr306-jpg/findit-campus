import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import ItemDetailModal from './components/ItemDetailModal';
import Toast from './components/Toast';
import DashboardView from './views/DashboardView';
import BrowseView from './views/BrowseView';
import ReportView from './views/ReportView';
import MatchingView from './views/MatchingView';
import MyReportsView from './views/MyReportsView';
import { getStoredItems, saveStoredItems, resetStoredItems } from './data/sampleData';
import { computeSmartMatches } from './utils/matchingEngine';

export default function App() {
  const [items, setItems] = useState([]);
  const [activeTab, setActiveTab] = useState('dashboard');
  const [selectedItem, setSelectedItem] = useState(null);
  const [toastMessage, setToastMessage] = useState(null);

  useEffect(() => {
    const loaded = getStoredItems();
    setItems(loaded);
  }, []);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => {
      setToastMessage(null);
    }, 4000);
  };

  const handleUpdateStatus = (id, newStatus) => {
    const updated = items.map(item => item.id === id ? { ...item, status: newStatus } : item);
    setItems(updated);
    saveStoredItems(updated);
    if (selectedItem && selectedItem.id === id) {
      setSelectedItem({ ...selectedItem, status: newStatus });
    }
    showToast(`Status updated to ${newStatus}`);
  };

  const handleDeleteItem = (id) => {
    const updated = items.filter(item => item.id !== id);
    setItems(updated);
    saveStoredItems(updated);
    setSelectedItem(null);
    showToast('Report deleted successfully');
  };

  const handleAddReport = (newItem) => {
    const updated = [newItem, ...items];
    setItems(updated);
    saveStoredItems(updated);
    showToast(`New ${newItem.type} report created successfully!`);
    setSelectedItem(newItem);
    setActiveTab('dashboard');
  };

  const handleResetData = () => {
    const reset = resetStoredItems();
    setItems(reset);
    showToast('Database reset to default campus sample data.');
  };

  const smartMatches = computeSmartMatches(items);

  return (
    <div className="app-container">
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onReportClick={() => setActiveTab('report')}
        onResetClick={handleResetData}
      />

      <main className="main-content">
        {activeTab === 'dashboard' && (
          <DashboardView
            items={items}
            matchesCount={smartMatches.length}
            onItemClick={(item) => setSelectedItem(item)}
            onReportClick={() => setActiveTab('report')}
            onNavigateTab={(tab) => setActiveTab(tab)}
          />
        )}

        {activeTab === 'browse' && (
          <BrowseView
            items={items}
            onItemClick={(item) => setSelectedItem(item)}
          />
        )}

        {activeTab === 'report' && (
          <ReportView
            onSubmitReport={handleAddReport}
          />
        )}

        {activeTab === 'matching' && (
          <MatchingView
            items={items}
            onViewItem={(item) => setSelectedItem(item)}
            onUpdateStatus={handleUpdateStatus}
          />
        )}

        {activeTab === 'my-reports' && (
          <MyReportsView
            items={items}
            onItemClick={(item) => setSelectedItem(item)}
            onReportClick={() => setActiveTab('report')}
          />
        )}
      </main>

      {/* Item Detail Modal */}
      {selectedItem && (
        <ItemDetailModal
          item={selectedItem}
          onClose={() => setSelectedItem(null)}
          onUpdateStatus={handleUpdateStatus}
          onDeleteItem={handleDeleteItem}
        />
      )}

      {/* Toast Notification */}
      <Toast message={toastMessage} onClose={() => setToastMessage(null)} />
    </div>
  );
}
