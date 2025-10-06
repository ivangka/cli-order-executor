```
!lev [symbol]* [leverage]
```

*\* — required parameter*

Sets leverage for the trading pair. This applies to both existing positions and any new orders for this symbol.

---

### Parameters

- `symbol*` — trading pair
- `leverage` — leverage size

---

### All variations

```
!lev [symbol] [leverage]     — sets specified leverage for the symbol
!lev [symbol]                — sets maximum leverage for the symbol
```

---

### Examples

#### Specified leverage

```
!lev ethusdt 5.5
```

This command sets the leverage to 5.5 for the all `ETHUSDT` positions and new orders.

#### Maximum leverage

```
!lev btcusdt
```

This command sets the leverage to maximum for the all `BTCUSDT` positions and new orders.
