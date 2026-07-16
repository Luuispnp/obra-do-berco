import { AxiosError } from "axios";
import type { ApiErrorBody } from "@/types/common";

export function extractErrorMessage(error: unknown): string {
  if (error instanceof AxiosError) {
    const body = error.response?.data as ApiErrorBody | undefined;
    if (body?.error?.message) {
      return body.error.message;
    }
  }
  return "Ocorreu um erro inesperado. Tente novamente.";
}

export function extractFieldErrors(error: unknown): Record<string, string> {
  if (error instanceof AxiosError) {
    const body = error.response?.data as ApiErrorBody | undefined;
    const details = body?.error?.details;
    if (details?.length) {
      return Object.fromEntries(details.map((detail) => [detail.field, detail.message]));
    }
  }
  return {};
}
