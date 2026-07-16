import { apiClient } from "@/api/client";

function nomeArquivo(contentDisposition: string | undefined, fallback: string): string {
  const match = contentDisposition?.match(/filename="?([^"]+)"?/);
  return match?.[1] ?? fallback;
}

function dispararDownload(blob: Blob, filename: string): void {
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
}

async function baixarPdf(caminho: string, nomeFallback: string): Promise<void> {
  const response = await apiClient.get(caminho, { responseType: "blob" });
  const filename = nomeArquivo(response.headers["content-disposition"], nomeFallback);
  dispararDownload(new Blob([response.data], { type: "application/pdf" }), filename);
}

export async function baixarFichaIndividual(solicitacaoId: string): Promise<void> {
  await baixarPdf(`/documentos/ficha/${solicitacaoId}`, `ficha-${solicitacaoId}.pdf`);
}

export async function baixarListaEvento(eventoId: string): Promise<void> {
  await baixarPdf(`/documentos/eventos/${eventoId}/lista`, `lista-evento-${eventoId}.pdf`);
}

export async function baixarFichasEvento(eventoId: string): Promise<void> {
  await baixarPdf(`/documentos/eventos/${eventoId}/fichas`, `fichas-evento-${eventoId}.pdf`);
}
