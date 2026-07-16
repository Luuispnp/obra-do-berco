import { apiClient } from "@/api/client";
import type {
  Evento,
  EventoFiltro,
  EventoInput,
  GestanteDisponivel,
  Participante,
} from "@/types/evento";

export async function listarEventos(filtro: EventoFiltro = {}): Promise<Evento[]> {
  const response = await apiClient.get<Evento[]>("/eventos", { params: filtro });
  return response.data;
}

export async function buscarEvento(id: string): Promise<Evento> {
  const response = await apiClient.get<Evento>(`/eventos/${id}`);
  return response.data;
}

export async function criarEvento(input: EventoInput): Promise<Evento> {
  const response = await apiClient.post<Evento>("/eventos", input);
  return response.data;
}

export async function atualizarEvento(id: string, input: EventoInput): Promise<Evento> {
  const response = await apiClient.put<Evento>(`/eventos/${id}`, input);
  return response.data;
}

export async function excluirEvento(id: string): Promise<void> {
  await apiClient.delete(`/eventos/${id}`);
}

export async function finalizarEvento(id: string): Promise<void> {
  await apiClient.patch(`/eventos/${id}/finalizar`);
}

export async function listarGestantesDisponiveis(): Promise<GestanteDisponivel[]> {
  const response = await apiClient.get<GestanteDisponivel[]>("/eventos/gestantes-disponiveis");
  return response.data;
}

export async function listarParticipantes(eventoId: string): Promise<Participante[]> {
  const response = await apiClient.get<Participante[]>(`/eventos/${eventoId}/participantes`);
  return response.data;
}

export async function adicionarParticipante(
  eventoId: string,
  solicitacaoId: string,
): Promise<Participante> {
  const response = await apiClient.post<Participante>(`/eventos/${eventoId}/participantes`, {
    solicitacaoId,
  });
  return response.data;
}

export async function adicionarParticipantesLote(
  eventoId: string,
  solicitacaoIds: string[],
): Promise<Participante[]> {
  const response = await apiClient.post<Participante[]>(
    `/eventos/${eventoId}/participantes/lote`,
    { solicitacaoIds },
  );
  return response.data;
}

export async function removerParticipante(eventoId: string, participanteId: string): Promise<void> {
  await apiClient.delete(`/eventos/${eventoId}/participantes/${participanteId}`);
}

export async function marcarPresenca(
  eventoId: string,
  participanteId: string,
  presente: boolean,
): Promise<Participante> {
  const response = await apiClient.patch<Participante>(
    `/eventos/${eventoId}/participantes/${participanteId}/presenca`,
    { presente },
  );
  return response.data;
}
