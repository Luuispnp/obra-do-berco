import { useState } from "react";
import { Link } from "react-router-dom";
import { Plus, Search } from "lucide-react";
import * as eventosApi from "@/api/eventos";
import { useAsync } from "@/hooks/useAsync";
import { PageHeader } from "@/components/ui/PageHeader";
import { buttonVariants } from "@/components/ui/Button";
import { Select } from "@/components/ui/Select";
import { Table, Tbody, Td, Th, Thead, Tr } from "@/components/ui/Table";
import { EmptyState } from "@/components/ui/EmptyState";
import { PageSpinner } from "@/components/ui/Spinner";
import { StatusEventoBadge } from "@/components/ui/StatusBadges";
import { formatDate } from "@/lib/utils";
import type { StatusEvento } from "@/types/evento";

export function EventosListPage() {
  const [status, setStatus] = useState<StatusEvento | "">("");

  const { data: eventos, isLoading } = useAsync(
    () => eventosApi.listarEventos({ status: status || undefined }),
    [status],
  );

  return (
    <div>
      <PageHeader
        title="Eventos"
        description="Eventos de entrega de enxoval e acompanhamento das gestantes."
        actions={
          <Link to="/app/eventos/novo" className={buttonVariants("primary", "md")}>
            <Plus className="size-4" />
            Novo evento
          </Link>
        }
      />

      <div className="mb-4 max-w-xs">
        <Select value={status} onChange={(event) => setStatus(event.target.value as StatusEvento | "")}>
          <option value="">Todos os status</option>
          <option value="CRIADO">Em aberto</option>
          <option value="FINALIZADO">Finalizado</option>
        </Select>
      </div>

      {isLoading ? (
        <PageSpinner />
      ) : !eventos || eventos.length === 0 ? (
        <EmptyState icon={Search} title="Nenhum evento encontrado" description="Cadastre um novo evento para começar." />
      ) : (
        <Table>
          <Thead>
            <tr>
              <Th>Título</Th>
              <Th>Data</Th>
              <Th>Status</Th>
              <Th>Participantes</Th>
              <Th className="text-right">Ações</Th>
            </tr>
          </Thead>
          <Tbody>
            {eventos.map((evento) => (
              <Tr key={evento.eventoId}>
                <Td className="font-medium text-slate-800">{evento.titulo}</Td>
                <Td>{formatDate(evento.dataEvento)}</Td>
                <Td>
                  <StatusEventoBadge status={evento.status} />
                </Td>
                <Td>{evento.participantes.length}</Td>
                <Td>
                  <div className="flex justify-end">
                    <Link to={`/app/eventos/${evento.eventoId}`} className={buttonVariants("outline", "sm")}>
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
