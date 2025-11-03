import React from "react";

type Props = {
  size: number;
  setSize: (s: number) => void;
  boardType: string;
  setBoardType: (type: string) => void;
  resetBoard: () => void;
  seconds: number;
  bestTime: number | null;
  queenCount: number;
  setRefreshKey: React.Dispatch<React.SetStateAction<number>>;
  children?: React.ReactNode;
};

export default function Controls({
  size,
  setSize,
  boardType,
  setBoardType,
  resetBoard,
  seconds,
  bestTime,
  queenCount,
  setRefreshKey,
  children,
}: Props) {
  const formatTime = (s: number) =>
    `${String(Math.floor(s / 60)).padStart(2, "0")}:${String(s % 60).padStart(
      2,
      "0"
    )}`;

  return (
    <div className="flex flex-col items-center mb-6">
      {/* Size dropdown */}
      <div className="mb-4">
        <label className="mr-2 font-semibold text-gray-700">Board size:</label>
        <select
          value={size}
          onChange={(e) => setSize(Number(e.target.value))}
          className="px-2 py-1 border border-gray-400 rounded text-black bg-white shadow-sm hover:bg-gray-50 focus:ring-2 focus:ring-blue-300"
        >
          <option value={7}>7 × 7</option>
          <option value={8}>8 × 8</option>
          <option value={9}>9 × 9</option>
        </select>
      </div>

      {/* Layout buttons */}
      <div className="flex gap-3 mb-4">
        <button
          onClick={() => {
            if (boardType === "fixed") {
              // pressing again does nothing, or you could reload same layout if you prefer
              setRefreshKey((prev) => prev + 1);
            } else {
              setBoardType("fixed");
            }
          }}
          className={`px-4 py-2 rounded shadow focus:outline-none focus:ring-2 ${
            boardType === "fixed"
              ? "bg-blue-600 text-white focus:ring-blue-400"
              : "bg-gray-200 hover:bg-gray-300 text-black"
          }`}
        >
          Fixed Layout
        </button>

        <button
          onClick={() => {
            if (boardType === "random") {
              // pressing again = new random layout
              setRefreshKey((prev) => prev + 1);
            } else {
              setBoardType("random");
            }
          }}
          className={`px-4 py-2 rounded shadow focus:outline-none focus:ring-2 ${
            boardType === "random"
              ? "bg-blue-600 text-white focus:ring-blue-400"
              : "bg-gray-200 hover:bg-gray-300 text-black"
          }`}
        >
          Random Layout
        </button>
      </div>

      {/* Reset button */}
      <button
        onClick={resetBoard}
        className="mt-4 px-4 py-2 bg-emerald-600 text-white rounded shadow hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-emerald-400"
      >
        Reset Board
      </button>

      {/* place ValidationMessage here */}
      {children}

      {/* Timer and counters */}
      <div className="flex justify-between w-[22rem] text-gray-800">
        <p>
          Queens placed:{" "}
          <span className="font-bold text-blue-700">{queenCount}</span>
        </p>
        <div className="text-right">
          <p>
            Time:{" "}
            <span className="font-bold text-blue-700">
              {formatTime(seconds)}
            </span>
          </p>
          {bestTime !== null && (
            <p className="text-sm text-gray-600">
              Best:{" "}
              <span className="font-semibold text-emerald-700">
                {formatTime(bestTime)}
              </span>
            </p>
          )}
        </div>
      </div>
    </div>
  );
}
