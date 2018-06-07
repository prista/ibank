ALTER TABLE "account"
  DROP CONSTRAINT IF EXISTS "account_fk0";

ALTER TABLE "account"
  DROP CONSTRAINT IF EXISTS "account_fk1";

ALTER TABLE "transaction"
  DROP CONSTRAINT IF EXISTS "transaction_fk0";

ALTER TABLE "transaction"
  DROP CONSTRAINT IF EXISTS "transaction_fk1";

ALTER TABLE "transaction"
  DROP CONSTRAINT IF EXISTS "transaction_fk2";

ALTER TABLE "payment_type"
  DROP CONSTRAINT IF EXISTS "payment_type_fk0";

ALTER TABLE "card"
  DROP CONSTRAINT IF EXISTS "card_fk0";

ALTER TABLE "branch"
  DROP CONSTRAINT IF EXISTS "branch_fk0";

ALTER TABLE "user_profile_2_branch"
  DROP CONSTRAINT IF EXISTS "user_profile_2_branch_fk0";

ALTER TABLE "user_profile_2_branch"
  DROP CONSTRAINT IF EXISTS "user_profile_2_branch_fk1";

DROP TABLE IF EXISTS "user_profile";

DROP TABLE IF EXISTS "account";

DROP TABLE IF EXISTS "transaction";

DROP TABLE IF EXISTS "payment_type";

DROP TABLE IF EXISTS "card";

DROP TABLE IF EXISTS "branch";

DROP TABLE IF EXISTS "bank";

DROP TABLE IF EXISTS "user_profile_2_branch";
