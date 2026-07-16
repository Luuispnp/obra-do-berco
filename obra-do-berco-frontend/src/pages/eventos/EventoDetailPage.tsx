import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { ArrowLeft, CheckCircle2, FileDown, Plus, Trash2, UserCheck, UserX } from "lucide-react";
import * as eventosApi from "@/api/eventos";
import * as gestantesApi from "@/api/gestantes";
import * as solicitacoesApi from "@/api/solicitacoes";
import * as documentosApi from "@/api/documentos";
import { useAsync } from "@/hooks/useAsync";
import { PageHeader } from "@/components/ui/PageHeader";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { PageSpinner } from "@/components/ui/Spinner";
import { StatusEventoBadge } from "@/components/ui/StatusBadges";
import { Table, Tbody, Td, Th, Thead, Tr } from "@/components/ui/Table";
import { EmptyState } from "@/components/ui/EmptyState";
import { Modal } from "@/components/ui/Modal";
import { Checkbox } from "@/components/ui/Checkbox";
import { ConfirmDialog } from "@/components/ui/ConfirmDialog";
import { extractErrorMessage } from "@/lib/errors";
import { formatDate } from "@/lib/utils";
import type { GestanteDisponivel } from "@/types/evento";

interface OpcaoDisponivel extends GestanteDisponivel {
  nomeGestante: string;
  telefone: string;
  idadeGestacionalSemanas?: number;
  dataProvavelParto?: string;
}

export function EventoDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const eventoId = id!;

  const { data: evento, isLoading, reload: recarregarEvento } = useAsync(
    () => eventosApi.buscarEvento(eventoId),
    [eventoId],
  );
  const { data: participantes, isLoading: isLoadingParticipantes, reload: recarregarParticipantes } = useAsync(
    () => eventosApi.listarParticipantes(eventoId),
    [eventoId],
  );

  const [modalAberto, setModalAberto] = useState(false);
  const [opcoes, setOpcoes] = useState<OpcaoDisponivel[]>([]);
  const [carregandoOpcoes, setCarregandoOpcoes] = useState(false);
  const [selecionados, setSelecionados] = useState<Set<string>>(new Set());
  const [salvandoParticipantes, setSalvandoParticipantes] = useState(false);

  const [participanteRemovendo, setParticipanteRemovendo] = useState<string | null>(null);
  const [processandoAcao, setProcessandoAcao] = useState<string | null>(null);
  const [finalizando, setFinalizando] = useState(false);
  const [baixando, setBaixando] = useState<"lista" | "fichas" | null>(null);

  function recarregarTudo() {
    recarregarEvento();
    recarregarParticipantes();
  }

  async function abrirModalAdicionar() {
    setModalAberto(true);
    setCarregandoOpcoes(true);
    setSelecionados(new Set());
    try {
      const disponiveis = await eventosApi.listarGestantesDisponiveis();
      const comDados = await Promise.all(
        disponiveis.map(async (item) => {
          const [gestante, solicitacao] = await Promise.all([
            gestantesApi.buscarGestante(item.gestanteId).catch(() => null),
            solicitacoesApi.buscarSolicitacao(item.solicitacaoId).catch(() => null),
          ]);
          return {
            ...item,
            nomeGestante: gestante?.nome ?? "Gestante",
            telefone: gestante?.telefone ?? "-",
            idadeGestacionalSemanas: solicitacao?.idadeGestacionalSemanas,
            dataProvavelParto: solicitacao?.dataProvavelParto,
          };
        }),
      );
      setOpcoes(comDados);
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setCarregandoOpcoes(false);
    }
  }

  function alternarSelecionado(solicitacaoId: string) {
    setSelecionados((atual) => {
      const proximo = new Set(atual);
      if (proximo.has(solicitacaoId)) proximo.delete(solicitacaoId);
      else proximo.add(solicitacaoId);
      return proximo;
    });
  }

  async function confirmarAdicao() {
    if (selecionados.size === 0) return;
    setSalvandoParticipantes(true);
    try {
      await eventosApi.adicionarParticipantesLote(eventoId, Array.from(selecionados));
      toast.success("Participantes adicionados.");
      setModalAberto(false);
      recarregarTudo();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setSalvandoParticipantes(false);
    }
  }

  async function removerParticipante() {
    if (!participanteRemovendo) return;
    setProcessandoAcao(participanteRemovendo);
    try {
      await eventosApi.removerParticipante(eventoId, participanteRemovendo);
      toast.success("Participante removido.");
      setParticipanteRemovendo(null);
      recarregarTudo();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setProcessandoAcao(null);
    }
  }

  async function alternarPresenca(participanteId: string, presenteAtual: boolean) {
    setProcessandoAcao(participanteId);
    try {
      await eventosApi.marcarPresenca(eventoId, participanteId, !presenteAtual);
      recarregarParticipantes();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setProcessandoAcao(null);
    }
  }

  async function finalizarEvento() {
    setProcessandoAcao("finalizar");
    try {
      await eventosApi.finalizarEvento(eventoId);
      toast.success("Evento finalizado.");
      setFinalizando(false);
      recarregarTudo();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setProcessandoAcao(null);
    }
  }

  async function baixarLista() {
    setBaixando("lista");
    try {
      await documentosApi.baixarListaEvento(eventoId);
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setBaixando(null);
    }
  }

  async function baixarFichas() {
    setBaixando("fichas");
    try {
      await documentosApi.baixarFichasEvento(eventoId);
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setBaixando(null);
    }
  }

  if (isLoading) return <PageSpinner />;
  if (!evento) return null;

  const emAberto = evento.status === "CRIADO";

  return (
    <div>
      <button
        onClick={() => navigate("/app/eventos")}
        className="mb-4 flex items-center gap-1 text-sm text-slate-500 hover:text-brand-600"
      >
        <ArrowLeft className="size-4" />
        Voltar para eventos
      </button>

      <PageHeader
        title={evento.titulo}
        description={`Data do evento: ${formatDate(evento.dataEvento)}`}
        actions={
          <>
            <Button variant="outline" isLoading={baixando === "lista"} onClick={baixarLista}>
              <FileDown className="size-4" />
              Lista de presença
            </Button>
            <Button variant="outline" isLoading={baixando === "fichas"} onClick={baixarFichas}>
              <FileDown className="size-4" />
              Fichas em lote
            </Button>
            {emAberto && (
              <Button variant="primary" onClick={() => setFinalizando(true)}>
                <CheckCircle2 className="size-4" />
                Finalizar evento
              </Button>
            )}
          </>
        }
      />

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Participantes</CardTitle>
            <div className="flex items-center gap-3">
              <StatusEventoBadge status={evento.status} />
              {emAberto && (
                <Button size="sm" onClick={abrirModalAdicionar}>
                  <Plus className="size-4" />
                  Adicionar
                </Button>
              )}
            </div>
          </div>
        </CardHeader>
        <CardContent>
          {isLoadingParticipantes ? (
            <PageSpinner />
          ) : !participantes || participantes.length === 0 ? (
            <EmptyState title="Nenhum participante" description="Adicione gestantes com solicitação aprovada a este evento." />
          ) : (
            <Table>
              <Thead>
                <tr>
                  <Th>Gestante</Th>
                  <Th>Presença</Th>
                  <Th className="text-right">Ações</Th>
                </tr>
              </Thead>
              <Tbody>
                {participantes.map((participante) => (
                  <Tr key={participante.participanteId}>
                    <Td className="font-medium text-slate-800">{participante.nomeGestante}</Td>
                    <Td>
                      {participante.presente ? (
                        <span className="inline-flex items-center gap-1 text-emerald-600">
                          <UserCheck className="size-4" /> Presente
                        </span>
                      ) : (
                        <span className="inline-flex items-center gap-1 text-slate-400">
                          <UserX className="size-4" /> Ausente
                        </span>
                      )}
                    </Td>
                    <Td>
                      <div className="flex justify-end gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          isLoading={processandoAcao === participante.participanteId}
                          onClick={() => alternarPresenca(participante.participanteId, participante.presente)}
                        >
                          {participante.presente ? "Marcar ausente" : "Marcar presente"}
                        </Button>
                        {emAberto && (
                          <Button
                            variant="danger"
                            size="sm"
                            onClick={() => setParticipanteRemovendo(participante.participanteId)}
                          >
                            <Trash2 className="size-4" />
                          </Button>
                        )}
                      </div>
                    </Td>
                  </Tr>
                ))}
              </Tbody>
            </Table>
          )}
        </CardContent>
      </Card>

      <Modal open={modalAberto} onClose={() => setModalAberto(false)} title="Adicionar participantes" size="lg">
        {carregandoOpcoes ? (
          <PageSpinner />
        ) : opcoes.length === 0 ? (
          <EmptyState title="Nenhuma gestante disponível" description="Todas as gestantes com solicitação aprovada já estão em algum evento." />
        ) : (
          <div className="flex flex-col gap-4">
            <div className="max-h-96 overflow-y-auto rounded-lg border border-slate-200">
              {opcoes.map((opcao) => (
                <label
                  key={opcao.solicitacaoId}
                  className="flex items-center gap-4 border-b border-slate-100 px-4 py-3 last:border-0 hover:bg-slate-50"
                >
                  <Checkbox
                    checked={selecionados.has(opcao.solicitacaoId)}
                    onChange={() => alternarSelecionado(opcao.solicitacaoId)}
                  />
                  <span className="flex size-10 shrink-0 items-center justify-center rounded-full bg-brand-100 text-sm font-semibold text-brand-700">
                    {opcao.nomeGestante.charAt(0).toUpperCase()}
                  </span>
                  <div className="flex-1">
                    <p className="text-sm font-medium text-slate-800">{opcao.nomeGestante}</p>
                    <div className="mt-0.5 flex flex-wrap gap-x-3 gap-y-0.5 text-xs text-slate-500">
                      {opcao.telefone && opcao.telefone !== "-" && <span>{opcao.telefone}</span>}
                      {typeof opcao.idadeGestacionalSemanas === "number" && (
                        <span>{opcao.idadeGestacionalSemanas} semanas</span>
                      )}
                      {opcao.dataProvavelParto && <span>DPP: {formatDate(opcao.dataProvavelParto)}</span>}
                    </div>
                  </div>
                </label>
              ))}
            </div>
            <div className="flex justify-end gap-2">
              <Button variant="outline" onClick={() => setModalAberto(false)}>
                Cancelar
              </Button>
              <Button
                onClick={confirmarAdicao}
                isLoading={salvandoParticipantes}
                disabled={selecionados.size === 0}
              >
                Adicionar {selecionados.size > 0 ? `(${selecionados.size})` : ""}
              </Button>
            </div>
          </div>
        )}
      </Modal>

      <ConfirmDialog
        open={participanteRemovendo !== null}
        title="Remover participante"
        description="Tem certeza que deseja remover esta gestante do evento?"
        confirmLabel="Remover"
        tone="danger"
        isLoading={processandoAcao === participanteRemovendo}
        onConfirm={removerParticipante}
        onCancel={() => setParticipanteRemovendo(null)}
      />

      <ConfirmDialog
        open={finalizando}
        title="Finalizar evento"
        description="Depois de finalizado, o evento não poderá mais receber ou remover participantes."
        confirmLabel="Finalizar"
        tone="primary"
        isLoading={processandoAcao === "finalizar"}
        onConfirm={finalizarEvento}
        onCancel={() => setFinalizando(false)}
      />
    </div>
  );
}
