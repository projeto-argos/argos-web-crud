const html = document.documentElement;

function lerPreferencia(chave) {
    try {
        return localStorage.getItem(chave);
    } catch (e) {
        return null;
    }
}

function salvarPreferencia(chave, valor) {
    try {
        localStorage.setItem(chave, valor);
    } catch (e) {}
}

if (lerPreferencia("argos-modo") === "escuro") {
    html.classList.add("dark");
}

if (lerPreferencia("argos-sidebar") === "fechada") {
    html.classList.add("sb-fechada");
}

window.addEventListener("pageshow", (e) => {
    const nav = performance.getEntriesByType("navigation")[0];
    if (e.persisted || (nav && nav.type === "back_forward")) {
        location.reload();
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const botaoToggle = document.querySelector(".sidebar .toggle");
    const botaoModo = document.querySelector(".sidebar .mode-btn");
    const textoModo = document.querySelector(".sidebar .mode-text");

    function atualizarTextoModo() {
        if (textoModo) {
            textoModo.textContent = html.classList.contains("dark")
                ? "Modo claro"
                : "Modo escuro";
        }
    }

    atualizarTextoModo();

    if (botaoToggle) {
        botaoToggle.addEventListener("click", () => {
            const fechada = html.classList.toggle("sb-fechada");

            salvarPreferencia(
                "argos-sidebar",
                fechada ? "fechada" : "aberta"
            );
        });
    }

    if (botaoModo) {
        botaoModo.addEventListener("click", () => {
            const escuro = html.classList.toggle("dark");

            salvarPreferencia(
                "argos-modo",
                escuro ? "escuro" : "claro"
            );

            atualizarTextoModo();
        });
    }
});