# CLI Order Executor

A console tool for managing perpetual futures orders on Bybit via API.

> **Usage Notes**
> 
> - For all functions of the program to work correctly, set **One-Way Mode** in your Bybit account.
> - The program doesn't use the **Post-Only** option. If you place a limit order at a price worse than the current market price, it may be executed immediately as a market order.
> - Both **USDT** and **USDC** perpetual contracts are supported.
> - When trading USDT‑based pairs, you can omit the “USDT” suffix.  
>   *For example, entering `BTC` will automatically be interpreted as `BTCUSDT`.*

**Table of contents:**

* [Disclaimer](#disclaimer)
* [Risk Management](#risk-management)
* [Commands](#commands)
  * [Open order](#open-order-o)
  * [Open order by quantity](#open-order-by-quantity-o)
  * [Close position](#close-position-x)
  * [Cancel limit orders](#cancel-limit-orders-c)
  * [Manage stop-loss](#manage-stop-loss-sl)
  * [Manage take-profit](#manage-take-profit-tp)
  * [Set leverage](#set-leverage-lev)
  * [Get position info](#get-position-info-gp)
  * [Get limit orders](#get-limit-orders-gl)
  * [Send test API request](#send-test-api-request-ping)
  * [Get instructions link](#get-instructions-link-help)
  * [Exit program](#exit-program-exit)
* [How to Use?](#how-to-use)
* [Error 10002](#error-10002)
* [License](#license)

## Disclaimer

This software is provided "as is", without any warranties. Use it at your own risk. The author is not responsible for any losses resulting from the use of this software, including but not limited to monetary losses, trading logic errors, API failures, violations of exchange terms of service (e.g., account bans or suspensions), or any other unforeseen consequences.

This software is not intended to provide financial or investment advice. Any trading or investment decisions you make using this software are your own responsibility. Consult a qualified financial advisor if needed.

Make sure you fully understand how this software works and how futures markets operate before using it. It is strongly recommended to test it in a safe environment (such as a sandbox account or with demo funds).

## Risk Management

This program uses an approach that requires setting a stop-loss for each trade and automatically calculates the total position size based on:

1. Your specified risk amount (in USD)
2. The distance between entry and stop-loss prices
3. Bybit's trading fees

With this approach, **leverage does not affect your risk** — your risk is fixed by the stop-loss distance and position size calculation. Leverage only affects the number of concurrent positions — the higher the leverage, the more positions you can open within your available margin.

## Commands

    !o          open order
    !x          close position
    !c          cancel limit orders

    !sl         manage stop-loss
    !tp         manage take-profit

    !lev        set leverage

    !gp         get position info
    !gl         get limit orders

    !ping       send test API request
    !help       get instructions link
    !exit       exit program

### Open order `!o`

```
!o [symbol] [sl] [tp] [risk] -l [price]
```

Opens a perpetual futures order with exact order sizing based on a specified risk amount, while factoring in trading fees. The leverage used will be the default setting for the given trading pair (symbol).

> [!IMPORTANT]
> 
> Slippage is not considered in the order size calculation.

Parameters:

- `symbol` — trading pair
- `sl` — stop-loss price
- `tp` — take-profit price (optional)
- `risk` — amount in USD to risk on the trade
- `-l [price]` — limit order price (optional)

All variations:

```
Market:
!o [symbol] [sl] [tp] [risk]                — market order with stop-loss and take-profit
!o [symbol] [sl] [risk]                     — market order with with stop-loss only

Limit:
!o [symbol] [sl] [tp] [risk] -l [price]     — limit order with stop-loss and take-profit
!o [symbol] [sl] [risk] -l [price]          — limit order with stop-loss only
```

**Example 1 (market order):**

```
!o ethusdt 2610.1 2550.14 50
```

This command opens a market sell order on `ETHUSDT` with a stop-loss at 2610.1, take-profit at 2550.14, risking $50.

**Example 2 (limit order):**

```
!o suiusdt 2.44 3.12 200 -l 2.7
```

This command places a limit buy order on `SUIUSDT` with a price 2.7, stop-loss at 2.44, take-profit at 3.12, risking $200.

### Open order by quantity `!o`

```
!o [symbol] -buy/sell [qty] -l [price]
```

This format of the command opens a perpetual futures order based on a specific quantity of contracts rather than risk-based position sizing. This is useful when you want to directly specify the amount of contracts to buy or sell without calculating risk — for example, to set partial or full take-profit limit orders, or to manually close part of a position at market price.

Parameters:

- `symbol` — trading pair
- `-buy/sell [qty]` — side and quantity of contracts
- `-l [price]` — limit order price (optional)

All variations:

```
Market:
!o [symbol] -buy/sell [qty]                — market order
!o [symbol] -buy/sell [qty] -l [price]     — limit order
```

**Example 1 (market order):**

```
!o btc -buy 0.4
```

This command opens a market buy order for 0.4 BTC on `BTCUSDT`.

**Example 2 (limit order):**

```
!o ada -sell 200 -l 0.8121
```

This command places a limit sell order for 200 ADA on `ADAUSDT` with a price 0.8121.

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

### Cancel limit orders `!c`

```
!c [symbol]
```

Cancels all limit orders for specified trading pair or for all pairs.

Parameters:

- `symbol` — trading pair (optional)

```
!c [symbol]     — cancels all limit orders for the symbol
!c              — cancels all limit orders across all pairs
```

**Example:**

```angular2html
!c trumpusdt
```

This command cancels all limit orders for `TRUMPUSDT`.

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

### Get position info `!gp`

```
!gp [symbol]
```

Displays information about the current positions.

Parameters:

- `symbol` — trading pair (optional)

All variations

```
!gp [symbol]     — shows position info for the symbol
!gp              — shows all positions
```

**Example:**

```
!gp sandusdt
```

This command displays position details for `SANDUSDT`.

### Get limit orders `!gl`

```
!gl [symbol]
```

Displays information about active limit orders.

Parameters:

- `symbol` — trading pair (optional)

All variations

```
!gl [symbol]     — shows limit orders for the symbol
!gl              — shows all limit orders
```

**Example:**

```
!gl btcusdt
```

This command displays limit orders for `BTCUSDT`.

### Send test API request `!ping`

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