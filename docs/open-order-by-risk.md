```
!o [symbol]* [sl]* [tp] [risk]* -l [price] -t [trigger]
```

*\* — required parameter*

Opens a perpetual futures order with exact order sizing based on a specified risk amount, while factoring in trading fees. The leverage used will be the default setting for the given trading pair (symbol).

> Slippage is not considered in the order size calculation.

---

### Parameters

- `symbol*` — trading pair
- `sl*` — stop-loss price
- `tp` — take-profit price
- `risk*` — amount in USD to risk on the trade
- `-l [price]` — limit order price
- `-t [trigger]` — trigger price

---

### All variations

```
Market:
!o [symbol] [sl] [tp] [risk]                             — market order with SL and TP
!o [symbol] [sl] [risk]                                  — market order with SL only

Limit:
!o [symbol] [sl] [tp] [risk] -l [price]                  — limit order with SL and TP
!o [symbol] [sl] [risk] -l [price]                       — limit order with SL only

Conditional Market:
!o [symbol] [sl] [tp] [risk] -t [trigger]                — conditional market order with SL and TP
!o [symbol] [sl] [risk] -t [trigger]                     — conditional market order with SL only

Conditional Limit:
!o [symbol] [sl] [tp] [risk] -l [price] -t [trigger]     — conditional limit order with TP and SL
!o [symbol] [sl] [risk] -l [price] -t [trigger]          — conditional limit order with SL only
```

---

### Examples

#### Market order

```
!o ethusdt 2550 3100 50
```

This command opens a market buy order on `ETHUSDT` with a stop-loss at 2550, take-profit at 3100, risking $50.

#### Limit order

```
!o suiusdt 3.13 2.33 600 -l 2.8
```

This command places a limit sell order on `SUIUSDT` with a price 2.8, stop-loss at 3.13, take-profit at 2.33, risking $600.

#### Conditional limit order

```
!o btcusdt 108200 112550 1500 -l 109000 -t 109100
```

This command places a conditional limit order (Long) on `BTCUSDT` with trigger price at 109100, limit order price 109000, stop-loss at 108200, take-profit 112550, risking $1500.
