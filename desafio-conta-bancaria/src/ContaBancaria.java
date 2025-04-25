public class ContaBancaria {
    private String nomeCliente;
    private String tipoConta;
    private double saldo;

public ContaBancaria(String nomeCliente, String tipoConta, double saldoInicial) {
    this.nomeCliente = nomeCliente;
    this.tipoConta = tipoConta;
    this.saldo = saldoInicial;
}

public void mostrarInformacoes() {
    System.out.println("********************");
    System.out.println("\nNome do Cliente: " + nomeCliente);
    System.out.println("\nTipo de Conta: " + tipoConta);
    System.out.println("Saldo atual: " + saldo);
    System.out.println("********************");
}

public void consultarSaldo() {
    System.out.println("O saldo atualizado é: " + saldo);
}

public void receberValor(double valor) {
    saldo += valor;
    System.out.println("Valor recebido com sucesso !");
    System.out.println("Seu saldo atual é de: " + saldo);
}

public void transferirValor(double valor) {
    if (valor > saldo) {
        System.out.println("Não há saldo suficiente para a transferência");
    } else {
        saldo -= valor;
        System.out.println("Transferência realizada com sucesso !");
        System.out.println("Seu saldo atual é de: " + saldo);
    }
}






}
