package ivangka.cliorderexecutor.cli;

import ivangka.cliorderexecutor.exception.*;
import ivangka.cliorderexecutor.model.Order;
import ivangka.cliorderexecutor.model.Position;
import ivangka.cliorderexecutor.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.List;
import java.util.Scanner;

@Component
public class CliHandler {

    private final OrderService orderService;

    @Autowired
    public CliHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n  :: CLI Order Executor ::\n");

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
            case "!o": // !o [symbol] [sl] [tp] [risk $] -l [price] -t [trigger]
                // market order
                if (commandParts.length == 5) {
                    orderService.openMarketOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[4]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Market order opened successfully").reset());

                // market order without tp
                } else if (commandParts.length == 4) {
                    orderService.openMarketOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Market order opened successfully").reset());

                // limit order
                } else if (commandParts.length == 7 && commandParts[5].equals("-l")) {
                    orderService.placeLimitOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[4],
                            commandParts[6]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Limit order placed successfully").reset());

                // limit order without tp
                } else if (commandParts.length == 6 && commandParts[4].equals("-l")){
                    orderService.placeLimitOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[5]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Limit order placed successfully").reset());

                // stop-market order
                } else if (commandParts.length == 7 && commandParts[5].equals("-t")) {
                    orderService.placeStopMarketOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[4],
                            commandParts[6]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Stop-market order placed successfully").reset());

                // stop-market order without tp
                } else if (commandParts.length == 6 && commandParts[4].equals("-t")){
                    orderService.placeStopMarketOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[5]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Stop-market order placed successfully").reset());

                // stop-limit order
                } else if (commandParts.length == 9 && commandParts[5].equals("-l") && commandParts[7].equals("-t")) {
                    orderService.placeStopLimitOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[4],
                            commandParts[6],
                            commandParts[8]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Stop-limit order placed successfully").reset());

                // stop-limit order without tp
                } else if (commandParts.length == 8 && commandParts[4].equals("-l") && commandParts[6].equals("-t")){
                    orderService.placeStopLimitOrder(
                            commandParts[1],
                            commandParts[2],
                            commandParts[3],
                            commandParts[5],
                            commandParts[7]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Stop-limit order placed successfully").reset());

                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
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
                    System.out.println(ansi().fgBrightGreen().a("  Positions closed successfully").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // cancel orders
            case "!c": // !c [symbol]
                if (commandParts.length == 2) { // by symbol
                    orderService.cancelOrders(
                            commandParts[1]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Orders cancelled successfully").reset());
                } else if (commandParts.length == 1) { // all orders
                    orderService.cancelOrders(
                            "-all"
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Orders cancelled successfully").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // cancel stop-orders
            case "!cs": // !cs [symbol]
                if (commandParts.length == 2) { // by symbol
                    orderService.cancelStopOrders(
                            commandParts[1]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Stop-orders cancelled successfully").reset());
                } else if (commandParts.length == 1) { // all orders
                    orderService.cancelStopOrders(
                            "-all"
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Stop-orders cancelled successfully").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // manage stop-loss
            case "!sl": // !sl [symbol] [price]
                if (commandParts.length == 3) {
                    orderService.manageStopLoss(
                            commandParts[1],
                            commandParts[2]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Stop-loss updated successfully").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // manage take-profit
            case "!tp": // !tp [symbol] [price]
                if (commandParts.length == 3) {
                    orderService.manageTakeProfit(
                            commandParts[1],
                            commandParts[2]
                    );
                    System.out.println(ansi().fgBrightGreen().a("  Take-profit updated successfully").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
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
                    System.out.println(ansi().fgBrightGreen().a("  Leverage set successfully").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // get position info
            case "!gp": // !gp [symbol]
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
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // get open orders
            case "!go": // !go [symbol]
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
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // test API request
            case "!ping":
                if (commandParts.length == 1) {
                    orderService.testRequest();
                    System.out.println(ansi().fgBrightGreen()
                            .a("  Connection OK, API credentials are valid").reset());
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // get instructions link
            case "!help":
                if (commandParts.length == 1) {
                    System.out.println("  Usage guide and examples: https://github.com/ivangka/cli-order-executor");
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // stop the program
            case "!exit":
                if (commandParts.length == 1) {
                    System.exit(0);
                } else {
                    throw new InvalidCommandException("Incorrect command format, try again");
                }
                break;

            // invalid command
            default:
                throw new InvalidCommandException("Unknown command, try again");
        }
    }

}
