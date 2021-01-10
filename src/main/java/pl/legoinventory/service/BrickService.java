package pl.legoinventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.legoinventory.entity.Brick;
import pl.legoinventory.repository.BrickRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BrickService {

    @Autowired
    BrickRepository brickRepository;

    public void addBrick(Brick brick) {
        brickRepository.save(brick);
    }

    public List getBricks() {
        return brickRepository.findAll();
    }

    public Optional<Brick> getBrickById(Long id) {
        return brickRepository.findById(id);
    }
}
