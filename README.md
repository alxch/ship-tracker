# Ship Tracker

### 1. Initialize Ports
This endpoint initializes the database with predefined ports.

```bash
curl -X POST http://localhost:7070/ports/init
```

### 2. Create a New Boat
This endpoint creates a new boat with a specified `boatId`, `startPort`, and `endPort`.

```bash
curl -X POST http://localhost:7070/boats \
  -H "Content-Type: application/json" \
  -d '{
    "boatId": "BOAT006",
    "startPort": "New York",
    "endPort": "Lisbon"
  }'
```

### 3. Get All Boats
Retrieve the details of all boats currently stored in the database.

```bash
curl -X GET http://localhost:7070/boats
```

### 4. Get Boat by ID
Retrieve the details of a specific boat by its `boatId`.

```bash
curl -X GET http://localhost:7070/boats/BOAT006
```

### 5. Start Simulation
Run the simulation to update each boat's position toward its destination.

```bash
curl -X POST http://localhost:7070/simulate
```
