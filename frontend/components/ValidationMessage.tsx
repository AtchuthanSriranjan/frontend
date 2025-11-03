import React from "react";

type Props = {
  validation: any;
  queenCount: number;
  size: number;
};

export default function ValidationMessage({
  validation,
  queenCount,
  size,
}: Props) {
  if (!validation) return null;

  const totalQueens = size;
  const solved =
    validation.valid &&
    queenCount === totalQueens &&
    validation.invalidRows.length === 0;

  const baseClass = "text-center font-semibold mb-4";

  if (solved)
    return <p className={`${baseClass} text-green-600`}>You solved it!</p>;
  if (validation.valid)
    return (
      <p className={`${baseClass} text-gray-700`}>Board is valid so far</p>
    );
  return <p className={`${baseClass} text-red-600`}>Board has errors</p>;
}
