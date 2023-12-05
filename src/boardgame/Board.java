package boardgame;

import java.util.Arrays;

public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    //Cria um tabuleiro com o número específico de linhas (rows) e colunas (columns).
    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    // verifica se a posição ta dentro do tabuleiro  || Se a posição existir, retorna a peça (Piece) que está 
    // localizada na posição especificada pelas coordenadas row e column no tabuleiro.
    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("Position not on the board");
        }
        return pieces[row][column];
    }
    
    //Verifica se uma posição específica está dentro dos limites do tabuleiro com base nas coordenadas de linha e coluna.
    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        return pieces[position.getRow()][position.getColumn()];

    }
    // Coloca uma peça (Piece) em uma determinada posição do tabuleiro (representada por um objeto Position).
    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("Ja tem uma peça na posição" + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }


    // Remover peças
    //Remove uma peça do tabuleiro, retornando a peça que foi removida.
    public Piece removePiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        if (piece(position) == null) {
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    public boolean positionExists(int row, int column) {
       return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
         if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        return piece(position) != null;
    }

    @Override
    public String toString() {
        return "Board [rows=" + rows + ", columns=" + columns + ", pieces=" + Arrays.toString(pieces) + "]";
    }

    
}
