```
!o [symbol]* -buy/sell [qty]* -l [price]
```

*\* — required parameter*

Opens a perpetual futures order based on a specific quantity of contracts rather than risk-based position sizing.

---

### Parameters

- `symbol*` — trading pair
- `-buy/sell [qty]*` — side and quantity of contracts
- `-l [price]` — limit order price

---

### All variations

```
!o [symbol] -buy/sell [qty]                — market order
!o [symbol] -buy/sell [qty] -l [price]     — limit order
```

---

### Examples

#### Market order

```
!o btcusdt -buy 0.4
```

This command opens a market buy order for 0.4 BTC on `BTCUSDT`.

#### Limit order

```
!o adausdt -sell 200 -l 0.8121
```

This command places a limit sell order for 200 ADA on `ADAUSDT` with a price 0.8121.
