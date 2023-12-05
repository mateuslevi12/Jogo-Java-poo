package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();
    private boolean check;
    private boolean checkMate;


    //Configura o tabuleiro, define o turno como 1, define o jogador atual como branco e chama o método initialSetup()
    // para posicionar as peças iniciais no tabuleiro.
    public ChessMatch() {
        board = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheck() {
        return check;
    }
    public boolean getCheckMate() {
        return checkMate;
    }
    public Color getCurrentPlayer() {
        return currentPlayer; 
    }
    // Retorna uma matriz de peças de xadrez (ChessPiece[][]) representando o estado atual do tabuleiro.
    public ChessPiece[][] getPieces() {
         ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i< board.getRows(); i++) {
            for (int j = 0; j< board.getRows(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
        }

        //Recebe uma posição de origem e recupera os movimentos possíveis para a peça nessa posição.

        public boolean[][] possibleMoves(ChessPosition sourcePosition) {
            Position position = sourcePosition.toPosition();
            validateSourcePosition(position);
            return board.piece(position).possibleMoves(); // retorna os movimentos possiveis da peça escolhida
        }

        //Movimentação de peças
        //Executa um movimento de xadrez da posição de origem para a posição de destino.
        //Valida o movimento, realiza o movimento e passa para o próximo turno.
        public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
            Position source = sourcePosition.toPosition();
            Position target = targetPosition.toPosition();
            validateSourcePosition(source);
            validateTargetPosition(source, target);
            Piece capturedPiece = makeMove(source, target);

            if (testCheck(currentPlayer)) {
                undoMove(source, target, capturedPiece);
                throw new ChessException("You can't put yourself in check");
            }

            check = (testCheck(opponent(currentPlayer))) ? true : false;

            if (testCheckMate(opponent(currentPlayer))) {
                checkMate = true;
            }
            else {
                nextTurn();
            }

            return (ChessPiece)capturedPiece;
        }
        //Valida se a posição de destino é um movimento válido para a peça na posição de origem.
        private void validateTargetPosition(Position source, Position target) {
            if (!board.piece(source).possibleMove(target)) {
                throw new ChessException("The chosen piece can't move to target position");
            }
        }
        //Realiza um movimento no tabuleiro da posição de origem para a posição de destino.
        //Remove peças capturadas e atualiza as listas de peças capturadas e peças no tabuleiro.
        private Piece makeMove(Position source, Position target) {
            ChessPiece p = (ChessPiece)board.removePiece(source);
            p.increaseMoveCount();
            Piece capturedPiece = board.removePiece(target);
            board.placePiece(p, target);

            if (capturedPiece != null) {
                piecesOnTheBoard.remove(capturedPiece);
                capturedPieces.add(capturedPiece);
            }
            return capturedPiece;
        } 

        //Desfaz o movimento anterior no tabuleiro
        private void undoMove(Position source, Position target, Piece capturedPiece) {
            ChessPiece p = (ChessPiece)board.removePiece(target); // remover a peça na posição final
            p.decreaseMoveCount();
            board.placePiece(p, source); // volta a peça para posição de origem

            if (capturedPiece != null) {
                board.placePiece(capturedPiece, target); //adiciona a peça que foi removida na posição final antes de ser capturada
                capturedPieces.remove(capturedPiece);
                piecesOnTheBoard.add(capturedPiece);
            }
        }

        // verifica se não tem peça na posição de origem
        private void validateSourcePosition(Position position) throws ChessException {
            if (!board.thereIsAPiece(position)) { // verifica se a peça de origem que o jogador escolheu existe ou não
                throw new ChessException("There is no piece on source position ");
            }
            if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) { // verifica se a peça que o jogador atual escolheu é dele ou não
                throw new ChessException("The chosen piece is not yours");
            }
            if (!board.piece(position).isTherePossibleMove()) { // verifica se é possivel mover para a  posição escolhida (varia de acordo com as regras(se a peça escolhida se mexe ate la, por exemplo, se o peao anda 3 casas ou se tem uma peça amiga no caminho))
                throw new ChessException("There is no possible move for the chosen piece ");

            }
        }
        //Atualiza a contagem do turno e altera o jogador atual.
        private void nextTurn() { // atualiza o turno e muda o jogador
            turn++;
            currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
        }

        private Color opponent(Color color) { //define o oponente
            return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        }

        private ChessPiece king(Color color) { //procura o rei na cor que foi passada como parametro
            List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
            for(Piece p : list) {
                if (p instanceof King) {
                    return (ChessPiece)p;
                }
            }
            throw new IllegalArgumentException("There is no " + color + "king on the board");
        }

        private boolean testCheck(Color color) {
            Position kingPosition = king(color).getChessPosition().toPosition(); //pega a posição do rei daquela cor em formato de matriz
            List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
            for ( Piece p : opponentPieces ) {
                boolean[][] mat = p.possibleMoves();
                if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { //verfica se a posição do rei esta disponivel, se tiver, check
                    return true;
                }
            }
            return false; 
        }

        private boolean testCheckMate(Color color) {
            if(!testCheck(color)) {
                return false;
            }
            List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
            for (Piece p : list) {
                boolean [][] mat = p.possibleMoves();
                for (int i = 0; i < board.getRows(); i++) {
                    for (int j = 0; j < board.getColumns(); j++) {
                        if(mat[i][j]) {
                            Position source = ((ChessPiece)p).getChessPosition().toPosition();
                            Position target = new Position(i, j);
                            Piece capturedPiece = makeMove(source, target); 
                            boolean testCheck = testCheck(color);
                            undoMove(source, target, capturedPiece);
                            if (!testCheck) {
                                return false;
                            }
                        }
                    }
                }
            } 
            return true;
        }

        // coloca uma peça no tabuleiro 
        private void placeNewPiece(char column, int row, ChessPiece piece) {
            board.placePiece(piece, new ChessPosition(column, row).toPosition());
            piecesOnTheBoard.add(piece);
        }

        //Monta o tabuleiro com as peças nos locais certos
        private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
        }

        @Override
        public String toString() {
            return "ChessMatch [board=" + board + "]";
        }

        
}

