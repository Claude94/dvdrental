package com.funnydvd.dvdrental.cli.movie.repository;

import com.funnydvd.dvdrental.cli.movie.domain.Movie;
import com.funnydvd.dvdrental.cli.movie.domain.SearchCondition;

import java.util.List;

//역할: 데이터 저장소 역할을 수행하는 저장소 기능의 명세
public interface MovieRepository {

    //영화 정보 추가
    void addMovie(Movie movie);

    //조건별 영화 검색
    /**
     * @param keyword 검색어
     * @param condition 검색 조건
     * @return 검색에 따른 영화정보 리스트
     */

//    searchMovieList("메롱", PUB_YEAR);
    List<Movie> searchMovieList(String keyword, SearchCondition condition);

    //특정 영화 1개 검색
    Movie searchMovieOne(int serialNumber);

    //특정 영화 삭제
    Movie removeMovie(int serialNumber);

}
