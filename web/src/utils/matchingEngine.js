export function computeSmartMatches(items) {
  const lostItems = items.filter(item => item.type === "LOST" && item.status === "OPEN");
  const foundItems = items.filter(item => item.type === "FOUND" && item.status === "OPEN");

  const matches = [];

  for (const lost of lostItems) {
    for (const found of foundItems) {
      const { score, reasons } = calculateMatchScore(lost, found);
      if (score >= 0.35) {
        matches.push({
          id: `match_${lost.id}_${found.id}`,
          lostItem: lost,
          foundItem: found,
          confidenceScore: score,
          reasons: reasons,
          status: "PENDING"
        });
      }
    }
  }

  return matches.sort((a, b) => b.confidenceScore - a.confidenceScore);
}

function calculateMatchScore(lost, found) {
  let score = 0;
  const reasons = [];

  // 1. Category Exact Match (0.35)
  if (lost.category.toLowerCase() === found.category.toLowerCase()) {
    score += 0.35;
    reasons.push(`Category match (${lost.category})`);
  }

  // 2. Location Proximity (0.35)
  if (lost.location.toLowerCase() === found.location.toLowerCase()) {
    score += 0.35;
    reasons.push(`Exact location match (${lost.location})`);
  } else if (
    lost.location.toLowerCase().includes(found.location.toLowerCase()) ||
    found.location.toLowerCase().includes(lost.location.toLowerCase())
  ) {
    score += 0.20;
    reasons.push(`Nearby location area (${lost.location} / ${found.location})`);
  }

  // 3. Keyword Overlap (0.20)
  const lostWords = extractKeywords(`${lost.title} ${lost.description}`);
  const foundWords = extractKeywords(`${found.title} ${found.description}`);

  if (lostWords.size > 0 && foundWords.size > 0) {
    const common = Array.from(lostWords).filter(w => foundWords.has(w));
    const unionSize = new Set([...lostWords, ...foundWords]).size;
    if (unionSize > 0) {
      const jaccard = common.length / unionSize;
      score += Math.min(0.20, jaccard * 0.5);
      if (common.length > 0) {
        reasons.push(`Keywords match: ${common.slice(0, 3).join(", ")}`);
      }
    }
  }

  // 4. Date Proximity (0.10)
  const hoursDiff = Math.abs(lost.dateReported - found.dateReported) / (1000 * 3600);
  if (hoursDiff <= 24) {
    score += 0.10;
    reasons.push("Reported within 24 hours of each other");
  } else if (hoursDiff <= 72) {
    score += 0.05;
    reasons.push("Reported within 3 days");
  }

  const finalScore = Math.min(1.0, score);
  return {
    score: finalScore,
    reasons: reasons.length > 0 ? reasons.join(" • ") : "General similarity"
  };
}

function extractKeywords(text) {
  const stopWords = new Set([
    "a", "an", "the", "and", "or", "in", "on", "at", "to", "for", "with",
    "is", "of", "my", "was", "near", "found", "lost", "has", "it", "this", "room", "card"
  ]);
  const words = text
    .toLowerCase()
    .replace(/[^a-z0-9\s]/g, "")
    .split(/\s+/)
    .filter(w => w.length > 2 && !stopWords.has(w));
  return new Set(words);
}
