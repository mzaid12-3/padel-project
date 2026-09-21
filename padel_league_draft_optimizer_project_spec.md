# Project Specification: Padel League AI Draft Optimizer & Management System

## 1. Executive Summary
An intelligent draft assistant and league management platform designed for competitive Padel leagues. Team owners manage a fixed budget to assemble the highest-performing squad. The system provides real-time next-pick recommendations, dynamic budget tracking, and squad valuation using a combination of combinatorial optimization and reinforcement learning / decision models.

---

## 2. System Architecture

```
┌────────────────────────────────────────────────────────┐
│             Frontend: Angular 17+ (SPA)               │
│   - Live Draft Board & Budget Gauge                    │
│   - Recommended Next Pick Widgets                      │
│   - Excel / CSV Drag-and-Drop Uploader                 │
│   - Player Search & Manual Rating Input                │
└──────────────────────────┬─────────────────────────────┘
                           │ REST / WebSockets
┌──────────────────────────▼─────────────────────────────┐
│             Backend Core: Java 21 / Spring Boot 3      │
│   - League, Team, & Owner Entity Management            │
│   - Draft State Engine (Snake / Auction Drafts)        │
│   - File Ingestion Service (Apache POI for Excel)      │
│   - Playtomic Integration & Data Scraping Service      │
│   - Communication Bridge to Python ML Engine           │
└──────────────────────────┬─────────────────────────────┘
                           │ gRPC or REST (FastAPI)
┌──────────────────────────▼─────────────────────────────┐
│          ML & Analytics Engine: Python (FastAPI)       │
│   - Optimization: Knapsack / Integer Linear Prog (ILP) │
│   - Recommendation: RL (Q-Learning/MCTS) or Heuristics │
│   - Player Rating & Synergy Scoring Model              │
└────────────────────────────────────────────────────────┘
```

---

## 3. Core Features

### 3.1. Dynamic Recommendation Engine (The "Next Best Pick" Model)
* **Problem Formulation:**
  * **Objective:** Maximize total team rating $\sum \text{Rating}_i$ subject to $\sum \text{Cost}_i \le \text{Budget}$ and positional/pair balance (e.g., right-court vs. left-court player compatibility).
  * **Algorithmic Approach:**
    * **Stage 1 (Baseline Baseline Optimization):** Integer Linear Programming (ILP) via `PuLP` or `OR-Tools` (dynamic knapsack problem). Solves the optimal subset given the current remaining budget and roster slots.
    * **Stage 2 (Real-Time Policy / Reinforcement Learning):** 
      * If modeled as an adversarial draft (other managers drafting players unpredictably), use a Markov Decision Process (MDP) or Monte Carlo Tree Search (MCTS) to predict competitor drafts and recommend a player that prevents draft snipes while optimizing value-over-replacement (VORP).
    * **"Next Pick" Lookahead:** If an owner selects Player $A$, the system computes:
      1. Remaining budget and slots.
      2. Top 3 viable future combinations reachable with the residual budget.
      3. Projected team synergy score.

### 3.2. Data Ingestion Pipeline
1. **Excel/CSV Upload:**
   * Handled by Java Spring Boot using **Apache POI**.
   * Flexible schema mapping (Name, Rating/Level, Preferred Side [Drive/Revés], Cost/Base Price, Matches Played).
2. **Playtomic Data Source:**
   * *Direct API or Scraping Pipeline:* Playtomic level metrics (typically ranging from 0.0 to 7.0), match history, win rates, and reliability score.
   * Cached locally in PostgreSQL/MongoDB to avoid rate limits.
3. **Manual Override & Ad-Hoc Player Creation:**
   * Ability to quickly register walk-ins or unrated players with custom skill levels and costs during pre-draft setup.

### 3.3. Draft Modes
* **Budget Draft (Auction Style):** Owners nominate players and place bids; dynamic recalculation of remaining purchasing power.
* **Fixed Price Snake Draft:** Players have assigned price tags; managers must pick turn-by-turn without exceeding salary caps.

---

## 4. Technical Stack Breakdown

| Component | Technology | Responsibilities |
| :--- | :--- | :--- |
| **Frontend** | Angular 17+, RxJS, Tailwind CSS, Lucide Icons | Responsive UI, state management, live draft websocket subscriptions, dashboard. |
| **Backend Core** | Java 21, Spring Boot 3, Spring Security, Hibernate | Business logic, authentication, draft orchestration, DB persistence, Excel parsing. |
| **Persistence** | PostgreSQL | Player profiles, league settings, historical drafts, user accounts. |
| **ML / Optimization** | Python 3.11+, FastAPI, NumPy, Pandas, Scipy / OR-Tools / Stable-Baselines3 | Knapsack optimization, value-over-replacement calculations, MCTS/RL recommendation model. |
| **Integration** | gRPC or REST via HTTP/2, WebSockets | Low-latency state sync between Java backend, Python ML microservice, and Angular UI. |

---

## 5. Phased Implementation Plan for Codex CLI

### Phase 1: Domain Entities & Data Ingestion (Java)
- [ ] Scaffold Spring Boot project with PostgreSQL integration.
- [ ] Define data models: `League`, `Team`, `Player`, `DraftSession`, `Pick`.
- [ ] Implement Excel/CSV parsing service via Apache POI.
- [ ] Create stub service for Playtomic player profile lookup.

### Phase 2: Python Decision & Recommendation Engine
- [ ] Build a FastAPI service with an endpoint `/recommend-next-picks`.
- [ ] Implement a dynamic knapsack/ILP solver using Google OR-Tools as the primary optimization baseline.
- [ ] Build an experimental RL/heuristic simulator for predicting competitor draft behavior.

### Phase 3: Live Draft Coordination Engine
- [ ] Implement WebSocket endpoints in Spring Boot for real-time draft events (turn tick, pick chosen, budget updated).
- [ ] Connect Spring Boot to Python service via synchronous HTTP client or gRPC.

### Phase 4: Angular Frontend
- [ ] Draft lobby, manager dashboard, and remaining budget meters.
- [ ] Drag-and-drop Excel file upload with column mapping wizard.
- [ ] Real-time recommendations drawer showing:
  - Immediate best pick.
  - "If you pick X, you will likely only afford Y later" contingency projections.

---

## 6. Prompt Starters for Codex CLI Sessions

```bash
# Session 1: Scaffolding the Spring Boot Data Ingestion
codex "Create a Spring Boot 3 service using Apache POI to parse an uploaded Excel file containing Padel player data (Name, Playtomic Level, Side, Cost). Include validation and error handling."

# Session 2: Optimization Service in Python
codex "Implement a Python FastAPI endpoint that accepts a list of available padel players, current team roster, remaining budget, and remaining slots, then returns the top 3 optimal draft picks using Google OR-Tools knapsack solver."
```