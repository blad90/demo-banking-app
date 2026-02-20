"use client"

import LoginPage from "./page"


export default function LogInButton() {
  return (
    <button
      type="button"
      onClick={() => window.location.href = "/api/auth/login"}>
      Sign In
    </button>
  )
}
