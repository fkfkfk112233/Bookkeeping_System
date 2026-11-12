// Shared category colors keep the category list and pie charts visually consistent.
export const CATEGORY_PALETTE = [
  "#4F7DF3", // Blue
  "#9671E8", // Purple
  "#F5A64A", // Orange
  "#32B889", // Green
  "#F16B72", // Coral
  "#28A9B8", // Teal
  "#D77BC7", // Pink
  "#8A9A5B", // Olive
  "#E58B5B", // Terracotta
  "#6D91B8", // Steel blue
  "#B07AA1", // Mauve
  "#59A14F", // Leaf green
  "#EDC948", // Mustard
  "#76B7B2", // Seafoam
  "#FF9DA7", // Soft rose
  "#9C755F", // Brown
  "#BAB0AC", // Warm gray
  "#86BCB6", // Muted teal
  "#AF7AA1", // Orchid
  "#FFBE7D", // Apricot
  "#A0CBE8", // Sky blue
  "#FF9D9A", // Salmon
  "#B6992D", // Gold
];

export function getCategoryColor(categoryName = "") {
  const normalizedName = String(categoryName).trim();

  if (!normalizedName) {
    return "#94A3B8";
  }

  // Stable hash: the same category always receives the same color,
  // regardless of API response order or chart filtering.
  let hash = 0;
  for (let index = 0; index < normalizedName.length; index += 1) {
    hash = (hash * 31 + normalizedName.charCodeAt(index)) >>> 0;
  }

  return CATEGORY_PALETTE[hash % CATEGORY_PALETTE.length];
}
