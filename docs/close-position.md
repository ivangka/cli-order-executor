```
!x [symbol] [percent]
```

Closes an open position for the given trading pair or for all pairs.

---

### Parameters

- `symbol` — trading pair
- `percent` — percentage of position to close

---

### All variations

```
!x [symbol]               — closes the whole position for the symbol
!x [symbol] [percent]     — closes a percentage of the position for the symbol
!x                        — closes all positions across all pairs
```

---

### Examples

#### Full close

```
!x solusdt
```

This command closes 100% of the current `SOLUSDT` position.

#### Partial close

```
!x ethusdt 50
```

This command closes 50% of the current `ETHUSDT` position.
