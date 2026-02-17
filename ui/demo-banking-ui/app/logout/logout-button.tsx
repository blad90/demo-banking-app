"use client"

import { signOut } from "next-auth/react"

export default function LogoutButton() {
  return (
    <button
      type="button"
      onClick={() => window.location.href = "/api/auth/logout"}>
      Sign Out
    </button>
  )
}
