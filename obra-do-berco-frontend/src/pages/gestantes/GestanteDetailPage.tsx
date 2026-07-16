import { Link, useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { useState, type ReactNode } from "react";
import { ArrowLeft, Pencil, Plus, RotateCcw, UserX } from "lucide-react";
import * as gestantesApi from "@/api/gestantes";
import * as solicitacoesApi from "@/api/solicitacoes";
import * as documentosApi from "@/api/documentos";
import { useAsync } from "@/hooks/useAsync";
import { PageHeader } from "@/components/ui/PageHeader";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import { Button, buttonVariants } from "@/components/ui/Button";
import { PageSpinner } from "@/components/ui/Spinner";
import { AtivoBadge, StatusSolicitacaoBadge } from "@/components/ui/StatusBadges";
import { Table, Tbody, Td, Th, Thead, Tr } from "@/components/ui/Table";
import { EmptyState } from "@/components/ui/EmptyState";
import { ConfirmDialog } from "@/components/ui/ConfirmDialog";
import { extractErrorMessage } from "@/lib/errors";
import { formatCpf, formatDate } from "@/lib/utils";

export function GestanteDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [confirmando, setConfirmando] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);

  const { data: gestante, isLoading, reload } = useAsync(
    () => gestantesApi.buscarGestante(id!),
    [id],
  );
  const { data: solicitacoes, isLoading: isLoadingSolicitacoes } = useAsync(
    () => solicitacoesApi.listarSolicitacoes({ gestanteId: id }),
    [id],
  );

  const [baixandoFicha, setBaixandoFicha] = useState<string | null>(null);

  async function emitirFicha(solicitacaoId: string) {
    setBaixandoFicha(solicitacaoId);
    try {
      await documentosApi.baixarFichaIndividual(solicitacaoId);
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setBaixandoFicha(null);
    }
  }

  async function alternarAtivo() {
    if (!gestante) return;
    setIsProcessing(true);
    try {
      if (gestante.ativo) {
        await gestantesApi.inativarGestante(gestante.id);
        toast.success("Gestante inativada.");
      } else {
        await gestantesApi.reativarGestante(gestante.id);
        toast.success("Gestante reativada.");
      }
      setConfirmando(false);
      reload();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setIsProcessing(false);
    }
  }

  if (isLoading) return <PageSpinner />;
  if (!gestante) return null;

  return (
    <div>
      <button
        onClick={() => navigate("/app/gestantes")}
        className="mb-4 flex items-center gap-1 text-sm text-slate-500 hover:text-brand-600"
      >
        <ArrowLeft className="size-4" />
        Voltar para gestantes
      </button>

      <PageHeader
        title={gestante.nome}
        description={`Cadastrada em ${formatDate(gestante.dataCadastro)}`}
        actions={
          <>
            <Link to={`/app/gestantes/${gestante.id}/editar`} className={buttonVariants("outline", "md")}>
              <Pencil className="size-4" />
              Editar
            </Link>
            <Button
              variant={gestante.ativo ? "danger" : "secondary"}
              onClick={() => setConfirmando(true)}
            >
              {gestante.ativo ? <UserX className="size-4" /> : <RotateCcw className="size-4" />}
              {gestante.ativo ? "Inativar" : "Reativar"}
            </Button>
          </>
        }
      />

      <div className="grid gap-6 lg:grid-cols-3">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle>Dados cadastrais</CardTitle>
          </CardHeader>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <Info label="Situação" value={<AtivoBadge ativo={gestante.ativo} />} />
            <Info label="CPF" value={formatCpf(gestante.cpf)} />
            <Info label="Nº de identidade" value={gestante.numeroIdentidade ?? "-"} />
            <Info label="Data de nascimento" value={formatDate(gestante.dataNascimento)} />
            <Info label="Estado civil" value={estadoCivilLabel(gestante.estadoCivil)} />
            <Info label="Telefone" value={gestante.telefone} />
            <Info label="E-mail" value={gestante.email ?? "-"} />
            <Info
              label="Endereço"
              value={`${gestante.endereco.logradouro}, ${gestante.endereco.bairro} - ${gestante.endereco.cidade}`}
            />
            {gestante.endereco.referencia && (
              <Info label="Referência" value={gestante.endereco.referencia} />
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Ações</CardTitle>
          </CardHeader>
          <CardContent className="flex flex-col gap-2">
            <Link
              to={`/app/solicitacoes/novo?gestanteId=${gestante.id}`}
              className={buttonVariants("primary", "md", "justify-start")}
            >
              <Plus className="size-4" />
              Nova solicitação
            </Link>
          </CardContent>
        </Card>
      </div>

      <div className="mt-6">
        <h2 className="mb-3 text-lg font-semibold text-slate-800">Solicitações</h2>
        {isLoadingSolicitacoes ? (
          <PageSpinner />
        ) : !solicitacoes || solicitacoes.length === 0 ? (
          <EmptyState title="Nenhuma solicitação encontrada" description="Essa gestante ainda não possui solicitações registradas." />
        ) : (
          <Table>
            <Thead>
              <tr>
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
                  <Td>{formatDate(solicitacao.dataSolicitacao)}</Td>
                  <Td>
                    <StatusSolicitacaoBadge status={solicitacao.status} />
                  </Td>
                  <Td>{solicitacao.idadeGestacionalSemanas} semanas</Td>
                  <Td>{formatDate(solicitacao.dataProvavelParto)}</Td>
                  <Td>
                    <div className="flex justify-end gap-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        isLoading={baixandoFicha === solicitacao.solicitacaoId}
                        onClick={() => emitirFicha(solicitacao.solicitacaoId)}
                      >
                        Emitir ficha
                      </Button>
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

      <ConfirmDialog
        open={confirmando}
        title={gestante.ativo ? "Inativar gestante" : "Reativar gestante"}
        description={`Tem certeza que deseja ${gestante.ativo ? "inativar" : "reativar"} ${gestante.nome}?`}
        confirmLabel={gestante.ativo ? "Inativar" : "Reativar"}
        tone={gestante.ativo ? "danger" : "primary"}
        isLoading={isProcessing}
        onConfirm={alternarAtivo}
        onCancel={() => setConfirmando(false)}
      />
    </div>
  );
}

function Info({ label, value }: { label: string; value: ReactNode }) {
  return (
    <div>
      <p className="text-xs font-medium uppercase tracking-wide text-slate-400">{label}</p>
      <p className="mt-0.5 text-sm text-slate-700">{value}</p>
    </div>
  );
}

function estadoCivilLabel(value: string): string {
  const labels: Record<string, string> = {
    SOLTEIRA: "Solteira",
    CASADA: "Casada",
    UNIAO_ESTAVEL: "União estável",
    VIUVA: "Viúva",
  };
  return labels[value] ?? value;
}
