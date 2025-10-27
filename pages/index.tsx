import React from "react";

export default function Home() {
  return (
    <main className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <h1 className="text-4xl font-bold mb-4">Queens Puzzle</h1>
      <p className="text-gray-700 mb-8">Atchus frontend is running 🚀</p>

      <div className="grid grid-cols-7 gap-1">
        {Array.from({ length: 49 }).map((_, i) => (
          <div
            key={i}
            className="w-12 h-12 bg-green-200 flex items-center justify-center border border-green-400"
          >
            {/* Empty cell for now */}
          </div>
        ))}
      </div>
    </main>
  );
}
