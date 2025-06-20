# CLI Order Executor

A console tool for managing perpetual futures orders on Bybit via API.

> **Usage Notes**
> 
> - For all functions of the program to work correctly, set **One-Way Mode** in your Bybit account.
> - Both **USDT** and **USDC** perpetual contracts are supported.
> - When trading USDT‑based pairs, you can omit the “USDT” suffix.  
>   *For example, entering `BTC` will automatically be interpreted as `BTCUSDT`.*

**Table of contents:**

* [Risk Management](#risk-management)
* [Commands](#commands)
  * [Open order](#open-order-o)
  * [Close position](#close-position-x)
  * [Cancel orders](#cancel-orders-c)
  * [Manage stop-loss](#manage-stop-loss-sl)
  * [Manage take-profit](#manage-take-profit-tp)
  * [Set leverage](#set-leverage-lev)
  * [Get position info](#get-position-info-gpi)
  * [Get open orders](#get-open-orders-goo)
  * [Test API connection](#test-api-connection-check)
  * [Get instructions link](#get-instructions-link-help)
  * [Exit program](#exit-program-exit)
* [How to Use?](#how-to-use)
* [Error 10002](#error-10002)
* [License](#license)

## Risk Management

This program uses an approach that requires setting a stop-loss for each trade and automatically calculates the total position size based on:

1. Your specified risk amount (in USD)
2. The distance between entry and stop-loss prices
3. Bybit's trading fees

With this approach, **leverage does not affect your risk** — your risk is fixed by the stop-loss distance and position size calculation. Leverage only affects the number of concurrent positions — the higher the leverage, the more positions you can open within your available margin.

## Commands

    !o          open order
    !x          close position
    !c          cancel orders

    !sl         manage stop-loss
    !tp         manage take-profit

    !lev        set leverage

    !gpi        get position info
    !goo        get open orders

    !check      test API connection
    !help       get instructions link
    !exit       exit program

### Open order `!o`

```
!o [symbol] [stopLoss] [takeProfit] [risk] -l [price]
```

Opens a perpetual futures order with exact order sizing based on a specified risk amount, while factoring in trading fees. The leverage used will be the default setting for the given trading pair (symbol).

> [!IMPORTANT]
> 
> Slippage is not considered in the order size calculation.

> **Limit Orders**
> 
> If you place a limit long order with a price higher than the current market price, it will be executed immediately as a market order. Similarly, if you place a limit short order with a price lower than the current market price, it will also be filled immediately as a market order.

Parameters:

- `symbol` — trading pair
- `stopLoss` — stop loss price
- `takeProfit` — take profit price (optional)
- `risk` — amount in USD to risk on the trade
- `-l [price]` — limit order price (optional)

All variations:

```
Market:
!o [symbol] [sl] [tp] [risk]                — market order with take-profit and stop-loss
!o [symbol] [sl] [risk]                     — market order with stop-loss only

Limit:
!o [symbol] [sl] [tp] [risk] -l [price]     — limit order with take-profit and stop-loss
!o [symbol] [sl] [risk] -l [price]          — limit order with stop-loss only
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

Closes an open position for the given trading pair or for all pairs.

Parameters:

- `symbol` — trading pair (optional)
- `percent` — percentage of position to close (optional)

All variations:

```
!x [symbol]               — closes the whole position for the symbol
!x [symbol] [percent]     — closes a percentage of the position for the symbol
!x                        — closes all positions across all pairs
```

**Example 1 (full close):**

```
!x solusdt
```

This command closes 100% of the current `SOLUSDT` position.

**Example 2 (partial close):**

```
!x ethusdt 50
```

This command closes 50% of the current `ETHUSDT` position.

### Cancel orders `!c`

```
!c [symbol]
```

Cancels all active orders (not positions) for specified trading pair or for all pairs.

Parameters:

- `symbol` — trading pair (optional)

```
!c [symbol]     — cancels all orders for the symbol
!c              — cancels all orders across all pairs
```

**Example:**

```angular2html
!c trumpusdt
```

This command cancels all active orders for `TRUMPUSDT`.

### Manage stop-loss `!sl`

```
!sl [symbol] [price]
```

Updates, sets, or removes the stop-loss order for an existing position.

Parameters:

- symbol — trading pair
- price — stop-loss price ("0" to remove the stop-loss)

**Example 1:**

```
!sl polusdt 0.1641
```

Moves the stop-loss for `POLUSDT` to 0.1641. If no stop-loss was set before, this sets a new one.

**Example 2:**

```
!sl ethusdt 0
```

Removes the stop-loss for the current `ETHUSDT` position.

### Manage take-profit `!tp`

```
!tp [symbol] [price]
```

Updates, sets, or removes the take-profit order for an existing position.

Parameters:

- symbol — trading pair
- price — take-profit price ("0" to remove the take-profit)

**Example 1:**

```
!tp btcusdt 98000
```

Moves the take-profit for `BTCUSDT` to 98000. If no take-profit was set before, this sets a new one.

**Example 2:**

```
!tp bnbusdt 0
```

Removes the take-profit for the current `BNBUSDT` position.

### Set leverage `!lev`

```
!lev [symbol] [leverage]
```

Sets leverage for the trading pair. This applies to both existing positions and any new orders for this symbol.

Parameters:

- `symbol` — trading pair
- `leverage` — leverage size (optional)

All variations

```
!lev [symbol] [leverage]     — sets specified leverage for the symbol
!lev [symbol]                — sets maximum leverage for the symbol
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

### Get position info `!gpi`

```
!gpi [symbol]
```

Displays information about the current positions.

Parameters:

- `symbol` — trading pair (optional)

All variations

```
!gpi [symbol]     — shows position info for the symbol
!gpi              — shows all positions
```

**Example:**

```
!gpi sandusdt
```

This command displays position details for `SANDUSDT`.

### Get open orders `!goo`

```
!goo [symbol]
```

Displays open limit orders info.

Parameters:

- `symbol` — trading pair (optional)

All variations

```
!goo [symbol]     — shows open orders for the symbol
!goo              — shows all open limit orders
```

**Example:**

```
!goo btcusdt
```

This command displays open orders for `BTCUSDT`.

### Test API connection `!check`

Sends a test request to the Bybit API to verify that the connection and credentials are working.

### Get instructions link `!help`

Displays a link to the usage guide and examples.

### Exit program `!exit`

Stops the program.

## How to Use?

Follow these steps to get started.

1. Install `Java` and `Maven`.
2. Download or clone this repository:

```
git clone https://github.com/ivangka/cli-order-executor.git
```

3. Create a Bybit API Key.
4. Configure API credentials in `application.properties`:

```properties
# bybit API
api.bybit.key=<YOUR_BYBIT_API_KEY>
api.bybit.secret=<YOUR_BYBIT_API_SECRET>
```

5. Compile the application:

```
mvn clean package -DskipTests
```

6. Run the application:

```
java -jar "<PATH>/target/cli-order-executor-0.0.1-SNAPSHOT.jar"
```

Here we go.

>
> You only need to complete steps 1-5 once.
> 

## Error 10002

If you get "Error Code 10002", it means your PC's clock is out of sync with the server's time.   
To fix this, you just need to synchronize the time in your PC settings.

*[See the full list of error codes.](https://bybit-exchange.github.io/docs/v5/error)*

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.