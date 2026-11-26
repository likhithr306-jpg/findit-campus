# FindIt Campus - Smart Lost & Found Platform 🔍

> **Smart lost & found mobile and web application for college campuses powered by Jetpack Compose, Room Database, React + Vite, and AI/rule-based smart candidate matching.**

[![Live Web App](https://img.shields.io/badge/Live%20Web%20App-GitHub%20Pages-006689?style=for-the-badge&logo=react)](https://likhithr306-jpg.github.io/findit-campus/)
[![GitHub Repo](https://img.shields.io/badge/GitHub-Repository-181717?style=for-the-badge&logo=github)](https://github.com/likhithr306-jpg/findit-campus)

---

## 🌐 Live Web Application

- **Live URL**: [https://likhithr306-jpg.github.io/findit-campus/](https://likhithr306-jpg.github.io/findit-campus/)
- **Local Dev Server**: `http://localhost:5173`

---

## ✨ Features

- 📱 **Android Client**: Built strictly using Kotlin, Jetpack Compose Material Design 3 (M3), full Edge-to-Edge display, and adaptive icons.
- 💻 **React + Vite Web App**: Interactive dashboard, catalog browse, report forms, modal details, and persistent state.
- 🧠 **Smart Candidate Matching**: Automated similarity matching comparing category, campus location proximity, title/description keyword overlap (Jaccard Index), and report timestamp.
- 💾 **Local Persistence**: Offline-first Room Database on Android and `localStorage` on Web with instant sample data reset capability.
- 📡 **Python Backend API Support**: Configured Retrofit client (`http://10.0.2.2:8000/api/match`) with robust fallback to offline rule-based calculation.

---

## 🚀 Repository Structure

```text
findit-campus/
├── app/                        # Android application source (Kotlin, Compose, Room, ViewModels)
├── web/                        # React + Vite web application (JSX, CSS, LocalStorage, GitHub Pages)
├── build.gradle.kts            # Root Gradle build file
├── settings.gradle.kts         # Gradle settings file
└── README.md                   # Repository documentation
```

---

## 🛠️ Getting Started

### 1. Web Application (React + Vite)
```bash
cd web
npm install
npm run dev
```
Open [http://localhost:5173](http://localhost:5173) in your browser.

### 2. Android Application
Open the project in **Android Studio** and run:
```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

---

## 👤 Author
- **GitHub**: [@likhithr306-jpg](https://github.com/likhithr306-jpg)
- **Repository**: [https://github.com/likhithr306-jpg/findit-campus](https://github.com/likhithr306-jpg/findit-campus)
