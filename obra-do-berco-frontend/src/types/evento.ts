export type StatusEvento = "CRIADO" | "FINALIZADO";

export interface EventoParticipanteResumo {
  participanteId: string;
  solicitacaoId: string;
  gestanteId: string;
  presente: boolean;
}

export interface Evento {
  eventoId: string;
  titulo: string;
  dataEvento: string;
  status: StatusEvento;
  dataCriacao: string;
  dataFinalizacao?: string | null;
  participantes: EventoParticipanteResumo[];
}

export interface EventoInput {
  titulo: string;
  dataEvento: string;
}

export interface EventoFiltro {
  dataInicio?: string;
  dataFim?: string;
  status?: StatusEvento;
}

export interface Participante {
  participanteId: string;
  solicitacaoId: string;
  gestanteId: string;
  nomeGestante: string;
  presente: boolean;
}

export interface GestanteDisponivel {
  solicitacaoId: string;
  gestanteId: string;
}
