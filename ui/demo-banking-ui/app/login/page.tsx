"use client"

import { signIn } from "next-auth/react"

export default function LoginPage() {
  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>OB DemoBanking</h2>
        <p style={styles.subtitle}>Secure Login</p>

        <button
          style={styles.button}
          onClick={() => signIn("keycloak",
            { callbackUrl: "/dashboard/transactions",}
          )}
        >
          Login with Keycloak
        </button>
      </div>
    </div>
  )
}

const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#f5f7fa",
  },
  card: {
    backgroundColor: "white",
    padding: "40px",
    borderRadius: "12px",
    boxShadow: "0 10px 25px rgba(0,0,0,0.1)",
    textAlign: "center" as const,
    width: "350px",
  },
  title: {
    marginBottom: "10px",
  },
  subtitle: {
    marginBottom: "30px",
    color: "gray",
  },
  button: {
    padding: "12px 20px",
    width: "100%",
    borderRadius: "8px",
    border: "none",
    backgroundColor: "#4f46e5",
    color: "white",
    fontWeight: "bold",
    cursor: "pointer",
  },
}
