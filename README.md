# CLI Order Executor

A console application that places market perpetual futures orders on Bybit. The program opens positions with precise position sizing based on a specified risk amount, while accounting for trading fees.

## Usage

The program supports the following commands:

    !o     open order
    !ex    exit program

### Open Order `!o`

```
!o [symbol] [stoploss] [takeprofit] [risk] -l [price]
```

Opens a perpetual futures order with the specified parameters:

- `symbol` — trading pair
- `stoploss` — stop loss price
- `takeprofit` — take profit price
- `risk` — amount in USD to risk on the trade
- `-l [price]` (optional) - limit order price

**Example 1:**

```
!o ethusdt 2550.14 2610.1 50
```

This command opens a market order on `ETHUSDT` with a stop loss at 2550.14, take profit at 2610.1, risking $50.

**Example 2:**

```
!o suiusdt 2.44 3.12 200 -l 2.7
```

This command opens a limit order on `SUIUSDT` with a price 2.7, stop loss at 2.44, take profit at 3.12, risking $200.

### Exit Program `!ex`

Stops the program.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.