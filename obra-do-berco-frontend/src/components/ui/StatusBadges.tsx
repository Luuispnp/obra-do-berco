import { Badge } from "@/components/ui/Badge";
import type { StatusSolicitacao } from "@/types/solicitacao";
import type { StatusEvento } from "@/types/evento";

const solicitacaoLabels: Record<StatusSolicitacao, { label: string; tone: "amber" | "green" | "red" | "blue" | "slate" }> = {
  EM_ANALISE: { label: "Em análise", tone: "amber" },
  APROVADA: { label: "Aprovada", tone: "blue" },
  RECUSADA: { label: "Recusada", tone: "red" },
  ENTREGUE: { label: "Entregue", tone: "green" },
  NAO_COMPARECEU: { label: "Não compareceu", tone: "slate" },
};

export function StatusSolicitacaoBadge({ status }: { status: StatusSolicitacao }) {
  const config = solicitacaoLabels[status];
  return <Badge tone={config.tone}>{config.label}</Badge>;
}

const eventoLabels: Record<StatusEvento, { label: string; tone: "amber" | "green" }> = {
  CRIADO: { label: "Em aberto", tone: "amber" },
  FINALIZADO: { label: "Finalizado", tone: "green" },
};

export function StatusEventoBadge({ status }: { status: StatusEvento }) {
  const config = eventoLabels[status];
  return <Badge tone={config.tone}>{config.label}</Badge>;
}

export function AtivoBadge({ ativo }: { ativo: boolean }) {
  return ativo ? <Badge tone="green">Ativo</Badge> : <Badge tone="slate">Inativo</Badge>;
}
