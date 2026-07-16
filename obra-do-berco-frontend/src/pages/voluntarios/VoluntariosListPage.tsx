import { useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "sonner";
import { Plus, RotateCcw, Search, UserX } from "lucide-react";
import * as voluntariosApi from "@/api/voluntarios";
import { useAsync } from "@/hooks/useAsync";
import { useAuth } from "@/hooks/useAuth";
import { PageHeader } from "@/components/ui/PageHeader";
import { Button, buttonVariants } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { Table, Tbody, Td, Th, Thead, Tr } from "@/components/ui/Table";
import { EmptyState } from "@/components/ui/EmptyState";
import { PageSpinner } from "@/components/ui/Spinner";
import { AtivoBadge } from "@/components/ui/StatusBadges";
import { Badge } from "@/components/ui/Badge";
import { ConfirmDialog } from "@/components/ui/ConfirmDialog";
import { extractErrorMessage } from "@/lib/errors";
import type { Voluntario } from "@/types/voluntario";

export function VoluntariosListPage() {
  const { isAdmin } = useAuth();
  const [filtroNome, setFiltroNome] = useState("");
  const [alvoInativacao, setAlvoInativacao] = useState<Voluntario | null>(null);
  const [isProcessing, setIsProcessing] = useState(false);

  const { data: voluntarios, isLoading, reload } = useAsync(
    () => voluntariosApi.listarVoluntarios(filtroNome ? { nome: filtroNome } : {}),
    [filtroNome],
  );

  async function alternarAtivo(voluntario: Voluntario) {
    setIsProcessing(true);
    try {
      if (voluntario.ativo) {
        await voluntariosApi.inativarVoluntario(voluntario.voluntarioId);
        toast.success("Voluntário inativado.");
      } else {
        await voluntariosApi.reativarVoluntario(voluntario.voluntarioId);
        toast.success("Voluntário reativado.");
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
        title="Voluntários"
        description="Equipe responsável pelo atendimento da ONG."
        actions={
          isAdmin ? (
            <Link to="/app/voluntarios/novo" className={buttonVariants("primary", "md")}>
              <Plus className="size-4" />
              Novo voluntário
            </Link>
          ) : undefined
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
      ) : !voluntarios || voluntarios.length === 0 ? (
        <EmptyState
          icon={Search}
          title="Nenhum voluntário encontrado"
          description="Tente ajustar a busca."
        />
      ) : (
        <Table>
          <Thead>
            <tr>
              <Th>Nome</Th>
              <Th>E-mail</Th>
              <Th>Telefone</Th>
              <Th>Perfil</Th>
              <Th>Situação</Th>
              {isAdmin && <Th className="text-right">Ações</Th>}
            </tr>
          </Thead>
          <Tbody>
            {voluntarios.map((voluntario) => (
              <Tr key={voluntario.voluntarioId}>
                <Td className="font-medium text-slate-800">{voluntario.nomeCompleto}</Td>
                <Td>{voluntario.email}</Td>
                <Td>{voluntario.telefone}</Td>
                <Td>
                  <Badge tone={voluntario.perfil === "ADMIN" ? "blue" : "slate"}>
                    {voluntario.perfil === "ADMIN" ? "Admin" : "Voluntário"}
                  </Badge>
                </Td>
                <Td>
                  <AtivoBadge ativo={voluntario.ativo} />
                </Td>
                {isAdmin && (
                  <Td>
                    <div className="flex justify-end gap-2">
                      <Link
                        to={`/app/voluntarios/${voluntario.voluntarioId}/editar`}
                        className={buttonVariants("outline", "sm")}
                      >
                        Editar
                      </Link>
                      <Button
                        variant={voluntario.ativo ? "outline" : "secondary"}
                        size="sm"
                        onClick={() => setAlvoInativacao(voluntario)}
                      >
                        {voluntario.ativo ? <UserX className="size-4" /> : <RotateCcw className="size-4" />}
                      </Button>
                    </div>
                  </Td>
                )}
              </Tr>
            ))}
          </Tbody>
        </Table>
      )}

      <ConfirmDialog
        open={alvoInativacao !== null}
        title={alvoInativacao?.ativo ? "Inativar voluntário" : "Reativar voluntário"}
        description={`Tem certeza que deseja ${alvoInativacao?.ativo ? "inativar" : "reativar"} ${alvoInativacao?.nomeCompleto}?`}
        confirmLabel={alvoInativacao?.ativo ? "Inativar" : "Reativar"}
        tone={alvoInativacao?.ativo ? "danger" : "primary"}
        isLoading={isProcessing}
        onConfirm={() => alvoInativacao && alternarAtivo(alvoInativacao)}
        onCancel={() => setAlvoInativacao(null)}
      />
    </div>
  );
}
