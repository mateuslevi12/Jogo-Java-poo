package chess;

import boardgame.Position;

public class ChessPosition {
    private char column;
    private int row;

    // Verifica se a coluna está entre 'a' e 'h' e se a linha está entre 1 e 8. 
    // Caso contrário, lança uma exceção ChessException.
    public ChessPosition(char column, int row) {
        if(column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    // Definindo as posições das peças
    //Pega a posição de xadrez e retorna uma generica
    protected Position toPosition() {
        return new Position(8 - row, column - 'a' );
    }

    // pega uma posição de xadrez e retorna uma generica
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
        // exemplo: 'a' + 2 = 'c' || 8 - 6(generica) = 2 || resultando na posição de xadrez: c2
	}
    
    @Override
    public String toString() {
        return "" + column + row;
    }

    
}
