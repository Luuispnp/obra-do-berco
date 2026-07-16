import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { Save } from "lucide-react";
import * as eventosApi from "@/api/eventos";
import { PageHeader } from "@/components/ui/PageHeader";
import { Card, CardContent } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { extractErrorMessage } from "@/lib/errors";

const formSchema = z.object({
  titulo: z.string().min(1, "Título é obrigatório"),
  dataEvento: z.string().min(1, "Data do evento é obrigatória"),
});

type FormValues = z.infer<typeof formSchema>;

export function EventoFormPage() {
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>({ resolver: zodResolver(formSchema), defaultValues: { titulo: "", dataEvento: "" } });

  async function onSubmit(data: FormValues) {
    setIsSubmitting(true);
    try {
      const criado = await eventosApi.criarEvento(data);
      toast.success("Evento criado.");
      navigate(`/app/eventos/${criado.eventoId}`);
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div>
      <PageHeader title="Novo evento" description="Crie um evento para organizar a entrega de enxovais." />

      <form onSubmit={(event) => void handleSubmit(onSubmit)(event)} className="flex flex-col gap-6">
        <Card>
          <CardContent className="grid gap-4 sm:grid-cols-2">
            <Input label="Título" className="sm:col-span-2" error={errors.titulo?.message} {...register("titulo")} />
            <Input label="Data do evento" type="date" error={errors.dataEvento?.message} {...register("dataEvento")} />
          </CardContent>
        </Card>

        <div className="flex justify-end gap-2">
          <Button type="button" variant="outline" onClick={() => navigate(-1)}>
            Cancelar
          </Button>
          <Button type="submit" isLoading={isSubmitting}>
            <Save className="size-4" />
            Criar evento
          </Button>
        </div>
      </form>
    </div>
  );
}
