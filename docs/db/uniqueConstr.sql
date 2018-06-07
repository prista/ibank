ALTER TABLE user_profile_2_branch
  ADD CONSTRAINT user_profile_2_branch_user_profile_id_branch_id_key UNIQUE (user_profile_id, branch_id);

------------------------------------------------------------------------------
ALTER TABLE public.account
  ALTER COLUMN balance SET DEFAULT 0;
------------------------------------------------------------------------------
------------------------------------------------------------------------------
UPDATE pg_database
SET datallowconn = 'false'
WHERE datname = 'ibank';
------------------------------------------------------------------------------
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'ibank' AND pid <> pg_backend_pid();
------------------------------------------------------------------------------
DROP DATABASE ibank;