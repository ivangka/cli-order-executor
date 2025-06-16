# CLI Order Executor

A console application for managing perpetual futures orders on Bybit.

* [Commands](#commands)
    * [Open order](#open-order-o)
    * [Close position](#close-position-x)
    * [Cancel limit orders](#cancel-limit-orders-c)
    * [Set leverage](#set-leverage-lev)
    * [Exit program](#exit-program-ex)
* [Error 10002](#error-10002)
* [License](#license)

## Commands

    !o           open order
    !x           close position
    !c           cancel limit orders
    !lev         set leverage
    !ex          exit program

### Open order `!o`

```
!o [symbol] [stopLoss] [takeProfit] [risk] -l [price]
```

Opens a perpetual futures order with exact order sizing based on a specified risk amount, while factoring in trading fees. The leverage used will be the default setting for the given trading pair (symbol).

> [!IMPORTANT]
> 
> Slippage is not considered in the order size calculation.

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

### Close position `!x`

```
!x [symbol] [percent]
```

Closes a specified percentage of an open position for the given trading pair. If no percentage is specified, closes the entire order (100%).

Parameters:

- `symbol` — trading pair
- `percent` — percentage of position to close (optional)

Constraints:

- `0 < percent <= 100`

All variations:

```
!x [symbol] [percent]
!x [symbol]
```

**Example 1 (partial close):**

!x ethusdt 50

This command closes 50% of the current `ETHUSDT` position.

**Example 2 (full close):**

!x solusdt

This command closes 100% of the current `SOLUSDT` position.

### Cancel limit orders `!c`

```
!c [symbol]
```

Cancels all active limit orders for the specified trading pair.

Parameters:

- `symbol` — trading pair

**Example:**

```angular2html
!c trumpusdt
```

Cancels all active limit orders for `TRUMPUSDT`.

### Set leverage `!lev`

```
!lev [symbol] [leverage]
```

Sets the specified leverage for the trading pair. If no leverage is provided, the maximum allowed leverage will be set. This applies to both existing positions and any new orders for this symbol.

Parameters:

- `symbol` — trading pair
- `leverage` — leverage size (optional)

All variations

```
!lev [symbol] [leverage]
!lev [symbol]
```

**Example 1:**

```
!lev ethusdt 5.5
```

This command sets the leverage to 5.5 for the all `ETHUSDT` positions and new orders.

**Example 2:**

```
!lev btcusdt
```

This command sets the leverage to maximum for the all `BTCUSDT` positions and new orders.

### Set leverage to maximum `!lev_max`

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

This command sets the leverage to maximum for the all `BTCUSDT` positions and new orders.

### Exit program `!ex`

Stops the program.

## Error 10002

If you get "Error Code 10002", it means your PC's clock is out of sync with the server's time.   
To fix this, you just need to synchronize the time in your PC settings.

*[See the full list of error codes.](https://bybit-exchange.github.io/docs/v5/error)*

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.