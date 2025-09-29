"use client";

interface ToggleSwitchProps {
  checked: boolean; // true = 재고있음, false = 품절
  onChange: () => void;
  onLabel?: string;
  offLabel?: string;
  onColor?: string;
  offColor?: string;
}

export default function ToggleSwitch({
  checked,
  onChange,
  onLabel = "재고있음",  // true일 때
  offLabel = "품절",     // false일 때
  onColor = "bg-green-500",
  offColor = "bg-gray-300",
}: ToggleSwitchProps) {
  return (
    <div className="flex items-center gap-2">
      <span className="text-sm text-gray-600">
        {checked ? onLabel : offLabel}
      </span>
      <button
        type="button"
        onClick={onChange}
        className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 ${
          checked ? onColor : offColor
        }`}
      >
        <span
          className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform duration-200 ${
            checked ? "translate-x-6" : "translate-x-1"
          }`}
        />
      </button>
    </div>
  );
}