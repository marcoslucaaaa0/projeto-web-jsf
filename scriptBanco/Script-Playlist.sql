CREATE TABLE public.playlist
(
    id integer NOT NULL DEFAULT nextval('playlist_id_seq'::regclass),
    title character varying(300) COLLATE pg_catalog."default",
    url_file character varying(200) COLLATE pg_catalog."default",
    group_name character varying(200) COLLATE pg_catalog."default",
    genero character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT playlist_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.playlist
    OWNER to postgres;