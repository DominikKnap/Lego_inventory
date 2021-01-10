package pl.legoinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.legoinventory.entity.Brick;

import java.util.List;

@Repository
public interface BrickRepository extends JpaRepository<Brick, Long> {
    List<Brick> findAllByName(String name);
}
