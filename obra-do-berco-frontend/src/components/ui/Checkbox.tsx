import { type InputHTMLAttributes, forwardRef } from "react";
import { cn } from "@/lib/utils";

interface CheckboxProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
}

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(function Checkbox(
  { className, label, id, ...props },
  ref,
) {
  const checkboxId = id ?? props.name;

  return (
    <label htmlFor={checkboxId} className="flex items-center gap-2 text-sm text-slate-700">
      <input
        ref={ref}
        id={checkboxId}
        type="checkbox"
        className={cn(
          "size-4 rounded border-slate-300 text-brand-600 focus:ring-2 focus:ring-brand-400",
          className,
        )}
        {...props}
      />
      {label}
    </label>
  );
});
