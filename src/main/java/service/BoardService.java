package service;

import db.Database;
import model.Board;

import java.util.List;

import static db.Database.addBoard;
import static db.Database.findAllBoard;

public class BoardService {
    private volatile static BoardService instance;

    private BoardService() {
    }

    public static BoardService getInstance() {
        if (instance == null) {
            instance = new BoardService();
        }
        return instance;
    }

    public void write(Board board) {
        addBoard(board);
    }

    public List<Board> findAll() {
        return findAllBoard();
    }

    public Board findBoardById(int id) {
        return Database.findBoardById(id);
    }
}
