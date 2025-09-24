// /src/main/resources/static/registro.js
document.addEventListener("DOMContentLoaded", function () {
  const carouselElement = document.getElementById("registroCarousel");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  const finishBtn = document.getElementById("finishBtn");
  const progressBar = document.getElementById("progressBar");

  // Asegura instancia (no inicia autoplay ni nada)
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

  // Cada vez que termine el slide, actualizo UI
  carouselElement.addEventListener("slid.bs.carousel", (e) => {
    // e.to es el índice del slide activo
    const idx =
      typeof e.to === "number"
        ? e.to
        : Array.from(items).findIndex((it) => it.classList.contains("active"));
    updateUI(idx);
  });
});
