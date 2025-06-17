package ivangka.cliorderexecutor.controller;

import ivangka.cliorderexecutor.exception.*;
import ivangka.cliorderexecutor.model.Position;
import ivangka.cliorderexecutor.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.List;
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
            } catch (InvalidCommandException | BadRetCodeException | OrderNotFoundException |
                     TooSmallOrderSizeException e) {
                System.out.println(ansi().fgBrightRed().a("  " + e.getMessage()).reset());
            }
        }
    }

    private void executeCommand(String[] commandParts)
            throws InvalidCommandException, BadRetCodeException, OrderNotFoundException, TooSmallOrderSizeException {
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
                        throw new BadRetCodeException("Error (retCode: " + retCode + ")");
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
                        throw new BadRetCodeException("Error (retCode: " + retCode + ")");
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
                        throw new BadRetCodeException("Error (retCode: " + retCode + ")");
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
                        throw new BadRetCodeException("Error (retCode: " + retCode + ")");
                    }

                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // close position for specified pair
            case "!x": // !x [symbol] [percent]
                if (commandParts.length == 3 || commandParts.length == 2) {
                    if (commandParts.length == 3) {
                        retCode = orderService.closePositions(
                                commandParts[1].toUpperCase(),
                                commandParts[2]
                        );
                    } else {
                        retCode = orderService.closePositions(
                                commandParts[1].toUpperCase(),
                               "100"
                        );
                    }
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen()
                                .a("  The position successfully closed").reset());
                    } else {
                        throw new BadRetCodeException("Error (retCode: " + retCode + ")");
                    }
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // cancel all limit orders for specified pair
            case "!c":
                if (commandParts.length == 2) {
                    retCode = orderService.cancelOrders(
                            commandParts[1].toUpperCase()
                    );
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen()
                                .a("  Limit orders successfully cancelled").reset());
                    } else {
                        throw new BadRetCodeException("Error (retCode: " + retCode + ")");
                    }
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // set the leverage for specified pair
            case "!lev": // !lev [symbol] [leverage]
                if (commandParts.length == 3 || commandParts.length == 2) {
                    if (commandParts.length == 3) {
                        retCode = orderService.setLeverage(
                                commandParts[1].toUpperCase(),
                                commandParts[2]
                        );
                    } else {
                        retCode = orderService.setLeverage(
                                commandParts[1].toUpperCase(),
                                "Max"
                        );
                    }
                    if (retCode.equals("0")) {
                        System.out.println(ansi().fgBrightGreen()
                                .a("  Leverage successfully set").reset());
                    } else {
                        throw new BadRetCodeException("Error (retCode: " + retCode + ")");
                    }
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // get position info
            case "!gpi": // !gpi [symbol]
                if (commandParts.length == 2) {
                    List<Position> positions = orderService.positions(commandParts[1].toUpperCase());
                    for (int i = 0; i < positions.size(); i++) {
                        String positionStr = positions.get(i).toString();
                        if (i > 0) {
                            positionStr = positionStr.substring(positionStr.indexOf('\n') + 1);
                        }
                        System.out.println(positionStr);
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
