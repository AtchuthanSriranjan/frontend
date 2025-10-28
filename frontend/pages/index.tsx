import React, { useState, useEffect } from "react";

// Define cell types
type CellState = "empty" | "x" | "queen";

export default function Home() {
  // Board configuration from backend
  const [size, setSize] = useState<number>(7);
  const [regions, setRegions] = useState<number[][]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/random-board")
      //fetch("http://localhost:8080/api/board")
      .then((res) => res.json())
      .then((data) => {
        setSize(data.size);
        setRegions(data.regions);
      })
      .catch((err) => console.error("Error fetching board:", err));
  }, []);

  // colormapping for regions
  const regionColors: Record<number, string> = {
    0: "bg-purple-300",
    1: "bg-orange-200",
    2: "bg-green-200",
    3: "bg-blue-200",
    4: "bg-slate-200",
    5: "bg-red-300",
    6: "bg-yellow-200",
  };

  // create an empty board
  const createEmptyBoard = (): CellState[][] =>
    Array.from({ length: size }, () => Array(size).fill("empty"));

  // board state
  const [board, setBoard] = useState<CellState[][]>(createEmptyBoard());

  // board type state (fixed or random)
  const [boardType, setBoardType] = useState<"fixed" | "random">("fixed");

  // refresh key to force reload
  const [refreshKey, setRefreshKey] = useState(0);

  // reset board when size changes
  useEffect(() => {
    setBoard(createEmptyBoard());
  }, [size]);

  // cell toggle logic/cycle
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

  // reset board
  const resetBoard = () => setBoard(createEmptyBoard());

  // count queens
  const queenCount = board.flat().filter((c) => c === "queen").length;

  // message from backend
  const [backendMessage, setBackendMessage] = useState<string>("");

  useEffect(() => {
    const endpoint =
      boardType === "fixed"
        ? "http://localhost:8080/api/board"
        : "http://localhost:8080/api/random-board";

    fetch(`${endpoint}?t=${Date.now()}`)
      .then((res) => res.json())
      .then((data) => {
        setSize(data.size);
        setRegions(data.regions);
        setBoard(createEmptyBoard());
      })
      .catch((err) => console.error("Error fetching board:", err));
  }, [boardType, refreshKey]);

  // validation state
  const [validation, setValidation] = useState<null | {
    valid: boolean;
    invalidRows: number[];
    invalidCols: number[];
    invalidRegions: number[];
    diagonalConflicts: number[][];
  }>(null);

  // validate board with backend
  const checkBoard = async () => {
    try {
      setChecking(true);
      const res = await fetch("http://localhost:8080/api/validate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          size,
          regions,
          board,
        }),
      });
      const data = await res.json();
      setValidation(data);
    } catch (e) {
      console.error("Validate error:", e);
      setValidation(null);
    } finally {
      setChecking(false);
    }
  };

  const [checking, setChecking] = useState(false);

  return (
    <main className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <h1 className="text-4xl font-bold mb-4 text-blue-700">Queens Puzzle</h1>
      <p className="text-gray-700 mb-2 text-center">
        Goal: 1 queen per row, column, and color region.
        <br />
        Tap once → X | Tap twice → Queen | Tap thrice → Empty.
      </p>
      <p className="text-gray-800 mb-4">
        Queens placed:{" "}
        <span className="font-bold text-blue-700">{queenCount}</span>
      </p>
      <button
        onClick={resetBoard}
        className="mb-6 px-4 py-2 bg-blue-600 text-white rounded shadow hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-400"
      >
        Reset Board
      </button>

      <button
        onClick={checkBoard}
        className="mb-4 px-4 py-2 bg-emerald-600 text-white rounded shadow hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-400"
        disabled={checking || regions.length === 0 || queenCount === 0}
      >
        {checking ? "Checking..." : "Check Board"}
      </button>

      <div className="flex gap-3 mb-6">
        <button
          onClick={() => setBoardType("fixed")}
          className={`px-4 py-2 rounded shadow focus:outline-none focus:ring-2 ${
            boardType === "fixed"
              ? "bg-blue-600 text-white focus:ring-blue-400"
              : "bg-gray-200 hover:bg-gray-300"
          }`}
        >
          Fixed Layout
        </button>

        <button
          onClick={() => {
            if (boardType === "random") {
              // already in random mode → just refresh
              setRefreshKey((k) => k + 1);
            } else {
              // switch to random mode
              setBoardType("random");
            }
          }}
          className={`px-4 py-2 rounded shadow focus:outline-none focus:ring-2 ${
            boardType === "random"
              ? "bg-blue-600 text-white focus:ring-blue-400"
              : "bg-gray-200 hover:bg-gray-300"
          }`}
        >
          Random Layout
        </button>


      {validation && (
        <div className="mb-6 text-sm text-gray-800 max-w-md">
          {validation.valid ? (
            <div className="font-semibold text-emerald-700">
              Board is valid.
            </div>
          ) : (
            <>
              <div className="font-semibold text-red-700">
                Board is invalid.
              </div>
              {validation.invalidRows.length > 0 && (
                <div>
                  Invalid rows:{" "}
                  {validation.invalidRows.map((r) => r + 1).join(", ")}
                </div>
              )}
              {validation.invalidCols.length > 0 && (
                <div>
                  Invalid columns:{" "}
                  {validation.invalidCols.map((c) => c + 1).join(", ")}
                </div>
              )}
              {validation.invalidRegions.length > 0 && (
                <div>
                  Invalid regions: {validation.invalidRegions.join(", ")}
                </div>
              )}
              {validation.diagonalConflicts.length > 0 && (
                <div>
                  Adjacent conflicts at:{" "}
                  {validation.diagonalConflicts
                    .map(([r, c]) => `(${r + 1},${c + 1})`)
                    .join(", ")}
                </div>
              )}
            </>
          )}
        </div>
      )}
      {/* Step 4 — conditional rendering */}
      {regions.length > 0 ? (
        <div
          className="grid gap-1"
          style={{ gridTemplateColumns: `repeat(${size}, 3rem)` }}
        >
          {board.map((row, i) =>
            row.map((cell, j) => {
              const regionId = regions[i][j];
              const baseColor = regionColors[regionId];

              return (
                <div
                  key={`${i}-${j}`}
                  onClick={() => toggleCell(i, j)}
                  className={`relative w-12 h-12 flex items-center justify-center border border-gray-400 cursor-pointer font-bold transition ${baseColor}`}
                >
                  {cell === "queen" && (
                    <span className="text-black text-lg">Q</span>
                  )}
                  {cell === "x" && (
                    <span className="text-black text-lg">X</span>
                  )}
                </div>
              );
            })
          )}
        </div>
      ) : (
        <p>Loading board...</p>
      )}
    </main>
  );
}
