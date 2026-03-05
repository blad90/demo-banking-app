// lib/auth.ts
import NextAuth from "next-auth"
import Keycloak from "next-auth/providers/keycloak"

export const { handlers, auth, signIn, signOut } = NextAuth({
  providers: [
    Keycloak({
      clientId: process.env.KEYCLOAK_CLIENT_ID!,
      clientSecret: process.env.KEYCLOAK_CLIENT_SECRET!,
      issuer: "http://localhost:9090/realms/demo-bank-realm",
    }),
  ],
  callbacks: {
    async jwt({ token, account, profile }) {
      if (account?.access_token) {
        token.accessToken = account.access_token
        token.idToken = account.id_token
        token.userId = profile?.sub
      }
      return token
    },
    async session({ session, token }) {
      if (typeof token.accessToken === "string") {
        session.accessToken = token.accessToken
        session.idToken = token.idToken as string
        session.userId = token.userId as string
      }
      return session
    },
  },
})
