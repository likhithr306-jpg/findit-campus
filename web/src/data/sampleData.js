export const INITIAL_ITEMS = [
  {
    id: 1,
    title: "Student ID Card - Alex Rivera",
    description: "Campus Student ID card with red lanyard. Lost near 2nd floor quiet study area.",
    category: "Cards & IDs",
    type: "LOST",
    location: "Central Library",
    dateReported: Date.now() - 2 * 3600 * 1000,
    contactInfo: "alex.rivera@campus.edu | (555) 019-2834",
    reporterName: "Alex Rivera",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1578574577315-3fbeb0cecdc2?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 2,
    title: "Student ID Card on Red Lanyard",
    description: "Found student ID card belonging to Alex on 2nd floor desk turned into front desk.",
    category: "Cards & IDs",
    type: "FOUND",
    location: "Central Library",
    dateReported: Date.now() - 1 * 3600 * 1000,
    contactInfo: "Turned in to Library Desk",
    reporterName: "Library Staff",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1578574577315-3fbeb0cecdc2?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 3,
    title: "Apple AirPods Pro 2",
    description: "White AirPods Pro case with blue silicone cover. Small scratch on top hinge.",
    category: "Electronics",
    type: "LOST",
    location: "Student Union",
    dateReported: Date.now() - 5 * 3600 * 1000,
    contactInfo: "sarah.m@campus.edu",
    reporterName: "Sarah Miller",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 4,
    title: "AirPods Case (White in Blue Sleeve)",
    description: "Found white wireless earbuds case with blue cover near food court tables.",
    category: "Electronics",
    type: "FOUND",
    location: "Student Union",
    dateReported: Date.now() - 3 * 3600 * 1000,
    contactInfo: "union.info@campus.edu",
    reporterName: "Jordan Tech",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 5,
    title: "Toyota Car Key & Gym Locker Key",
    description: "Found silver keychain with 1 Toyota smart key and brass locker key #142.",
    category: "Keys",
    type: "FOUND",
    location: "University Gym",
    dateReported: Date.now() - 24 * 3600 * 1000,
    contactInfo: "gym.frontdesk@campus.edu",
    reporterName: "Gym Reception",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1582142839970-2b932715694c?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 6,
    title: "Toyota Car Keys on Silver Ring",
    description: "Lost my Toyota key fob along with gym locker key while exercising on treadmills.",
    category: "Keys",
    type: "LOST",
    location: "University Gym",
    dateReported: Date.now() - 26 * 3600 * 1000,
    contactInfo: "dave.k@campus.edu",
    reporterName: "David Kim",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1582142839970-2b932715694c?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 7,
    title: "Blue Hydro Flask 32oz Bottle",
    description: "Navy blue metal insulated water bottle with National Park stickers.",
    category: "Other",
    type: "LOST",
    location: "Science Hall",
    dateReported: Date.now() - 48 * 3600 * 1000,
    contactInfo: "emily.s@campus.edu",
    reporterName: "Emily Stone",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 8,
    title: "Blue Metal Flask with Stickers",
    description: "Found blue insulated water bottle left in Science Hall lecture room 102.",
    category: "Other",
    type: "FOUND",
    location: "Science Hall",
    dateReported: Date.now() - 47 * 3600 * 1000,
    contactInfo: "science.dept@campus.edu",
    reporterName: "Dr. Evans",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 9,
    title: "Brown Leather Fossil Wallet",
    description: "Contains driver's license, student meal card, and cash. Lost during lunch.",
    category: "Bags & Wallets",
    type: "LOST",
    location: "Cafeteria",
    dateReported: Date.now() - 72 * 3600 * 1000,
    contactInfo: "marcus.v@campus.edu",
    reporterName: "Marcus Vance",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1627123424574-724758594e93?auto=format&fit=crop&w=600&q=80"
  },
  {
    id: 10,
    title: "TI-84 Plus CE Graphing Calculator",
    description: "Black Texas Instruments graphic calculator with label 'Chris' on back cover.",
    category: "Electronics",
    type: "LOST",
    location: "Math & Physics Center",
    dateReported: Date.now() - 96 * 3600 * 1000,
    contactInfo: "chris.b@campus.edu",
    reporterName: "Chris Bennett",
    status: "OPEN",
    image: "https://images.unsplash.com/photo-1611125832047-1d7ad1e8e48b?auto=format&fit=crop&w=600&q=80"
  }
];

export const CATEGORIES = [
  "Electronics",
  "Cards & IDs",
  "Keys",
  "Bags & Wallets",
  "Apparel & Accessories",
  "Books & Notes",
  "Other"
];

export const LOCATIONS = [
  "Central Library",
  "Student Union",
  "Science Hall",
  "University Gym",
  "Cafeteria",
  "Engineering Building",
  "Math & Physics Center",
  "Campus Quad",
  "Transit Station",
  "North Dorms"
];

const STORAGE_KEY = "findit_campus_items_v1";

export function getStoredItems() {
  try {
    const data = localStorage.getItem(STORAGE_KEY);
    if (data) {
      return JSON.parse(data);
    }
  } catch (e) {
    console.error("Failed to load items", e);
  }
  localStorage.setItem(STORAGE_KEY, JSON.stringify(INITIAL_ITEMS));
  return INITIAL_ITEMS;
}

export function saveStoredItems(items) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(items));
  } catch (e) {
    console.error("Failed to save items", e);
  }
}

export function resetStoredItems() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(INITIAL_ITEMS));
  return INITIAL_ITEMS;
}
