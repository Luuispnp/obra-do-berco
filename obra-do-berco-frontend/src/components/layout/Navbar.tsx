import { NavLink, useNavigate } from "react-router-dom";
import { Baby, CalendarDays, ClipboardList, LayoutDashboard, LogOut, Users } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";
import { cn } from "@/lib/utils";

const navItems = [
  { to: "/app", label: "Painel", icon: LayoutDashboard, end: true },
  { to: "/app/gestantes", label: "Gestantes", icon: Baby },
  { to: "/app/solicitacoes", label: "Solicitações", icon: ClipboardList },
  { to: "/app/eventos", label: "Eventos", icon: CalendarDays },
  { to: "/app/voluntarios", label: "Voluntários", icon: Users },
];

export function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  async function handleLogout() {
    await logout();
    navigate("/login", { replace: true });
  }

  return (
    <header className="sticky top-0 z-40 border-b border-slate-200 bg-white/90 backdrop-blur">
      <div className="mx-auto flex h-16 max-w-7xl items-center justify-between gap-4 px-4 sm:px-6">
        <div className="flex items-center gap-8">
          <NavLink to="/app" className="flex items-center gap-2 font-semibold text-brand-700">
            <span className="flex size-8 items-center justify-center rounded-full bg-brand-100">
              <Baby className="size-4 text-brand-600" />
            </span>
            <span className="hidden sm:inline">Obra do Berço</span>
          </NavLink>

          <nav className="hidden items-center gap-1 md:flex">
            {navItems.map(({ to, label, icon: Icon, end }) => (
              <NavLink
                key={to}
                to={to}
                end={end}
                className={({ isActive }) =>
                  cn(
                    "flex items-center gap-1.5 rounded-lg px-3 py-2 text-sm font-medium transition-colors",
                    isActive ? "bg-brand-100 text-brand-800" : "text-slate-600 hover:bg-slate-100",
                  )
                }
              >
                <Icon className="size-4" />
                {label}
              </NavLink>
            ))}
          </nav>
        </div>

        <div className="flex items-center gap-3">
          <div className="hidden text-right sm:block">
            <p className="text-sm font-medium text-slate-700">{user?.nome}</p>
            <p className="text-xs capitalize text-slate-400">{user?.role}</p>
          </div>
          <button
            type="button"
            onClick={() => void handleLogout()}
            className="flex size-9 items-center justify-center rounded-lg text-slate-500 hover:bg-slate-100 hover:text-slate-700"
            aria-label="Sair"
            title="Sair"
          >
            <LogOut className="size-4" />
          </button>
        </div>
      </div>

      <nav className="flex items-center gap-1 overflow-x-auto border-t border-slate-100 px-4 py-2 md:hidden">
        {navItems.map(({ to, label, icon: Icon, end }) => (
          <NavLink
            key={to}
            to={to}
            end={end}
            className={({ isActive }) =>
              cn(
                "flex shrink-0 items-center gap-1.5 rounded-lg px-3 py-1.5 text-sm font-medium",
                isActive ? "bg-brand-100 text-brand-800" : "text-slate-600",
              )
            }
          >
            <Icon className="size-4" />
            {label}
          </NavLink>
        ))}
      </nav>
    </header>
  );
}
