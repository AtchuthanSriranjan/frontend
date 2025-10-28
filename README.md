# Queens Puzzle (Next.js + Java Spring Boot)

An interactive logic puzzle inspired by the "LinkedIn Queens" game.  
Built to demonstrate full-stack software-engineering skills, from frontend interactivity to backend validation.

---

## Goal

Place exactly one queen in each row, column, and color region,  
while ensuring no two queens touch, not even diagonally.

---

## Features (so far)

- 7×7 interactive board  
- Colored regions (hardcoded layout)  
- Three-state cells: Empty → X → Queen  
- Reset button + queen counter  
- Responsive design (mobile-friendly layout)

---

## Planned Next Steps

- [ ] Add region outlines for better visualization  
- [ ] Implement rule validation (rows, columns, regions, no-touch rule)  
- [ ] Create Java Spring Boot backend  
  - `/api/puzzles` — serve puzzle layouts  
  - `/api/validate` — check board validity  
- [ ] Integrate frontend ↔ backend communication via REST API  
- [ ] Deploy frontend (Vercel) + backend (Render or Railway)  
- [ ] Generate random puzzle boards  
- [ ] Write a solver algorithm

---

## Tech Stack

**Frontend:** Next.js (TypeScript) + Tailwind CSS  
**Backend:** Java 17 + Spring Boot (planned)  
**Hosting:** Vercel (frontend) + Render/Railway (backend)  
**Version Control:** GitHub

---

## Project Structure

