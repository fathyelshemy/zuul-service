CREATE TABLE IF NOT EXISTS public.deleted_user (
  id bigserial NOT NULL,
  email character varying (325) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);
