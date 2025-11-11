package org.example.forum_platform.service;

import org.example.forum_platform.dto.BoardDTO;
import org.example.forum_platform.entity.Board;
import org.example.forum_platform.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    // 返回 BoardDTO 列表，仅包含 id、name、description
    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(board -> new BoardDTO(
                        board.getId(),
                        board.getName(),
                        board.getDescription()
                ))
                .collect(Collectors.toList());
    }

    // 获取单个版块，仅返回 id、name、description
    public Optional<BoardDTO> getBoardById(Long id) {
        return boardRepository.findById(id)
                .map(board -> new BoardDTO(
                        board.getId(),
                        board.getName(),
                        board.getDescription()
                ));
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}