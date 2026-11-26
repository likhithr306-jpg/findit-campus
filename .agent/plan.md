# Project Plan

Build the FindIt Campus Android application using Kotlin, Jetpack Compose, Room Database, and rule-based / Python matching engine simulation. Implement full edge-to-edge design, Material 3 campus palette, Room entities, repository layer, ViewModels, compose UI screens (Dashboard, Lost/Found list, Report form, Detail screen, My Reports screen, Matching screen), seed initial realistic sample data with reset functionality, and verify with critic agent.

## Project Brief

# FindIt Campus - Project Brief

**FindIt Campus** is a smart lost and found mobile platform for college campuses. It combines a Kotlin Jetpack Compose Android client with local Room persistence and a Python-powered smart matching backend service to efficiently pair lost items with found reports across campus.

---

## Features

1. **Dashboard & Item Search**: Unified view displaying recent lost and found listings with search, category/location filtering, and local offline caching.
2. **Report Lost & Found Items**: Intuitive forms to capture lost or found item details, including category, campus location, date/time, contact info, and attached photos.
3. **Python-Powered Smart Matching**: Automated matching engine combining local rule-based matching with a Python analytics API service that calculates match confidence based on title keywords, location proximity, and category similarity.
4. **Item Details & Claim Management**: Comprehensive item details page featuring contact options, matching candidate lists, and status updating (e.g., Mark as Claimed/Returned).

---

## High-Level Tech Stack

* **Mobile Client Language**: Kotlin
* **UI Framework**: Jetpack Compose with Material Design 3 (M3), full Edge-to-Edge display, and Adaptive Icon
* **Navigation & Adaptive Strategy**: State-driven **Jetpack Navigation 3** (`androidx.navigation3`) combined with **Compose Material Adaptive** (`androidx.compose.material3.adaptive`) for single and multi-pane adaptive layouts
* **Architecture & State**: MVVM with Unidirectional Data Flow (UDF), Kotlin Coroutines, and StateFlow
* **Local Persistence**: **Room Database** for offline-first data caching and **DataStore Preferences** for app settings
* **Network & Backend Services**: **Retrofit** with **Moshi/OkHttp** communicating with a **Python Backend Service** (FastAPI / Flask) for advanced smart-matching algorithms and analytics

## Implementation Steps

### Task_1_DataArchitectureAndPersistence: Define Room entities (LostFoundItem, MatchCandidate), DAOs, Room Database setup, DataStore preferences, initial realistic sample data seeding logic with reset capability, and repository layer for local persistence.
- **Status:** COMPLETED
- **Updates:** Defined Room entities LostFoundItem and MatchCandidate, ItemDao, AppDatabase, DataStore preferences manager, initial sample data seeder, and ItemRepository with Kotlin Flows. All build and tests pass.
- **Acceptance Criteria:**
  - Room Database and DAOs created for lost and found items
  - Sample data seeding and database reset functionality implemented
  - Repository layer provides flows for item management and CRUD operations
  - Build pass

### Task_2_MatchingEngineAndViewModels: Implement local rule-based and Python backend matching service client (Retrofit/Moshi integration), alongside ViewModels managing state for Dashboard, Item Details, Reporting, and Smart Matching.
- **Status:** COMPLETED
- **Updates:** Implemented Python matching Retrofit API client with local fallback rule-based matching engine. Created DashboardViewModel, ReportViewModel, ItemDetailViewModel, MatchingViewModel, and MyReportsViewModel with StateFlow. All unit tests and assembleDebug passed.
- **Acceptance Criteria:**
  - Matching engine calculates match confidence based on title keywords, category, and location proximity
  - Retrofit service configured for Python backend matching analytics with robust fallback logic
  - ViewModels expose StateFlow for UI consumption
  - Build pass

### Task_3_ComposeUiScreensAndNavigation: Build Jetpack Compose UI screens (Dashboard with search/filters, Report Item form, Item Detail & Claim management screen, My Reports screen, Smart Matching screen) and state-driven navigation flow.
- **Status:** COMPLETED
- **Updates:** Built DashboardScreen with stats/search/filters, ReportFormScreen with lost/found toggle and photo attachment, ItemDetailScreen with status updates and candidate pairs, MatchingScreen with Python status banner and confidence badges, MyReportsScreen, and state-driven bottom navigation. assembleDebug and testDebugUnitTest passed.
- **Acceptance Criteria:**
  - Dashboard displays search bar, category/location filter chips, and listing cards
  - Report form allows capturing item details, category, campus location, date/time, contact info
  - Item Detail screen shows claim/return status updates and matching candidates
  - Navigation between screens operates seamlessly
  - Build pass

### Task_4_AppThemeIconAndRunVerify: Apply vibrant Material Design 3 campus theme, full edge-to-edge display configuration, adaptive app icon matching app function, and perform full run and verify check.
- **Status:** COMPLETED
- **Updates:** Vibrant M3 campus theme and full edge-to-edge display verified. Adaptive app icon verified. All unit tests passed (5/5). Build succeeded.
- **Acceptance Criteria:**
  - Vibrant Material 3 color theme and full edge-to-edge configured
  - Adaptive app icon created
  - Make sure all existing tests pass
  - Build pass
  - App does not crash
- **Duration:** N/A

