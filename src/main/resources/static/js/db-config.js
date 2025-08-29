// === Crear Tablas ===
function agregarFila() {
  const tabla = document
    .getElementById("tablaColumnas")
    .getElementsByTagName("tbody")[0];
  const nuevaFila = document.createElement("tr");
  nuevaFila.innerHTML = `
        <td><input type="text" class="form-control" placeholder="Nombre de columna"></td>
        <td>
            <select class="form-select">
                <option>INT</option>
                <option>VARCHAR(100)</option>
                <option>DATE</option>
                <option>BOOLEAN</option>
            </select>
        </td>
        <td>
            <select class="form-select">
                <option>PRIMARY KEY</option>
                <option>NOT NULL</option>
                <option>NULL</option>
                <option>UNIQUE</option>
            </select>
        </td>
    `;
  tabla.appendChild(nuevaFila);
}

function eliminarFila() {
  const tabla = document
    .getElementById("tablaColumnas")
    .getElementsByTagName("tbody")[0];
  if (tabla.rows.length > 0) {
    tabla.deleteRow(tabla.rows.length - 1);
  }
}

// === Acciones de Carga de Datos y Consultas ===
function mostrarAccion() {
  const accion = document.getElementById("accionSelect").value;
  const contenedor = document.getElementById("accionContenido");

  if (accion === "subir") {
    contenedor.innerHTML = `
            <h5>Insertar datos en la tabla seleccionada</h5>
            <table class="table table-bordered text-center align-middle" id="tablaDatos">
                <thead class="table-light">
                    <tr>
                        <th>Columna 1</th>
                        <th>Columna 2</th>
                        <th>Columna 3</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><input type="text" class="form-control" placeholder="Valor..."></td>
                        <td><input type="text" class="form-control" placeholder="Valor..."></td>
                        <td><input type="text" class="form-control" placeholder="Valor..."></td>
                    </tr>
                </tbody>
            </table>
            <div class="d-flex gap-2">
                    <button class="btn btn-secondary" onclick="agregarFilaDatos()">Agregar fila</button>
                    <button class="btn btn-danger" onclick="eliminarFilaDatos()">Eliminar fila</button>
                    <button class="btn btn-success ms-auto">Guardar datos</button>
            </div>

            <hr class="my-4">

            <h5>Cargar desde archivo</h5>
            <div class="mb-3">
                <label for="csvFile" class="form-label">Subir CSV</label>
                <input type="file" class="form-control" id="csvFile" accept=".csv">
            </div>
            <div class="mb-3">
                <label for="jsonFile" class="form-label">Subir JSON</label>
                <input type="file" class="form-control" id="jsonFile" accept=".json">
            </div>
        `;
  } else if (accion === "consultar") {
    contenedor.innerHTML = `
            <h5>Consultas estructuradas</h5>
            <div class="mb-3">
                <button class="btn btn-outline-primary" onclick="verTodos()">Ver todos los datos</button>
            </div>

            <div class="mb-3">
                <label class="form-label">Filtrar por columna:</label>
                <select id="columnaFiltro" class="form-select">
                    <option>Columna 1</option>
                    <option>Columna 2</option>
                    <option>Columna 3</option>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Valor a buscar:</label>
                <input type="text" id="valorFiltro" class="form-control" placeholder="Ej: gato">
            </div>

            <button class="btn btn-primary" onclick="filtrar()">Aplicar filtro</button>

            <hr class="my-4">

            <div id="resultadoConsulta" class="mt-3"></div>
        `;
  } else {
    contenedor.innerHTML = "";
  }
}

function agregarFilaDatos() {
  const tabla = document
    .getElementById("tablaDatos")
    .getElementsByTagName("tbody")[0];
  const nuevaFila = document.createElement("tr");
  nuevaFila.innerHTML = `
        <td><input type="text" class="form-control" placeholder="Valor..."></td>
        <td><input type="text" class="form-control" placeholder="Valor..."></td>
        <td><input type="text" class="form-control" placeholder="Valor..."></td>
    `;
  tabla.appendChild(nuevaFila);
}

function eliminarFilaDatos() {
  const tabla = document
    .getElementById("tablaDatos")
    .getElementsByTagName("tbody")[0];
  if (tabla.rows.length > 0) {
    tabla.deleteRow(tabla.rows.length - 1);
  }
}

function verTodos() {
  document.getElementById("resultadoConsulta").innerHTML = `
        <h6>Resultados:</h6>
        <table class="table table-striped">
            <thead>
                <tr><th>Columna 1</th><th>Columna 2</th><th>Columna 3</th></tr>
            </thead>
            <tbody>
                <tr><td>Juan</td><td>25</td><td>Cliente</td></tr>
                <tr><td>Ana</td><td>30</td><td>Cliente</td></tr>
            </tbody>
        </table>
    `;
}

function filtrar() {
  const columna = document.getElementById("columnaFiltro").value;
  const valor = document.getElementById("valorFiltro").value;
  document.getElementById("resultadoConsulta").innerHTML = `
        <h6>Resultados filtrados por <strong>${columna}</strong> = "${valor}"</h6>
        <table class="table table-striped">
            <thead>
                <tr><th>Columna 1</th><th>Columna 2</th><th>Columna 3</th></tr>
            </thead>
            <tbody>
                <tr><td>Ana</td><td>30</td><td>Cliente</td></tr>
            </tbody>
        </table>
    `;
}
