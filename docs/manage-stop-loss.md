```
!sl [symbol]* [price]* [percent]
```

*\* — required parameter*

Updates, sets, or removes a stop-loss order for an existing position.

---

### Parameters

- `symbol*` — trading pair
- `price*` — stop-loss price ("0" to remove a full position SL order)
- `percent` — percentage of the existing position (for a partial position SL order)

---

### All variations

```
!sl [symbol] [price]               — set or move a full position SL order
!sl [symbol] 0                     — remove a full position SL order
!sl [symbol] [price] [percent]     — set a partial position SL order
```

---

### Examples

#### Full position SL order

```
!sl polusdt 0.1641
```

Sets or moves a full position SL order for `POLUSDT` at 0.1641.

#### Remove full position SL order

```
!sl ethusdt 0
```

Removes a full position SL order for the current `ETHUSDT` position.

#### Partial position SL order

```
!sl btcusdt 101000 40
```

Sets a partial position SL order for `BTCUSDT` at 101000 that will close 40% of the position.
