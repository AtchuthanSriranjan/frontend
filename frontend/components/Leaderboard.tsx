import React, { useEffect, useState } from "react";

type Entry = {
  name: string;
  timeSeconds: number;
  size: number;
  boardType: string;
  solvedAt: string;
};

export default function Leaderboard({
  size,
  refreshKey,
}: {
  size: number;
  refreshKey: number;
}) {
  const [entries, setEntries] = useState<Entry[]>([]);
  const API_BASE =
    typeof window !== "undefined" &&
    ["localhost", "127.0.0.1", "::1"].includes(window.location.hostname)
      ? "http://localhost:8080"
      : process.env.NEXT_PUBLIC_API_BASE || "http://localhost:8080";

  useEffect(() => {
    fetch(`${API_BASE}/api/leaderboard/${size}`)
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data)) setEntries(data);
        else setEntries([]);
      })
      .catch(() => setEntries([]));
  }, [size, refreshKey]); // refresh when key changes

  if (entries.length === 0)
    return (
      <p className="text-sm text-gray-500 text-center">
        No records yet for {size}×{size}.
      </p>
    );

  const fmt = (s: number) =>
    `${String(Math.floor(s / 60)).padStart(2, "0")}:${String(s % 60).padStart(
      2,
      "0"
    )}`;

  return (
    <table className="w-full text-sm border-collapse">
      <thead>
        <tr className="border-b border-gray-300">
          <th className="text-left py-1">Name</th>
          <th className="text-center py-1">Time</th>
          <th className="text-center py-1">Type</th>
        </tr>
      </thead>
      <tbody>
        {entries.map((e, i) => (
          <tr key={i} className="border-b border-gray-200">
            <td className="py-1">{e.name}</td>
            <td className="text-center py-1">{fmt(e.timeSeconds)}</td>
            <td className="text-center py-1">{e.boardType}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
