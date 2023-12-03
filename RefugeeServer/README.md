# RefugeeApp

URL to connect to local machine: 10.0.2.2

## Anforderungen

### Anbieterseite

[x][] Kurse sollen alle wichtigen Daten wie Name, Kurzbeschreibung, Termine, Ort, max. Teilnehmerzahl, Teilnahmevoraussetzungen, lat, lon etc. erfasst werden können.
[] App holt sich über OSM die geolocation (http://nominatim.openstreetmap.org/search?q=Geranienweg 3 Poing&format=json&addressdetails=1)
[x][] Übersicht über die aktuell eingeschriebenen Asylbewerber


### Teilnehmerseite
[][] Suchfunktion
    [x][] Der Name oder die Beschreibung enthalten ein bestimmtes Stichwort.
    [][] Der Kurs findet in einem bestimmten Umkreis um den aktuellen Standort statt.
    [][] Der Kurs findet in einem bestimmten Umkreis um einen bestimmten Ort statt.
[][] Die Asylbewerber:innen können die Suchergebnisse nach Nähe zum aktuellen Standort sortieren
[][] Asylbewerber:innen sollen sich an Kursen anmelden können. 
    [x][] Per App
    [][] Per QrCode

## libs
http: https://square.github.io/okhttp/
logging: timber


https://betterprogramming.pub/create-an-app-that-uses-livedata-and-viewmodel-in-java-f8086ca94229

https://codingwithmitch.com/blog/getting-started-with-mvvm-android/


## Database tables


CREATE TABLE IF NOT EXISTS courses (
    course_id serial PRIMARY KEY,
    name VARCHAR (50) UNIQUE NOT NULL,
    short_description TEXT,
    dates TIMESTAMP [] NOT NULL,
    place VARCHAR (50) NOT NULL,
    max_contestants INT NOT NULL,
    requirements text [],
    lat VARCHAR (50),
    lon VARCHAR (50)
);

TIMESTAMP format : 1999-01-08 04:05:06

CREATE TABLE IF NOT EXISTS contestants (
    contestants_id serial PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);


m to n relation

CREATE TABLE IF NOT EXISTS courses_contestants (
    course_id INT REFERENCES courses(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
    contestants_id INT REFERENCES contestants(contestants_id) ON UPDATE CASCADE,
    CONSTRAINT course_product_id PRIMARY KEY (course_id, contestants_id)  
);

## Aufbau
- PostgreSql Datenbank, die alle Informationen hält
- Abstraktion über einen RestService vor der Datenbank, händelt alle Datenbank transaktionen

## Vorgehen/Probleme
- Generell werden OpenSource Lösungen bevorzugt
- Für Android application gibt es keine offizielle Postgres Driver Untersützung
- Daher kleiner RestService der Schnittstellen für die Apps anbietet diese in CRUD Operation an die DB sendet
- Es wird Docker als Werkzeug verwendet, um Datenbank und RestService in eigenen, leicht verwaltbaren Container zu administrieren 
- Um Suchergebnisse mit einem Standort verknüpfen zu können, werden mithilfe der Addresse und Nominatim geocoder API zusätzlich lat lon gespeichert.