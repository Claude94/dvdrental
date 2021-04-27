package com.funnydvd.dvdrental.cli.config;

import com.funnydvd.dvdrental.cli.movie.controller.MovieController;
import com.funnydvd.dvdrental.cli.movie.repository.MemoryMovieRepository;
import com.funnydvd.dvdrental.cli.movie.repository.MovieRepository;
import com.funnydvd.dvdrental.cli.order.controller.OrderController;
import com.funnydvd.dvdrental.cli.order.repository.MemoryOrderRepository;
import com.funnydvd.dvdrental.cli.user.controller.UserController;
import com.funnydvd.dvdrental.cli.user.repository.MemoryUserRepository;

//역할: 객체를 생성해서 필요에 맞는 구현체를 주입해주는 책임을 가진 클래스
public class DiContainer {
//    MovieRepository Bean 등록
    public MovieRepository movieRepository() {
        return new MemoryMovieRepository();
    }
//    MovieController에 의존성 주입
    public MovieController movieController() {
        return new MovieController(movieRepository());
    }
//    OrderController에 의존성 주입
    public OrderController orderController() {
        return new OrderController(
                new MemoryUserRepository(),
                movieRepository(),
                new MemoryOrderRepository()
        );
    }

//    UserRepository에 의존성 주입
    public UserController userController() {
        return new UserController(new MemoryUserRepository());
    }
}
