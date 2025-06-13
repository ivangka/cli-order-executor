package ivangka.cliorderexecutor.controller;

import ivangka.cliorderexecutor.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class TerminalController {

    private final OrderService orderService;

    @Autowired
    public TerminalController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void controller() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The program is running");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            String[] commandParts = command.split(" ");

            switch (commandParts[0]) {
                // place an order
                case "!o": // !o [symbol] [sl] [tp] [risk $]
                    String retCode = orderService.placeOrder(
                            commandParts[1].toUpperCase(),
                            commandParts[2],
                            commandParts[3],
                            commandParts[4]
                    );
                    if (retCode.equals("0"))
                        System.out.println("The order has been placed");
                    else
                        System.out.println("Something went wrong (retCode: " + retCode + ")");
                    break;

                // stop the program
                case "!ex":
                    scanner.close();
                    System.exit(1);
                    break;

                // invalid command
                default:
                    System.out.println("Invalid command, try again");
                    break;
            }
            System.out.println();
        }
    }

}
