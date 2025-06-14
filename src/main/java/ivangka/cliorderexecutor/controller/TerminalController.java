package ivangka.cliorderexecutor.controller;

import ivangka.cliorderexecutor.exception.InvalidCommandException;
import ivangka.cliorderexecutor.exception.UnknownSymbolException;
import ivangka.cliorderexecutor.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static org.fusesource.jansi.Ansi.ansi;

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

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            String[] commandParts = command.trim().split("\\s+");

            try {
                executeCommand(commandParts);
            } catch (InvalidCommandException | UnknownSymbolException e) {
                System.out.println(ansi().fgBrightRed().a("  " + e.getMessage()).reset());
            }
        }
    }

    private void executeCommand(String[] commandParts) throws InvalidCommandException, UnknownSymbolException {
        if (commandParts.length == 0 || commandParts[0].isEmpty()) {
            return;
        }

        String retCode;
        switch (commandParts[0]) {
            // place an order
            case "!o": // !o [symbol] [sl] [tp] [risk $] -l [price]
                // market order
                if (commandParts.length == 5) {
                    retCode = orderService.openMarketOrder(
                            commandParts[1].toUpperCase(),
                            commandParts[2],
                            commandParts[3],
                            commandParts[4]
                    );
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen().a("  Market order has been opened").reset());
                    } else {
                        System.out.println(ansi().fgBrightRed()
                                .a("  The order hasn't been opened (retCode: " + retCode + ")").reset());
                    }

                // market order without tp
                } else if (commandParts.length == 4) {
                    retCode = orderService.openMarketOrder(
                            commandParts[1].toUpperCase(),
                            commandParts[2],
                            commandParts[3]
                    );
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen().a("  Market order has been opened").reset());
                    } else {
                        System.out.println(ansi().fgBrightRed()
                                .a("  The order hasn't been opened (retCode: " + retCode + ")").reset());
                    }

                // limit order
                } else if (commandParts.length == 7 && commandParts[5].equals("-l")) {
                    retCode = orderService.placeLimitOrder(
                            commandParts[1].toUpperCase(),
                            commandParts[2],
                            commandParts[3],
                            commandParts[4],
                            commandParts[6]
                    );
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen().a("  Limit order has been placed").reset());
                    } else {
                        System.out.println(ansi().fgBrightRed()
                                .a("  The order hasn't been placed (retCode: " + retCode + ")").reset());
                    }

                // limit order without tp
                } else if (commandParts.length == 6 && commandParts[4].equals("-l")){
                    retCode = orderService.placeLimitOrder(
                            commandParts[1].toUpperCase(),
                            commandParts[2],
                            commandParts[3],
                            commandParts[5]
                    );
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen().a("  Limit order has been placed").reset());
                    } else {
                        System.out.println(ansi().fgBrightRed()
                                .a("  The order hasn't been placed (retCode: " + retCode + ")").reset());
                    }

                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // set the leverage for specified pair
            case "!lev":
                if (commandParts.length == 3) {
                    retCode = orderService.setLeverage(
                            commandParts[1].toUpperCase(),
                            commandParts[2]
                    );
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen()
                                .a("  Leverage successfully set").reset());
                    } else {
                        System.out.println(ansi().fgBrightRed().a(
                                "  Leverage wasn't set (retCode: " + retCode + ")").reset());
                    }
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;


            // set the leverage to maximum for specified pair
            case "!lev_max":
                if (commandParts.length == 2) {
                    retCode = orderService.setMaxLeverage(
                            commandParts[1].toUpperCase()
                    );
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen()
                                .a("  Leverage successfully set").reset());
                    } else {
                        System.out.println(ansi().fgBrightRed().a(
                                "  Leverage wasn't set (retCode: " + retCode + ")").reset());
                    }
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // stop the program
            case "!ex":
                System.exit(0);
                break;

            // invalid command
            default:
                throw new InvalidCommandException("Unknown command");
        }
    }

}
