# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table competencia (
  id                            bigint not null,
  nombre                        varchar(255),
  created                       timestamp,
  updated                       timestamp,
  constraint uq_competencia_nombre unique (nombre),
  constraint pk_competencia primary key (id)
);
create sequence competencia_seq;

create table competidor (
  id                            bigint not null,
  nombre                        varchar(255),
  email                         varchar(255),
  created                       timestamp,
  updated                       timestamp,
  constraint uq_competidor_nombre unique (nombre),
  constraint pk_competidor primary key (id)
);
create sequence competidor_seq;

create table inscripcion (
  id                            bigint not null,
  nombre                        varchar(255),
  descripcion                   varchar(255),
  ruta_imagen                   varchar(255),
  competencia_id                bigint,
  competidor_id                 bigint,
  created                       timestamp,
  updated                       timestamp,
  constraint pk_inscripcion primary key (id)
);
create sequence inscripcion_seq;

alter table inscripcion add constraint fk_inscripcion_competencia_id foreign key (competencia_id) references competencia (id) on delete restrict on update restrict;
create index ix_inscripcion_competencia_id on inscripcion (competencia_id);

alter table inscripcion add constraint fk_inscripcion_competidor_id foreign key (competidor_id) references competidor (id) on delete restrict on update restrict;
create index ix_inscripcion_competidor_id on inscripcion (competidor_id);


# --- !Downs

alter table if exists inscripcion drop constraint if exists fk_inscripcion_competencia_id;
drop index if exists ix_inscripcion_competencia_id;

alter table if exists inscripcion drop constraint if exists fk_inscripcion_competidor_id;
drop index if exists ix_inscripcion_competidor_id;

drop table if exists competencia cascade;
drop sequence if exists competencia_seq;

drop table if exists competidor cascade;
drop sequence if exists competidor_seq;

drop table if exists inscripcion cascade;
drop sequence if exists inscripcion_seq;

