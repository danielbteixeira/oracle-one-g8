import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContaBancaria conta = new ContaBancaria("Fernanda Ribeiro", "Corrente", 2385.19);
        conta.mostrarInformacoes();

        int opcao = 0;
        String menu = """
                ** Digite sua opção ** 
                1 - Consultar saldo
                2 - Transferir valor 
                3 - Receber valor 
                4 - Sair
                
                """;

        while (opcao != 4) {
            System.out.println(menu);
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    conta.consultarSaldo();
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Qual valor deseja transferir?");
                    double valorTransferencia = scanner.nextDouble();
                    conta.transferirValor(valorTransferencia);
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Qual valor deseja receber?");
                    double valorRecebido = scanner.nextDouble();
                    conta.receberValor(valorRecebido);
                    System.out.println();
                    break;
                case 4:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    System.out.println();
            }
        }




        scanner.close();
    }
}
