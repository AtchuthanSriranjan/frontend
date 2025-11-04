import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import Controls from "../components/Controls";
import ValidationMessage from "../components/ValidationMessage";
import Board from "../components/Board";
import Leaderboard from "../components/Leaderboard";

// Use environment variable if available (for deployment on Vercel)
const API_BASE = process.env.NEXT_PUBLIC_API_BASE || "http://localhost:8080";

export default function Home() {
  const [size, setSize] = useState(7);
  const [boardType, setBoardType] = useState("fixed");
  // Board holds cells of type: "empty" | "x" | "queen"
  const [board, setBoard] = useState<("empty" | "x" | "queen")[][]>([]);
  const [regions, setRegions] = useState<number[][]>([]);
  const [validation, setValidation] = useState<any>(null);
  const [queenCount, setQueenCount] = useState(0);
  const [seconds, setSeconds] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const [bestTime, setBestTime] = useState<number | null>(null);
  const [refreshKey, setRefreshKey] = useState(0);
  const [leaderboardKey, setLeaderboardKey] = useState(0);

  // Timer logic
  useEffect(() => {
    let timer: NodeJS.Timeout;
    if (isRunning) {
      timer = setInterval(() => setSeconds((prev) => prev + 1), 1000);
    }
    return () => clearInterval(timer);
  }, [isRunning]);

  // Load best time from localStorage
  useEffect(() => {
    const key = `bestTime_${size}_${boardType}`;
    const stored = localStorage.getItem(key);
    setBestTime(stored ? Number(stored) : null);
  }, [size, boardType]);

  // Fetch board layout (fixed or random)
  useEffect(() => {
    const endpoint =
      boardType === "fixed"
        ? `${API_BASE}/api/board?size=${size}`
        : `${API_BASE}/api/random-board?size=${size}`;

    fetch(endpoint)
      .then((res) => res.json())
      .then((data) => {
        setRegions(data.regions);
        setBoard(createEmptyBoard(data.size));
        setSeconds(0);
        setIsRunning(false);
        setValidation(null);
        setQueenCount(0);
      })
      .catch((err) => console.error("Error fetching board:", err));
  }, [boardType, size, refreshKey]);

  // Validation whenever board changes
  useEffect(() => {
    if (regions.length === 0) return;

    fetch(`${API_BASE}/api/validate`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ board, regions, size }),
    })
      .then((res) => res.json())
      .then((data) => {
        setValidation(data);

        const placedQueens = board.flat().filter((c) => c === "queen").length;
        setQueenCount(placedQueens);

        // Start timer when first queen placed
        if (placedQueens > 0 && !isRunning) setIsRunning(true);

        // Stop timer when solved
        if (data.valid && placedQueens === size) {
          setIsRunning(false);

          // Save leaderboard entry
          const name =
            prompt("Enter your name for the leaderboard:", "Guest") || "Guest";

          fetch(`${API_BASE}/api/leaderboard`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              name,
              timeSeconds: seconds,
              size,
              boardType,
            }),
          })
            .then(() => setLeaderboardKey((k) => k + 1))
            .catch((err) => console.error("Error saving leaderboard:", err));

          // Update best time
          setBestTime((prevBest) => {
            if (prevBest === null || seconds < prevBest) {
              localStorage.setItem(
                `bestTime_${size}_${boardType}`,
                String(seconds)
              );
              return seconds;
            }
            return prevBest;
          });
        }
      })
      .catch((err) => console.error("Validation error:", err));
  }, [board, regions]);

  // Create empty board
  function createEmptyBoard(size: number): ("empty" | "x" | "queen")[][] {
    return Array(size)
      .fill(null)
      .map(() => Array<"empty" | "x" | "queen">(size).fill("empty"));
  }

  // Reset board
  function resetBoard() {
    setBoard(createEmptyBoard(size));
    setSeconds(0);
    setIsRunning(false);
    setValidation(null);
    setQueenCount(0);
  }

  // Cell toggle handler
  function toggleCell(i: number, j: number) {
    setBoard((prev) => {
      const newBoard = prev.map((row) => [...row]);
      newBoard[i][j] =
        newBoard[i][j] === "empty"
          ? "x"
          : newBoard[i][j] === "x"
          ? "queen"
          : "empty";
      return newBoard;
    });
  }

  return (
    <main className="min-h-screen flex flex-col items-center bg-gray-100 py-10">
      <Header />

      <Controls
        size={size}
        setSize={setSize}
        boardType={boardType}
        setBoardType={setBoardType}
        resetBoard={resetBoard}
        seconds={seconds}
        bestTime={bestTime}
        queenCount={queenCount}
        setRefreshKey={setRefreshKey}
      >
        <ValidationMessage
          validation={validation}
          queenCount={queenCount}
          size={size}
        />
      </Controls>

      <div className="relative flex justify-center w-full">
        {regions.length > 0 ? (
          <>
            <div className="flex justify-center w-fit">
              <Board
                board={board}
                regions={regions}
                validation={validation}
                toggleCell={toggleCell}
              />
            </div>
            <div
              className={`absolute top-0 w-64 text-gray-800 ${
                size === 7
                  ? "left-[calc(50%+15rem)]"
                  : size === 8
                  ? "left-[calc(50%+17rem)]"
                  : "left-[calc(50%+19rem)]"
              }`}
            >
              <h2 className="text-lg font-semibold mb-2 text-center text-blue-700">
                Fastest Times (Size {size})
              </h2>
              <Leaderboard size={size} refreshKey={leaderboardKey} />
            </div>
          </>
        ) : (
          <p>Loading board...</p>
        )}
      </div>
    </main>
  );
}
