document.addEventListener("DOMContentLoaded", function () {
  const modalEditar = document.getElementById("modalEditar");

  if (modalEditar) {
    modalEditar.addEventListener("show.bs.modal", function (event) {
      const button = event.relatedTarget;
      const id = button.getAttribute("data-id");
      const nombre = button.getAttribute("data-nombre");
      const correo = button.getAttribute("data-correo");

      document.getElementById("editarId").value = id;
      document.getElementById("editarNombre").value = nombre;
      document.getElementById("editarCorreo").value = correo;

      // Actualizamos la action del form dinámicamente
      document.getElementById("formEditarUsuario").action =
        "/dashboard/usuarios/" + id + "/editar";
    });
  }
});
