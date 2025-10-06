A console tool for managing perpetual futures orders on Bybit via API.

**Usage Notes:**

- For all functions of the program to work correctly, set **One-Way Mode** in your Bybit account.
- The program doesn't use the **Post-Only** option. If you place a limit order at a price worse than the current market price, it will be executed immediately as a market order.
- Both **USDT** and **USDC** perpetual contracts are supported.
- When trading USDT‑based pairs, you can omit the “USDT” suffix (`BTC` → `BTCUSDT`).
