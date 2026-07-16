import { useState } from "react";
import { Link } from "react-router-dom";
import { Plus, Search } from "lucide-react";
import * as solicitacoesApi from "@/api/solicitacoes";
import * as gestantesApi from "@/api/gestantes";
import { useAsync } from "@/hooks/useAsync";
import { PageHeader } from "@/components/ui/PageHeader";
import { buttonVariants } from "@/components/ui/Button";
import { Select } from "@/components/ui/Select";
import { Input } from "@/components/ui/Input";
import { Table, Tbody, Td, Th, Thead, Tr } from "@/components/ui/Table";
import { EmptyState } from "@/components/ui/EmptyState";
import { PageSpinner } from "@/components/ui/Spinner";
import { StatusSolicitacaoBadge } from "@/components/ui/StatusBadges";
import { formatDate } from "@/lib/utils";
import type { Solicitacao, StatusSolicitacao } from "@/types/solicitacao";

interface SolicitacaoComGestante extends Solicitacao {
  nomeGestante: string;
}

const statusOptions: { value: StatusSolicitacao | ""; label: string }[] = [
  { value: "", label: "Todos os status" },
  { value: "EM_ANALISE", label: "Em análise" },
  { value: "APROVADA", label: "Aprovada" },
  { value: "RECUSADA", label: "Recusada" },
  { value: "ENTREGUE", label: "Entregue" },
  { value: "NAO_COMPARECEU", label: "Não compareceu" },
];

export function SolicitacoesListPage() {
  const [status, setStatus] = useState<StatusSolicitacao | "">("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");

  const { data: solicitacoes, isLoading } = useAsync<SolicitacaoComGestante[]>(
    async () => {
      const [lista, gestantes] = await Promise.all([
        solicitacoesApi.listarSolicitacoes({
          status: status || undefined,
          dataInicio: dataInicio || undefined,
          dataFim: dataFim || undefined,
        }),
        gestantesApi.listarGestantes(),
      ]);
      const nomesPorId = new Map(gestantes.map((gestante) => [gestante.id, gestante.nome]));
      return lista.map((solicitacao) => ({
        ...solicitacao,
        nomeGestante: nomesPorId.get(solicitacao.gestanteId) ?? "Gestante",
      }));
    },
    [status, dataInicio, dataFim],
  );

  return (
    <div>
      <PageHeader
        title="Solicitações"
        description="Pedidos de enxoval das gestantes atendidas."
        actions={
          <Link to="/app/solicitacoes/novo" className={buttonVariants("primary", "md")}>
            <Plus className="size-4" />
            Nova solicitação
          </Link>
        }
      />

      <div className="mb-4 grid gap-3 sm:grid-cols-3 sm:max-w-2xl">
        <Select value={status} onChange={(event) => setStatus(event.target.value as StatusSolicitacao | "")}>
          {statusOptions.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </Select>
        <Input type="date" value={dataInicio} onChange={(event) => setDataInicio(event.target.value)} />
        <Input type="date" value={dataFim} onChange={(event) => setDataFim(event.target.value)} />
      </div>

      {isLoading ? (
        <PageSpinner />
      ) : !solicitacoes || solicitacoes.length === 0 ? (
        <EmptyState icon={Search} title="Nenhuma solicitação encontrada" description="Tente ajustar os filtros." />
      ) : (
        <Table>
          <Thead>
            <tr>
              <Th>Gestante</Th>
              <Th>Data</Th>
              <Th>Status</Th>
              <Th>Idade gestacional</Th>
              <Th>Data provável do parto</Th>
              <Th className="text-right">Ações</Th>
            </tr>
          </Thead>
          <Tbody>
            {solicitacoes.map((solicitacao) => (
              <Tr key={solicitacao.solicitacaoId}>
                <Td className="font-medium text-slate-800">{solicitacao.nomeGestante}</Td>
                <Td>{formatDate(solicitacao.dataSolicitacao)}</Td>
                <Td>
                  <StatusSolicitacaoBadge status={solicitacao.status} />
                </Td>
                <Td>{solicitacao.idadeGestacionalSemanas} semanas</Td>
                <Td>{formatDate(solicitacao.dataProvavelParto)}</Td>
                <Td>
                  <div className="flex justify-end">
                    <Link
                      to={`/app/solicitacoes/${solicitacao.solicitacaoId}`}
                      className={buttonVariants("outline", "sm")}
                    >
                      Ver detalhes
                    </Link>
                  </div>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      )}
    </div>
  );
}
