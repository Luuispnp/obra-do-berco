import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Heart, LogIn } from "lucide-react";
import { toast } from "sonner";
import { useAuth } from "@/hooks/useAuth";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { extractErrorMessage } from "@/lib/errors";

const loginSchema = z.object({
  email: z.string().min(1, "E-mail é obrigatório").email("Formato de e-mail inválido"),
  senha: z.string().min(1, "Senha é obrigatória"),
});

type LoginForm = z.infer<typeof loginSchema>;

export function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginForm>({ resolver: zodResolver(loginSchema) });

  const from = (location.state as { from?: Location })?.from?.pathname ?? "/app";

  async function onSubmit(data: LoginForm) {
    setIsSubmitting(true);
    try {
      await login(data.email, data.senha);
      navigate(from, { replace: true });
    } catch (error) {
      toast.error(extractErrorMessage(error));
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-b from-brand-50 via-white to-white px-4">
      <div className="w-full max-w-sm">
        <div className="mb-8 flex flex-col items-center gap-2 text-center">
          <span className="flex size-12 items-center justify-center rounded-full bg-brand-100">
            <Heart className="size-6 text-brand-600" />
          </span>
          <h1 className="text-xl font-semibold text-slate-800">Obra do Berço</h1>
          <p className="text-sm text-slate-500">Entre com sua conta para continuar</p>
        </div>

        <form
          onSubmit={(event) => void handleSubmit(onSubmit)(event)}
          className="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-6 shadow-sm shadow-slate-200/50"
        >
          <Input
            label="E-mail"
            type="email"
            autoComplete="email"
            placeholder="voce@exemplo.org"
            error={errors.email?.message}
            {...register("email")}
          />
          <Input
            label="Senha"
            type="password"
            autoComplete="current-password"
            placeholder="••••••••"
            error={errors.senha?.message}
            {...register("senha")}
          />
          <Button type="submit" isLoading={isSubmitting} className="mt-2 justify-center">
            <LogIn className="size-4" />
            Entrar
          </Button>
        </form>

        <p className="mt-6 text-center text-sm text-slate-400">
          <Link to="/" className="hover:text-brand-600">
            Voltar para o site
          </Link>
        </p>
      </div>
    </div>
  );
}
