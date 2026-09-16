function Logo({ size = 40 }) {
  return (
    <svg width={size} height={size} viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg">
      <defs>
        <linearGradient id="badgeGrad" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" stopColor="#14294d" />
          <stop offset="100%" stopColor="#2f6fed" />
        </linearGradient>
      </defs>
      <circle cx="100" cy="100" r="98" fill="url(#badgeGrad)" />
      <path
        d="M 70 70 L 58 40 L 74 58 L 66 28 L 84 52 L 80 24 L 96 50
           L 100 20 L 104 50 L 120 24 L 116 52 L 134 28 L 126 58 L 142 40 L 130 70 Z"
        fill="#f4f7fb"
      />
      <circle cx="100" cy="82" r="26" fill="#f4f7fb" />
      <circle cx="89" cy="82" r="9" fill="none" stroke="#14294d" strokeWidth="4" />
      <circle cx="111" cy="82" r="9" fill="none" stroke="#14294d" strokeWidth="4" />
      <line x1="98" y1="82" x2="102" y2="82" stroke="#14294d" strokeWidth="4" />
      <ellipse cx="100" cy="95" rx="7" ry="5" fill="#14294d" />
      <path d="M 66 190 Q 66 138 100 128 Q 134 138 134 190 Z" fill="#f4f7fb" />
      <line x1="100" y1="128" x2="100" y2="190" stroke="#14294d" strokeWidth="3" />
      <path d="M 70 150 Q 50 140 40 108" stroke="#f4f7fb" strokeWidth="10" fill="none" strokeLinecap="round" />
      <circle cx="38" cy="102" r="11" fill="#e8b84b" />
      <path d="M 130 150 Q 150 140 160 108" stroke="#f4f7fb" strokeWidth="10" fill="none" strokeLinecap="round" />
      <circle cx="162" cy="102" r="11" fill="#e8b84b" />
    </svg>
  );
}

export default Logo;
