import { apiClient } from "@/api/client";
import type {
  Gestante,
  GestanteFiltro,
  GestanteInput,
  GestanteUpdateInput,
} from "@/types/gestante";

export async function listarGestantes(filtro: GestanteFiltro = {}): Promise<Gestante[]> {
  const response = await apiClient.get<Gestante[]>("/gestantes", { params: filtro });
  return response.data;
}

export async function buscarGestante(id: string): Promise<Gestante> {
  const response = await apiClient.get<Gestante>(`/gestantes/${id}`);
  return response.data;
}

export async function criarGestante(input: GestanteInput): Promise<Gestante> {
  const response = await apiClient.post<Gestante>("/gestantes", input);
  return response.data;
}

export async function atualizarGestante(id: string, input: GestanteUpdateInput): Promise<Gestante> {
  const response = await apiClient.put<Gestante>(`/gestantes/${id}`, input);
  return response.data;
}

export async function inativarGestante(id: string): Promise<Gestante> {
  const response = await apiClient.patch<Gestante>(`/gestantes/${id}/inativar`);
  return response.data;
}

export async function reativarGestante(id: string): Promise<Gestante> {
  const response = await apiClient.patch<Gestante>(`/gestantes/${id}/reativar`);
  return response.data;
}
