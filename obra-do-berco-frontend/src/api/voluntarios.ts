import { apiClient } from "@/api/client";
import type { Voluntario, VoluntarioFiltro, VoluntarioInput } from "@/types/voluntario";

export async function listarVoluntarios(filtro: VoluntarioFiltro = {}): Promise<Voluntario[]> {
  const response = await apiClient.get<Voluntario[]>("/voluntarios", { params: filtro });
  return response.data;
}

export async function buscarVoluntario(id: string): Promise<Voluntario> {
  const response = await apiClient.get<Voluntario>(`/voluntarios/${id}`);
  return response.data;
}

export async function criarVoluntario(input: VoluntarioInput): Promise<Voluntario> {
  const response = await apiClient.post<Voluntario>("/voluntarios", input);
  return response.data;
}

export async function atualizarVoluntario(id: string, input: VoluntarioInput): Promise<Voluntario> {
  const response = await apiClient.put<Voluntario>(`/voluntarios/${id}`, input);
  return response.data;
}

export async function inativarVoluntario(id: string): Promise<Voluntario> {
  const response = await apiClient.patch<Voluntario>(`/voluntarios/${id}/inativar`);
  return response.data;
}

export async function reativarVoluntario(id: string): Promise<Voluntario> {
  const response = await apiClient.patch<Voluntario>(`/voluntarios/${id}/reativar`);
  return response.data;
}
