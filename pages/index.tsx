// Import React
import React, { useState } from "react";

// interactive 7x7 "Queens Puzzle" board.
// Users can click on cells to toggle queens on or off.
export default function Home() {
  // Define the grid size 7x7 board
  const size = 7;

  // Initialize a 2D boolean array to represent the board.
  // Each cell is initially set to "false" meaning: no queen.
  const [board, setBoard] = useState<boolean[][]>(
    Array.from({ length: size }, () => Array(size).fill(false))
  );

  // Cell Toggle Function
  // Flips the cell's boolean value (true → false or false → true)
  const toggleCell = (row: number, col: number) => {
    const newBoard = board.map((r, i) =>
      r.map((c, j) => (i === row && j === col ? !c : c))
    );
    // Update the state with the new board
    setBoard(newBoard);
  };

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

      {/* --- Game Board Container --- */}
      {/* Use CSS grid to arrange cells into a 7x7 grid */}
      <div
        className="grid gap-1"
        style={{
          // Define number of columns based on size
          gridTemplateColumns: `repeat(${size}, 3rem)`,
        }}
      >
        {/* --- Render each row and cell --- */}
        {board.map((row, i) =>
          row.map((cell, j) => (
            <div
              key={`${i}-${j}`} // Unique key for each cell
              onClick={() => toggleCell(i, j)} // Toggle cell on click
              className={`w-12 h-12 flex items-center justify-center border border-gray-400 cursor-pointer transition
                ${
                  cell
                    ? "bg-red-400 text-white font-bold" // Active cell → red background + "Q"
                    : "bg-green-200 hover:bg-green-300" // Empty cell → light green, darker on hover
                }
              `}
            >
              {/* Display a "Q" (Queen) only if cell is active */}
              {cell ? "Q" : ""}
            </div>
          ))
        )}
      </div>
    </main>
  );
}
