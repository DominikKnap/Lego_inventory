package pl.legoinventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrickRepository extends JpaRepository<Brick, Long> {
    List<Brick> findAllByNumber(String number);
}
