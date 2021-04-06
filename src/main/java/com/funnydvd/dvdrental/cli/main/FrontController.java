package com.funnydvd.dvdrental.cli.main;

import com.funnydvd.dvdrental.cli.movie.controller.MovieController;
import com.funnydvd.dvdrental.cli.order.controller.OrderController;
import com.funnydvd.dvdrental.cli.user.controller.UserController;

//시스템 분기를 결정해주는 클래스
public class FrontController {

    private static AppController appController;

    //분기 결정 기능
    public static void chooseSystem(int selection) {

        switch (selection) {
            case 1:
                appController = new UserController();
                break;
            case 2:
                appController = new OrderController();
                break;
            case 3:
                appController = new MovieController();
                break;
            case 4:
                System.out.println("# 프로그램을 종료합니다.");
                System.exit(0);
            default:
                System.out.println("\n### 메뉴를 잘못 선택했습니다.");
                return;
        }//end switch

        appController.start();
    }
}
