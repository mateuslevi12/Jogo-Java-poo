package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount;
    
    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount++;
    }

    @Override
    public String toString() {
        return "ChessPiece [color=" + color + "]";
    }
    
    // pega a posição da peça em formato de matriz
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
    
    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece)getBoard().piece(position); // pega a peça na posição x do tabuleiro
        return p != null && p.getColor() != color; // se for diferente de nulo e diferente da cor do jogador da vez, é inimigo
    }


}
