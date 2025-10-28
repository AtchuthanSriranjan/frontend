// Import React
import React, { useState } from "react";

// Define allowed cell states
type CellState = "empty" | "x" | "queen";

// interactive 7x7 "Queens Puzzle" board.
// Users can click on cells to toggle queens on or off.
export default function Home() {
  // Define the grid size 7x7 board
  const size = 7;

  // Helper to create a fresh empty board
  const createEmptyBoard = (): CellState[][] =>
    Array.from({ length: size }, () => Array(size).fill("empty"));

  // Initialize board state
  const [board, setBoard] = useState<CellState[][]>(createEmptyBoard());

  // Cycle through states: empty → X → Queen → empty
  const toggleCell = (row: number, col: number) => {
    const newBoard = board.map((r, i) =>
      r.map((c, j) => {
        if (i === row && j === col) {
          if (c === "empty") return "x";
          if (c === "x") return "queen";
          return "empty";
        }
        return c;
      })
    );
    setBoard(newBoard);
  };

  // Reset Function
  // Clears the board back to all false
  const resetBoard = () => {
    setBoard(createEmptyBoard());
  };

  // Count how many queens are currently on the board
  const queenCount = board.flat().filter((c) => c === "queen").length;

  return (
    // === Page Layout Container ===
    <main className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      {/* --- Header --- */}
      {/* Large title centered at the top */}
      <h1 className="text-4xl font-bold mb-4 text-blue-700">Queens Puzzle</h1>

      {/* --- Description text --- */}
      <p className="text-gray-700 mb-6">
        Click on a square to place/remove a queen 👑
      </p>

      {/* --- Queen Counter --- */}
      <p className="text-gray-800 mb-2">
        Queens placed:{" "}
        <span className="font-bold text-blue-700">{queenCount}</span>
      </p>

      {/* --- Controls --- */}
      <button
        onClick={resetBoard}
        className="mb-6 px-4 py-2 bg-blue-600 text-white rounded shadow hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-400"
      >
        Reset Board
      </button>

      {/* --- Game Board Container --- */}
      {/* Use CSS grid to arrange cells into a 7x7 grid */}
      <div
        className="grid gap-1"
        style={{
          // Define number of columns based on size
          gridTemplateColumns: `repeat(${size}, 3rem)`,
        }}
      >
        {board.map((row, i) =>
          row.map((cell, j) => (
            <div
              key={`${i}-${j}`}
              onClick={() => toggleCell(i, j)}
              className={`w-12 h-12 flex items-center justify-center border border-gray-400 cursor-pointer transition font-bold
                ${
                  cell === "queen"
                    ? "bg-red-400 text-white"
                    : cell === "x"
                    ? "bg-green-200 text-gray-700"
                    : "bg-green-200 hover:bg-green-300"
                }
              `}
            >
              {cell === "queen" ? "Q" : cell === "x" ? "X" : ""}
            </div>
          ))
        )}
      </div>
    </main>
  );
}
