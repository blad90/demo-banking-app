-- If db exists, it'll throw an error.
-- Commented out when any db exists.
-- TODO: Pending to improve this script.
SELECT 'CREATE DATABASE users_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'users_db')\gexec
SELECT 'CREATE DATABASE accounts_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'accounts_db')\gexec
SELECT 'CREATE DATABASE transactions_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'transactions_db')\gexec