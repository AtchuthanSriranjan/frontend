# Queens Puzzle (Next.js + Java Spring Boot)

An interactive logic puzzle inspired by the "LinkedIn Queens" game.  
Built to demonstrate full-stack software-engineering skills.

---

## Goal

Place exactly **one queen** in each **row**, **column**, and **color region**,  
while ensuring **no two queens touch**, not even diagonally.

---

## Features (so far)

- 7×7 interactive board
- Colored regions (data-driven)
- Three-state cells: _Empty → X → Queen_
- Reset button + queen counter
- Responsive design

---

## Planned next steps

- [ ] Add **region outlines** for better visualization
- [ ] Implement **rule validation** (rows, columns, regions, no-touch)
- [ ] Create **Java Spring Boot backend**
  - `/api/puzzles` — fetch puzzle layouts
  - `/api/validate` — check board validity
- [ ] Integrate **frontend <-> backend** communication
- [ ] Deploy frontend (Vercel) + backend
- [ ] Generate random boards
- [ ] Write Solver

---

## Tech Stack

**Frontend:** Next.js (TypeScript) + Tailwind CSS  
**Backend:** Java 17 + Spring Boot (planned)  
**Hosting:** Vercel + Render/Railway  
**Version Control:** GitHub

---

## Run locally

```bash
# Frontend
cd frontend
npm install
npm run dev
```
