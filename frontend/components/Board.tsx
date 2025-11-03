import React from "react";
import Cell from "./Cell";

type Props = {
  board: ("empty" | "x" | "queen")[][];
  regions: number[][];
  validation: any;
  toggleCell: (i: number, j: number) => void;
};

export default function Board({
  board,
  regions,
  validation,
  toggleCell,
}: Props) {
  const regionColors: Record<number, string> = {
    0: "bg-purple-300",
    1: "bg-orange-200",
    2: "bg-green-200",
    3: "bg-blue-200",
    4: "bg-slate-200",
    5: "bg-red-300",
    6: "bg-yellow-200",
    7: "bg-pink-200",
    8: "bg-teal-200",
  };

  const size = board.length;

  return (
    <div
      className="grid gap-1"
      style={{ gridTemplateColumns: `repeat(${size}, 3rem)` }}
    >
      {board.map((row, i) =>
        row.map((cell, j) => (
          <Cell
            key={`${i}-${j}`}
            i={i}
            j={j}
            cell={cell}
            color={regionColors[regions[i]?.[j] ?? 0]}
            validation={validation}
            toggleCell={toggleCell}
            regionId={regions[i]?.[j] ?? 0}
          />
        ))
      )}
    </div>
  );
}
