package com.example.demo.repository;

import com.example.demo.model.Habit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    @Query("""
        select h from Habit h
        where h.user.id = :userId
          and (:categoryId is null or h.category.id = :categoryId)
          and (:active is null or h.active = :active)
          and (:search is null or lower(h.name) like lower(concat('%', :search, '%')))
    """)
    List<Habit> findFiltered(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("active") Boolean active,
            @Param("search") String search,
            Sort sort
    );
}
