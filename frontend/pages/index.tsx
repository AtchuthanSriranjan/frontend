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
    // start timer when first queen is placed
    if (!isRunning && newBoard.flat().includes("x")) {
      setIsRunning(true);
    }
  };

  // reset board
  const resetBoard = () => {
    setBoard(createEmptyBoard());
    setSeconds(0);
    setIsRunning(false);
    setBestTime(null);
    localStorage.removeItem("bestTime");
  };

  // count queens
  const queenCount = board.flat().filter((c) => c === "queen").length;

  // timer state
  const [seconds, setSeconds] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const [bestTime, setBestTime] = useState<number | null>(null);

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
        setSeconds(0);
        setIsRunning(false);
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

  // Auto-validate board whenever it changes
  useEffect(() => {
    if (regions.length === 0) return;

    const validate = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/validate", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ size, regions, board }),
        });
        const data = await res.json();
        setValidation(data);
        if (
          data.valid &&
          board.flat().filter((c) => c === "queen").length === size
        ) {
          setIsRunning(false);

          // Update best time if this solve is faster or no best time yet
          setBestTime((prevBest) => {
            if (prevBest === null || seconds < prevBest) {
              localStorage.setItem("bestTime", String(seconds));
              return seconds;
            }
            return prevBest;
          });
        }
      } catch (e) {
        console.error("Auto-validate error:", e);
      }
    };

    validate();
  }, [board, regions, size]);

  // timer effect
  useEffect(() => {
    let timer: NodeJS.Timeout | null = null;
    if (isRunning) {
      timer = setInterval(() => setSeconds((s) => s + 1), 1000);
    }
    return () => {
      if (timer) clearInterval(timer);
    };
  }, [isRunning]);

  // Load best time from localStorage on mount
  useEffect(() => {
    const storedBest = localStorage.getItem("bestTime");
    if (storedBest) {
      setBestTime(Number(storedBest));
    }
  }, []);

  return (
    <main className="min-h-screen flex flex-col items-center bg-gray-100 py-10">
      <h1 className="text-4xl font-bold mb-4 text-blue-700">Queens Puzzle</h1>
      <p className="text-gray-700 mb-2 text-center">
        Goal: 1 queen per row, column, and color region.
        <br />
        Tap once → X | Tap twice → Queen | Tap thrice → Empty.
      </p>

      <button
        onClick={resetBoard}
        className="mb-6 px-4 py-2 bg-blue-600 text-white rounded shadow hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-400"
      >
        Reset Board
      </button>
      {validation && (
        <div
          className={`mt-4 mb-6 p-3 rounded text-center w-64
          ${
            validation.valid && queenCount === size
              ? "bg-green-300 text-green-900 font-semibold"
              : validation.valid
              ? "bg-gray-200 text-gray-800"
              : "bg-red-200 text-red-800"
          }`}
        >
          {validation.valid && queenCount === size
            ? "You solved it!"
            : validation.valid
            ? "Board is valid!"
            : "Board has errors."}
        </div>
      )}
      <div className="flex gap-3 mb-6">
        <button
          onClick={() => setBoardType("fixed")}
          className={`px-4 py-2 rounded shadow focus:outline-none focus:ring-2 transition
            ${
              boardType === "fixed"
                ? "bg-blue-600 text-white focus:ring-blue-400"
                : "bg-gray-200 text-black hover:bg-gray-300 focus:ring-gray-400"
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
          className={`px-4 py-2 rounded shadow focus:outline-none focus:ring-2 transition
            ${
              boardType === "random"
                ? "bg-blue-600 text-white focus:ring-blue-400"
                : "bg-gray-200 text-black hover:bg-gray-300 focus:ring-gray-400"
            }`}
        >
          Random Layout
        </button>
      </div>

      {/* Queen count and timer above the board */}
      <div className="flex justify-between items-center w-[22rem] mb-2 text-gray-800">
        <p>
          Queens placed:{" "}
          <span className="font-bold text-blue-700">{queenCount}</span>
        </p>
        <div className="text-right">
          <p>
            Time:{" "}
            <span className="font-bold text-blue-700">
              {String(Math.floor(seconds / 60)).padStart(2, "0")}:
              {String(seconds % 60).padStart(2, "0")}
            </span>
          </p>
          {bestTime !== null && (
            <p className="text-sm text-gray-600">
              Best:{" "}
              <span className="font-semibold text-emerald-700">
                {String(Math.floor(bestTime / 60)).padStart(2, "0")}:
                {String(bestTime % 60).padStart(2, "0")}
              </span>
            </p>
          )}
        </div>
      </div>

      {/* conditional rendering */}
      {regions.length > 0 ? (
        <div
          className="grid gap-1"
          style={{ gridTemplateColumns: `repeat(${size}, 3rem)` }}
        >
          {board.map((row, i) =>
            row.map((cell, j) => {
              const regionId = regions[i][j];
              const baseColor = regionColors[regionId];

              const isInvalidCell =
                validation &&
                !validation.valid &&
                (validation.invalidRows.includes(i) ||
                  validation.invalidCols.includes(j) ||
                  validation.invalidRegions.includes(regionId) ||
                  validation.diagonalConflicts.some(
                    ([r, c]) => r === i && c === j
                  ));

              return (
                <div
                  key={`${i}-${j}`}
                  onClick={() => toggleCell(i, j)}
                  className={`relative w-12 h-12 flex items-center justify-center cursor-pointer font-bold transition
                  ${baseColor} border border-gray-400
                `}
                >
                  {cell === "queen" && (
                    <span className="text-black text-xl">♛</span>
                  )}
                  {cell === "x" && (
                    <span className="text-black text-lg">X</span>
                  )}

                  {/* Overlay diagonal red stripes for ANY violation */}
                  {isInvalidCell && (
                    <div
                      className="absolute inset-0 pointer-events-none rounded"
                      style={{
                        backgroundImage:
                          "repeating-linear-gradient(135deg, rgba(239,68,68,0.65) 0px, rgba(239,68,68,0.65) 8px, transparent 8px, transparent 16px)",
                      }}
                    />
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
