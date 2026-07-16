import { useState, type ReactNode } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { ArrowLeft, Check, FileDown, Pencil, X } from "lucide-react";
import * as solicitacoesApi from "@/api/solicitacoes";
import * as gestantesApi from "@/api/gestantes";
import * as voluntariosApi from "@/api/voluntarios";
import * as documentosApi from "@/api/documentos";
import { useAsync } from "@/hooks/useAsync";
import { PageHeader } from "@/components/ui/PageHeader";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import { Button, buttonVariants } from "@/components/ui/Button";
import { PageSpinner } from "@/components/ui/Spinner";
import { StatusSolicitacaoBadge } from "@/components/ui/StatusBadges";
import { Modal } from "@/components/ui/Modal";
import { Textarea } from "@/components/ui/Textarea";
import { extractErrorMessage } from "@/lib/errors";
import { formatDate } from "@/lib/utils";

export function SolicitacaoDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [isProcessing, setIsProcessing] = useState(false);
  const [baixandoFicha, setBaixandoFicha] = useState(false);
  const [recusando, setRecusando] = useState(false);
  const [motivoRecusa, setMotivoRecusa] = useState("");

  const { data: solicitacao, isLoading, reload } = useAsync(
    () => solicitacoesApi.buscarSolicitacao(id!),
    [id],
  );
  const { data: gestante } = useAsync(
    () => (solicitacao ? gestantesApi.buscarGestante(solicitacao.gestanteId) : Promise.resolve(null)),
    [solicitacao?.gestanteId],
  );
  const { data: voluntario } = useAsync(
    () => (solicitacao ? voluntariosApi.buscarVoluntario(solicitacao.voluntarioResponsavelId) : Promise.resolve(null)),
    [solicitacao?.voluntarioResponsavelId],
  );

  async function aprovar() {
    if (!id) return;
    setIsProcessing(true);
    try {
      await solicitacoesApi.aprovarSolicitacao(id);
      toast.success("Solicitação aprovada.");
      reload();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setIsProcessing(false);
    }
  }

  async function recusar() {
    if (!id) return;
    setIsProcessing(true);
    try {
      await solicitacoesApi.recusarSolicitacao(id, motivoRecusa);
      toast.success("Solicitação recusada.");
      setRecusando(false);
      setMotivoRecusa("");
      reload();
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setIsProcessing(false);
    }
  }

  async function emitirFicha() {
    if (!id) return;
    setBaixandoFicha(true);
    try {
      await documentosApi.baixarFichaIndividual(id);
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setBaixandoFicha(false);
    }
  }

  if (isLoading) return <PageSpinner />;
  if (!solicitacao) return null;

  const podeAprovarOuRecusar = solicitacao.status === "EM_ANALISE";

  return (
    <div>
      <button
        onClick={() => navigate("/app/solicitacoes")}
        className="mb-4 flex items-center gap-1 text-sm text-slate-500 hover:text-brand-600"
      >
        <ArrowLeft className="size-4" />
        Voltar para solicitações
      </button>

      <PageHeader
        title={gestante?.nome ?? "Solicitação"}
        description={`Registrada em ${formatDate(solicitacao.dataSolicitacao)}`}
        actions={
          <>
            <Button variant="outline" isLoading={baixandoFicha} onClick={emitirFicha}>
              <FileDown className="size-4" />
              Emitir ficha
            </Button>
            <Link to={`/app/solicitacoes/${solicitacao.solicitacaoId}/editar`} className={buttonVariants("outline", "md")}>
              <Pencil className="size-4" />
              Editar
            </Link>
            {podeAprovarOuRecusar && (
              <>
                <Button variant="danger" onClick={() => setRecusando(true)}>
                  <X className="size-4" />
                  Recusar
                </Button>
                <Button variant="primary" isLoading={isProcessing} onClick={aprovar}>
                  <Check className="size-4" />
                  Aprovar
                </Button>
              </>
            )}
          </>
        }
      />

      <div className="grid gap-6 lg:grid-cols-3">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle>Dados da solicitação</CardTitle>
          </CardHeader>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <Info label="Status" value={<StatusSolicitacaoBadge status={solicitacao.status} />} />
            <Info label="Voluntário responsável" value={voluntario?.nomeCompleto ?? "-"} />
            <Info label="Idade gestacional" value={`${solicitacao.idadeGestacionalSemanas} semanas`} />
            <Info label="Data provável do parto" value={formatDate(solicitacao.dataProvavelParto)} />
            <Info label="Cartão de pré-natal" value={solicitacao.cartaoPreNatal ? "Sim" : "Não"} />
            <Info label="Sexo da criança" value={sexoLabel(solicitacao.sexoCrianca)} />
            <Info label="Nome do pai" value={solicitacao.nomeDoPai ?? "-"} />
            <Info label="Está trabalhando" value={solicitacao.trabalhando ? "Sim" : "Não"} />
            <Info label="Religião" value={solicitacao.religiao ?? "-"} />
            <Info label="Nº de pessoas na residência" value={solicitacao.qtdPessoasResidencia ?? "-"} />
            <Info label="Benefício do governo" value={solicitacao.beneficioSocial ?? "-"} />
            <Info label="Atendimento profissional" value={solicitacao.atendimentoSocial ?? "-"} />
            {solicitacao.observacaoGravidez && (
              <Info label="Observações" value={solicitacao.observacaoGravidez} />
            )}
            {solicitacao.status === "RECUSADA" && solicitacao.motivoRecusa && (
              <Info label="Motivo da recusa" value={solicitacao.motivoRecusa} />
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Gestante</CardTitle>
          </CardHeader>
          <CardContent>
            {gestante ? (
              <Link to={`/app/gestantes/${gestante.id}`} className={buttonVariants("outline", "md", "w-full")}>
                {gestante.nome}
              </Link>
            ) : (
              <PageSpinner />
            )}
          </CardContent>
        </Card>
      </div>

      <Modal open={recusando} onClose={() => setRecusando(false)} title="Recusar solicitação">
        <div className="flex flex-col gap-4">
          <Textarea
            label="Motivo da recusa"
            value={motivoRecusa}
            onChange={(event) => setMotivoRecusa(event.target.value)}
            placeholder="Explique o motivo da recusa..."
          />
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setRecusando(false)} disabled={isProcessing}>
              Cancelar
            </Button>
            <Button variant="danger" onClick={recusar} isLoading={isProcessing} disabled={!motivoRecusa.trim()}>
              Confirmar recusa
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}

function Info({ label, value }: { label: string; value: ReactNode }) {
  return (
    <div>
      <p className="text-xs font-medium uppercase tracking-wide text-slate-400">{label}</p>
      <p className="mt-0.5 text-sm text-slate-700">{value}</p>
    </div>
  );
}

function sexoLabel(sexo: string | null): string {
  if (sexo === "MASCULINO") return "Masculino";
  if (sexo === "FEMININO") return "Feminino";
  return "Não informado";
}
