package boardgame;

public abstract class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        position = null;
    }
    // Retorna o tabuleiro ao qual a peça está associada.
    protected Board getBoard() {
        return board;
    }
    //Retorna uma representação em string do estado atual da peça, incluindo sua posição e referência ao tabuleiro.
    @Override
    public String toString() {
        return "Piece [position=" + position + ", board=" + board + "]";
    }

    public abstract boolean [][] possibleMoves();
    
    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    //verifica se o movimento é possivel
    public boolean isTherePossibleMove() {
        boolean [][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
