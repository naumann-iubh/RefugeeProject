# Refugee Course Management

Dieses Projekt ist entstanden, bei der Bearbeitung eines Projektbericht in Zuge meines Studiums

## Start DB

´´´
cd db

docker build -t refugeePostgres .

docker run 

docker run --name refugeeDatabase -p 6666:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=refugee -d refugeePostgres
´´´