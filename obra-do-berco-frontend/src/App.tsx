import { Route, Routes } from "react-router-dom";
import { AdminRoute, ProtectedRoute } from "@/components/layout/ProtectedRoute";
import { AppLayout } from "@/components/layout/AppLayout";
import { Home } from "@/pages/Home";
import { Login } from "@/pages/Login";
import { Dashboard } from "@/pages/Dashboard";
import { GestantesListPage } from "@/pages/gestantes/GestantesListPage";
import { GestanteFormPage } from "@/pages/gestantes/GestanteFormPage";
import { GestanteDetailPage } from "@/pages/gestantes/GestanteDetailPage";
import { VoluntariosListPage } from "@/pages/voluntarios/VoluntariosListPage";
import { VoluntarioFormPage } from "@/pages/voluntarios/VoluntarioFormPage";
import { SolicitacoesListPage } from "@/pages/solicitacoes/SolicitacoesListPage";
import { SolicitacaoFormPage } from "@/pages/solicitacoes/SolicitacaoFormPage";
import { SolicitacaoDetailPage } from "@/pages/solicitacoes/SolicitacaoDetailPage";
import { EventosListPage } from "@/pages/eventos/EventosListPage";
import { EventoFormPage } from "@/pages/eventos/EventoFormPage";
import { EventoDetailPage } from "@/pages/eventos/EventoDetailPage";

function NotFound() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-2 text-center">
      <h1 className="text-3xl font-semibold text-slate-800">Página não encontrada</h1>
      <p className="text-slate-500">O endereço acessado não existe.</p>
    </div>
  );
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route path="/app" element={<Dashboard />} />

          <Route path="/app/gestantes" element={<GestantesListPage />} />
          <Route path="/app/gestantes/novo" element={<GestanteFormPage />} />
          <Route path="/app/gestantes/:id" element={<GestanteDetailPage />} />
          <Route path="/app/gestantes/:id/editar" element={<GestanteFormPage />} />

          <Route path="/app/solicitacoes" element={<SolicitacoesListPage />} />
          <Route path="/app/solicitacoes/novo" element={<SolicitacaoFormPage />} />
          <Route path="/app/solicitacoes/:id" element={<SolicitacaoDetailPage />} />
          <Route path="/app/solicitacoes/:id/editar" element={<SolicitacaoFormPage />} />

          <Route path="/app/eventos" element={<EventosListPage />} />
          <Route path="/app/eventos/novo" element={<EventoFormPage />} />
          <Route path="/app/eventos/:id" element={<EventoDetailPage />} />

          <Route path="/app/voluntarios" element={<VoluntariosListPage />} />
          <Route element={<AdminRoute />}>
            <Route path="/app/voluntarios/novo" element={<VoluntarioFormPage />} />
            <Route path="/app/voluntarios/:id/editar" element={<VoluntarioFormPage />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App;
