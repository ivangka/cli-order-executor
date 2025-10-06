```
!tp [symbol]* [price]* [percent]
```

*\* — required parameter*

Updates, sets, or removes a take-profit order for an existing position.

---

### Parameters

- `symbol*` — trading pair
- `price*` — take-profit price ("0" to remove a full position TP order)
- `percent` — percentage of the existing position (for a partial position TP order)

---

### All variations

```
!tp [symbol] [price]               — set or move a full position TP order
!tp [symbol] 0                     — remove a full position TP order
!tp [symbol] [price] [percent]     — set a partial position TP order
```

---

### Examples

#### Full position TP order

```
!tp btcusdt 98000
```

Sets or moves a full position TP order for `BTCUSDT` at 98000.

#### Remove full position TP order

```
!tp bnbusdt 0
```

Removes a full position TP order for the current `BNBUSDT` position.

#### Partial position TP order

```
!tp ethusdt 3500 50
```

Sets a partial position TP order for `ETHUSDT` at 3500 that will close 50% of the position.
