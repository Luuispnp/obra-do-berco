import { Link } from "react-router-dom";
import {
  Baby,
  CalendarHeart,
  ClipboardCheck,
  HandHeart,
  Heart,
  Mail,
  MapPin,
  Phone,
} from "lucide-react";

const pilares = [
  {
    icon: Baby,
    title: "Cadastro de gestantes",
    description:
      "Acompanhamento organizado de cada gestante atendida, com histórico completo e seguro.",
  },
  {
    icon: ClipboardCheck,
    title: "Solicitações de enxoval",
    description:
      "Fluxo claro de análise e aprovação das solicitações de enxoval, do pedido até a entrega.",
  },
  {
    icon: CalendarHeart,
    title: "Eventos de entrega",
    description:
      "Organização dos eventos de entrega dos kits, com checklist de presença no dia.",
  },
  {
    icon: HandHeart,
    title: "Voluntariado",
    description: "Gestão dos voluntários que tornam o trabalho da Obra do Berço possível.",
  },
];

export function Home() {
  return (
    <div className="min-h-screen bg-white">
      <header className="border-b border-slate-100">
        <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 sm:px-6">
          <div className="flex items-center gap-2 font-semibold text-brand-700">
            <span className="flex size-8 items-center justify-center rounded-full bg-brand-100">
              <Heart className="size-4 text-brand-600" />
            </span>
            Obra do Berço
          </div>
          <Link
            to="/login"
            className="rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white shadow-sm shadow-brand-600/20 transition-colors hover:bg-brand-700"
          >
            Entrar no sistema
          </Link>
        </div>
      </header>

      <section className="relative overflow-hidden bg-gradient-to-b from-brand-50 via-white to-white">
        <div className="mx-auto max-w-5xl px-4 py-24 text-center sm:px-6">
          <span className="inline-flex items-center gap-1.5 rounded-full bg-brand-100 px-3 py-1 text-xs font-medium text-brand-700">
            <Heart className="size-3.5" />
            Cuidando de quem está esperando
          </span>
          <h1 className="mt-6 text-4xl font-semibold tracking-tight text-slate-800 sm:text-5xl">
            Obra do Berço
          </h1>
          <p className="mx-auto mt-4 max-w-2xl text-lg text-slate-500">
            Um sistema para organizar, com carinho e eficiência, o trabalho da nossa obra social junto a
            gestantes em situação de vulnerabilidade — do cadastro à entrega do enxoval.
          </p>
          <p className="mx-auto mt-2 max-w-xl text-sm text-slate-400">
            A Obra do Berço é um projeto mantido por voluntários para apoiar gestantes carentes cadastradas na paróquia. A iniciativa fornece enxovais para os recém-nascidos e promove encontros de acolhimento, oferecendo lanches e palestras educativas sobre a importância e os cuidados da maternidade.
          </p>
        </div>
      </section>

      <section className="mx-auto max-w-6xl px-4 py-20 sm:px-6">
        <div className="mx-auto mb-12 max-w-2xl text-center">
          <h2 className="text-2xl font-semibold text-slate-800">O que fazemos</h2>
          <p className="mt-2 text-slate-500">
            Uma plataforma única para acompanhar cada etapa do apoio às gestantes atendidas pela
            Obra do Berço.
          </p>
        </div>
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {pilares.map(({ icon: Icon, title, description }) => (
            <div
              key={title}
              className="rounded-2xl border border-slate-100 bg-white p-6 shadow-sm shadow-slate-100 transition-shadow hover:shadow-md"
            >
              <div className="flex size-11 items-center justify-center rounded-xl bg-brand-100">
                <Icon className="size-5 text-brand-600" />
              </div>
              <h3 className="mt-4 font-semibold text-slate-800">{title}</h3>
              <p className="mt-1.5 text-sm text-slate-500">{description}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="bg-brand-50/60">
        <div className="mx-auto max-w-5xl px-4 py-20 text-center sm:px-6">
          <h2 className="text-2xl font-semibold text-slate-800">Sobre a Obra do Berço</h2>
          <p className="mx-auto mt-4 max-w-2xl text-slate-500">
            A Obra do Berço, criada em 1954, é um projeto essencial que conta com uma dedicada equipe de voluntários na confecção de enxovais para recém-nascidos.

            Nosso objetivo vai além de suprir as necessidades materiais das futuras mães; buscamos também orientar e ensinar sobre a importância da maternidade e suas responsabilidades para a família diante de Deus. A entrega do kit maternidade ocorre uma vez por mês, mediante cadastro na secretaria paroquial, atendendo em média 30 gestantes carentes por evento.

            A Obra do Berço dispõe de uma equipe de voluntários, tanto presenciais quanto não presenciais, que desempenham diversas tarefas. Isso inclui o apoio no dia do evento, preparando e servindo lanches às gestantes, além de oferecer assistência e ministrar palestras educativas.
          </p>
        </div>
      </section>

      <section className="mx-auto max-w-5xl px-4 py-20 sm:px-6">
        <div className="mx-auto mb-10 max-w-2xl text-center">
          <h2 className="text-2xl font-semibold text-slate-800">Contato</h2>
          <p className="mt-2 text-slate-500">Fale conosco para saber mais sobre nossos projetos</p>
        </div>
        <div className="grid gap-6 sm:grid-cols-3">
          <div className="flex flex-col items-center gap-2 rounded-2xl border border-slate-100 p-6 text-center">
            <MapPin className="size-5 text-brand-600" />
            <p className="text-sm text-slate-500">Rua Sergipe, 178 A – Região da Boa Viagem – Belo Horizonte</p>
          </div>
          <div className="flex flex-col items-center gap-2 rounded-2xl border border-slate-100 p-6 text-center">
            <Phone className="size-5 text-brand-600" />
            <p className="text-sm text-slate-500">(31) 9 8402-7674</p>
          </div>
          <div className="flex flex-col items-center gap-2 rounded-2xl border border-slate-100 p-6 text-center">
            <Mail className="size-5 text-brand-600" />
            <p className="text-sm text-slate-500">contato@obradoberco.org</p>
          </div>
        </div>
      </section>

      <footer className="border-t border-slate-100 py-8 text-center text-sm text-slate-400">
        © {new Date().getFullYear()} Obra do Berço. Todos os direitos reservados.
      </footer>
    </div>
  );
}
