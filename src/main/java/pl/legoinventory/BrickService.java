package pl.legoinventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrickService {

    @Autowired
    BrickRepository brickRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/bricks")
    public ResponseEntity getBricks() throws JsonProcessingException {
        List<Brick> bricks = brickRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(bricks));
    }

    @PostMapping("/bricks")
    public ResponseEntity addBrick(@RequestBody Brick brick) {
        List<Brick> brickFromDb = brickRepository.findAllByNumber(brick.getNumber());
        if (!brickFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Brick savedBrick = brickRepository.save(brick);
        return ResponseEntity.ok(savedBrick);
    }
}
