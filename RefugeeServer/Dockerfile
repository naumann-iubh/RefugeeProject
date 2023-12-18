FROM postgres:bullseye

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD password
ENV POSTGRES_DB refugee

COPY init.sql /docker-entrypoint-initdb.d/

CREATE USER docker;
CREATE DATABASE docker;
GRANT ALL PRIVILEGES ON DATABASE refugee TO postgres;