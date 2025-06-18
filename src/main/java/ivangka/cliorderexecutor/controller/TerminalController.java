package ivangka.cliorderexecutor.controller;

import ivangka.cliorderexecutor.exception.*;
import ivangka.cliorderexecutor.model.Order;
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
            } catch (Exception e) {
                System.out.println(ansi().fgBrightRed().a("  Unexpected exception: "
                        + e.getClass().getSimpleName()).reset());
            }
        }
    }

    private void executeCommand(String[] commandParts)
            throws InvalidCommandException, BadRetCodeException, OrderNotFoundException, TooSmallOrderSizeException,
            InterruptedException {
        if (commandParts.length == 0 || commandParts[0].isEmpty()) {
            return;
        }

        // formatting symbol
        if (commandParts.length > 1) {
            String symbol = commandParts[1].toUpperCase();
            commandParts[1] = symbol.endsWith("USDT") ? symbol
                    : symbol.endsWith("USDC") ? symbol.substring(0, symbol.length() - 4) + "PERP"
                    : symbol + "USDT";
        }

        switch (commandParts[0]) {
            // place an order
            case "!o": // !o [symbol] [sl] [tp] [risk $] -l [price]
                // market order
                if (commandParts.length == 5) {
                    orderService.openMarketOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[4]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Market order has been opened").reset());

                // market order without tp
                } else if (commandParts.length == 4) {
                    orderService.openMarketOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Market order has been opened").reset());

                // limit order
                } else if (commandParts.length == 7 && commandParts[5].equals("-l")) {
                    orderService.placeLimitOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[4],
                            commandParts[6]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Limit order has been placed").reset());

                // limit order without tp
                } else if (commandParts.length == 6 && commandParts[4].equals("-l")){
                    orderService.placeLimitOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[5]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Limit order has been placed").reset());

                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // close positions
            case "!x": // !x [symbol] [percent]
                if (commandParts.length == 3 || commandParts.length == 2 || commandParts.length == 1) {
                    if (commandParts.length == 3) { // percent
                        orderService.closePositions(
                                commandParts[1],
                                commandParts[2]
                        );
                    } else if (commandParts.length == 2) { // 100%
                        orderService.closePositions(
                                commandParts[1],
                               "100"
                        );
                    } else { // close all
                        orderService.closePositions("-all", "100");
                    }
                    System.out.println(ansi().fgBrightGreen().a("  The positions successfully closed").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // cancel orders
            case "!c": // !c [symbol]
                if (commandParts.length == 2) { // by symbol
                    orderService.cancelOrders(
                            commandParts[1]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Orders successfully cancelled").reset());
                } else if (commandParts.length == 1) { // all orders
                    orderService.cancelOrders(
                            "-all"
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Orders successfully cancelled").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // set the leverage for specified pair
            case "!lev": // !lev [symbol] [leverage]
                if (commandParts.length == 3 || commandParts.length == 2) {
                    if (commandParts.length == 3) { // lev
                        orderService.setLeverage(
                                commandParts[1],
                                commandParts[2]
                        );
                    } else { // max lev
                        orderService.setLeverage(
                                commandParts[1],
                                "-max"
                        );
                    }
                    System.out.println(ansi().fgBrightGreen().a("  Leverage successfully set").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format");
                }
                break;

            // get position info
            case "!gpi": // !gpi [symbol]
                if (commandParts.length == 2 || commandParts.length == 1) {
                    List<Position> positions;
                    if (commandParts.length == 2) { // position by symbol
                        positions = orderService.positions(commandParts[1]);
                    } else { // all positions
                        positions = orderService.positions("-all");
                    }
                    // print positions
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

            // get open orders
            case "!goo": // !goo [symbol]
                if (commandParts.length == 2 || commandParts.length == 1) {
                    List<Order> orders;
                    if (commandParts.length == 2) { // orders by symbol
                        orders = orderService.orders(commandParts[1]);
                    } else { // all orders
                        orders = orderService.orders("-all");
                    }
                    // print orders
                    for (int i = 0; i < orders.size(); i++) {
                        String orderStr = orders.get(i).toString();
                        if (i > 0) {
                            orderStr = orderStr.substring(orderStr.indexOf('\n') + 1);
                        }
                        System.out.println(orderStr);
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
