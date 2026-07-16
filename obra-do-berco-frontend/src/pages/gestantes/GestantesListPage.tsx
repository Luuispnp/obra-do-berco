import { useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "sonner";
import { Plus, RotateCcw, Search, UserX } from "lucide-react";
import * as gestantesApi from "@/api/gestantes";
import { useAsync } from "@/hooks/useAsync";
import { PageHeader } from "@/components/ui/PageHeader";
import { Button, buttonVariants } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { Table, Tbody, Td, Th, Thead, Tr } from "@/components/ui/Table";
import { EmptyState } from "@/components/ui/EmptyState";
import { PageSpinner } from "@/components/ui/Spinner";
import { AtivoBadge } from "@/components/ui/StatusBadges";
import { ConfirmDialog } from "@/components/ui/ConfirmDialog";
import { extractErrorMessage } from "@/lib/errors";
import { formatCpf } from "@/lib/utils";
import type { Gestante } from "@/types/gestante";

export function GestantesListPage() {
  const [filtroNome, setFiltroNome] = useState("");
  const [alvoInativacao, setAlvoInativacao] = useState<Gestante | null>(null);
  const [isProcessing, setIsProcessing] = useState(false);

  const { data: gestantes, isLoading, reload } = useAsync(
    () => gestantesApi.listarGestantes(filtroNome ? { nome: filtroNome } : {}),
    [filtroNome],
  );

  async function alternarAtivo(gestante: Gestante) {
    setIsProcessing(true);
    try {
      if (gestante.ativo) {
        await gestantesApi.inativarGestante(gestante.id);
        toast.success("Gestante inativada.");
      } else {
        await gestantesApi.reativarGestante(gestante.id);
        toast.success("Gestante reativada.");
      }
      setAlvoInativacao(null);
      reload();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setIsProcessing(false);
    }
  }

  return (
    <div>
      <PageHeader
        title="Gestantes"
        description="Cadastro e acompanhamento das gestantes atendidas."
        actions={
          <Link to="/app/gestantes/novo" className={buttonVariants("primary", "md")}>
            <Plus className="size-4" />
            Nova gestante
          </Link>
        }
      />

      <div className="relative mb-4 max-w-sm">
        <Search className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-slate-400" />
        <Input
          placeholder="Buscar por nome..."
          value={filtroNome}
          onChange={(event) => setFiltroNome(event.target.value)}
          className="pl-9"
        />
      </div>

      {isLoading ? (
        <PageSpinner />
      ) : !gestantes || gestantes.length === 0 ? (
        <EmptyState
          icon={Search}
          title="Nenhuma gestante encontrada"
          description="Tente ajustar a busca ou cadastre uma nova gestante."
        />
      ) : (
        <Table>
          <Thead>
            <tr>
              <Th>Nome</Th>
              <Th>CPF</Th>
              <Th>Telefone</Th>
              <Th>Cidade</Th>
              <Th>Situação</Th>
              <Th className="text-right">Ações</Th>
            </tr>
          </Thead>
          <Tbody>
            {gestantes.map((gestante) => (
              <Tr key={gestante.id}>
                <Td className="font-medium text-slate-800">
                  <Link to={`/app/gestantes/${gestante.id}`} className="hover:text-brand-600">
                    {gestante.nome}
                  </Link>
                </Td>
                <Td>{formatCpf(gestante.cpf)}</Td>
                <Td>{gestante.telefone}</Td>
                <Td>{gestante.endereco?.cidade ?? "-"}</Td>
                <Td>
                  <AtivoBadge ativo={gestante.ativo} />
                </Td>
                <Td>
                  <div className="flex justify-end gap-2">
                    <Link to={`/app/gestantes/${gestante.id}`} className={buttonVariants("outline", "sm")}>
                      Ver / editar
                    </Link>
                    <Button
                      variant={gestante.ativo ? "outline" : "secondary"}
                      size="sm"
                      onClick={() => setAlvoInativacao(gestante)}
                    >
                      {gestante.ativo ? <UserX className="size-4" /> : <RotateCcw className="size-4" />}
                    </Button>
                  </div>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      )}

      <ConfirmDialog
        open={alvoInativacao !== null}
        title={alvoInativacao?.ativo ? "Inativar gestante" : "Reativar gestante"}
        description={`Tem certeza que deseja ${alvoInativacao?.ativo ? "inativar" : "reativar"} ${alvoInativacao?.nome}?`}
        confirmLabel={alvoInativacao?.ativo ? "Inativar" : "Reativar"}
        tone={alvoInativacao?.ativo ? "danger" : "primary"}
        isLoading={isProcessing}
        onConfirm={() => alvoInativacao && alternarAtivo(alvoInativacao)}
        onCancel={() => setAlvoInativacao(null)}
      />
    </div>
  );
}
