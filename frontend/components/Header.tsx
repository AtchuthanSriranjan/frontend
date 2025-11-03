import React from "react";

export default function Header() {
  return (
    <header className="text-center mb-6">
      <h1 className="text-4xl font-bold text-blue-700 mb-2">Queens Puzzle</h1>
      <p className="text-gray-600">
        Place exactly one queen in each row, column, and region – no two queens
        may attack each other.
      </p>
    </header>
  );
}
