export type StatusSolicitacao =
  | "EM_ANALISE"
  | "APROVADA"
  | "RECUSADA"
  | "ENTREGUE"
  | "NAO_COMPARECEU";

export type SexoCrianca = "MASCULINO" | "FEMININO" | "NAO_INFORMADO";

export interface Solicitacao {
  solicitacaoId: string;
  gestanteId: string;
  voluntarioResponsavelId: string;
  status: StatusSolicitacao;
  dataSolicitacao: string;
  idadeGestacionalSemanas: number;
  dataProvavelParto: string;
  sexoCrianca: SexoCrianca | null;
  nomeDoPai?: string | null;
  trabalhando?: boolean | null;
  observacaoGravidez?: string | null;
  qtdPessoasResidencia?: number | null;
  cartaoPreNatal: boolean;
  religiao?: string | null;
  atendimentoSocial?: string | null;
  beneficioSocial?: string | null;
  motivoRecusa?: string | null;
  dataEncerramento?: string | null;
}

export interface SolicitacaoInput {
  gestanteId: string;
  voluntarioResponsavelId: string;
  idadeGestacionalSemanas: number;
  dataProvavelParto: string;
  sexoCrianca?: SexoCrianca;
  nomeDoPai?: string;
  trabalhando?: boolean;
  observacaoGravidez?: string;
  qtdPessoasResidencia?: number;
  cartaoPreNatal: boolean;
  religiao?: string;
  atendimentoSocial?: string;
  beneficioSocial?: string;
}

export type SolicitacaoUpdateInput = Omit<
  SolicitacaoInput,
  "gestanteId" | "voluntarioResponsavelId"
>;

export interface SolicitacaoFiltro {
  status?: StatusSolicitacao;
  gestanteId?: string;
  dataInicio?: string;
  dataFim?: string;
}
