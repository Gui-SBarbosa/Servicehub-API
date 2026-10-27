-- A tabela providers foi substituída por users com role = 'PROVIDER'.
-- Esta migration é deliberadamente destrutiva e foi aprovada para remover os dados legados.
DROP TABLE IF EXISTS providers;
