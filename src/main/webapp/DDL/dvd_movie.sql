-- movie 도메인 테이블 설계
CREATE SEQUENCE seq_dvd_movie;

CREATE TABLE dvd_movie (
    serial_number NUMBER(10),
    movie_name VARCHAR2(50) NOT NULL,
    nation VARCHAR2(20) NOT NULL,
    pub_year CHAR(4) NOT NULL,
    charge NUMBER(5) NOT NULL,
    rental CHAR(1) DEFAULT 'F',
    user_number NUMBER(10),
    CONSTRAINT pk_dvd_movie PRIMARY KEY (serial_number),
    CONSTRAINT fk_dvd_movie_dvd_user
    FOREIGN KEY (user_number)
    REFERENCES dvd_user (user_number)
);
