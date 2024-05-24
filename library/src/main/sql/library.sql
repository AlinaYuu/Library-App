CREATE TABLE person
(
    id            SERIAL PRIMARY KEY,
    full_name     VARCHAR(100) NOT NULL,
    year_of_birth INT          NOT NULL
);

CREATE TABLE book
(
    id               SERIAL PRIMARY KEY,
    title            VARCHAR(100) NOT NULL,
    author           VARCHAR(100) NOT NULL,
    year             INT          NOT NULL,
    publishing_house VARCHAR(100) NOT NULL,
    number_of_copies INT          NOT NULL
);

CREATE TABLE book_person
(
    book_id     INT REFERENCES book (id) ON DELETE CASCADE,
    person_id   INT REFERENCES person (id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, person_id),
    borrow_date DATE NOT NULL
);
