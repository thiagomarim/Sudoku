import model.Board;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static Board board;

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int option = getMenuOption();

            if (option == 1) {
                startGame(args);
                continue;
            }

            if (board == null && option != 8) {
                System.out.println("\nO jogo ainda não foi iniciado. Selecione a opção 1.");
                continue;
            }

            switch (option) {
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> {
                    System.out.println("Saindo do jogo. Até mais!");
                    System.exit(0);
                }
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n--- JOGO DE SUDOKU ---");
        System.out.println("1 - Iniciar um novo Jogo");
        System.out.println("2 - Colocar um novo número");
        System.out.println("3 - Remover um número");
        System.out.println("4 - Visualizar jogo atual");
        System.out.println("5 - Verificar status do jogo");
        System.out.println("6 - Limpar jogo");
        System.out.println("7 - Finalizar jogo");
        System.out.println("8 - Sair");
        System.out.print("Selecione uma opção: ");
    }

    private static void startGame(String[] positions) {
        if (board != null) {
            System.out.println("\nO jogo já foi iniciado. Para recomeçar, saia e entre novamente.");
            return;
        }
        try {
            board = new Board(positions);
            System.out.println("\nO jogo está pronto para começar!");
            showCurrentGame();
        } catch (IllegalArgumentException e) {
            System.err.println("\nErro ao iniciar o jogo: " + e.getMessage());
            System.err.println("Verifique os argumentos da linha de comando. Formato esperado: 'linha,coluna;valor,fixo'");
            board = null;
        }
    }

    private static void inputNumber() {
        System.out.println("\n--- Inserir Número ---");
        System.out.print("Informe a linha (0-8): ");
        int row = getValidCoordinate();
        System.out.print("Informe a coluna (0-8): ");
        int col = getValidCoordinate();
        System.out.printf("Informe o número (1-9) para a posição [%d,%d]: ", row, col);
        int value = getValidValue();

        if (!board.changeValue(row, col, value)) {
            System.out.printf("\nERRO: A posição [%d,%d] tem um valor fixo e não pode ser alterada.\n", row, col);
        } else {
            System.out.println("\nNúmero inserido com sucesso!");
            showCurrentGame();
        }
    }

    private static void removeNumber() {
        System.out.println("\n--- Remover Número ---");
        System.out.print("Informe a linha (0-8): ");
        int row = getValidCoordinate();
        System.out.print("Informe a coluna (0-8): ");
        int col = getValidCoordinate();

        if (!board.clearValue(row, col)) {
            System.out.printf("\nERRO: A posição [%d,%d] tem um valor fixo e não pode ser removida.\n", row, col);
        } else {
            System.out.println("\nNúmero removido com sucesso!");
            showCurrentGame();
        }
    }

    private static void showCurrentGame() {
        System.out.println("\nSeu jogo se encontra da seguinte forma:");
        System.out.println(board.getFormattedBoard());
    }

    private static void showGameStatus() {
        System.out.println("\n--- Status do Jogo ---");
        System.out.printf("O jogo atualmente se encontra no status: %s\n", board.getStatus().getLabel());
        if (board.hasErrors()) {
            System.out.println("Atenção: O jogo contém erros (números em posições incorretas).");
        } else {
            System.out.println("O jogo não contém erros.");
        }
    }

    private static void clearGame() {
        System.out.print("\nTem certeza que deseja limpar seu jogo e perder todo seu progresso? (sim/não): ");
        String confirm = SCANNER.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")) {
            System.out.print("Resposta inválida. Informe 'sim' ou 'não': ");
            confirm = SCANNER.next();
        }

        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
            System.out.println("\nJogo resetado para o estado inicial.");
            showCurrentGame();
        } else {
            System.out.println("\nOperação cancelada.");
        }
    }

    private static void finishGame() {
        if (board.gameIsFinished()) {
            System.out.println("\n***********************************");
            System.out.println("* Parabéns, você concluiu o jogo! *");
            System.out.println("***********************************");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("\nSeu jogo contém erros. Verifique o tabuleiro e ajuste os números incorretos.");
        } else {
            System.out.println("\nVocê ainda não preencheu todos os espaços. Continue jogando!");
        }
    }

    private static int getMenuOption() {
        while (true) {
            try {
                int option = SCANNER.nextInt();
                if (option >= 1 && option <= 8) {
                    return option;
                } else {
                    System.out.print("Opção inválida. Selecione um número entre 1 e 8: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Por favor, digite um número: ");
                SCANNER.next();
            }
        }
    }

    private static int getValidCoordinate() {
        return runUntilGetValidNumber(0, 8);
    }

    private static int getValidValue() {
        return runUntilGetValidNumber(1, 9);
    }

    private static int runUntilGetValidNumber(int min, int max) {
        while (true) {
            try {
                int number = SCANNER.nextInt();
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.out.printf("Número fora do intervalo. Informe um número entre %d e %d: ", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Por favor, digite um número inteiro: ");
                SCANNER.next();
            }
        }
    }
}