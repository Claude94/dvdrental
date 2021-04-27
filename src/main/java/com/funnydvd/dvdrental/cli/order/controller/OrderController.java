package com.funnydvd.dvdrental.cli.order.controller;

import com.funnydvd.dvdrental.cli.main.AppController;
import com.funnydvd.dvdrental.cli.movie.domain.Movie;
import com.funnydvd.dvdrental.cli.movie.domain.SearchCondition;
import com.funnydvd.dvdrental.cli.movie.repository.MemoryMovieRepository;
import com.funnydvd.dvdrental.cli.movie.repository.MovieRepository;
import com.funnydvd.dvdrental.cli.order.domain.Order;
import com.funnydvd.dvdrental.cli.order.repository.MemoryOrderRepository;
import com.funnydvd.dvdrental.cli.order.repository.OrderRepository;
import com.funnydvd.dvdrental.cli.user.domain.User;
import com.funnydvd.dvdrental.cli.user.repository.MemoryUserRepository;
import com.funnydvd.dvdrental.cli.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.funnydvd.dvdrental.cli.ui.AppUI.*;

public class OrderController implements AppController {

    private UserRepository userRepository;
    private MovieRepository movieRepository;
    private OrderRepository orderRepository;

    public OrderController(UserRepository userRepository, MovieRepository movieRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void start() {

        while (true) {
            orderManagementScreen();
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1:
                    processOrderDvd();
                    break;
                case 2:
                    processReturnDvd();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("## 메뉴를 잘못 입력했습니다.");
            }
        }
    }

    //대여 처리
    private void processOrderDvd() {

        while (true) {
            List<Movie> movieList = movieRepository.searchMovieList("", SearchCondition.POSSIBLE);
            int count = movieList.size();

            if (count > 0) {
                System.out.println("\n======================== 대여 가능 DVD 목록 =========================");
                for (Movie movie : movieList) {
                    System.out.println(movie);
                }
                System.out.println("=======================================================================");
                System.out.println("### 대여할 DVD의 번호를 입력하세요. 이전으로 돌아가려면 0을 입력하세요.");
                int serialNumber = inputInteger(">>> ");

                if (serialNumber == 0) return;

                Movie rentalMovie = movieRepository.searchMovieOne(serialNumber);

                System.out.printf("\n## [%s] DVD를 대여합니다.\n", rentalMovie.getMovieName());
                System.out.println("## 대여자의 이름을 입력하세요.");
                String userName = inputString(">>> ");

                List<User> users = userRepository.findAllByName(userName);

                if (users.size() > 0) {
                    List<Integer> userNums = new ArrayList<>();
                    System.out.println("\n====================== 회원 정보 =======================");
                    for (User user : users) {
                        System.out.println(user);
                        userNums.add(user.getUserNumber());
                    }
                    System.out.println("=========================================================");
                    System.out.println("## 대여할 회원의 회원번호을 입력하세요.");
                    int userNumber = inputInteger(">>> ");

                    if (userNums.contains(userNumber)) {
                        //대여 완료 처리
                        User rentalUser = userRepository.findByUserNumber(userNumber); //렌탈 유저 정보 획득

                        Order newOrder = new Order(rentalUser, rentalMovie);

                        orderRepository.saveOrder(newOrder);

                        String phoneNumber = rentalUser.getPhoneNumber();

                        System.out.printf("\n## [%s(%s)회원님] 대여 처리가 완료되었습니다. 감사합니다!\n"
                                , rentalUser.getUserName(), phoneNumber.substring(phoneNumber.lastIndexOf("-") + 1));
                        System.out.printf("## 현재 등급: [%s], 총 누적 결제액: %d원\n", rentalUser.getGrade(), rentalUser.getTotalPaying());

                    } else {
                        System.out.println("\n## 검색된 회원의 번호를 입력하세요.");
                    }
                } else {
                    System.out.println("\n## 대여자 정보가 없습니다.");
                }

            } else {
                System.out.println("\n## 대여 가능한 DVD가 없습니다.");
            }
        }
    }

    //반납 처리
    private void processReturnDvd() {
        System.out.println("\n============================ 반납관리 시스템을 실행합니다. ============================");
        System.out.println("## 반납자의 이름을 입력하세요.");
        String name = inputString(">>> ");
        List<User> users = userRepository.findAllByName(name);

        int count = users.size();
        if (count > 0) {
            List<Integer> userNums = new ArrayList<>();
            System.out.printf("\n================================검색 결과(총 %d건)===================================\n", count);
            for (User user : users) {
                System.out.println(user);
                userNums.add(user.getUserNumber());
            }
            System.out.println("========================================================================================");
            System.out.println("## 반납자의 회원번호를 입력하세요.");
            int userNumber = inputInteger(">>> ");

            showReturnedList(userNums, userNumber);
        } else {
            System.out.println("\n## 반납자의 정보가 없습니다.");
        }
    }

    private void showReturnedList(List<Integer> userNums, int userNumber) {
        if (userNums.contains(userNumber)) {
            User returnUser = userRepository.findByUserNumber(userNumber);
            System.out.printf("\n## 현재 [%s]회원님의 대여 목록입니다.\n", returnUser.getUserName());
            System.out.println("============================================================================================");
            Map<Integer, Order> orderList = returnUser.getOrderMap();
            for (int key : orderList.keySet()) {
                System.out.println(orderList.get(key));
            }
            System.out.println("============================================================================================");
            System.out.println("## 반납할 DVD의 번호를 입력하세요.");
            int returnMovieSerialNumber = inputInteger(">>> ");

            if (orderList.containsKey(returnMovieSerialNumber)) {

                Order order = returnUser.getOrder(returnMovieSerialNumber);
                order.checkOverdue();
                orderRepository.returnOrder(order);

                int charge = order.getOverdueCharge();
                if (charge > 0) {
                    System.out.printf("\n## 반납이 완료되었습니다. %d원을 결제해주세요.\n", charge);
                } else {
                    System.out.println("\n## 반납이 완료되었습니다. 즐거운 하루되세요!");
                }
            } else {
                System.out.println("\n# 해당 DVD는 반납대상이 아닙니다.");
            }

        } else {
            System.out.println("\n## 검색된 회원의 번호를 입력하세요.");
        }
    }
}
