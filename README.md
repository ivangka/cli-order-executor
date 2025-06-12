# CLI Order Executor

A console application that places market perpetual futures orders on Bybit. It enables users to open positions with precise position sizing based on a specified risk amount, while accounting for trading fees.

## Usage

The program supports the following commands:

    !o     open market order
    !ex    exit program

---

### Open Market Order `!o`

```
!o [symbol] [stoploss] [takeprofit] [risk]
```

Opens a market perpetual futures order with the specified parameters:

- `symbol` — trading pair
- `stoploss` — stop loss price
- `takeprofit` — take profit price
- `risk` — amount in USD to risk on the trade

**Example:**

```
!o ethusdt 2550.14 2610.1 50
```

This command opens a position on `ETHUSDT` with a stop loss at 2550.14, take profit at 2610.1, risking $50.

---

### Exit Program `!ex`

Stops the program.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.