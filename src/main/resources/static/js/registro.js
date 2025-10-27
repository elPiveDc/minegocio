// /src/main/resources/static/js/registro.js
document.addEventListener("DOMContentLoaded", function () {
  const carouselElement = document.getElementById("registroCarousel");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  const finishBtn = document.getElementById("finishBtn");
  const progressBar = document.getElementById("progressBar");

  const carousel = bootstrap.Carousel.getOrCreateInstance(carouselElement, {
    interval: false,
    wrap: false,
  });

  const items = carouselElement.querySelectorAll(".carousel-item");

  function updateUI(index) {
    const isFirst = index === 0;
    const isLast = index === items.length - 1;

    prevBtn.classList.toggle("d-none", isFirst);
    nextBtn.classList.toggle("d-none", isLast);
    finishBtn.classList.toggle("d-none", !isLast);

    const percent = ((index + 1) / items.length) * 100;
    progressBar.style.width = `${percent}%`;
    progressBar.setAttribute("aria-valuenow", String(Math.round(percent)));
  }

  // Estado inicial
  const initialIndex =
    Array.from(items).findIndex((it) => it.classList.contains("active")) || 0;
  updateUI(initialIndex);

  carouselElement.addEventListener("slid.bs.carousel", (e) => {
    const idx =
      typeof e.to === "number"
        ? e.to
        : Array.from(items).findIndex((it) => it.classList.contains("active"));
    updateUI(idx);
  });

  // --- VALIDACIONES ---
  function validarCorreo(correo) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(correo);
  }

  function validarPassword(pass) {
    return pass.length >= 8;
  }

  function mostrarError(input, mensaje) {
    input.classList.add("is-invalid");
    const feedback = input.parentElement.querySelector(".invalid-feedback");
    if (feedback) {
      feedback.textContent = mensaje;
    }
  }

  function limpiarError(input) {
    input.classList.remove("is-invalid");
  }

  nextBtn.addEventListener("click", function () {
    const activeItem = carouselElement.querySelector(".carousel-item.active");
    const index = Array.from(items).indexOf(activeItem);

    let valido = true;

    // Paso 1: correo y contraseña
    if (index === 0) {
      const correoInput = document.querySelector("input[name='correo']");
      const passInput = document.querySelector("input[name='password']");

      if (!validarCorreo(correoInput.value.trim())) {
        mostrarError(correoInput, "Por favor, ingresa un correo válido.");
        valido = false;
      } else {
        limpiarError(correoInput);
      }

      if (!validarPassword(passInput.value.trim())) {
        mostrarError(
          passInput,
          "La contraseña debe tener al menos 8 caracteres."
        );
        valido = false;
      } else {
        limpiarError(passInput);
      }
    }

    // Paso 2: nombre de la franquicia
    if (index === 1) {
      const franquiciaInput = document.querySelector(
        "input[name='nombreFranquicia']"
      );
      if (franquiciaInput.value.trim() === "") {
        mostrarError(
          franquiciaInput,
          "El nombre de la franquicia es obligatorio."
        );
        valido = false;
      } else {
        limpiarError(franquiciaInput);
      }
    }

    // Paso 3: nombre de la base de datos
    if (index === 2) {
      const bdInput = document.querySelector("input[name='nombreBd']");
      if (bdInput.value.trim() === "") {
        mostrarError(bdInput, "El nombre de la base de datos es obligatorio.");
        valido = false;
      } else {
        limpiarError(bdInput);
      }
    }


    if (valido) {
      carousel.next(); // avanzar solo si pasa las validaciones
    }
  });
});
