import { createContext, useCallback, useEffect, useMemo, useState, type ReactNode } from "react";
import * as authApi from "@/api/auth";
import {
  AUTH_LOGOUT_EVENT,
  clearTokens,
  getRefreshToken,
  setAccessToken,
  setRefreshToken,
} from "@/lib/tokenStore";
import type { Usuario } from "@/types/auth";

interface AuthContextValue {
  user: Usuario | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  isAdmin: boolean;
  login: (email: string, senha: string) => Promise<void>;
  logout: () => Promise<void>;
}

// eslint-disable-next-line react-refresh/only-export-components
export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<Usuario | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function rehydrate() {
      const existingRefreshToken = getRefreshToken();
      if (!existingRefreshToken) {
        setIsLoading(false);
        return;
      }

      try {
        const { accessToken } = await authApi.refresh(existingRefreshToken);
        setAccessToken(accessToken);
        const usuario = await authApi.me();
        setUser(usuario);
      } catch {
        clearTokens();
      } finally {
        setIsLoading(false);
      }
    }

    void rehydrate();
  }, []);

  useEffect(() => {
    function handleLoggedOut() {
      setUser(null);
    }

    window.addEventListener(AUTH_LOGOUT_EVENT, handleLoggedOut);
    return () => window.removeEventListener(AUTH_LOGOUT_EVENT, handleLoggedOut);
  }, []);

  const login = useCallback(async (email: string, senha: string) => {
    const response = await authApi.login({ email, senha });
    setAccessToken(response.accessToken);
    setRefreshToken(response.refreshToken);
    setUser(response.user);
  }, []);

  const logout = useCallback(async () => {
    const currentRefreshToken = getRefreshToken();
    clearTokens();
    setUser(null);
    if (currentRefreshToken) {
      try {
        await authApi.logout(currentRefreshToken);
      } catch {
        // já limpamos o estado local; falha ao revogar no servidor não é crítica aqui
      }
    }
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      isLoading,
      isAuthenticated: user !== null,
      isAdmin: user?.role === "admin",
      login,
      logout,
    }),
    [user, isLoading, login, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
