-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION pg_database_owner;
-- public."USER" definition

-- Drop table

-- DROP TABLE "USER";

CREATE TABLE "USER" ( id_user uuid DEFAULT gen_random_uuid() NOT NULL, full_name varchar(120) NULL, phone varchar(20) NULL, cpf varchar(11) NULL, email varchar(120) NULL, "role" varchar(50) NULL, "password" bpchar(60) NOT NULL, birth_date date NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, CONSTRAINT ck_user_cpf CHECK (((cpf IS NULL) OR ((cpf)::text ~ '^[0-9]{11}\s*$'::text))), CONSTRAINT ck_user_phone CHECK (((phone IS NULL) OR ((phone)::text ~ '^[0-9]{10,11}\s*$'::text))), CONSTRAINT pk_user PRIMARY KEY (id_user), CONSTRAINT uk_user_cpf UNIQUE (cpf), CONSTRAINT uk_user_email UNIQUE (email));


-- public.address definition

-- Drop table

-- DROP TABLE address;

CREATE TABLE address ( id_address uuid DEFAULT gen_random_uuid() NOT NULL, street varchar(150) NULL, "number" int4 NULL, city varchar(100) NULL, state bpchar(2) NULL, zip_code bpchar(8) NULL, complement text NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, CONSTRAINT ck_address_zip_code CHECK (((zip_code IS NULL) OR (zip_code ~ '^[0-9]{8}\s*$'::text))), CONSTRAINT pk_address PRIMARY KEY (id_address));


-- public."admin" definition

-- Drop table

-- DROP TABLE "admin";

CREATE TABLE "admin" ( id_admin uuid DEFAULT gen_random_uuid() NOT NULL, full_name varchar(120) NULL, cpf varchar(11) NULL, email varchar(120) NULL, phone varchar(20) NULL, "password" bpchar(60) NOT NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, CONSTRAINT ck_admin_cpf CHECK (((cpf)::text ~ '^[0-9]{11}\s*$'::text)), CONSTRAINT ck_admin_phone CHECK (((phone IS NULL) OR ((phone)::text ~ '^[0-9]{10,11}\s*$'::text))), CONSTRAINT pk_admin PRIMARY KEY (id_admin), CONSTRAINT uk_admin_cpf UNIQUE (cpf), CONSTRAINT uk_admin_email UNIQUE (email));


-- public.herd definition

-- Drop table

-- DROP TABLE herd;

CREATE TABLE herd ( id_herd uuid DEFAULT gen_random_uuid() NOT NULL, herd_name varchar(60) NULL, breed varchar(40) NULL, purpose varchar(20) DEFAULT 'Beef'::character varying NULL, head_count int4 DEFAULT 0 NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_property uuid NOT NULL, CONSTRAINT ck_herd_head_count CHECK ((head_count >= 0)), CONSTRAINT ck_herd_purpose CHECK ((upper(TRIM(BOTH FROM purpose)) = 'BEEF'::text)), CONSTRAINT pk_herd PRIMARY KEY (id_herd));


-- public.lot definition

-- Drop table

-- DROP TABLE lot;

CREATE TABLE lot ( id_lot uuid DEFAULT gen_random_uuid() NOT NULL, lot_code varchar(20) NOT NULL, category varchar(30) NOT NULL, head_count int4 DEFAULT 0 NULL, open_date date NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_herd uuid NOT NULL, CONSTRAINT ck_lot_category CHECK (((category)::text = ANY ((ARRAY['FATTENING'::character varying, 'BREEDING'::character varying, 'REARING'::character varying])::text[]))), CONSTRAINT ck_lot_head_count CHECK ((head_count >= 0)), CONSTRAINT pk_lot PRIMARY KEY (id_lot), CONSTRAINT uk_lot_code_herd UNIQUE (id_herd, lot_code), CONSTRAINT fk_lot_herd FOREIGN KEY (id_herd) REFERENCES herd(id_herd));


-- public.property definition

-- Drop table

-- DROP TABLE property;

CREATE TABLE property ( id_property uuid DEFAULT gen_random_uuid() NOT NULL, "name" varchar(120) NULL, cnpj bpchar(14) NULL, phone varchar(20) NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_address uuid NOT NULL, CONSTRAINT ck_property_cnpj CHECK (((cnpj IS NULL) OR (cnpj ~ '^[0-9]{14}\s*$'::text))), CONSTRAINT ck_property_phone CHECK (((phone IS NULL) OR ((phone)::text ~ '^[0-9]{10,11}\s*$'::text))), CONSTRAINT pk_property PRIMARY KEY (id_property), CONSTRAINT uk_property_address UNIQUE (id_address), CONSTRAINT fk_property_address FOREIGN KEY (id_address) REFERENCES address(id_address));


-- public.supplier definition

-- Drop table

-- DROP TABLE supplier;

CREATE TABLE supplier ( id_supplier uuid DEFAULT gen_random_uuid() NOT NULL, full_name varchar(120) NULL, cnpj varchar(14) NULL, phone varchar(20) NULL, email varchar(120) NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_address uuid NULL, CONSTRAINT ck_supplier_cnpj CHECK (((cnpj IS NULL) OR ((cnpj)::text ~ '^[0-9]{14}\s*$'::text))), CONSTRAINT ck_supplier_phone CHECK (((phone IS NULL) OR ((phone)::text ~ '^[0-9]{10,11}\s*$'::text))), CONSTRAINT pk_supplier PRIMARY KEY (id_supplier), CONSTRAINT uk_supplier_cnpj UNIQUE (cnpj), CONSTRAINT fk_supplier_address FOREIGN KEY (id_address) REFERENCES address(id_address));


-- public.user_property definition

-- Drop table

-- DROP TABLE user_property;

CREATE TABLE user_property ( id_user uuid NOT NULL, id_property uuid NOT NULL, CONSTRAINT pk_user_property PRIMARY KEY (id_user, id_property), CONSTRAINT fk_user_property_property FOREIGN KEY (id_property) REFERENCES property(id_property), CONSTRAINT fk_user_property_user FOREIGN KEY (id_user) REFERENCES "USER"(id_user));


-- public.animal definition

-- Drop table

-- DROP TABLE animal;

CREATE TABLE animal ( id_animal uuid DEFAULT gen_random_uuid() NOT NULL, ear_tag varchar(20) NOT NULL, current_weight numeric(7, 2) NULL, birth_date date NULL, exception_reason varchar(255) NULL, exception_start_date date NOT NULL, exception_end_date date NULL, cleared_for_slaughter bool DEFAULT false NULL, notes text NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_lot uuid NOT NULL, id_origin_lot uuid NULL, CONSTRAINT ck_animal_current_weight CHECK (((current_weight IS NULL) OR (current_weight > (0)::numeric))), CONSTRAINT ck_animal_exception_dates CHECK (((exception_end_date IS NULL) OR (exception_end_date >= exception_start_date))), CONSTRAINT pk_animal PRIMARY KEY (id_animal), CONSTRAINT uk_animal_eartag_lot UNIQUE (id_lot, ear_tag), CONSTRAINT fk_animal_lot FOREIGN KEY (id_lot) REFERENCES lot(id_lot), CONSTRAINT fk_animal_origin_lot FOREIGN KEY (id_origin_lot) REFERENCES lot(id_lot));


-- public.medicine definition

-- Drop table

-- DROP TABLE medicine;

CREATE TABLE medicine ( id_medicine uuid DEFAULT gen_random_uuid() NOT NULL, brand_name varchar(80) NULL, dose numeric(10, 3) NULL, unit varchar(10) NULL, active_ingredient varchar(80) NULL, indication text NULL, indicated_withdrawal_days int4 DEFAULT 0 NULL, therapeutic_category text NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_supplier uuid NOT NULL, CONSTRAINT ck_medication_withdrawal CHECK ((indicated_withdrawal_days >= 0)), CONSTRAINT pk_medication PRIMARY KEY (id_medicine), CONSTRAINT fk_medication_supplier FOREIGN KEY (id_supplier) REFERENCES supplier(id_supplier));


-- public.withdrawal_period definition

-- Drop table

-- DROP TABLE withdrawal_period;

CREATE TABLE withdrawal_period ( id_withdrawal uuid DEFAULT gen_random_uuid() NOT NULL, withdrawal_start_date date NOT NULL, withdrawal_end_date date NULL, notes text NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_animal uuid NOT NULL, CONSTRAINT ck_withdrawal_dates CHECK (((withdrawal_end_date IS NULL) OR (withdrawal_end_date >= withdrawal_start_date))), CONSTRAINT pk_withdrawal_period PRIMARY KEY (id_withdrawal), CONSTRAINT fk_withdrawal_animal FOREIGN KEY (id_animal) REFERENCES animal(id_animal));


-- public.medication_batch definition

-- Drop table

-- DROP TABLE medication_batch;

CREATE TABLE medication_batch ( id_medication_batch uuid DEFAULT gen_random_uuid() NOT NULL, batch_number varchar(30) NULL, initial_quantity numeric(10, 3) NULL, manufacture_date date NOT NULL, expiry_date date NULL, delivery_date date NULL, id_medicine uuid NOT NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, CONSTRAINT ck_med_batch_expiration CHECK (((expiry_date IS NULL) OR (expiry_date >= manufacture_date))), CONSTRAINT ck_med_batch_initial_qty CHECK ((initial_quantity >= (0)::numeric)), CONSTRAINT pk_medication_batch PRIMARY KEY (id_medication_batch), CONSTRAINT fk_med_batch_medication FOREIGN KEY (id_medicine) REFERENCES medicine(id_medicine));


-- public.medication_inventory definition

-- Drop table

-- DROP TABLE medication_inventory;

CREATE TABLE medication_inventory ( id_medication_inventory uuid DEFAULT gen_random_uuid() NOT NULL, available_quantity numeric(10, 3) NULL, minimum_stock numeric(10, 3) NULL, maximum_stock numeric(10, 3) NULL, storage_location varchar(60) NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_medication_batch uuid NOT NULL, CONSTRAINT ck_inventory_available_qty CHECK ((available_quantity >= (0)::numeric)), CONSTRAINT ck_inventory_min_max CHECK ((minimum_stock <= maximum_stock)), CONSTRAINT pk_medication_inventory PRIMARY KEY (id_medication_inventory), CONSTRAINT fk_inventory_medication_batch FOREIGN KEY (id_medication_batch) REFERENCES medication_batch(id_medication_batch));


-- public.application definition

-- Drop table

-- DROP TABLE application;

CREATE TABLE application ( id_application uuid DEFAULT gen_random_uuid() NOT NULL, dose numeric(10, 3) NULL, unit varchar(10) NULL, treated_head_count int4 DEFAULT 1 NULL, route varchar(20) NULL, application_limb varchar(50) NULL, application_site varchar(100) NULL, goal varchar(120) NULL, date_time timestamp DEFAULT CURRENT_TIMESTAMP NULL, notes text NULL, active bool DEFAULT true NULL, updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL, id_user uuid NOT NULL, id_medication_batch uuid NOT NULL, id_lot uuid NOT NULL, CONSTRAINT ck_application_head_count CHECK ((treated_head_count > 0)), CONSTRAINT ck_application_route CHECK (((route IS NULL) OR (upper(TRIM(BOTH FROM route)) = ANY (ARRAY['INTRAMUSCULAR'::text, 'SUBCUTANEOUS'::text, 'ORAL'::text, 'TOPICAL'::text, 'INTRAVENOUS'::text])))), CONSTRAINT pk_application PRIMARY KEY (id_application), CONSTRAINT fk_application_lot FOREIGN KEY (id_lot) REFERENCES lot(id_lot), CONSTRAINT fk_application_medication_batch FOREIGN KEY (id_medication_batch) REFERENCES medication_batch(id_medication_batch), CONSTRAINT fk_application_user FOREIGN KEY (id_user) REFERENCES "USER"(id_user));



-- DROP FUNCTION public.armor(bytea);

CREATE OR REPLACE FUNCTION public.armor(bytea)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_armor$function$
;

-- DROP FUNCTION public.armor(bytea, _text, _text);

CREATE OR REPLACE FUNCTION public.armor(bytea, text[], text[])
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_armor$function$
;

-- DROP FUNCTION public.crypt(text, text);

CREATE OR REPLACE FUNCTION public.crypt(text, text)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_crypt$function$
;

-- DROP FUNCTION public.dearmor(text);

CREATE OR REPLACE FUNCTION public.dearmor(text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_dearmor$function$
;

-- DROP FUNCTION public.decrypt(bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.decrypt(bytea, bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_decrypt$function$
;

-- DROP FUNCTION public.decrypt_iv(bytea, bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.decrypt_iv(bytea, bytea, bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_decrypt_iv$function$
;

-- DROP FUNCTION public.digest(bytea, text);

CREATE OR REPLACE FUNCTION public.digest(bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_digest$function$
;

-- DROP FUNCTION public.digest(text, text);

CREATE OR REPLACE FUNCTION public.digest(text, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_digest$function$
;

-- DROP FUNCTION public.encrypt(bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.encrypt(bytea, bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_encrypt$function$
;

-- DROP FUNCTION public.encrypt_iv(bytea, bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.encrypt_iv(bytea, bytea, bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_encrypt_iv$function$
;

-- DROP FUNCTION public.fips_mode();

CREATE OR REPLACE FUNCTION public.fips_mode()
 RETURNS boolean
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_check_fipsmode$function$
;

-- DROP FUNCTION public.gen_random_bytes(int4);

CREATE OR REPLACE FUNCTION public.gen_random_bytes(integer)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_random_bytes$function$
;

-- DROP FUNCTION public.gen_random_uuid();

CREATE OR REPLACE FUNCTION public.gen_random_uuid()
 RETURNS uuid
 LANGUAGE c
 PARALLEL SAFE
AS '$libdir/pgcrypto', $function$pg_random_uuid$function$
;

-- DROP FUNCTION public.gen_salt(text);

CREATE OR REPLACE FUNCTION public.gen_salt(text)
 RETURNS text
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_gen_salt$function$
;

-- DROP FUNCTION public.gen_salt(text, int4);

CREATE OR REPLACE FUNCTION public.gen_salt(text, integer)
 RETURNS text
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_gen_salt_rounds$function$
;

-- DROP FUNCTION public.hmac(bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.hmac(bytea, bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_hmac$function$
;

-- DROP FUNCTION public.hmac(text, text, text);

CREATE OR REPLACE FUNCTION public.hmac(text, text, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pg_hmac$function$
;

-- DROP FUNCTION public.pgp_armor_headers(in text, out text, out text);

CREATE OR REPLACE FUNCTION public.pgp_armor_headers(text, OUT key text, OUT value text)
 RETURNS SETOF record
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_armor_headers$function$
;

-- DROP FUNCTION public.pgp_key_id(bytea);

CREATE OR REPLACE FUNCTION public.pgp_key_id(bytea)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_key_id_w$function$
;

-- DROP FUNCTION public.pgp_pub_decrypt(bytea, bytea);

CREATE OR REPLACE FUNCTION public.pgp_pub_decrypt(bytea, bytea)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_decrypt_text$function$
;

-- DROP FUNCTION public.pgp_pub_decrypt(bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.pgp_pub_decrypt(bytea, bytea, text)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_decrypt_text$function$
;

-- DROP FUNCTION public.pgp_pub_decrypt(bytea, bytea, text, text);

CREATE OR REPLACE FUNCTION public.pgp_pub_decrypt(bytea, bytea, text, text)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_decrypt_text$function$
;

-- DROP FUNCTION public.pgp_pub_decrypt_bytea(bytea, bytea);

CREATE OR REPLACE FUNCTION public.pgp_pub_decrypt_bytea(bytea, bytea)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_decrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_pub_decrypt_bytea(bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.pgp_pub_decrypt_bytea(bytea, bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_decrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_pub_decrypt_bytea(bytea, bytea, text, text);

CREATE OR REPLACE FUNCTION public.pgp_pub_decrypt_bytea(bytea, bytea, text, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_decrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_pub_encrypt(text, bytea);

CREATE OR REPLACE FUNCTION public.pgp_pub_encrypt(text, bytea)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_encrypt_text$function$
;

-- DROP FUNCTION public.pgp_pub_encrypt(text, bytea, text);

CREATE OR REPLACE FUNCTION public.pgp_pub_encrypt(text, bytea, text)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_encrypt_text$function$
;

-- DROP FUNCTION public.pgp_pub_encrypt_bytea(bytea, bytea);

CREATE OR REPLACE FUNCTION public.pgp_pub_encrypt_bytea(bytea, bytea)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_encrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_pub_encrypt_bytea(bytea, bytea, text);

CREATE OR REPLACE FUNCTION public.pgp_pub_encrypt_bytea(bytea, bytea, text)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_pub_encrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_sym_decrypt(bytea, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_decrypt(bytea, text)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_decrypt_text$function$
;

-- DROP FUNCTION public.pgp_sym_decrypt(bytea, text, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_decrypt(bytea, text, text)
 RETURNS text
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_decrypt_text$function$
;

-- DROP FUNCTION public.pgp_sym_decrypt_bytea(bytea, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_decrypt_bytea(bytea, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_decrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_sym_decrypt_bytea(bytea, text, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_decrypt_bytea(bytea, text, text)
 RETURNS bytea
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_decrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_sym_encrypt(text, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_encrypt(text, text)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_encrypt_text$function$
;

-- DROP FUNCTION public.pgp_sym_encrypt(text, text, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_encrypt(text, text, text)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_encrypt_text$function$
;

-- DROP FUNCTION public.pgp_sym_encrypt_bytea(bytea, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_encrypt_bytea(bytea, text)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_encrypt_bytea$function$
;

-- DROP FUNCTION public.pgp_sym_encrypt_bytea(bytea, text, text);

CREATE OR REPLACE FUNCTION public.pgp_sym_encrypt_bytea(bytea, text, text)
 RETURNS bytea
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/pgcrypto', $function$pgp_sym_encrypt_bytea$function$
;

-- DROP FUNCTION public.uuid_generate_v1();

CREATE OR REPLACE FUNCTION public.uuid_generate_v1()
 RETURNS uuid
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v1$function$
;

-- DROP FUNCTION public.uuid_generate_v1mc();

CREATE OR REPLACE FUNCTION public.uuid_generate_v1mc()
 RETURNS uuid
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v1mc$function$
;

-- DROP FUNCTION public.uuid_generate_v3(uuid, text);

CREATE OR REPLACE FUNCTION public.uuid_generate_v3(namespace uuid, name text)
 RETURNS uuid
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v3$function$
;

-- DROP FUNCTION public.uuid_generate_v4();

CREATE OR REPLACE FUNCTION public.uuid_generate_v4()
 RETURNS uuid
 LANGUAGE c
 PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v4$function$
;

-- DROP FUNCTION public.uuid_generate_v5(uuid, text);

CREATE OR REPLACE FUNCTION public.uuid_generate_v5(namespace uuid, name text)
 RETURNS uuid
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_generate_v5$function$
;

-- DROP FUNCTION public.uuid_nil();

CREATE OR REPLACE FUNCTION public.uuid_nil()
 RETURNS uuid
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_nil$function$
;

-- DROP FUNCTION public.uuid_ns_dns();

CREATE OR REPLACE FUNCTION public.uuid_ns_dns()
 RETURNS uuid
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_dns$function$
;

-- DROP FUNCTION public.uuid_ns_oid();

CREATE OR REPLACE FUNCTION public.uuid_ns_oid()
 RETURNS uuid
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_oid$function$
;

-- DROP FUNCTION public.uuid_ns_url();

CREATE OR REPLACE FUNCTION public.uuid_ns_url()
 RETURNS uuid
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_url$function$
;

-- DROP FUNCTION public.uuid_ns_x500();

CREATE OR REPLACE FUNCTION public.uuid_ns_x500()
 RETURNS uuid
 LANGUAGE c
 IMMUTABLE PARALLEL SAFE STRICT
AS '$libdir/uuid-ossp', $function$uuid_ns_x500$function$
;