CREATE TABLE "user_profile" (
  "id"       SERIAL                 NOT NULL,
  "username" CHARACTER VARYING(150) NOT NULL UNIQUE,
  "password" CHARACTER VARYING(150) NOT NULL,
  "role"     CHARACTER VARYING      NOT NULL,
  "created"  TIMESTAMP              NOT NULL,
  "updated"  TIMESTAMP              NOT NULL,
  CONSTRAINT user_profile_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);



CREATE TABLE "account" (
  "id"              SERIAL                 NOT NULL,
  "name"            CHARACTER VARYING(150) NOT NULL,
  "user_profile_id" integer,
  "account_type"    CHARACTER VARYING      NOT NULL,
  "balance"         NUMERIC(12, 2) DEFAULT '0.0',
  "currency"        CHARACTER VARYING(10)  NOT NULL,
  "locked"          BOOLEAN                NOT NULL,
  "bank_id"         INTEGER                NOT NULL,
  "deleted"         BOOLEAN                NOT NULL,
  "created"         TIMESTAMP              NOT NULL,
  "updated"         TIMESTAMP              NOT NULL,
  CONSTRAINT account_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);



CREATE TABLE "transaction" (
  "id"               SERIAL                 NOT NULL,
  "from_account_id"  INTEGER                NOT NULL,
  "to_account_id"    INTEGER                NOT NULL,
  "amount"           NUMERIC(12, 2)         NOT NULL,
  "note"             CHARACTER VARYING(150) NOT NULL,
  "payment_type_id"  INTEGER,
  "transaction_type" CHARACTER VARYING      NOT NULL,
  "created"          TIMESTAMP              NOT NULL,
  "updated"          TIMESTAMP              NOT NULL,
  CONSTRAINT transaction_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);



CREATE TABLE "payment_type" (
  "id"        SERIAL    NOT NULL,
  "parent_id" INTEGER,
  "name"      CHARACTER VARYING UNIQUE,
  "created"   TIMESTAMP NOT NULL,
  "updated"   TIMESTAMP NOT NULL,
  CONSTRAINT payment_type_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);



CREATE TABLE "card" (
  "id"              INTEGER   NOT NULL,
  "card_type"       CHARACTER VARYING,
  "max_limit"       NUMERIC,
  "expiration_date" TIMESTAMP,
  "cashback"        NUMERIC,
  "rate"            NUMERIC,
  "created"         TIMESTAMP NOT NULL,
  "updated"         TIMESTAMP NOT NULL,
  CONSTRAINT card_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);



CREATE TABLE "branch" (
  "id"             SERIAL            NOT NULL,
  "bank_id"        INTEGER           NOT NULL,
  "name"           CHARACTER VARYING NOT NULL,
  "street_address" character varying NOT NULL,
  "city"           CHARACTER VARYING NOT NULL,
  "post_code"      INTEGER           NOT NULL,
  "created"        TIMESTAMP         NOT NULL,
  "updated"        TIMESTAMP         NOT NULL,
  CONSTRAINT branch_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);



CREATE TABLE "bank" (
  "id"      SERIAL            NOT NULL,
  "name"    CHARACTER VARYING NOT NULL,
  "created" TIMESTAMP         NOT NULL,
  "updated" TIMESTAMP         NOT NULL,
  CONSTRAINT bank_pk PRIMARY KEY ("id")
) WITH (
OIDS = FALSE
);



CREATE TABLE "user_profile_2_branch" (
  "user_profile_id" INTEGER NOT NULL,
  "branch_id"       INTEGER NOT NULL
) WITH (
OIDS = FALSE
);




ALTER TABLE "account" ADD CONSTRAINT "account_fk0" FOREIGN KEY ("user_profile_id") REFERENCES "user_profile"("id");
ALTER TABLE "account" ADD CONSTRAINT "account_fk1" FOREIGN KEY ("bank_id") REFERENCES "bank"("id");

ALTER TABLE "transaction"
  ADD CONSTRAINT "transaction_fk0" FOREIGN KEY ("from_account_id") REFERENCES "account" ("id");
ALTER TABLE "transaction"
  ADD CONSTRAINT "transaction_fk1" FOREIGN KEY ("to_account_id") REFERENCES "account" ("id");
ALTER TABLE "transaction" ADD CONSTRAINT "transaction_fk2" FOREIGN KEY ("payment_type_id") REFERENCES "payment_type"("id");

ALTER TABLE "payment_type" ADD CONSTRAINT "payment_type_fk0" FOREIGN KEY ("parent_id") REFERENCES "payment_type"("id");

ALTER TABLE "card" ADD CONSTRAINT "card_fk0" FOREIGN KEY ("id") REFERENCES "account"("id");

ALTER TABLE "branch" ADD CONSTRAINT "branch_fk0" FOREIGN KEY ("bank_id") REFERENCES "bank"("id");


ALTER TABLE "user_profile_2_branch"
  ADD CONSTRAINT "user_profile_2_branch_fk0" FOREIGN KEY ("user_profile_id") REFERENCES "user_profile" ("id");
ALTER TABLE "user_profile_2_branch"
  ADD CONSTRAINT "user_profile_2_branch_fk1" FOREIGN KEY ("branch_id") REFERENCES "branch" ("id");
