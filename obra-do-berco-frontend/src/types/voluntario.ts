export type PerfilAcesso = "ADMIN" | "VOLUNTARIO";

export interface Voluntario {
  voluntarioId: string;
  nomeCompleto: string;
  email: string;
  telefone: string;
  dataCadastro: string;
  perfil: PerfilAcesso;
  ativo: boolean;
}

export interface VoluntarioInput {
  nomeCompleto: string;
  email: string;
  senha: string;
  telefone: string;
}

export interface VoluntarioFiltro {
  nome?: string;
  email?: string;
  ativo?: boolean;
}
