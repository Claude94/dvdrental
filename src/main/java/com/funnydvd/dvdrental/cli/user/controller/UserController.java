package com.funnydvd.dvdrental.cli.user.controller;

import com.funnydvd.dvdrental.cli.main.AppController;
import com.funnydvd.dvdrental.cli.user.domain.User;
import com.funnydvd.dvdrental.cli.user.repository.JdbcUserRepository;
import com.funnydvd.dvdrental.cli.user.repository.MemoryUserRepository;
import com.funnydvd.dvdrental.cli.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static com.funnydvd.dvdrental.cli.ui.AppUI.*;

public class UserController implements AppController {

//    private final UserRepository userRepository = new MemoryUserRepository();
        private final UserRepository userRepository = new JdbcUserRepository();
    @Override
    public void start() {

        while (true) {
            userManagementScreen();
            int selection = inputInteger(">>> ");

            switch (selection) {
                case 1:
                    join();
                    break;
                case 2:
                    showSearchResult();
                    break;
                case 3:
                    break;
                case 4:
                    return;
                default:
                    System.out.println("### 메뉴를 잘못 입력했습니다.");
            }
            System.out.println("\n===== 계속 진행하려면 ENTER를 누르세요 ======");
            inputString("");
        }

    }

    //회원 탈퇴
    private void deleteUser(List<Integer> userNumList) {
        System.out.println("\n## 탈퇴할 회원의 회원번호를 입력하세요.");
        int delUserNum = inputInteger(">>> ");

        if (userNumList.contains(delUserNum)) {
            User delUser = userRepository.deleteUser(delUserNum);
            System.out.printf("\n## %s[%s] 님의 회원정보가 정상 삭제되었습니다.\n"
                    , delUser.getUserName(), delUser.getPhoneNumber());
        } else {
            System.out.println("\n## 검색한 회원의 회원번호로만 삭제할 수 있습니다.");
        }
    }

    //회원 검색
    private void showSearchResult() {
        System.out.println("\n ### 조회할 회원의 이름을 입력하세요.");
        String name = inputString(">>> ");
        List<User> userList = userRepository.findAllByName(name);

        int count = userList.size();
        if (count > 0) {
            //검색된 회원의 회원번호만 따로 저장
            List<Integer> searchUserNums = new ArrayList<>();

            System.out.printf("\n================================= 회원 조회 결과(%d건) ================================\n", count);
            for (User user : userList) {
                System.out.println(user);
                searchUserNums.add(user.getUserNumber());
            }
            System.out.println("=========================================================================================");
            System.out.println("[ 1. 회원 탈퇴 | 2. 돌아가기]");
            int selection = inputInteger(">>> ");
            if (selection == 1) {
                deleteUser(searchUserNums);
            } else {
                return;
            }

        } else {
            System.out.println("\n### 조회 결과가 없습니다.");
        }
    }

    //회원 가입
    private void join() {
        System.out.println("\n====== 회원 가입을 진행합니다. ======");
        String name = inputString("# 회원명: ");
        String phone = inputString("# 전화번호: ");

        userRepository.addUser(new User(name, phone));
        System.out.printf("\n### [%s]님의 회원가입이 완료되었습니다.\n"
                            , name);
    }
}
