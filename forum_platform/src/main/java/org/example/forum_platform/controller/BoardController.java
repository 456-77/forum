package org.example.forum_platform.controller;

import org.example.forum_platform.dto.BoardDTO;
import org.example.forum_platform.entity.Board;
import org.example.forum_platform.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;
    // 创建版块
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }
    // 获取所有版块（仅返回id, name, description）
    @GetMapping
    public ResponseEntity<List<BoardDTO>> getAllBoards() {

        return ResponseEntity.ok(boardService.getAllBoards());
    }

    // 获取单个版块（只返回 id、name、description）
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable Long id) {
        return boardService.getBoardById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(
                        java.util.Map.of("success", false, "message", "版块不存在")
                ));
    }
    // 删除板块
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}