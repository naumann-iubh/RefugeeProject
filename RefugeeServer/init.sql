CREATE USER docker;
CREATE DATABASE docker;
GRANT ALL PRIVILEGES ON DATABASE refugee TO postgres;


CREATE TABLE IF NOT EXISTS courses (
    course_id serial PRIMARY KEY,
    name VARCHAR (50) UNIQUE NOT NULL,
    short_description TEXT,
    dates TEXT [] NOT NULL,
    place VARCHAR (50) NOT NULL,
    max_contestants INT NOT NULL,
    requirements text [],
    lat VARCHAR(50) NOT NULL,
    lon VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS contestants (
    contestants_id serial PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS registration (
    registration_id serial PRIMARY KEY,
    registered_at VARCHAR(50),
    course_id INT NOT NULL REFERENCES courses(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
    contestants_id INT NOT NULL REFERENCES contestants(contestants_id) ON UPDATE CASCADE
);