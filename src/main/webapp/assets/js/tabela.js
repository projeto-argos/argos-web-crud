/*
 * tabela.js - busca e paginação de qualquer tabela.
 * Serve para todas as telas de listagem.
 */
const LINHAS_POR_PAGINA = 10;
let paginaAtual = 1;

const campoBusca = document.getElementById("campoBusca");
const infoPaginas = document.getElementById("infoPaginas");
const btnAnterior = document.getElementById("btnAnterior");
const btnProxima = document.getElementById("btnProxima");
const linhaVazia = document.getElementById("linhaVazia");

// Pega os dados da tabela e guarda num array
const todasAsLinhas = Array.from(document.querySelectorAll("#tabela tbody tr.linha"));


// CPF e telefone.
document.querySelectorAll(".cpf").forEach(function (celula) {
    const n = celula.textContent.trim();
    if (n.length === 11) {
        celula.textContent = n.slice(0, 3) + "." + n.slice(3, 6) + "." + n.slice(6, 9) + "-" + n.slice(9);
    }
});

document.querySelectorAll(".telefone").forEach(function (celula) {
    const n = celula.textContent.trim();
    if (n.length === 11) {
        celula.textContent = "(" + n.slice(0, 2) + ") " + n.slice(2, 7) + "-" + n.slice(7);
    } else if (n.length === 10) {
        celula.textContent = "(" + n.slice(0, 2) + ") " + n.slice(2, 6) + "-" + n.slice(6);
    }
});


//  Busca
function limparTexto(texto) {
    return texto.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();
}

// Desenha a tabela (busca + página atual)
function mostrarTabela() {
    const busca = limparTexto(campoBusca.value);

    const encontradas = todasAsLinhas.filter(function (linha) {
        return limparTexto(linha.textContent).includes(busca);
    });

    const totalPaginas = Math.max(1, Math.ceil(encontradas.length / LINHAS_POR_PAGINA));
    if (paginaAtual > totalPaginas) {
        paginaAtual = totalPaginas;
    }

    const inicio = (paginaAtual - 1) * LINHAS_POR_PAGINA;
    const daPagina = encontradas.slice(inicio, inicio + LINHAS_POR_PAGINA);

    todasAsLinhas.forEach(function (linha) {
        linha.hidden = true;
    });
    daPagina.forEach(function (linha) {
        linha.hidden = false;
    });

    linhaVazia.hidden = encontradas.length > 0;

    if (encontradas.length === 0) {
        infoPaginas.textContent = "Nenhum resultado encontrado";
    } else {
        infoPaginas.textContent = "Mostrando " + (inicio + 1) + "-" + (inicio + daPagina.length) +
                                  " de " + encontradas.length;
    }
    btnAnterior.disabled = paginaAtual <= 1;
    btnProxima.disabled = paginaAtual >= totalPaginas;
}

//  Ligar os botões
campoBusca.addEventListener("input", function () {
    paginaAtual = 1;
    mostrarTabela();
});

btnAnterior.addEventListener("click", function () {
    paginaAtual = paginaAtual - 1;
    mostrarTabela();
});

btnProxima.addEventListener("click", function () {
    paginaAtual = paginaAtual + 1;
    mostrarTabela();
});

mostrarTabela();
