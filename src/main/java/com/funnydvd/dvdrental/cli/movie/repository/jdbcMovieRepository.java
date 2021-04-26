package com.funnydvd.dvdrental.cli.movie.repository;

import com.funnydvd.dvdrental.cli.movie.domain.Movie;
import com.funnydvd.dvdrental.cli.movie.domain.SearchCondition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.funnydvd.dvdrental.cli.movie.domain.SearchCondition.ALL;

class JdbcMovieRepository implements MovieRepository {

    //데이터베이스 연결 접속정보
    private String id = "java_web2";
    private String pw = "202104";
    private String url = "jdbc:oracle:thin:@localhost:1521:xe"; //db서버 위치 : 1521
    private String driverClassName = "oracle.jdbc.driver.OracleDriver"; //오라클

    @Override
    public void addMovie(Movie movie) {
        try (Connection conn = DriverManager.getConnection(url, id, pw)) {

            Class.forName(driverClassName);

            String sql = "INSERT INTO dvd_movie " +
                    "(serial_number, movie_name, nation, pub_year, charge) " +
                    "VALUES (seq_dvd_movie.nextval, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movie.getMovieName());
            pstmt.setString(2, movie.getNation());
            pstmt.setString(3, "" + movie.getPubYear());
            pstmt.setInt(4, movie.getCharge());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Movie> searchMovieList(String keyword, SearchCondition condition) {

        List<Movie> movieList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, id, pw)) {

            Class.forName(driverClassName);

            String sql = "SELECT * FROM dvd_movie";
            switch (condition) {
                case TITLE:
                    sql += " WHERE movie_name LIKE ?";
                    keyword = "%" + keyword + "%";
                    break;
                case NATION:
                    sql += " WHERE nation LIKE ?";
                    keyword = "%" + keyword + "%";
                    break;
                case PUB_YEAR:
                    sql += " WHERE pub_year LIKE ?";
                    keyword = "%" + keyword + "%";
                    break;
                case ALL:
                    break;
                case POSSIBLE:
                    sql += "WHERE rental = ?";
                    keyword = "F";
                    break;
                default:
                    return null;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);

            if (condition != ALL) {
                pstmt.setString(1, keyword);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                movieList.add(new Movie(rs));
            }
            return movieList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Movie searchMovieOne(int serialNumber) {
        try (Connection conn = DriverManager.getConnection(url, id, pw)) {

            Class.forName(driverClassName);

            String sql = "SELECT * FROM dvd_movie WHERE serial_number=?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, serialNumber);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Movie(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Movie removeMovie(int serialNumber) {
        try (Connection conn = DriverManager.getConnection(url, id, pw)) {

            Class.forName(driverClassName);

            String sql = "DELETE FROM dvd_movie WHERE serial_number = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, serialNumber);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}