package auth

import (
	"context"
	"net/http"
	"strings"
	"sync"

	"github.com/coreos/go-oidc/v3/oidc"
)

// RequireToken validates a Keycloak-issued JWT - the same access tokens the rest of the
// system already uses - before letting a request through. It checks issuer, signature and
// expiry only, not audience, matching how the API gateway's resource-server config already
// validates these tokens elsewhere in the system.
//
// The verifier is resolved lazily against issuerURL's OIDC discovery document on first use
// (and retried on failure) rather than at startup, so this service doesn't need Keycloak to
// already be up when its own process starts.
func RequireToken(issuerURL string) func(http.HandlerFunc) http.HandlerFunc {
	var (
		mu       sync.Mutex
		verifier *oidc.IDTokenVerifier
	)

	getVerifier := func(ctx context.Context) (*oidc.IDTokenVerifier, error) {
		mu.Lock()
		defer mu.Unlock()
		if verifier != nil {
			return verifier, nil
		}
		provider, err := oidc.NewProvider(ctx, issuerURL)
		if err != nil {
			return nil, err
		}
		verifier = provider.Verifier(&oidc.Config{SkipClientIDCheck: true})
		return verifier, nil
	}

	return func(next http.HandlerFunc) http.HandlerFunc {
		return func(w http.ResponseWriter, r *http.Request) {
			token := bearerToken(r)
			if token == "" {
				http.Error(w, "missing bearer token", http.StatusUnauthorized)
				return
			}
			v, err := getVerifier(r.Context())
			if err != nil {
				http.Error(w, "token verifier unavailable", http.StatusServiceUnavailable)
				return
			}
			if _, err := v.Verify(r.Context(), token); err != nil {
				http.Error(w, "invalid token", http.StatusUnauthorized)
				return
			}
			next(w, r)
		}
	}
}

// bearerToken reads the token from the Authorization header first (for non-browser
// clients), falling back to a query parameter - the browser EventSource API can't set
// custom headers, so a query parameter is the standard way to authenticate an SSE
// connection from a browser.
func bearerToken(r *http.Request) string {
	if h := r.Header.Get("Authorization"); strings.HasPrefix(h, "Bearer ") {
		return strings.TrimPrefix(h, "Bearer ")
	}
	return r.URL.Query().Get("token")
}
