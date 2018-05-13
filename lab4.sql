-- 1.2 limit

SELECT *
FROM passport
LIMIT 3;

-- 1.2 returning

UPDATE passport
SET given = TRUE
WHERE passport_id <= 2
RETURNING passport_id, given;

-- 1.3 recursive
-- wtf

WITH RECURSIVE LeastDOB(person1, least_dob, is_cycle) AS (
  SELECT
    passport_mame,
    dob,
    FALSE AS a
  FROM passport
  WHERE dob IS NOT NULL
  UNION ALL
  SELECT
    passport_mame,
    dob,
    FALSE AS b
  FROM passport
  WHERE dob IS NOT NULL)
SELECT least_dob
FROM LeastDOB;


-- 1.3 row_number

SELECT
  passport_id,
  row_number()
  OVER (
    ORDER BY dob ) row_number,
  passport_mame,
  dob
FROM passport
ORDER BY dob;

-- 1.3 rank

SELECT
  passport_id,
  rank()
  OVER (
    ORDER BY dob ) rank,
  passport_mame,
  dob
FROM passport
ORDER BY passport_mame;

-- 1.3 dense_rank

SELECT
  passport_id,
  dense_rank()
  OVER (
    ORDER BY dob ) dense_rank,
  passport_mame,
  dob
FROM passport
ORDER BY dob;

-- 1.3 ntile

SELECT
  passport_id,
  ntile(3)
  OVER (
    ORDER BY dob ) ntile,
  passport_mame,
  dob
FROM passport
ORDER BY dob;

-- 2 scalar func

CREATE OR REPLACE FUNCTION "shortestNameLength"()
  RETURNS INTEGER AS
'SELECT min(char_length(passport_mame))
 FROM passport'
LANGUAGE SQL
VOLATILE;

SELECT "shortestNameLength"();

-- 2 table func

SELECT *
FROM passport;

DROP FUNCTION "selectBySurname"( TEXT );

CREATE OR REPLACE FUNCTION "selectBySurname"(IN param TEXT)
  RETURNS TABLE(passport_mame TEXT, dob DATE, passport_id INT) AS
'SELECT
   passport_mame,
   dob,
   passport_id
 FROM passport
   JOIN person p ON passport.person_id = p.person_id
 WHERE p.surname = param'
LANGUAGE SQL
VOLATILE;

SELECT "selectBySurname"('Иванов');

-- 2 stored procedure

DROP FUNCTION "addPerson"();

CREATE OR REPLACE FUNCTION "addPerson"(
  ID                INT,
  param_Surname     TEXT,
  param_Name        TEXT,
  param_MiddlieName TEXT,
  param_Dob         DATE,
  param_City        TEXT)
  RETURNS VOID AS $$
BEGIN
  IF (exists(SELECT person_id
             FROM person
             WHERE (surname = param_Surname AND name = param_Name AND middlename = param_MiddlieName)))
  THEN RAISE EXCEPTION 'person already exists';
  ELSE
    INSERT INTO person VALUES (ID, param_Surname, param_Name, param_MiddlieName);
    INSERT INTO passport VALUES
      (ID, ID, param_Surname || ' ' || param_Name || ' ' || param_MiddlieName, param_Dob, 'M', param_City, FALSE,
       FALSE);
  END IF;
END;
$$
LANGUAGE plpgsql;

SELECT "addPerson"(7, TEXT 'Иванов', TEXT 'Один', TEXT 'Дваевич', DATE '1995-01-01', TEXT 'Ростов');
SELECT "addPerson"(8, TEXT 'Ивановaaa', TEXT 'Один', TEXT 'Дваевич', DATE '1995-01-01', TEXT 'Ростов');

-- 2 func with cursor

CREATE OR REPLACE FUNCTION personsGiven()
  RETURNS TEXT AS $$
DECLARE
  str TEXT DEFAULT '';
    curs CURSOR FOR SELECT passport_mame
                    FROM passport
                    WHERE given = TRUE;
  row RECORD;
BEGIN
  OPEN curs;
  LOOP
    FETCH curs INTO row;
    EXIT WHEN NOT FOUND;
    str = str || ' ' || row.passport_mame || ', ';
  END LOOP;
  CLOSE curs;
  RETURN str;
END;
$$
LANGUAGE plpgsql;

SELECT personsGiven();

-- 3 aggregate

CREATE OR REPLACE FUNCTION gcd(a BIGINT, b BIGINT)
  RETURNS BIGINT
IMMUTABLE
STRICT
LANGUAGE SQL
AS $$
WITH RECURSIVE t(a, b) AS (
  VALUES (abs($1) :: BIGINT, abs($2) :: BIGINT)
  UNION ALL
  SELECT
    b,
    MOD(a, b)
  FROM t
  WHERE b > 0
)
SELECT a
FROM t
WHERE b = 0
$$;

SELECT gcd(12, 13);

DROP FUNCTION gcd_state( BIGINT, BIGINT ) CASCADE;

CREATE OR REPLACE FUNCTION gcd_state(state BIGINT, val BIGINT)
  RETURNS BIGINT
as $$
BEGIN
  RETURN gcd(state, val);
END
$$
LANGUAGE plpgsql;

CREATE AGGREGATE gcd (BIGINT)
(
  SFUNC = gcd_state,
  STYPE = BIGINT,
  INITCOND = 0
);

CREATE TABLE ints
(
  val BIGINT
);

INSERT INTO ints VALUES (5);
INSERT INTO ints VALUES (10);
INSERT INTO ints VALUES (15);
INSERT INTO ints VALUES (100);

SELECT gcd(val)
FROM ints;


-- 4 dynamic query

CREATE or REPLACE FUNCTION copy_passports(
  city text
) RETURNS VOID AS $$
BEGIN
  EXECUTE format('CREATE TABLE if not exists %I
      ( passport_mame character varying(100),
          dob date,
          gender character varying(1),
          status boolean,
          given boolean
      );
  ', city);
  EXECUTE format('delete from %I', city);

  EXECUTE format('insert into %I ' ||
                 'select passport_mame, dob, gender, status, given from passport ' ||
                 'where ciry=$1', city) USING  city;
END;
$$ LANGUAGE plpgsql;

SELECT * from copy_passports('Ростов');
SELECT * FROM Ростов;

CREATE TABLE if not exists city
( passport_mame character varying(100) primary key,
  dob date,
  gender character varying(1),
  status boolean,
  given boolean
);