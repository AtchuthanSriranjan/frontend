import React from "react";

type Props = {
  i: number;
  j: number;
  cell: "empty" | "x" | "queen";
  color: string;
  validation: any;
  toggleCell: (i: number, j: number) => void;
  regionId: number;
};

export default function Cell({
  i,
  j,
  cell,
  color,
  validation,
  toggleCell,
  regionId,
}: Props) {
  const isInvalidCell =
    validation &&
    !validation.valid &&
    (validation.invalidRows.includes(i) ||
      validation.invalidCols.includes(j) ||
      validation.invalidRegions.includes(regionId) ||
      validation.diagonalConflicts.some(
        ([r, c]: [number, number]) => r === i && c === j
      ));

  return (
    <div
      onClick={() => toggleCell(i, j)}
      className={`relative w-12 h-12 flex items-center justify-center cursor-pointer font-bold transition ${color} border border-gray-400`}
    >
      {cell === "queen" && <span className="text-black text-xl">♛</span>}
      {cell === "x" && <span className="text-black text-lg">X</span>}
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
}
