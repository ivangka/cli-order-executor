# CLI Order Executor

A console application for managing perpetual futures orders on Bybit.

* [Commands](#commands)
    * [Open order](#open-order-o)
    * [Set leverage](#set-leverage-lev)
    * [Set leverage to maximum](#set-leverage-to-maximum-lev_max)
    * [Exit program](#exit-program-ex)
* [Error 10002](#error-10002)
* [License](#license)

## Commands

    !o          open order
    !lev        set leverage
    !lev_max    set leverage to maximum
    !ex         exit program

### Open Order `!o`

```
!o [symbol] [stopLoss] [takeProfit] [risk] -l [price]
```

Opens a perpetual futures order with exact position sizing based on a specified risk amount, while factoring in trading fees. The leverage used will be the default setting for the given trading pair (symbol).

Parameters:

- `symbol` — trading pair
- `stopLoss` — stop loss price
- `takeProfit` — take profit price (optional)
- `risk` — amount in USD to risk on the trade
- `-l [price]` — limit order price (optional)

All variations:

```
Market:
!o [symbol] [sl] [tp] [risk]
!o [symbol] [sl] [risk]

Limit:
!o [symbol] [sl] [tp] [risk] -l [price]
!o [symbol] [sl] [risk] -l [price]
```

**Example 1 (market order):**

```
!o ethusdt 2610.1 2550.14 50
```

This command opens a market order (Short) on `ETHUSDT` with a stop loss at 2610.1, take profit at 2550.14, risking $50.

**Example 2 (limit order):**

```
!o suiusdt 2.44 3.12 200 -l 2.7
```

This command places a limit order (Long) on `SUIUSDT` with a price 2.7, stop loss at 2.44, take profit at 3.12, risking $200.

### Set Leverage `!lev`

```
!lev [symbol] [leverage]
```

Sets the specified leverage for the trading pair.  This applies to both existing positions and any new orders for this symbol.

Parameters:

- `symbol` — trading pair
- `leverage` — leverage size

**Example:**

```
!lev ethusdt 5.5
```

This command sets the leverage to 5.5 for the all `ETHUSDT` orders.

### Set Leverage to Maximum `!lev_max`

```
!lev_max [symbol]
```

Sets the leverage to maximum for the specified trading pair. This applies to both existing positions and any new orders for this symbol.

Parameters:

- `symbol` — trading pair

**Example:**

```
!lev_max btcusdt
```

This command sets the leverage to maximum for the all `BTCUSDT` orders.

### Exit Program `!ex`

Stops the program.

## Error 10002

If you get "Error Code 10002", it means your PC's clock is out of sync with the server's time.   
To fix this, you just need to synchronize the time in your PC settings.

[See the full list of error codes.](https://bybit-exchange.github.io/docs/v5/error)

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.