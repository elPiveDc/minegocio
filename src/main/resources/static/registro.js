document.addEventListener("DOMContentLoaded", () => {
  const steps = document.querySelectorAll(".form-step");
  const progressItems = document.querySelectorAll(".step-item");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  let currentStep = 0;

  function updateStep() {
    steps.forEach((step, i) => {
      step.classList.toggle("active", i === currentStep);
    });

    progressItems.forEach((item, i) => {
      item.classList.remove("active", "completed");
      if (i < currentStep) {
        item.classList.add("completed");
      } else if (i === currentStep) {
        item.classList.add("active");
      }
    });

    prevBtn.disabled = currentStep === 0;
    if (currentStep === steps.length - 1) {
      nextBtn.textContent = "Finalizar Registro";
      nextBtn.type = "submit";
    } else {
      nextBtn.textContent = "Siguiente →";
      nextBtn.type = "button";
    }
  }

  window.nextStep = () => {
    if (currentStep < steps.length - 1) {
      currentStep++;
      updateStep();
    }
  };

  window.prevStep = () => {
    if (currentStep > 0) {
      currentStep--;
      updateStep();
    }
  };

  // Permitir click en pasos anteriores
  progressItems.forEach((item) => {
    item.addEventListener("click", () => {
      const step = parseInt(item.getAttribute("data-step"));
      if (step <= currentStep) {
        currentStep = step;
        updateStep();
      }
    });
  });

  updateStep();
});

nextBtn.addEventListener("click", () => {
  if (nextBtn.type === "button") {
    nextStep();
  }
});

prevBtn.addEventListener("click", () => {
  prevStep();
});
