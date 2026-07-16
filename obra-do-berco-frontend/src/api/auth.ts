import { apiClient } from "@/api/client";
import type { LoginRequest, LoginResponse, RefreshResponse, Usuario } from "@/types/auth";

export async function login(request: LoginRequest): Promise<LoginResponse> {
  const response = await apiClient.post<LoginResponse>("/auth/login", request);
  return response.data;
}

export async function refresh(refreshToken: string): Promise<RefreshResponse> {
  const response = await apiClient.post<RefreshResponse>("/auth/refresh", { refreshToken });
  return response.data;
}

export async function logout(refreshToken: string): Promise<void> {
  await apiClient.post("/auth/logout", { refreshToken });
}

export async function me(): Promise<Usuario> {
  const response = await apiClient.get<Usuario>("/auth/me");
  return response.data;
}
