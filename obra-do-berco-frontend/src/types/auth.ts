export type Role = "admin" | "voluntario";

export interface Usuario {
  id: string;
  nome: string;
  email: string;
  role: Role;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  user: Usuario;
}

export interface RefreshResponse {
  accessToken: string;
  expiresIn: number;
}
