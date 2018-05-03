CREATE SEQUENCE person_person_id_seq;


CREATE TABLE public.person
(
  person_id integer NOT NULL DEFAULT nextval('person_person_id_seq'::regclass),
  surname character varying(30) NOT NULL,
  name character varying(30) NOT NULL,
  middlename character varying(40) NOT NULL,
  CONSTRAINT pk_1_person PRIMARY KEY (person_id)
);

CREATE SEQUENCE passport_passport_id_seq;

CREATE TABLE public.passport
(
  passport_id integer NOT NULL DEFAULT nextval('passport_passport_id_seq'::regclass),
  person_id integer NOT NULL,
  passport_mame character varying(100),
  dob date,
  gender character varying(1),
  ciry character varying DEFAULT 'Москва'::character varying,
  status boolean,
  given boolean,
  CONSTRAINT pk_1_passport PRIMARY KEY (passport_id),
  CONSTRAINT fp_2_passport FOREIGN KEY (person_id)
  REFERENCES public.person (person_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT passport_name_key UNIQUE (passport_id, passport_mame),
  CONSTRAINT passport_dob_check CHECK (dob < '2004-01-01'::date)
);

SELECT * FROM passport;
SELECT * from person;

insert INTO person VALUES (1, 'Иванов', 'Иван', 'ИВанович');
insert INTO passport VALUES (1,1,'Иванов Иван Иванович', '01.01.1990', 'M', false, false);

insert INTO person VALUES (2, 'Качков', 'Андрей', 'Качкович');
insert INTO passport VALUES (2,2,'Качков Андрей Качкович', '01.01.1990', 'M', false, false);

insert INTO person VALUES (3, 'Жан', 'Клод', 'Вандамович');
insert INTO passport VALUES (3,3,'Жан Клод Вандамович', '01.01.1980', 'M', false, false);

insert INTO person VALUES (4, 'Арнольдов', 'Шварц', 'Неггерович');
insert INTO passport VALUES (4,4,'Арнольдов Шварц Неггерович', '01.01.1970', 'M', false, false);

insert INTO person VALUES (5, 'Йоко', 'Зуна', 'Зунович');
insert INTO passport VALUES (5,5,'Йоко Зуна Зунович', '01.01.1950', 'F', false, false);

insert INTO person VALUES (6, 'Натов', 'Нат', 'НАтович');
insert INTO passport VALUES (6,6,'Натов Нат НАточич', null, 'F', false, false);

--fields as elements

SELECT xmlelement(name person, xmlforest("person_id","surname","name","middlename")) from public."person";

-- fields as attributes

SELECT xmlelement(name person, xmlattributes("person_id","surname","name","middlename")) from public."person";

-- add root element

SELECT xmlelement(name root, xmlagg(xmlelement(name person, xmlforest("person_id","surname","name","middlename")))) from public."person";

-- rename rows

SELECT xmlelement(name renamed_row, xmlforest("person_id","surname","name","middlename")) from public."person";

-- default schema

SELECT table_to_xmlschema('person', true, true, '');

-- auto

SELECT table_to_xml('person', true, false, '');

-- nulls

SELECT table_to_xml('passport', true, false, '');

-- query to xml

SELECT query_to_xml('SELECT * FROM public."person" WHERE person_id=6', TRUE ,true, 'name');

-- read from xml file

SELECT XMLPARSE(DOCUMENT convert_from(pg_read_binary_file('passports2.xml'), 'UTF8'));

-- check exists

select xpath_exists('//passport/row[person_id=2 and gender="M"]',
                    xmlparse(DOCUMENT pg_read_file('passports2.xml')));

-- check all surnames

select unnest(xpath('//passport/row/passport_mame/text()',
                    xmlparse(DOCUMENT pg_read_file('passports2.xml'))));

-- count all persons where passports are not given

select unnest(xpath('//passport/row/passport_mame/text()',
                    xmlparse(DOCUMENT pg_read_file('passports2.xml'))));

-- get all names and all passports status

SELECT xpath('passport_mame/text()',a),  xpath('count(given)',a)
from unnest(xpath('//passport/row', xmlparse(DOCUMENT pg_read_file('passports2.xml')))) as a;