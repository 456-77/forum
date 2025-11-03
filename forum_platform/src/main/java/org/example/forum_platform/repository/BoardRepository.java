package org.example.forum_platform.repository;

import org.example.forum_platform.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByNameContaining(String keyword);
}