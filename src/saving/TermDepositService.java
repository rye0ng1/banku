package saving;

import database.AccountDao;
import database.DatabaseManager;
import database.DateDao;
import database.UserDao;
import java.util.Scanner;

public class TermDepositService {
    private final Scanner scanner;
    private final UserDao userDao;
    private final AccountDao accountDao;
    private final SavingServiceManager savingServiceManager;
    private final DateDao dateDao;

    public TermDepositService(SavingServiceManager savingServiceManager){
        this.accountDao = new AccountDao(new DatabaseManager());
        this.userDao = new UserDao(new DatabaseManager());
        this.scanner = new Scanner(System.in);
        this.savingServiceManager = savingServiceManager;
        this.dateDao = new DateDao(new DatabaseManager());
    }

    public void doSavingService(String loggedInUserId) {
        try {
            System.out.println();
            System.out.println("\n\n[적금 서비스]");
            System.out.println("============================================");
            System.out.println("적금 가능한 상품");
            System.out.println("[1] 6개월 적금 - 연 2.0%, 월 ₩ 200,000    예상 수령액 : ₩ 1,224,000");
            System.out.println("[2] 12개월 적금 - 연 3.0%, 월 ₩ 500,000    예상 수령액 : ₩ 6,180,000");
            System.out.println("[3] 24개월 적금 - 연 5.0%, 월 ₩ 1,000,000    예상 수령액 : ₩ 25,830,000");
            System.out.println("[0] 뒤로가기");
            System.out.println("============================================");

            while (true) {
                String account = userDao.findUserToAccount(loggedInUserId);
                String startDate = dateDao.getDate();
                String input = "";

                System.out.print("적금하실 상품 번호의 숫자만 입력하세요 (0~3): ");
                input = scanner.nextLine();

                if(!input.matches("^[0-3]+$")){
                    System.out.println("숫자만 입력하세요.");
                    continue;
                }

                int termDepositNum = Integer.parseInt(input);

                switch (termDepositNum){
                    case 1:
                        if (accountDao.hasSavings(account, 2)) {
                            System.out.println("이미 가입한 적금입니다.");
                        } else {
                            int type1 = accountDao.executeTransaction(account, 200000, "savings", startDate);
                            if(type1 == 0){
                                accountDao.updateSavings(account, 1, "200000", startDate);
                                System.out.println("적금 가입이 완료되었습니다!");
                            } else {
                                System.out.println();
                                System.out.println("현재 잔액이 부족합니다. 현재 남은 잔액은 ₩" + accountDao.showSavings(account) + " 입니다.");
                            }
                        }
                        break;
                    case 2:
                        if (accountDao.hasSavings(account, 3)) {
                            System.out.println("이미 가입한 적금입니다.");
                        }else {
                            int type2 = accountDao.executeTransaction(account, 500000, "savings", startDate);
                            if (type2 == 0) {
                                accountDao.updateSavings(account, 2, "500000", startDate);
                                System.out.println("적금 가입이 완료되었습니다!");
                            } else {
                                System.out.println();
                                System.out.println("현재 잔액이 부족합니다. 현재 남은 잔액은 ₩" + accountDao.showSavings(account) + " 입니다.");
                            }
                        }
                        break;
                    case 3:
                        if (accountDao.hasSavings(account, 4)) {
                            System.out.println("이미 가입한 적금입니다.");
                        }else {
                            int type3 = accountDao.executeTransaction(account, 1000000, "savings", startDate);
                            if (type3 == 0) {
                                accountDao.updateSavings(account, 3, "1000000", startDate);
                                System.out.println("적금 가입이 완료되었습니다!");
                            } else {
                                System.out.println("현재 잔액이 부족합니다. 현재 남은 잔액은 ₩" + accountDao.showSavings(account) + " 입니다.");
                            }
                        }
                        break;
                    case 0:
                        return;
                }
                System.out.println();
                break;
            }
        } catch (Exception e){
            e.getMessage();
        }
    }
}
