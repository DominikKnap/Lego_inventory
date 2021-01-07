package pl.legoinventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrickRepository extends JpaRepository<Brick, Long> {
    List<Brick> findAllByNumber(String number);
}
