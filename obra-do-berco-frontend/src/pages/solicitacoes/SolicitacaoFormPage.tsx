import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { toast } from "sonner";
import { Save } from "lucide-react";
import * as solicitacoesApi from "@/api/solicitacoes";
import * as gestantesApi from "@/api/gestantes";
import * as voluntariosApi from "@/api/voluntarios";
import { useAuth } from "@/hooks/useAuth";
import { PageHeader } from "@/components/ui/PageHeader";
import { Card, CardContent } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { Select } from "@/components/ui/Select";
import { Textarea } from "@/components/ui/Textarea";
import { Checkbox } from "@/components/ui/Checkbox";
import { PageSpinner } from "@/components/ui/Spinner";
import { extractErrorMessage } from "@/lib/errors";
import type { Gestante } from "@/types/gestante";
import type { Voluntario } from "@/types/voluntario";

const formSchema = z.object({
  gestanteId: z.string().min(1, "Selecione a gestante"),
  voluntarioResponsavelId: z.string().min(1, "Selecione o voluntário responsável"),
  idadeGestacionalSemanas: z.coerce.number().int().min(1, "Informe a idade gestacional").max(45),
  dataProvavelParto: z.string().min(1, "Informe a data provável do parto"),
  sexoCrianca: z.union([z.literal("MASCULINO"), z.literal("FEMININO"), z.literal("NAO_INFORMADO"), z.literal("")]),
  nomeDoPai: z.string().optional(),
  trabalhando: z.boolean(),
  observacaoGravidez: z.string().optional(),
  qtdPessoasResidencia: z.coerce.number().int().min(0).optional(),
  cartaoPreNatal: z.boolean(),
  religiao: z.string().optional(),
  atendimentoSocial: z.string().optional(),
  beneficioSocial: z.string().optional(),
});

type FormInput = z.input<typeof formSchema>;
type FormValues = z.output<typeof formSchema>;

export function SolicitacaoFormPage() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const { user } = useAuth();

  const [isLoading, setIsLoading] = useState(isEdit);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [gestantes, setGestantes] = useState<Gestante[]>([]);
  const [voluntarios, setVoluntarios] = useState<Voluntario[]>([]);
  const [gestanteFixa, setGestanteFixa] = useState<Gestante | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<FormInput, unknown, FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      gestanteId: searchParams.get("gestanteId") ?? "",
      voluntarioResponsavelId: user?.id ?? "",
      idadeGestacionalSemanas: 0,
      dataProvavelParto: "",
      sexoCrianca: "",
      nomeDoPai: "",
      trabalhando: false,
      observacaoGravidez: "",
      qtdPessoasResidencia: undefined,
      cartaoPreNatal: false,
      religiao: "",
      atendimentoSocial: "",
      beneficioSocial: "",
    },
  });

  useEffect(() => {
    voluntariosApi.listarVoluntarios({ ativo: true }).then(setVoluntarios).catch(() => undefined);

    const gestanteIdFixo = searchParams.get("gestanteId");
    if (gestanteIdFixo) {
      gestantesApi.buscarGestante(gestanteIdFixo).then(setGestanteFixa).catch(() => undefined);
    } else {
      gestantesApi.listarGestantes({}).then(setGestantes).catch(() => undefined);
    }
  }, [searchParams]);

  useEffect(() => {
    if (!id) return;
    solicitacoesApi
      .buscarSolicitacao(id)
      .then((solicitacao) => {
        reset({
          gestanteId: solicitacao.gestanteId,
          voluntarioResponsavelId: solicitacao.voluntarioResponsavelId,
          idadeGestacionalSemanas: solicitacao.idadeGestacionalSemanas,
          dataProvavelParto: solicitacao.dataProvavelParto.slice(0, 10),
          sexoCrianca: solicitacao.sexoCrianca ?? "",
          nomeDoPai: solicitacao.nomeDoPai ?? "",
          trabalhando: solicitacao.trabalhando ?? false,
          observacaoGravidez: solicitacao.observacaoGravidez ?? "",
          qtdPessoasResidencia: solicitacao.qtdPessoasResidencia ?? undefined,
          cartaoPreNatal: solicitacao.cartaoPreNatal,
          religiao: solicitacao.religiao ?? "",
          atendimentoSocial: solicitacao.atendimentoSocial ?? "",
          beneficioSocial: solicitacao.beneficioSocial ?? "",
        });
        gestantesApi.buscarGestante(solicitacao.gestanteId).then(setGestanteFixa).catch(() => undefined);
      })
      .catch((error) => toast.error(extractErrorMessage(error)))
      .finally(() => setIsLoading(false));
  }, [id, reset]);

  async function onSubmit(data: FormValues) {
    setIsSubmitting(true);
    const payloadBase = {
      idadeGestacionalSemanas: data.idadeGestacionalSemanas,
      dataProvavelParto: data.dataProvavelParto,
      sexoCrianca: data.sexoCrianca || undefined,
      nomeDoPai: data.nomeDoPai || undefined,
      trabalhando: data.trabalhando,
      observacaoGravidez: data.observacaoGravidez || undefined,
      qtdPessoasResidencia: data.qtdPessoasResidencia,
      cartaoPreNatal: data.cartaoPreNatal,
      religiao: data.religiao || undefined,
      atendimentoSocial: data.atendimentoSocial || undefined,
      beneficioSocial: data.beneficioSocial || undefined,
    };

    try {
      if (isEdit && id) {
        await solicitacoesApi.atualizarSolicitacao(id, payloadBase);
        toast.success("Solicitação atualizada.");
        navigate(`/app/solicitacoes/${id}`);
      } else {
        const criada = await solicitacoesApi.criarSolicitacao({
          ...payloadBase,
          gestanteId: data.gestanteId,
          voluntarioResponsavelId: data.voluntarioResponsavelId,
        });
        toast.success("Solicitação registrada.");
        navigate(`/app/solicitacoes/${criada.solicitacaoId}`);
      }
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setIsSubmitting(false);
    }
  }

  if (isLoading) return <PageSpinner />;

  return (
    <div>
      <PageHeader
        title={isEdit ? "Editar solicitação" : "Nova solicitação"}
        description="Informações usadas para análise e para a ficha de cadastro impressa."
      />

      <form onSubmit={(event) => void handleSubmit(onSubmit)(event)} className="flex flex-col gap-6">
        <Card>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            {gestanteFixa ? (
              <Input label="Gestante" value={gestanteFixa.nome} disabled />
            ) : (
              <Select label="Gestante" error={errors.gestanteId?.message} {...register("gestanteId")} disabled={isEdit}>
                <option value="">Selecione...</option>
                {gestantes.map((gestante) => (
                  <option key={gestante.id} value={gestante.id}>
                    {gestante.nome}
                  </option>
                ))}
              </Select>
            )}

            <Select
              label="Voluntário responsável"
              error={errors.voluntarioResponsavelId?.message}
              disabled={isEdit}
              {...register("voluntarioResponsavelId")}
            >
              <option value="">Selecione...</option>
              {voluntarios.map((voluntario) => (
                <option key={voluntario.voluntarioId} value={voluntario.voluntarioId}>
                  {voluntario.nomeCompleto}
                </option>
              ))}
            </Select>

            <Input
              label="Idade gestacional (semanas)"
              type="number"
              error={errors.idadeGestacionalSemanas?.message}
              {...register("idadeGestacionalSemanas")}
            />
            <Input
              label="Data provável do parto"
              type="date"
              error={errors.dataProvavelParto?.message}
              {...register("dataProvavelParto")}
            />
            <Select label="Sexo da criança" {...register("sexoCrianca")}>
              <option value="">Não informado</option>
              <option value="MASCULINO">Masculino</option>
              <option value="FEMININO">Feminino</option>
              <option value="NAO_INFORMADO">Não informado</option>
            </Select>
            <Input label="Nome do pai" {...register("nomeDoPai")} />
            <Checkbox label="Cartão de pré-natal em dia" {...register("cartaoPreNatal")} />
            <Checkbox label="Está trabalhando atualmente" {...register("trabalhando")} />
          </CardContent>
        </Card>

        <Card>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <h2 className="text-base font-semibold text-slate-800 sm:col-span-2">
              Dados socioassistenciais
            </h2>
            <Input label="Religião" {...register("religiao")} />
            <Input
              label="Nº de pessoas na residência"
              type="number"
              error={errors.qtdPessoasResidencia?.message}
              {...register("qtdPessoasResidencia")}
            />
            <Input label="Recebe benefício do governo" {...register("beneficioSocial")} />
            <Input label="Precisa de atendimento profissional" {...register("atendimentoSocial")} />
            <Textarea
              label="Observações sobre a gravidez"
              className="sm:col-span-2"
              {...register("observacaoGravidez")}
            />
          </CardContent>
        </Card>

        <div className="flex justify-end gap-2">
          <Button type="button" variant="outline" onClick={() => navigate(-1)}>
            Cancelar
          </Button>
          <Button type="submit" isLoading={isSubmitting}>
            <Save className="size-4" />
            Salvar
          </Button>
        </div>
      </form>
    </div>
  );
}
