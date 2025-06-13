package ivangka.cliorderexecutor.controller;

import ivangka.cliorderexecutor.exception.InvalidCommandException;
import ivangka.cliorderexecutor.exception.UnknownSymbolException;
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
            String[] commandParts = command.trim().split("\\s+");

            try {
                executeCommand(commandParts);
            } catch (InvalidCommandException | UnknownSymbolException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void executeCommand(String[] commandParts) throws InvalidCommandException, UnknownSymbolException {
        if (commandParts.length == 0 || commandParts[0].isEmpty()) {
            return;
        }

        switch (commandParts[0]) {
            // place an order
            case "!o": // !o [symbol] [sl] [tp] [risk $]
                if (commandParts.length != 5) {
                    throw new InvalidCommandException("Incorrect command format");
                }
                String retCode = orderService.placeOrder(
                        commandParts[1].toUpperCase(),
                        commandParts[2],
                        commandParts[3],
                        commandParts[4]
                );

                if (retCode.equals("0")) {
                    System.out.println("The order has been placed");
                } else {
                    System.out.println("The order hasn't been placed (retCode: " + retCode + ")");
                }
                break;

            // stop the program
            case "!ex":
                System.out.println("Bye!");
                System.exit(0);
                break;

            // invalid command
            default:
                throw new InvalidCommandException("Unknown command");
        }
    }

}
