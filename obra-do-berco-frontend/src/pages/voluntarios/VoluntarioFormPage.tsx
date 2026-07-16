import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { Save } from "lucide-react";
import * as voluntariosApi from "@/api/voluntarios";
import { PageHeader } from "@/components/ui/PageHeader";
import { Card, CardContent } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { PageSpinner } from "@/components/ui/Spinner";
import { extractErrorMessage } from "@/lib/errors";

const formSchema = z.object({
  nomeCompleto: z.string().min(1, "Nome é obrigatório"),
  email: z.string().min(1, "E-mail é obrigatório").email("Formato de e-mail inválido"),
  telefone: z.string().min(1, "Telefone é obrigatório"),
  senha: z.string().min(6, "A senha deve ter ao menos 6 caracteres"),
});

type FormValues = z.infer<typeof formSchema>;

export function VoluntarioFormPage() {
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
  } = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: { nomeCompleto: "", email: "", telefone: "", senha: "" },
  });

  useEffect(() => {
    if (!id) return;
    voluntariosApi
      .buscarVoluntario(id)
      .then((voluntario) => {
        reset({
          nomeCompleto: voluntario.nomeCompleto,
          email: voluntario.email,
          telefone: voluntario.telefone,
          senha: "",
        });
      })
      .catch((error) => toast.error(extractErrorMessage(error)))
      .finally(() => setIsLoading(false));
  }, [id, reset]);

  async function onSubmit(data: FormValues) {
    setIsSubmitting(true);
    try {
      if (isEdit && id) {
        await voluntariosApi.atualizarVoluntario(id, data);
        toast.success("Voluntário atualizado.");
      } else {
        await voluntariosApi.criarVoluntario(data);
        toast.success("Voluntário cadastrado.");
      }
      navigate("/app/voluntarios");
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
        title={isEdit ? "Editar voluntário" : "Novo voluntário"}
        description={isEdit ? "Atualize os dados do voluntário." : "Cadastre um novo voluntário para acessar o sistema."}
      />

      <form onSubmit={(event) => void handleSubmit(onSubmit)(event)} className="flex flex-col gap-6">
        <Card>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <Input
              label="Nome completo"
              className="sm:col-span-2"
              error={errors.nomeCompleto?.message}
              {...register("nomeCompleto")}
            />
            <Input label="E-mail" type="email" error={errors.email?.message} {...register("email")} />
            <Input label="Telefone" placeholder="(00) 00000-0000" error={errors.telefone?.message} {...register("telefone")} />
            <Input
              label="Senha"
              type="password"
              hint={isEdit ? "A atualização exige uma nova senha para o voluntário." : undefined}
              error={errors.senha?.message}
              {...register("senha")}
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
