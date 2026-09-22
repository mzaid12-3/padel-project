# Project Specification: Padel League AI Draft Optimizer & Management System

## 1. Executive Summary

An intelligent single-user Padel auction draft assistant designed to help one user build the strongest possible squad within a fixed league budget.

The user creates the league, configures the salary cap and squad size, uploads a CSV or Excel file containing the available players, and then begins bidding on players.

During the auction, the system continuously tracks:

* Current squad
* Remaining budget
* Remaining squad slots
* Current player being bid on
* Current bid price
* Maximum recommended bid
* Player value
* Squad balance
* Best remaining squad combinations
* Recommended next targets

The AI recommendation engine recalculates after every successful purchase so the user always knows how much they can safely spend without damaging the strength of the final squad.

---

## 2. User Flow

The application is designed around one user.

### Step 1 — Create League

The user enters:

* League name
* Total budget / salary cap
* Number of players required in the squad
* Optional minimum or maximum player requirements
* Optional preferred balance between left-side and right-side players

Example:

```text
League Name: Thursday Padel League
Budget: R10,000
Squad Size: 6 Players
```

---

### Step 2 — Upload Player List

The user uploads a CSV or Excel file containing the available players.

Example:

```csv
Name,Rating,Side,BasePrice,MatchesPlayed
Zaid,5.2,Left,1500,42
Faheem,4.9,Right,1200,37
Joe,5.5,Left,1700,50
Hammies,4.6,Right,900,28
```

The system validates the file and imports the players into the league.

The user can review and edit the imported information before starting the auction.

---

### Step 3 — Review Player Pool

The user sees all available players with information such as:

* Player name
* Rating
* Preferred side
* Base price
* Matches played
* Value score
* Availability
* AI rating / recommendation score

The user can manually edit players or add additional players before the draft begins.

---

### Step 4 — Start Auction

Once the league settings and player list are ready, the user clicks:

```text
START AUCTION
```

The auction dashboard becomes active.

The user selects the player currently being auctioned.

Example:

```text
Player: Joe
Current Bid: R1,500
```

The system immediately calculates:

```text
Player Rating: 5.5

Current Bid: R1,500

AI Recommended Maximum Bid:
R1,850

Value Rating:
Very Good

Remaining Budget If Purchased:
R8,500

Remaining Squad Slots:
5
```

---

### Step 5 — Bid Decision

As the price increases, the user updates the bid amount.

For example:

```text
R1,500
R1,600
R1,700
R1,800
R1,900
```

The recommendation engine recalculates after every price change.

Example:

```text
Current Bid: R1,900

Recommended Maximum:
R1,850

Recommendation:
PASS

Reason:
Buying Joe for R1,900 would reduce your ability to afford two high-value right-side players later in the auction.
```

The AI is therefore not bidding automatically.

It acts as the user's draft assistant.

The user makes the final decision.

---

### Step 6 — Player Won

If the user wins the player, they click:

```text
BUY PLAYER
```

The player is added to the squad.

The system automatically updates:

* Remaining budget
* Remaining roster slots
* Squad rating
* Squad balance
* Available player pool
* Recommended next targets
* Maximum recommended future bids

Example:

```text
Squad

Joe
Rating: 5.5
Price: R1,850

Budget Remaining:
R8,150

Players Remaining:
5
```

---

### Step 7 — Continue Auction

The same process continues for each player.

Every new player and bid is evaluated against the user's current squad and remaining budget.

The recommendation engine becomes more restrictive as fewer squad slots and less budget remain.

---

## 3. System Architecture

```text
┌─────────────────────────────────────────────────────────────┐
│                 Frontend: Angular 17+                      │
│                                                             │
│  - League Setup                                             │
│  - CSV / Excel Upload                                       │
│  - Player Pool                                              │
│  - Auction Dashboard                                        │
│  - Current Bid Input                                        │
│  - Budget Gauge                                             │
│  - Squad Display                                            │
│  - AI Recommendations                                       │
└────────────────────────────┬────────────────────────────────┘
                             │
                             │ REST
                             │
┌────────────────────────────▼────────────────────────────────┐
│              Backend: Java 21 / Spring Boot 3              │
│                                                             │
│  - User Management                                          │
│  - League Management                                        │
│  - Player Management                                        │
│  - CSV / Excel Import                                       │
│  - Auction State Management                                 │
│  - Squad Management                                         │
│  - Budget Management                                        │
│  - Communication with Python AI Engine                      │
└────────────────────────────┬────────────────────────────────┘
                             │
                             │ REST / FastAPI
                             │
┌────────────────────────────▼────────────────────────────────┐
│              AI Engine: Python + FastAPI                   │
│                                                             │
│  - Player Value Calculation                                 │
│  - Maximum Bid Calculation                                  │
│  - Squad Optimization                                       │
│  - Next Best Player Recommendation                          │
│  - Future Squad Simulation                                  │
│  - Synergy / Side Balance Scoring                           │
│  - OR-Tools Integer Optimization                            │
└─────────────────────────────────────────────────────────────┘
```

---

# 4. Core Domain Model

The application does not require multiple owners, managers or captains.

The main entities are:

```text
User
League
Player
DraftSession
SquadPlayer
Bid
```

---

## User

Represents the person using the application.

```text
User
----
id
name
email
password
```

One user may create multiple leagues over time.

---

## League

Represents the auction configuration.

```text
League
------
id
userId
name
salaryCap
squadSize
createdAt
status
```

Example:

```text
League:
Thursday Night League

Budget:
R10,000

Squad Size:
6
```

---

## Player

Represents an available player.

```text
Player
------
id
leagueId
name
rating
preferredSide
basePrice
matchesPlayed
status
```

Possible player statuses:

```text
AVAILABLE
PURCHASED
REMOVED
```

---

## DraftSession

Stores the current auction state.

```text
DraftSession
------------
id
leagueId
startingBudget
remainingBudget
squadSize
remainingSlots
status
startedAt
completedAt
```

Possible statuses:

```text
SETUP
ACTIVE
COMPLETED
```

---

## SquadPlayer

Represents a player purchased by the user.

```text
SquadPlayer
-----------
id
draftSessionId
playerId
purchasePrice
purchasedAt
```

---

## Bid

Stores the bids evaluated during the auction.

```text
Bid
---
id
draftSessionId
playerId
amount
recommendedMaxBid
recommendation
createdAt
```

Possible recommendation values:

```text
STRONG_BUY
BUY
CAUTION
PASS
```

These labels represent the model's valuation of the bid rather than an automatic action.

---

# 5. Data Ingestion

## CSV / Excel Upload

The user uploads the league player list.

Supported formats:

```text
.csv
.xlsx
.xls
```

CSV files can be parsed using standard Java CSV libraries.

Excel files can be parsed using Apache POI.

Expected columns:

```text
Name
Rating
Side
BasePrice
MatchesPlayed
```

Optional columns:

```text
WinRate
Reliability
PlaytomicLevel
MatchesWon
MatchesLost
```

---

## Column Mapping

The application should allow flexible column mapping.

For example, if the uploaded spreadsheet contains:

```text
Player Name
Level
Position
Starting Bid
```

The user can map:

```text
Player Name  → Name
Level        → Rating
Position     → Side
Starting Bid → BasePrice
```

---

# 6. Auction Dashboard

The auction dashboard is the main screen of the application.

Suggested layout:

```text
-------------------------------------------------------

THURSDAY PADEL LEAGUE

Budget Remaining
R7,250 / R10,000

Squad
2 / 6 Players

-------------------------------------------------------

CURRENT PLAYER

Joe

Rating:
5.5

Preferred Side:
Left

Base Price:
R1,200

Current Bid:

[ R1,650 ]

-------------------------------------------------------

AI ANALYSIS

Recommended Maximum Bid

R1,850

Current Value

GOOD VALUE

Squad Impact

+8.2%

Recommendation

BUY

-------------------------------------------------------

IF YOU BUY JOE FOR R1,650

Remaining Budget
R5,600

Remaining Slots
3

Average Budget Per Slot
R1,866

Best Remaining Combination

Faheem
Hammies
Moos

Projected Squad Rating
29.6

-------------------------------------------------------

[ BUY PLAYER ]

[ PASS ]

-------------------------------------------------------
```

---

# 7. Recommendation Engine

The recommendation engine should answer one important question:

```text
Should I continue bidding on this player,
and what is the highest price I should pay?
```

---

## Inputs

The Python engine receives:

```json
{
  "budgetRemaining": 7250,
  "remainingSlots": 4,
  "currentBid": 1650,
  "currentPlayer": {},
  "currentSquad": [],
  "availablePlayers": []
}
```

---

## Outputs

Example:

```json
{
  "recommendation": "BUY",
  "recommendedMaxBid": 1850,
  "playerValueScore": 8.6,
  "projectedSquadRating": 29.6,
  "remainingBudgetAfterPurchase": 5600,
  "recommendedFuturePlayers": [
    "Faheem",
    "Hammies",
    "Moos"
  ]
}
```

---

# 8. Optimization Model

The baseline AI should use Integer Linear Programming.

Google OR-Tools is recommended.

The objective is:

```text
Maximise:

Total Squad Rating
+
Player Value
+
Squad Balance
+
Player Synergy
```

Subject to:

```text
Total Cost <= Remaining Budget

Number of Players <= Remaining Squad Slots
```

Optional constraints can include:

```text
Minimum Left-Side Players
Minimum Right-Side Players
Maximum Players of Similar Playing Style
```

---

# 9. Maximum Bid Calculation

One of the most important AI features should be:

```text
MAXIMUM SAFE BID
```

Instead of simply recommending the best player, the AI determines how much the user can afford to spend on that player before the remaining squad quality falls too far.

For example:

```text
Current Player:
Joe

Current Bid:
R1,500

Maximum Safe Bid:
R1,850
```

At:

```text
R1,500
```

the remaining optimal squad may score:

```text
31.2
```

At:

```text
R1,850
```

the remaining optimal squad may score:

```text
30.7
```

At:

```text
R2,100
```

the remaining optimal squad may fall to:

```text
27.9
```

Therefore the model may recommend:

```text
Maximum Bid:
R1,850
```

---

# 10. Future Squad Simulation

For every bid, the system should simulate:

```text
If I buy this player now,
what squad can I still build afterward?
```

The system calculates the best possible combination from the remaining players.

Example:

```text
BUY JOE FOR R1,800

Remaining Budget:
R5,450

Possible Final Squad:

Joe
Faheem
Hammies
Moos
Zaid
Altaaf

Projected Rating:
30.4
```

This makes the AI recommendation much more useful than simply comparing player ratings.

---

# 11. Player Value Score

A simple initial value metric could be:

```text
Value = Rating / Expected Cost
```

A more advanced model could use:

```text
Value Score =
Rating
+ Win Rate
+ Experience
+ Reliability
+ Positional Need
+ Squad Synergy
- Cost Penalty
```

The score can later become more sophisticated as historical league data becomes available.

---

# 12. Squad Balance

Padel squad building should account for preferred playing side.

Players may be:

```text
LEFT
RIGHT
BOTH
```

The optimizer should reward balanced squads.

For example:

```text
Current Squad

Left:
Joe
Zaid
Hammies

Right:
Faheem

```

The AI may therefore prioritize right-side players.

Example recommendation:

```text
Faheem

Rating:
4.9

Recommendation Score:
9.1

Reason:
Strong value and improves right-side squad balance.
```

---

# 13. Recommended Next Players

After each purchase, the system should automatically generate:

```text
TOP TARGETS

1. Faheem
Recommended Max: R1,500

2. Hammies
Recommended Max: R1,250

3. Moos
Recommended Max: R1,100
```

These recommendations change dynamically as the budget changes.

---

# 14. Technical Stack

| Component      | Technology              | Responsibility                                 |
| -------------- | ----------------------- | ---------------------------------------------- |
| Frontend       | Angular 17+             | League setup, auction dashboard, squad display |
| Styling        | Tailwind CSS            | Responsive UI                                  |
| Backend        | Java 21 + Spring Boot 3 | Application logic and persistence              |
| Database       | PostgreSQL              | Users, leagues, players, auctions              |
| File Parsing   | Apache POI / CSV Parser | Excel and CSV uploads                          |
| AI Engine      | Python 3.11+            | Optimization and recommendations               |
| API            | FastAPI                 | AI service endpoints                           |
| Optimization   | Google OR-Tools         | Squad optimization                             |
| Communication  | REST                    | Spring Boot ↔ Python                           |
| Authentication | Spring Security + JWT   | User authentication                            |

---

# 15. Simplified Architecture Decision

The first version should NOT include:

```text
Multiple owners

Multiple captains

Multiple managers

Manager invites

Draft turns

Snake drafts

Live multiplayer

WebSocket manager synchronization

Competing user accounts inside one league

Opponent bid prediction

Reinforcement learning for opponent behaviour
```

These features add significant complexity without helping the primary goal of the application.

The first version should focus on:

```text
One user

One squad

One budget

One player pool

One live auction

AI-assisted bidding
```

---

# 16. Phase 1 — League and Player Setup

Build the Spring Boot backend.

Tasks:

* [ ] Create Spring Boot project
* [ ] Configure PostgreSQL
* [ ] Create User entity
* [ ] Create League entity
* [ ] Create Player entity
* [ ] Create DraftSession entity
* [ ] Create SquadPlayer entity
* [ ] Create Bid entity
* [ ] Implement league creation
* [ ] Implement player CRUD
* [ ] Implement CSV upload
* [ ] Implement Excel upload
* [ ] Implement validation
* [ ] Allow player editing before draft

---

# 17. Phase 2 — Basic Auction Engine

Create the auction functionality.

Tasks:

* [ ] Start draft session
* [ ] Select current player
* [ ] Enter current bid
* [ ] Buy player
* [ ] Pass player
* [ ] Deduct purchase price
* [ ] Update remaining budget
* [ ] Update remaining squad slots
* [ ] Mark purchased player as unavailable
* [ ] Prevent purchases that exceed the budget
* [ ] Prevent squad size from exceeding league limit

---

# 18. Phase 3 — Python Optimization Engine

Create FastAPI service.

Primary endpoint:

```text
POST /recommend
```

Input:

```json
{
  "remainingBudget": 6000,
  "remainingSlots": 4,
  "currentBid": 1400,
  "currentPlayer": {},
  "currentSquad": [],
  "availablePlayers": []
}
```

Output:

```json
{
  "recommendation": "BUY",
  "maximumRecommendedBid": 1650,
  "projectedFinalRating": 28.9,
  "recommendedFutureSquad": [],
  "nextTargets": []
}
```

Tasks:

* [ ] Build FastAPI project
* [ ] Implement OR-Tools optimizer
* [ ] Calculate optimal remaining squad
* [ ] Calculate projected squad rating
* [ ] Calculate maximum safe bid
* [ ] Calculate next-player recommendations
* [ ] Return explanations for recommendations

---

# 19. Phase 4 — Angular Frontend

Build the main user experience.

Pages:

```text
/login

/dashboard

/league/new

/league/:id/setup

/league/:id/players

/league/:id/draft

/league/:id/results
```

League setup page:

```text
League Name
Budget
Squad Size
Player File Upload
```

Auction page:

```text
Current Budget

Current Squad

Available Players

Current Player

Current Bid

Recommended Maximum Bid

AI Recommendation

Future Squad Projection

BUY PLAYER

PASS
```

---

# 20. Phase 5 — Advanced Intelligence

Only after the basic application works should more advanced modelling be introduced.

Possible improvements:

* Player synergy models
* Historical auction price prediction
* Playtomic statistics
* Historical match performance
* Expected market price
* Price inflation detection
* Bargain detection
* Remaining-player scarcity
* Positional scarcity
* Monte Carlo auction simulation
* Machine-learning player valuation

Reinforcement learning should therefore be treated as a future enhancement rather than a requirement for the MVP.

---

# 21. Example Complete Auction

Initial league:

```text
Budget:
R10,000

Squad Size:
6
```

Player auction:

```text
Joe

Current Bid:
R1,600

AI Maximum:
R1,850

Recommendation:
BUY
```

User purchases Joe:

```text
Purchase:
R1,750
```

System becomes:

```text
Budget:
R8,250

Squad:
1 / 6
```

Next player:

```text
Faheem

Current Bid:
R1,400

AI Maximum:
R1,550

Recommendation:
BUY
```

Later:

```text
Zaid

Current Bid:
R2,200

AI Maximum:
R1,800

Recommendation:
PASS
```

Reason:

```text
At R2,200 your remaining average budget per squad slot
falls below the expected cost required to complete the squad
with competitive players.
```

---

# 22. Codex CLI Development Prompts

## Session 1 — Spring Boot Setup

```bash
codex "Create a Java 21 Spring Boot 3 backend for a single-user Padel auction draft application. The user creates a league with a salary cap and squad size, uploads players, and runs an auction to build one squad. Create entities for User, League, Player, DraftSession, SquadPlayer and Bid using Spring Data JPA and PostgreSQL."
```

---

## Session 2 — CSV / Excel Import

```bash
codex "Implement CSV and Excel player import for the Padel draft application. Accept columns Name, Rating, Side, BasePrice and MatchesPlayed. Use Apache POI for Excel and implement validation with useful error messages."
```

---

## Session 3 — Auction Engine

```bash
codex "Implement the auction service for the Padel draft application. A single user selects a player, enters the current bid and can either buy or pass. When purchasing a player, deduct the purchase price from the league budget, reduce remaining roster slots, add the player to the user's squad and mark the player as unavailable."
```

---

## Session 4 — Python Optimizer

```bash
codex "Create a Python FastAPI service using Google OR-Tools for a Padel auction draft assistant. Given the user's current squad, remaining budget, remaining slots, available players, current player and current bid, determine the strongest possible remaining squad and calculate a recommended maximum bid for the current player."
```

---

## Session 5 — Angular Auction Dashboard

```bash
codex "Build an Angular 17 auction dashboard for a single-user Padel draft optimizer. Display remaining budget, squad slots, current squad, available players, selected auction player, current bid input, recommended maximum bid, AI recommendation and projected future squad. Include Buy Player and Pass buttons."
```

---

# 23. MVP Goal

The first working version should allow this complete flow:

```text
User logs in

↓

Creates league

↓

Sets budget

↓

Sets squad size

↓

Uploads CSV / Excel player list

↓

Reviews players

↓

Starts auction

↓

Selects player

↓

Enters current bid

↓

AI calculates recommended maximum bid

↓

User buys or passes

↓

Budget updates

↓

Squad updates

↓

AI recalculates optimal remaining squad

↓

Auction continues

↓

Squad completed

↓

Final squad report shown
```

The central goal of the application is:

> Help one user build the strongest possible Padel squad while making intelligent bidding decisions under a fixed budget.
