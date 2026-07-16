import { apiClient } from "@/api/client";
import type {
  Solicitacao,
  SolicitacaoFiltro,
  SolicitacaoInput,
  SolicitacaoUpdateInput,
} from "@/types/solicitacao";

export async function listarSolicitacoes(filtro: SolicitacaoFiltro = {}): Promise<Solicitacao[]> {
  const response = await apiClient.get<Solicitacao[]>("/solicitacoes", { params: filtro });
  return response.data;
}

export async function buscarSolicitacao(id: string): Promise<Solicitacao> {
  const response = await apiClient.get<Solicitacao>(`/solicitacoes/${id}`);
  return response.data;
}

export async function criarSolicitacao(input: SolicitacaoInput): Promise<Solicitacao> {
  const response = await apiClient.post<Solicitacao>("/solicitacoes", input);
  return response.data;
}

export async function atualizarSolicitacao(
  id: string,
  input: SolicitacaoUpdateInput,
): Promise<Solicitacao> {
  const response = await apiClient.put<Solicitacao>(`/solicitacoes/${id}`, input);
  return response.data;
}

export async function aprovarSolicitacao(id: string): Promise<Solicitacao> {
  const response = await apiClient.patch<Solicitacao>(`/solicitacoes/${id}/aprovar`);
  return response.data;
}

export async function recusarSolicitacao(id: string, motivoRecusa: string): Promise<Solicitacao> {
  const response = await apiClient.patch<Solicitacao>(`/solicitacoes/${id}/recusar`, {
    motivoRecusa,
  });
  return response.data;
}

export async function excluirSolicitacao(id: string): Promise<void> {
  await apiClient.delete(`/solicitacoes/${id}`);
}
