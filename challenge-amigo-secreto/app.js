// Lista para armazenar os amigos
let listaAmigos = [];

// Função para adicionar um amigo à lista
function adicionarAmigo() {
    // Captura o valor do input
    let nomeDigitado = document.getElementById("amigo").value.trim();
    let tamanhoNome = nomeDigitado.length;

    // Verifica se o nome não está vazio
    if (nomeDigitado === "" || nomeDigitado.length < 3) {
        alert("Por favor, digite um nome válido.");
        limparCampo();
        return;
    } else if (listaAmigos.includes(nomeDigitado)) {
            alert("Este nome já foi adicionado!");
            limparCampo(); 
            return;
    } else {
    // Adiciona o nome ao array
    listaAmigos.push(nomeDigitado);

    // Atualiza a exibição da lista
    atualizarLista();

    // Limpa o campo de entrada
    document.getElementById("amigo").value = "";
    }

    
}

// Função para atualizar a lista na tela
function atualizarLista() {
    let listaElement = document.getElementById("listaAmigos");
    
    // Limpa a lista antes de atualizar
    listaElement.innerHTML = "";

    // Adiciona cada nome na lista como um item <li>
    listaAmigos.forEach((amigo) => {
        let item = document.createElement("li");
        item.textContent = amigo;
        listaElement.appendChild(item);
    });
}

// Função para sortear um amigo secreto
function sortearAmigo() {
    if (listaAmigos.length < 2) {
        alert("Adicione pelo menos dois amigos para realizar o sorteio!");
        return;
    }

    // Sorteia um índice aleatório dentro do array
    let indiceSorteado = Math.floor(Math.random() * listaAmigos.length);
    let amigoSorteado = listaAmigos[indiceSorteado];

    // Exibe o resultado na lista de resultado
    let resultadoElement = document.getElementById("resultado");
    resultadoElement.innerHTML = `<li>🎉 O amigo secreto sorteado é: <strong>${amigoSorteado}</strong>! 🎁</li>`;
}


// Função para resetar lista de amigos
function resetarLista() {
    listaAmigos = [];
    document.getElementById("listaAmigos").innerHTML = "";
    document.getElementById("resultado").innerHTML = "";
}

// Função para limpar o campo de entrada
function limparCampo() {
    document.getElementById("amigo").value = "";
}
