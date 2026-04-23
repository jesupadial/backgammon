# Plan de desarrollo — Backgammon

## Stack técnico
- **Backend**: Spring Boot 3.5 / Java 17 / Lombok / Maven
- **Frontend**: Angular 21 / TypeScript 5.9 / standalone components / Vitest
- **Contrato API**: `shared/openapi.yaml` (API-first)

---

## Principios de diseño aplicados
- Dominio inmutable (`@Value` + `@With` de Lombok) → el motor usa funciones puras
- Separación estricta: modelo → reglas → motor → API → frontend
- Sin estado mutable en el dominio; cada movimiento produce un `GameState` nuevo

---

## Flujo de juego implementado

```
ROLLING_FOR_FIRST_TURN
  └─ cada jugador tira 1 dado
  └─ el mayor empieza usando esos dos dados
       ↓
WAITING_FOR_ROLL
  └─ jugador activo tira 2 dados (o doblete → 4 movimientos)
       ↓
WAITING_FOR_MOVE
  └─ jugador mueve fichas según dados disponibles
       ↓
  (todos los dados usados o sin movimientos válidos)
       ├─ WAITING_FOR_ROLL  (siguiente turno)
       └─ GAME_OVER         (15 fichas escapadas)
```

---

## Lo que ya está implementado

### `backend/src/main/java/com/backgammon/game/model/`

| Clase | Tipo | Descripción |
|-------|------|-------------|
| `Player` | enum | WHITE / BLACK con `opponent()` |
| `GamePhase` | enum | ROLLING_FOR_FIRST_TURN / WAITING_FOR_ROLL / WAITING_FOR_MOVE / GAME_OVER |
| `Point` | `@Value` | Casilla del tablero (índice 0-23), con `isEmpty`, `isBlot`, `isOwnedBy`, `isOpenFor` |
| `Bar` | `@Value` | Fichas en la barra, consulta por jugador |
| `BorneOff` | `@Value` | Fichas escapadas, consulta por jugador |
| `Board` | `@Value` | 24 puntos + barra + escapadas; `Board.initial()` con posición estándar |
| `GameState` | `@Value @With` | Estado completo; `GameState.newGame(id)` arranca con `currentPlayer=null` y fase `ROLLING_FOR_FIRST_TURN` |

**Posición inicial del tablero** (índices 0-23, WHITE mueve de alto a bajo):
- WHITE: índice 23 (×2), 12 (×5), 7 (×3), 5 (×5)
- BLACK: índice 0 (×2), 11 (×5), 16 (×3), 18 (×5)

---

## Próximos pasos en orden

### 1. `game/rules/` — Reglas del juego
- `DiceRoller` — genera tiradas aleatorias (1 dado o 2 dados); los dobles producen 4 movimientos
- `FirstTurnResolver` — compara dados iniciales y determina quién empieza (repetir si empate)
- `MoveValidator` — valida si un movimiento (`from` → `to`) es legal para el jugador y dados actuales
- `BearOffValidator` — reglas especiales de escape (todas las fichas deben estar en el home board)
- `WinConditionChecker` — detecta si un jugador tiene 15 fichas escapadas

### 2. `game/engine/` — Motor del juego
- `GameEngine` — orquesta transiciones de estado usando las reglas:
  - `rollForFirstTurn(GameState, Player, int die)` → `GameState`
  - `rollDice(GameState)` → `GameState`
  - `applyMove(GameState, Move)` → `GameState`
- `Move` — record/value con `from` (0-23 ó 24=barra) y `to` (0-23 ó 25=escapar)

### 3. `session/` — Gestión de partidas
- `GameRepository` — almacena partidas en memoria (`Map<String, GameState>`)
- `GameService` — fachada que conecta motor + repositorio; genera IDs de partida (UUID)

### 4. `api/` — Capa HTTP
Implementar los 4 endpoints del contrato `shared/openapi.yaml`:
- `POST /api/games` → crea partida nueva
- `GET /api/games/{gameId}` → devuelve estado actual
- `POST /api/games/{gameId}/roll` → tira dados
- `POST /api/games/{gameId}/moves` → aplica movimiento

Subcarpetas: `controller/`, `dto/`, `mapper/`

### 5. `frontend/` — Interfaz Angular
- `core/models/` — interfaces TypeScript espejando los DTOs del OpenAPI
- `core/services/GameService` — cliente HTTP hacia la API
- `game/board/` — componente tablero (24 puntos, barra, área de escape)
- `game/dice/` — componente dados
- `game/point/` — componente casilla con fichas

---

## Decisiones pendientes
- **IA**: la carpeta `ai/` está vacía; no priorizar hasta tener el juego funcional entre humanos
- **Persistencia**: empezar con repositorio en memoria; añadir base de datos si se necesita multisesión
- **WebSocket vs polling**: la API actual es REST puro; valorar WebSocket si se quiere tiempo real
