ALTER TABLE public.deleted_user
RENAME TO "user";

ALTER TABLE public.user
ADD COLUMN token_last_updated TIMESTAMP;

ALTER TABLE public.user
ADD COLUMN deleted BOOL DEFAULT false;