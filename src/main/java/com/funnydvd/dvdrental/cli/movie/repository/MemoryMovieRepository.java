package com.funnydvd.dvdrental.cli.movie.repository;

import com.funnydvd.dvdrental.cli.movie.domain.Movie;
import com.funnydvd.dvdrental.cli.movie.domain.SearchCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryMovieRepository implements MovieRepository {

    //영화정보들을 저장할 자료구조
    private static final Map<Integer, Movie> movieMemoryDB = new HashMap<>();
    
    static {
        insertTestData();
    }

    private static void insertTestData() {
        Movie movie1 = new Movie("인터스텔라", "미국", 2014);
        Movie movie2 = new Movie("포레스트 검프", "미국", 1994);
        Movie movie3 = new Movie("너의 이름은", "일본", 2017);
        Movie movie4 = new Movie("라라랜드", "미국", 2016);
        Movie movie5 = new Movie("레옹", "프랑스", 1994);
        Movie movie6 = new Movie("어바웃 타임", "영국", 2013);
        Movie movie7 = new Movie("타이타닉", "미국", 1998);
        Movie movie8 = new Movie("인생은 아름다워", "이탈리아", 1999);
        Movie movie9 = new Movie("쇼생크 탈출", "미국", 1995);
        Movie movie10 = new Movie("기생충", "대한민국", 2019);

        movieMemoryDB.put(movie1.getSerialNumber(), movie1);
        movieMemoryDB.put(movie2.getSerialNumber(), movie2);
        movieMemoryDB.put(movie3.getSerialNumber(), movie3);
        movieMemoryDB.put(movie4.getSerialNumber(), movie4);
        movieMemoryDB.put(movie5.getSerialNumber(), movie5);
        movieMemoryDB.put(movie6.getSerialNumber(), movie6);
        movieMemoryDB.put(movie7.getSerialNumber(), movie7);
        movieMemoryDB.put(movie8.getSerialNumber(), movie8);
        movieMemoryDB.put(movie9.getSerialNumber(), movie9);
        movieMemoryDB.put(movie10.getSerialNumber(), movie10);
    }

    @Override
    public void addMovie(Movie movie) {
        movieMemoryDB.put(movie.getSerialNumber(), movie);
    }

    @Override
    public List<Movie> searchMovieList(String keyword, SearchCondition condition) {

        //호출부에 전달할 검색데이터 리스트
        List<Movie> results = null;

        switch (condition) {
            case TITLE:
//                results = searchByTitle(keyword);
                results = search(keyword, (k, m) -> k.equals(m.getMovieName()));
                break;
            case NATION:
//                results = searchByNation(keyword);
                results = search(keyword, (k, m) -> k.equals(m.getNation()));
                break;
            case PUB_YEAR:
                results = search(keyword, (k, m) -> Integer.parseInt(k) == m.getPubYear());
                break;
            case ALL:
                results = search(keyword, (k, m) -> true);
                break;
            case POSSIBLE:
                results = search(keyword, (k, m) -> !m.isRental());
                break;
            default:
                return null;
        }
        return results;
    }

    private List<Movie> search(String keyword, MoviePredicate mp) {
        List<Movie> movieList = new ArrayList<>();
        for (int key : movieMemoryDB.keySet()) {
            Movie movie = movieMemoryDB.get(key);

            //검색 키워드와 발매연도 일치하는 movie만 리스트에 추가
            if (mp.test(keyword, movie)) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    /*private List<Movie> searchByPubYear(String keyword) throws NumberFormatException {
        List<Movie> movieList = new ArrayList<>();
        for (int key : movieMemoryDB.keySet()) {
            Movie movie = movieMemoryDB.get(key);

            //검색 키워드와 발매연도 일치하는 movie만 리스트에 추가
            if (Integer.parseInt(keyword) == movie.getPubYear()) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    private List<Movie> searchByNation(String keyword) {
        List<Movie> movieList = new ArrayList<>();
        for (int key : movieMemoryDB.keySet()) {
            Movie movie = movieMemoryDB.get(key);

            //검색 키워드와 국가가 일치하는 movie만 리스트에 추가
            if (keyword.equals(movie.getNation())) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    private List<Movie> searchByTitle(String keyword) {

        List<Movie> movieList = new ArrayList<>();
        for (int key : movieMemoryDB.keySet()) {
            Movie movie = movieMemoryDB.get(key);

            //검색 키워드와 제목이 일치하는 movie만 리스트에 추가
            if (keyword.equals(movie.getMovieName())) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    private List<Movie> searchAll() {
        
        List<Movie> movieList = new ArrayList<>();
        for (int key : movieMemoryDB.keySet()) {
            Movie movie = movieMemoryDB.get(key);
            movieList.add(movie);
        }
        return movieList;
    }
*/
    @Override
    public Movie searchMovieOne(int serialNumber) {
        return movieMemoryDB.get(serialNumber);
    }

    @Override
    public Movie removeMovie(int serialNumber) {
        movieMemoryDB.remove(serialNumber);
        return null;
    }

    //영화 검색 조건을 위한 인터페이스
    @FunctionalInterface
    interface MoviePredicate {
        boolean test(String keyword, Movie movie);
    }
}
