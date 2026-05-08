# 👶 BabyPing - Application mobile de gestion des routines bébé

BabyPing est une application Android développée dans le cadre du cours d’informatique mobile.  
L’objectif principal de l’application est d’aider les parents à organiser et suivre les routines quotidiennes de leur bébé : biberon, promenade, rendez-vous médical, courses, activités, etc.

L’application permet de créer des rappels, de suivre les routines complétées, d’analyser les statistiques de suivi et de recevoir des notifications selon l’heure ou selon un contexte géographique.

---

## 📱 Fonctionnalités principales

- Création, modification et suppression de routines
- Catégories de routines :
  - Quotidiens
  - Programmes
  - Santé
  - Activités
  - Courses
- Rappels programmés selon l’heure choisie
- Notifications locales pour les routines
- Déclenchement contextuel par géolocalisation avec Geofencing
- Sélection d’un lieu sur une carte OpenStreetMap
- Suivi des routines complétées chaque jour
- Statistiques de progression hebdomadaire
- Affichage des rappels ignorés
- Mode clair et mode sombre
- Écran de profil utilisateur
- Interface inspirée du prototype Figma final

---

## 🧱 Architecture du projet

Le projet suit une organisation proche de l’architecture MVVM.

```text
com.app.babyroutine
│
├── data
│   ├── AppDatabase
│   ├── RoutineDao
│   ├── RoutineDailyStateDao
│   ├── RoutineRepository
│   └── RoutineConverters
│
├── domain
│   └── RoutineValidator
│
├── location
│   ├── GeofenceManager
│   ├── GeofenceBroadcastReceiver
│   └── LocationPermissionHelper
│
├── model
│   └── Routine, Frequency, Priority, HomeTab
│
├── navigation
│   └── AppRoot, Screen, AppDependencies
│
├── notifications
│   ├── NotificationHelper
│   ├── NotificationCoordinator
│   ├── RoutineScheduler
│   ├── RoutineAlarmReceiver
│   └── NotificationDismissReceiver
│
├── ui
│   ├── components
│   ├── screens
│   ├── theme
│   └── viewmodel
│
└── MainActivity
```

---

## 🛠️ Technologies utilisées

- Kotlin
- Android Jetpack Compose
- Material 3
- MVVM
- Room Database
- Kotlin Coroutines
- Flow
- AlarmManager
- NotificationManager
- Google Play Services Location
- Geofencing
- OpenStreetMap avec osmdroid
- Gradle Kotlin DSL

---

## 🗃️ Base de données

L’application utilise Room pour stocker les données localement.

### Tables principales

- `routines` : contient les routines créées par l’utilisateur
- `routine_daily_state` : contient l’état quotidien d’une routine, par exemple :
  - routine complétée
  - routine ignorée
  - date associée

---

## 🔔 Notifications

BabyPing utilise des notifications locales pour rappeler les routines à l’utilisateur.

Les rappels sont gérés par :

- `RoutineScheduler` pour programmer les alarmes
- `RoutineAlarmReceiver` pour recevoir les alarmes
- `NotificationHelper` pour afficher les notifications
- `NotificationDismissReceiver` pour enregistrer les rappels ignorés

---

## 📍 Géolocalisation et déclenchement contextuel

L’application permet d’associer une routine à une zone géographique.  
Lorsque l’utilisateur entre dans une zone définie, une notification peut être déclenchée automatiquement.

Cette fonctionnalité utilise :

- `GeofenceManager`
- `GeofenceBroadcastReceiver`
- Google Play Services Location
- OpenStreetMap pour la sélection du lieu

---

## 🚀 Installation et lancement

### Prérequis

- Android Studio
- JDK 17
- Android SDK installé
- Émulateur ou téléphone Android
- Connexion Internet pour l’affichage de la carte OpenStreetMap

### Étapes

1. Cloner le projet :

```bash
git clone https://github.com/OussamaMaaroufi11/BabyPing.git
```

2. Ouvrir le projet dans Android Studio.

3. Synchroniser Gradle.

4. Lancer l’application sur un émulateur ou un appareil Android.

---

## 📄 Licence

Projet académique réalisé dans le cadre du cours d’informatique mobile.
