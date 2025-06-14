# CLI Order Executor

A console application for managing perpetual futures orders on Bybit.

## Commands

    !o     open order
    !ex    exit program

### Open Order `!o`

```
!o [symbol] [stopLoss] [takeProfit] [risk] -l [price]
```

Opens a perpetual futures order with exact position sizing based on a specified risk amount, while factoring in trading fees. The leverage used will be the default setting for the given trading pair (symbol).

Parameters:

- `symbol` — trading pair
- `stopLoss` — stop loss price
- `takeProfit` — take profit price
- `risk` — amount in USD to risk on the trade
- `-l [price]` — limit order price (optional)

**Example 1 (market order):**

```
!o ethusdt 2550.14 2610.1 50
```

This command opens a **market** order (long) on `ETHUSDT` with a stop loss at 2550.14, take profit at 2610.1, risking $50.

**Example 2 (limit order):**

```
!o suiusdt 2.44 3.12 200 -l 2.7
```

This command places a **limit** order (long) on `SUIUSDT` with a price 2.7, stop loss at 2.44, take profit at 3.12, risking $200.

**Example 3 (market order without TP):**

```
!o xrpusdt 2.1512 15
```

This command places a **market** order on `XRPUSDT` with a stop loss at 2.1512, risking $15.

### Exit Program `!ex`

Stops the program.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.