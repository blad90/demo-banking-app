import { auth } from "@/app/api/auth/[...nextauth]/route"
import { signOut } from "@/app/lib/auth"
import { NextResponse } from "next/server"

export async function GET() {
  const session = await auth()

  if (!session?.idToken) {
    return NextResponse.redirect("http://localhost:3000/login")
  }

  await signOut({ redirect: false })

  const keycloakLogoutUrl = new URL(
    "http://localhost:9090/realms/demo-bank-realm/protocol/openid-connect/logout"
  )

  keycloakLogoutUrl.searchParams.set("id_token_hint", session.idToken)
  keycloakLogoutUrl.searchParams.set(
    "post_logout_redirect_uri",
    "http://localhost:3000/login"
  )

  return NextResponse.redirect(keycloakLogoutUrl.toString())
}
