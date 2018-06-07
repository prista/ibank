--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

-- Started on 2018-06-05 12:18:46 +03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;

--
-- TOC entry 1 (class 3079 OID 12961)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

--
-- TOC entry 2930 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_with_oids = FALSE;

--
-- TOC entry 199 (class 1259 OID 26969)
-- Name: account; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE account (
  id              INTEGER                     NOT NULL,
  name            CHARACTER VARYING(150)      NOT NULL,
  user_profile_id INTEGER,
  account_type    CHARACTER VARYING           NOT NULL,
  balance         NUMERIC(12, 2) DEFAULT 0,
  currency        CHARACTER VARYING(10)       NOT NULL,
  locked          BOOLEAN                     NOT NULL,
  bank_id         INTEGER                     NOT NULL,
  deleted         BOOLEAN                     NOT NULL,
  created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated         TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

--
-- TOC entry 198 (class 1259 OID 26967)
-- Name: account_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE account_id_seq
  AS INTEGER
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

--
-- TOC entry 2931 (class 0 OID 0)
-- Dependencies: 198
-- Name: account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE account_id_seq OWNED BY account.id;

--
-- TOC entry 208 (class 1259 OID 27020)
-- Name: bank; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE bank (
  id      INTEGER                     NOT NULL,
  name    CHARACTER VARYING           NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

--
-- TOC entry 207 (class 1259 OID 27018)
-- Name: bank_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bank_id_seq
  AS INTEGER
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

--
-- TOC entry 2932 (class 0 OID 0)
-- Dependencies: 207
-- Name: bank_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bank_id_seq OWNED BY bank.id;

--
-- TOC entry 206 (class 1259 OID 27009)
-- Name: branch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE branch (
  id             INTEGER                     NOT NULL,
  bank_id        INTEGER                     NOT NULL,
  name           CHARACTER VARYING           NOT NULL,
  street_address CHARACTER VARYING           NOT NULL,
  city           CHARACTER VARYING           NOT NULL,
  post_code      INTEGER                     NOT NULL,
  created        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated        TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

--
-- TOC entry 205 (class 1259 OID 27007)
-- Name: branch_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE branch_id_seq
  AS INTEGER
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

--
-- TOC entry 2933 (class 0 OID 0)
-- Dependencies: 205
-- Name: branch_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE branch_id_seq OWNED BY branch.id;

--
-- TOC entry 204 (class 1259 OID 26999)
-- Name: card; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE card (
  id              INTEGER                     NOT NULL,
  card_type       CHARACTER VARYING,
  max_limit       NUMERIC,
  expiration_date TIMESTAMP WITHOUT TIME ZONE,
  cashback        NUMERIC,
  rate            NUMERIC,
  created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated         TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

--
-- TOC entry 203 (class 1259 OID 26988)
-- Name: payment_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE payment_type (
  id        INTEGER                     NOT NULL,
  parent_id INTEGER,
  name      CHARACTER VARYING,
  created   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated   TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

--
-- TOC entry 202 (class 1259 OID 26986)
-- Name: payment_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE payment_type_id_seq
  AS INTEGER
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

--
-- TOC entry 2934 (class 0 OID 0)
-- Dependencies: 202
-- Name: payment_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE payment_type_id_seq OWNED BY payment_type.id;

--
-- TOC entry 201 (class 1259 OID 26977)
-- Name: transaction; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE transaction (
  id               INTEGER                     NOT NULL,
  from_account_id  INTEGER                     NOT NULL,
  to_account_id    INTEGER                     NOT NULL,
  amount           NUMERIC(12, 2)              NOT NULL,
  note             CHARACTER VARYING(150)      NOT NULL,
  payment_type_id  INTEGER,
  transaction_type CHARACTER VARYING           NOT NULL,
  created          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated          TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

--
-- TOC entry 200 (class 1259 OID 26975)
-- Name: transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE transaction_id_seq
  AS INTEGER
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

--
-- TOC entry 2935 (class 0 OID 0)
-- Dependencies: 200
-- Name: transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE transaction_id_seq OWNED BY transaction.id;

--
-- TOC entry 197 (class 1259 OID 26956)
-- Name: user_profile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_profile (
  id       INTEGER                     NOT NULL,
  username CHARACTER VARYING(150)      NOT NULL,
  password CHARACTER VARYING(150)      NOT NULL,
  role     CHARACTER VARYING           NOT NULL,
  created  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  updated  TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

--
-- TOC entry 209 (class 1259 OID 27029)
-- Name: user_profile_2_branch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_profile_2_branch (
  user_profile_id INTEGER NOT NULL,
  branch_id       INTEGER NOT NULL
);

--
-- TOC entry 196 (class 1259 OID 26954)
-- Name: user_profile_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE user_profile_id_seq
  AS INTEGER
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

--
-- TOC entry 2936 (class 0 OID 0)
-- Dependencies: 196
-- Name: user_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE user_profile_id_seq OWNED BY user_profile.id;

--
-- TOC entry 2753 (class 2604 OID 26972)
-- Name: account id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY account
  ALTER COLUMN id SET DEFAULT nextval('account_id_seq' :: REGCLASS);

--
-- TOC entry 2758 (class 2604 OID 27023)
-- Name: bank id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bank
  ALTER COLUMN id SET DEFAULT nextval('bank_id_seq' :: REGCLASS);

--
-- TOC entry 2757 (class 2604 OID 27012)
-- Name: branch id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY branch
  ALTER COLUMN id SET DEFAULT nextval('branch_id_seq' :: REGCLASS);

--
-- TOC entry 2756 (class 2604 OID 26991)
-- Name: payment_type id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY payment_type
  ALTER COLUMN id SET DEFAULT nextval('payment_type_id_seq' :: REGCLASS);

--
-- TOC entry 2755 (class 2604 OID 26980)
-- Name: transaction id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY transaction
  ALTER COLUMN id SET DEFAULT nextval('transaction_id_seq' :: REGCLASS);

--
-- TOC entry 2752 (class 2604 OID 26959)
-- Name: user_profile id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
  ALTER COLUMN id SET DEFAULT nextval('user_profile_id_seq' :: REGCLASS);

--
-- TOC entry 2913 (class 0 OID 26969)
-- Dependencies: 199
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO account (id, name, user_profile_id, account_type, balance, currency, locked, bank_id, deleted, created, updated)
VALUES
  (13, 'accountName1', 1, 'bank', 0.00, 'EUR', FALSE, 1, FALSE, '2018-05-29 13:44:19.473', '2018-06-02 15:03:15.105');
INSERT INTO account (id, name, user_profile_id, account_type, balance, currency, locked, bank_id, deleted, created, updated)
VALUES
  (-1, 'START', 4, 'bank', -100000.00, 'EUR', FALSE, 1, FALSE, '2018-06-02 15:13:19.812', '2018-06-02 15:13:43.197');
INSERT INTO account (id, name, user_profile_id, account_type, balance, currency, locked, bank_id, deleted, created, updated)
VALUES
  (15, 'accountName3', 1, 'bank', 0.00, 'EUR', FALSE, 2, FALSE, '2018-05-31 16:48:15.275', '2018-06-02 13:26:39.685');
INSERT INTO account (id, name, user_profile_id, account_type, balance, currency, locked, bank_id, deleted, created, updated)
VALUES (16, 'magicAccount', 4, 'bank', 100000.00, 'BYN', FALSE, 1, FALSE, '2018-06-02 13:28:34.95',
            '2018-06-02 15:27:54.45');
INSERT INTO account (id, name, user_profile_id, account_type, balance, currency, locked, bank_id, deleted, created, updated)
VALUES
  (14, 'accountName2', 2, 'card', 0.00, 'BYN', FALSE, 1, FALSE, '2018-05-29 13:44:42.38', '2018-06-02 14:55:39.719');

--
-- TOC entry 2922 (class 0 OID 27020)
-- Dependencies: 208
-- Data for Name: bank; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO bank (id, name, created, updated) VALUES (1, 'bank1', '2018-05-14 17:35:46.748', '2018-05-14 17:35:46.748');
INSERT INTO bank (id, name, created, updated) VALUES (2, 'bank2', '2018-05-14 17:35:53.126', '2018-05-14 17:35:53.126');
INSERT INTO bank (id, name, created, updated) VALUES (3, 'bank3', '2018-05-14 17:35:58.015', '2018-05-14 17:35:58.015');
INSERT INTO bank (id, name, created, updated) VALUES (4, 'bank4', '2018-05-14 17:36:03.078', '2018-05-14 17:36:03.078');

--
-- TOC entry 2920 (class 0 OID 27009)
-- Dependencies: 206
-- Data for Name: branch; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO branch (id, bank_id, name, street_address, city, post_code, created, updated)
VALUES (2, 2, 'branch2', 'street2', 'city2', 222222, '2018-05-14 18:24:40.948', '2018-05-14 18:24:40.948');
INSERT INTO branch (id, bank_id, name, street_address, city, post_code, created, updated)
VALUES (3, 1, 'branch3', 'street3', 'city3', 333333, '2018-05-14 18:24:59.592', '2018-05-14 18:24:59.592');
INSERT INTO branch (id, bank_id, name, street_address, city, post_code, created, updated)
VALUES (1, 1, 'branch1', 'street1', 'city1', 111111, '2018-05-14 18:23:24.721', '2018-05-25 11:06:57.236');
INSERT INTO branch (id, bank_id, name, street_address, city, post_code, created, updated)
VALUES (5, 1, 'testBranch', 'tt', 'ss', 22, '2018-05-25 11:09:29.989', '2018-05-25 11:09:29.989');

--
-- TOC entry 2918 (class 0 OID 26999)
-- Dependencies: 204
-- Data for Name: card; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO card (id, card_type, max_limit, expiration_date, cashback, rate, created, updated)
VALUES (15, '', NULL, NULL, NULL, NULL, '2018-05-31 16:48:15.275', '2018-06-02 13:26:39.685');
INSERT INTO card (id, card_type, max_limit, expiration_date, cashback, rate, created, updated)
VALUES (14, 'type2', NULL, NULL, NULL, NULL, '2018-05-29 13:44:42.38', '2018-06-02 14:55:39.719');
INSERT INTO card (id, card_type, max_limit, expiration_date, cashback, rate, created, updated)
VALUES (13, 'card1', NULL, '2018-06-02 00:00:00', NULL, NULL, '2018-05-29 13:44:19.473', '2018-06-02 15:03:15.105');
INSERT INTO card (id, card_type, max_limit, expiration_date, cashback, rate, created, updated)
VALUES (16, 'magic type', NULL, NULL, NULL, NULL, '2018-06-02 13:28:34.95', '2018-06-02 15:27:54.45');

--
-- TOC entry 2917 (class 0 OID 26988)
-- Dependencies: 203
-- Data for Name: payment_type; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO payment_type (id, parent_id, name, created, updated)
VALUES (1, NULL, 'name1', '2018-05-14 18:51:17.74', '2018-05-14 18:51:17.74');
INSERT INTO payment_type (id, parent_id, name, created, updated)
VALUES (3, 1, 'child1', '2018-05-22 16:25:39.496', '2018-05-22 16:25:39.496');
INSERT INTO payment_type (id, parent_id, name, created, updated)
VALUES (4, 3, 'dasistt', '2018-05-22 16:45:53.8', '2018-05-22 16:45:53.8');

--
-- TOC entry 2915 (class 0 OID 26977)
-- Dependencies: 201
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO transaction (id, from_account_id, to_account_id, amount, note, payment_type_id, transaction_type, created, updated)
VALUES (34, -1, 16, 100000.00, 'initial transaction for start app', 1, 'transfer', '2018-06-02 15:46:47.608',
        '2018-06-02 15:46:47.608');

--
-- TOC entry 2911 (class 0 OID 26956)
-- Dependencies: 197
-- Data for Name: user_profile; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO user_profile (id, username, password, role, created, updated)
VALUES (1, 'user1', 'password1', 'role1', '2018-05-14 18:23:44.988', '2018-05-14 18:23:44.988');
INSERT INTO user_profile (id, username, password, role, created, updated)
VALUES (2, 'user2', 'password2', 'role2', '2018-05-14 18:23:53.978', '2018-05-14 18:23:53.978');
INSERT INTO user_profile (id, username, password, role, created, updated)
VALUES (3, 'use3', 'password3', 'role3', '2018-05-14 18:24:07.004', '2018-05-14 18:24:07.004');
INSERT INTO user_profile (id, username, password, role, created, updated)
VALUES (4, 'AlfaBank', 'alpha', 'admin', '2018-06-02 13:27:35.687', '2018-06-02 13:27:35.687');

--
-- TOC entry 2923 (class 0 OID 27029)
-- Dependencies: 209
-- Data for Name: user_profile_2_branch; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO user_profile_2_branch (user_profile_id, branch_id) VALUES (2, 1);
INSERT INTO user_profile_2_branch (user_profile_id, branch_id) VALUES (1, 1);
INSERT INTO user_profile_2_branch (user_profile_id, branch_id) VALUES (2, 2);
INSERT INTO user_profile_2_branch (user_profile_id, branch_id) VALUES (2, 3);
INSERT INTO user_profile_2_branch (user_profile_id, branch_id) VALUES (3, 3);
INSERT INTO user_profile_2_branch (user_profile_id, branch_id) VALUES (3, 1);
INSERT INTO user_profile_2_branch (user_profile_id, branch_id) VALUES (2, 5);

--
-- TOC entry 2937 (class 0 OID 0)
-- Dependencies: 198
-- Name: account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('account_id_seq', 17, TRUE);

--
-- TOC entry 2938 (class 0 OID 0)
-- Dependencies: 207
-- Name: bank_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('bank_id_seq', 4, TRUE);

--
-- TOC entry 2939 (class 0 OID 0)
-- Dependencies: 205
-- Name: branch_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('branch_id_seq', 5, TRUE);

--
-- TOC entry 2940 (class 0 OID 0)
-- Dependencies: 202
-- Name: payment_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('payment_type_id_seq', 4, TRUE);

--
-- TOC entry 2941 (class 0 OID 0)
-- Dependencies: 200
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('transaction_id_seq', 34, TRUE);

--
-- TOC entry 2942 (class 0 OID 0)
-- Dependencies: 196
-- Name: user_profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('user_profile_id_seq', 4, TRUE);

--
-- TOC entry 2764 (class 2606 OID 26974)
-- Name: account account_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account
  ADD CONSTRAINT account_pk PRIMARY KEY (id);

--
-- TOC entry 2776 (class 2606 OID 27028)
-- Name: bank bank_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bank
  ADD CONSTRAINT bank_pk PRIMARY KEY (id);

--
-- TOC entry 2774 (class 2606 OID 27017)
-- Name: branch branch_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY branch
  ADD CONSTRAINT branch_pk PRIMARY KEY (id);

--
-- TOC entry 2772 (class 2606 OID 27006)
-- Name: card card_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY card
  ADD CONSTRAINT card_pk PRIMARY KEY (id);

--
-- TOC entry 2768 (class 2606 OID 26998)
-- Name: payment_type payment_type_name_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY payment_type
  ADD CONSTRAINT payment_type_name_key UNIQUE (name);

--
-- TOC entry 2770 (class 2606 OID 26996)
-- Name: payment_type payment_type_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY payment_type
  ADD CONSTRAINT payment_type_pk PRIMARY KEY (id);

--
-- TOC entry 2766 (class 2606 OID 26985)
-- Name: transaction transaction_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transaction
  ADD CONSTRAINT transaction_pk PRIMARY KEY (id);

--
-- TOC entry 2778 (class 2606 OID 27083)
-- Name: user_profile_2_branch user_profile_2_branch_user_profile_id_branch_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile_2_branch
  ADD CONSTRAINT user_profile_2_branch_user_profile_id_branch_id_key UNIQUE (user_profile_id, branch_id);

--
-- TOC entry 2760 (class 2606 OID 26964)
-- Name: user_profile user_profile_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
  ADD CONSTRAINT user_profile_pk PRIMARY KEY (id);

--
-- TOC entry 2762 (class 2606 OID 26966)
-- Name: user_profile user_profile_username_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
  ADD CONSTRAINT user_profile_username_key UNIQUE (username);

--
-- TOC entry 2779 (class 2606 OID 27032)
-- Name: account account_fk0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account
  ADD CONSTRAINT account_fk0 FOREIGN KEY (user_profile_id) REFERENCES user_profile (id);

--
-- TOC entry 2780 (class 2606 OID 27037)
-- Name: account account_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY account
  ADD CONSTRAINT account_fk1 FOREIGN KEY (bank_id) REFERENCES bank (id);

--
-- TOC entry 2786 (class 2606 OID 27067)
-- Name: branch branch_fk0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY branch
  ADD CONSTRAINT branch_fk0 FOREIGN KEY (bank_id) REFERENCES bank (id);

--
-- TOC entry 2785 (class 2606 OID 27062)
-- Name: card card_fk0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY card
  ADD CONSTRAINT card_fk0 FOREIGN KEY (id) REFERENCES account (id);

--
-- TOC entry 2784 (class 2606 OID 27057)
-- Name: payment_type payment_type_fk0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY payment_type
  ADD CONSTRAINT payment_type_fk0 FOREIGN KEY (parent_id) REFERENCES payment_type (id);

--
-- TOC entry 2781 (class 2606 OID 27042)
-- Name: transaction transaction_fk0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transaction
  ADD CONSTRAINT transaction_fk0 FOREIGN KEY (from_account_id) REFERENCES account (id);

--
-- TOC entry 2782 (class 2606 OID 27047)
-- Name: transaction transaction_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transaction
  ADD CONSTRAINT transaction_fk1 FOREIGN KEY (to_account_id) REFERENCES account (id);

--
-- TOC entry 2783 (class 2606 OID 27052)
-- Name: transaction transaction_fk2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY transaction
  ADD CONSTRAINT transaction_fk2 FOREIGN KEY (payment_type_id) REFERENCES payment_type (id);

--
-- TOC entry 2787 (class 2606 OID 27072)
-- Name: user_profile_2_branch user_profile_2_branch_fk0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile_2_branch
  ADD CONSTRAINT user_profile_2_branch_fk0 FOREIGN KEY (user_profile_id) REFERENCES user_profile (id);

--
-- TOC entry 2788 (class 2606 OID 27077)
-- Name: user_profile_2_branch user_profile_2_branch_fk1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile_2_branch
  ADD CONSTRAINT user_profile_2_branch_fk1 FOREIGN KEY (branch_id) REFERENCES branch (id);

-- Completed on 2018-06-05 12:18:46 +03

--
-- PostgreSQL database dump complete
--

