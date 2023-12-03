#!/bin/bash

docker pull postgres:16.0

docker run -p 6666:5432 -v data:/var/lib/postgresql/data --name refugeeDatabase -e POSTGRES_PASSWORD=mysecretpassword -d postgres:16.0 