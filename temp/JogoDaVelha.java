import java.util.Scanner;

public class JogoDaVelha{
    public static void main(String[] args) {
        Campos[][] tictactoe = new Campos[3][3];
        Boolean game = true;
        String symbol_play = "X";
        String win = "";
    
        Scanner sc = new Scanner(System.in);

        play_start(tictactoe);

        while(game){
            game_scream(tictactoe);
            try {
                if(analist_play(tictactoe,plays(sc,symbol_play),symbol_play)){
                    if(symbol_play.equals("X")){
                        symbol_play = "O";
                    }else{
                        symbol_play = "X";
                    }
                }
            } catch (Exception e) {
                System.out.printf("Erro");
            }
            win = winner(tictactoe,symbol_play);
            if(!win.equals("")){
                System.out.printf("jogador %s venceu",win);
                game = false;
            }
        }
        System.out.printf("\n Fim de jogo");
    }

    public static void clear_scream() {
        for(int count=0;count<200;count++){
            System.out.printf(" ");
        }
    }
    
    public static void game_scream(Campos[][] tictactoe){
        System.out.println("    0   1   2");
        System.out.printf("0   %s  |   %s  |   %s  %n",tictactoe[0][0].getSymbol(),tictactoe[0][1].getSymbol(),tictactoe[0][2].getSymbol());
        System.out.printf("1   %s  |   %s  |   %s  %n",tictactoe[1][0].getSymbol(),tictactoe[1][1].getSymbol(),tictactoe[1][2].getSymbol());
        System.out.printf("2   %s  |   %s  |   %s  %n",tictactoe[2][0].getSymbol(),tictactoe[2][1].getSymbol(),tictactoe[2][2].getSymbol());
    }

    public static int[] plays(Scanner sc, String sa) {
        int p[] = new int [2];
        System.out.printf("%s %s%n","Quem joga:",sa);
        System.out.printf("informe a linha:");
        p[0] = sc.nextInt();
        System.out.printf("informe a coluna:");
        p[1] = sc.nextInt();
        return p;
    }

    public static Boolean analist_play(Campos[][] tictactoe, int p[],String symbol) {
        if(tictactoe[p[0]][p[1]].getSymbol().equals(" ")){
            tictactoe[p[0]][p[1]].setSymbol(symbol);
            return true;
        }
        else{
            return false;
        }
    }

    public static String winner(Campos[][] tictactoe, String sa){
        int soma = 0;
        int soma2 = 0;
        int soma3 = 0;
        int soma4 = 0;

        if(sa.equals("O")){
            int primary_sum = 0;
            int secundary_sum = 0;
            for(int l = 0; l < 3; l++){
                for(int c = 0; c < 3; c++){
                    if(tictactoe[l][c].getSymbol().equals("X")){
                        soma++;
                    }
                    if(soma == 3){
                        return "X";
                    }

                    if(tictactoe[c][l].getSymbol().equals("X")){
                        soma2++;
                    }
                    if(soma2 == 3){
                        return "X";
                    }
                }
                soma = 0;
                soma2 = 0;
            }
            for(int i = 0; i < 3; i++){
                if(tictactoe[i][i].getSymbol().equals("X")){
                    primary_sum++;
                }
            }
            if(primary_sum == 3){
                return "X";
            }

            for(int linha = 2; linha >= 0; linha--){
                for(int coluna = 0; coluna < 3; coluna++){
                    if(tictactoe[linha][coluna].getSymbol().equals("X")){
                        secundary_sum++;
                    }
                }
                if(secundary_sum == 3){
                    return "X";
                }
            }
            primary_sum = 0;
            secundary_sum = 0;
        }else{
            int primary_sum = 0;
            int secundary_sum = 0;
            for(int l = 0; l < 3; l++){
                for(int c = 0; c < 3; c++){
                    if(tictactoe[l][c].getSymbol().equals("O")){
                        soma3 +=1;
                    }
                    if(soma3 == 3){
                        return "O";
                    }

                    if(tictactoe[c][l].getSymbol().equals("O")){
                        soma4 +=1;
                    }
                    if(soma4 == 3){
                        return "O";
                    }
                }
                soma3 = 0;
                soma4 = 0;
            }
            for(int i = 0; i < 3; i++){
                if(tictactoe[i][i].getSymbol().equals("O")){
                    primary_sum++;
                }
            }
            if(primary_sum == 3){
                return "O";
            }
            for(int linha = 2; linha >= 0; linha--){
                for(int coluna = 0; coluna < 3; coluna++){
                    if(tictactoe[linha][coluna].getSymbol().equals("O")){
                        secundary_sum++;
                    }
                }
                if(secundary_sum == 3){
                    return "O";
                }
            }
            primary_sum = 0;
            secundary_sum = 0;
        }
        return "";

    }

    public static void play_start(Campos[][] tictactoe) {
        for(int l=0;l<3;l++){
            for(int c = 0; c < 3;c++){
                tictactoe[l][c] = new Campos();
            }
        }
    }
}