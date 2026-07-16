export type EstadoCivil = "SOLTEIRA" | "CASADA" | "UNIAO_ESTAVEL" | "VIUVA";

export interface Endereco {
  logradouro: string;
  bairro: string;
  cidade: string;
  referencia?: string | null;
}

export interface EnderecoInput {
  cep: string;
  logradouro: string;
  bairro: string;
  cidade: string;
  referencia?: string;
}

export interface Gestante {
  id: string;
  nome: string;
  cpf: string;
  numeroIdentidade?: string | null;
  dataNascimento: string;
  endereco: Endereco;
  estadoCivil: EstadoCivil;
  telefone: string;
  email?: string | null;
  dataCadastro: string;
  ativo: boolean;
}

export interface GestanteInput {
  nome: string;
  cpf: string;
  numeroIdentidade?: string;
  dataNascimento: string;
  estadoCivil: EstadoCivil;
  telefone: string;
  email?: string;
  endereco: EnderecoInput;
}

export interface GestanteUpdateInput {
  nome: string;
  estadoCivil: EstadoCivil;
  telefone: string;
  email?: string;
  endereco: EnderecoInput;
}

export interface GestanteFiltro {
  nome?: string;
  cpf?: string;
  telefone?: string;
}
