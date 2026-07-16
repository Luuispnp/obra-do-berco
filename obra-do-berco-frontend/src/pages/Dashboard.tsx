import { Link } from "react-router-dom";
import { Baby, CalendarDays, ClipboardList, Users } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";
import { Card, CardContent } from "@/components/ui/Card";
import { PageHeader } from "@/components/ui/PageHeader";

const modulos = [
  {
    to: "/app/gestantes",
    title: "Gestantes",
    description: "Cadastrar e consultar gestantes atendidas.",
    icon: Baby,
  },
  {
    to: "/app/solicitacoes",
    title: "Solicitações",
    description: "Analisar e aprovar solicitações de enxoval.",
    icon: ClipboardList,
  },
  {
    to: "/app/eventos",
    title: "Eventos",
    description: "Organizar eventos de entrega e checklist de presença.",
    icon: CalendarDays,
  },
  {
    to: "/app/voluntarios",
    title: "Voluntários",
    description: "Consultar a equipe de voluntários da ONG.",
    icon: Users,
  },
];

export function Dashboard() {
  const { user } = useAuth();

  return (
    <div>
      <PageHeader
        title={`Olá, ${user?.nome?.split(" ")[0] ?? ""}`}
        description="O que você gostaria de fazer hoje?"
      />

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {modulos.map(({ to, title, description, icon: Icon }) => (
          <Link key={to} to={to}>
            <Card className="h-full transition-shadow hover:shadow-md">
              <CardContent className="flex flex-col gap-3">
                <span className="flex size-11 items-center justify-center rounded-xl bg-brand-100">
                  <Icon className="size-5 text-brand-600" />
                </span>
                <div>
                  <p className="font-semibold text-slate-800">{title}</p>
                  <p className="mt-1 text-sm text-slate-500">{description}</p>
                </div>
              </CardContent>
            </Card>
          </Link>
        ))}
      </div>
    </div>
  );
}
