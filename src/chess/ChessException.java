package chess;

import boardgame.BoardException;

public class ChessException extends BoardException {
    private static final long serialVersionUID = 1L;

    //criando mensagem de exceção
    public ChessException(String message) {
        super(message);
    }
}
