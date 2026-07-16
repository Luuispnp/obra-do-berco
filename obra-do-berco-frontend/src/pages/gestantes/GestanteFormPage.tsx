import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { Save } from "lucide-react";
import * as gestantesApi from "@/api/gestantes";
import { PageHeader } from "@/components/ui/PageHeader";
import { Card, CardContent } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { Select } from "@/components/ui/Select";
import { PageSpinner } from "@/components/ui/Spinner";
import { extractErrorMessage } from "@/lib/errors";
import type { EstadoCivil } from "@/types/gestante";

const estadoCivilOptions: { value: EstadoCivil; label: string }[] = [
  { value: "SOLTEIRA", label: "Solteira" },
  { value: "CASADA", label: "Casada" },
  { value: "UNIAO_ESTAVEL", label: "União estável" },
  { value: "VIUVA", label: "Viúva" },
];

const formSchema = z.object({
  nome: z.string().min(1, "Nome é obrigatório"),
  cpf: z.string().min(11, "CPF inválido").max(14),
  numeroIdentidade: z.string().optional(),
  dataNascimento: z.string().min(1, "Data de nascimento é obrigatória"),
  estadoCivil: z.enum(["SOLTEIRA", "CASADA", "UNIAO_ESTAVEL", "VIUVA"]),
  telefone: z.string().min(1, "Telefone é obrigatório"),
  email: z.union([z.string().email("E-mail inválido"), z.literal("")]),
  cep: z.string().min(1, "CEP é obrigatório"),
  logradouro: z.string().min(1, "Logradouro é obrigatório"),
  bairro: z.string().min(1, "Bairro é obrigatório"),
  cidade: z.string().min(1, "Cidade é obrigatória"),
  referencia: z.string().optional(),
});

type FormValues = z.infer<typeof formSchema>;

const emptyValues: FormValues = {
  nome: "",
  cpf: "",
  numeroIdentidade: "",
  dataNascimento: "",
  estadoCivil: "SOLTEIRA",
  telefone: "",
  email: "",
  cep: "",
  logradouro: "",
  bairro: "",
  cidade: "",
  referencia: "",
};

export function GestanteFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(isEdit);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<FormValues>({ resolver: zodResolver(formSchema), defaultValues: emptyValues });

  useEffect(() => {
    if (!id) return;
    gestantesApi
      .buscarGestante(id)
      .then((gestante) => {
        reset({
          nome: gestante.nome,
          cpf: gestante.cpf,
          numeroIdentidade: gestante.numeroIdentidade ?? "",
          dataNascimento: gestante.dataNascimento.slice(0, 10),
          estadoCivil: gestante.estadoCivil,
          telefone: gestante.telefone,
          email: gestante.email ?? "",
          cep: "",
          logradouro: gestante.endereco.logradouro,
          bairro: gestante.endereco.bairro,
          cidade: gestante.endereco.cidade,
          referencia: gestante.endereco.referencia ?? "",
        });
      })
      .catch((error) => toast.error(extractErrorMessage(error)))
      .finally(() => setIsLoading(false));
  }, [id, reset]);

  async function onSubmit(data: FormValues) {
    setIsSubmitting(true);
    const endereco = {
      cep: data.cep,
      logradouro: data.logradouro,
      bairro: data.bairro,
      cidade: data.cidade,
      referencia: data.referencia || undefined,
    };

    try {
      if (isEdit && id) {
        await gestantesApi.atualizarGestante(id, {
          nome: data.nome,
          estadoCivil: data.estadoCivil,
          telefone: data.telefone,
          email: data.email || undefined,
          endereco,
        });
        toast.success("Gestante atualizada.");
        navigate(`/app/gestantes/${id}`);
      } else {
        const criada = await gestantesApi.criarGestante({
          nome: data.nome,
          cpf: data.cpf,
          numeroIdentidade: data.numeroIdentidade || undefined,
          dataNascimento: data.dataNascimento,
          estadoCivil: data.estadoCivil,
          telefone: data.telefone,
          email: data.email || undefined,
          endereco,
        });
        toast.success("Gestante cadastrada.");
        navigate(`/app/gestantes/${criada.id}`);
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
        title={isEdit ? "Editar gestante" : "Nova gestante"}
        description={
          isEdit
            ? "Documentos e data de nascimento não podem ser alterados após o cadastro."
            : "Preencha os dados para cadastrar uma nova gestante."
        }
      />

      <form onSubmit={(event) => void handleSubmit(onSubmit)(event)} className="flex flex-col gap-6">
        <Card>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <Input
              label="Nome completo"
              className="sm:col-span-2"
              error={errors.nome?.message}
              {...register("nome")}
            />
            <Input
              label="CPF"
              disabled={isEdit}
              placeholder="000.000.000-00"
              error={errors.cpf?.message}
              {...register("cpf")}
            />
            <Input
              label="Nº de identidade (RG)"
              disabled={isEdit}
              error={errors.numeroIdentidade?.message}
              {...register("numeroIdentidade")}
            />
            <Input
              label="Data de nascimento"
              type="date"
              disabled={isEdit}
              error={errors.dataNascimento?.message}
              {...register("dataNascimento")}
            />
            <Select label="Estado civil" error={errors.estadoCivil?.message} {...register("estadoCivil")}>
              {estadoCivilOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </Select>
            <Input label="Telefone" placeholder="(00) 00000-0000" error={errors.telefone?.message} {...register("telefone")} />
            <Input label="E-mail" type="email" error={errors.email?.message} {...register("email")} />
          </CardContent>
        </Card>

        <Card>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <h2 className="text-base font-semibold text-slate-800 sm:col-span-2">Endereço</h2>
            <Input label="CEP" error={errors.cep?.message} {...register("cep")} />
            <Input label="Logradouro" error={errors.logradouro?.message} {...register("logradouro")} />
            <Input label="Bairro" error={errors.bairro?.message} {...register("bairro")} />
            <Input label="Cidade" error={errors.cidade?.message} {...register("cidade")} />
            <Input
              label="Ponto de referência"
              className="sm:col-span-2"
              error={errors.referencia?.message}
              {...register("referencia")}
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
