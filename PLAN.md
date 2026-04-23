# Plan de desarrollo — Backgammon

## Stack técnico
- **Backend**: Spring Boot 3.5 / Java 17 / Lombok / Maven
- **Frontend**: Angular 21 / TypeScript 5.9 / standalone components / signals
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
  └─ cada jugador tira 1 dado (POST /first-roll)
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

### `backend/` — Completo y funcional

| Capa | Clases |
|------|--------|
| `model/` | `Player`, `GamePhase`, `Point`, `Bar`, `BorneOff`, `Board`, `GameState`, `Move` |
| `rules/` | `DiceRoller`, `FirstTurnResolver`, `MoveValidator`, `BearOffValidator`, `WinConditionChecker` |
| `engine/` | `GameEngine`, `BoardUpdater` |
| `session/` | `GameRepository`, `GameService` |
| `api/` | `GameController`, `GameExceptionHandler`, `GameMapper`, DTOs |

**Endpoints disponibles:**
- `POST /api/games` → crea partida
- `GET /api/games/{gameId}` → estado actual
- `POST /api/games/{gameId}/first-roll` → tirada del primer turno
- `POST /api/games/{gameId}/roll` → tirada de dados
- `POST /api/games/{gameId}/moves` → aplica movimiento

### `frontend/` — Funcional, pendiente de rediseño

| Capa | Archivos |
|------|----------|
| `core/models/` | `game.models.ts` |
| `core/services/` | `game.service.ts` |
| `game/board/` | `BoardComponent` |
| `game/point/` | `PointComponent` |
| `game/dice/` | `DiceComponent` |
| `game/game-page/` | `GamePageComponent` |

---

## Próximos pasos en orden

### 1. Rediseño del frontend
El frontend actual es funcional pero básico. Objetivos del rediseño:
- Tablero con triángulos reales (puntos alternando colores rojo/blanco sobre marrón)
- Numeración de casillas visible
- Animación de movimiento de fichas
- Indicador visual claro del jugador activo y los dados disponibles
- Resaltar casillas de destino válidas al seleccionar una ficha
- Pantalla de inicio y pantalla de fin de partida
- Diseño responsive

### 2. Tests del backend
El código está diseñado para ser testeable. Prioridad:
- Tests unitarios de `MoveValidator` y `BearOffValidator` (casos límite)
- Tests unitarios de `GameEngine` (transiciones de estado)
- Tests de integración de los endpoints HTTP

### 3. IA
- La carpeta `ai/` está vacía
- No priorizar hasta tener el juego pulido entre humanos

---

## Decisiones pendientes
- **Persistencia**: actualmente en memoria; añadir base de datos si se necesita multisesión
- **WebSocket vs polling**: la API es REST puro; valorar WebSocket para tiempo real
