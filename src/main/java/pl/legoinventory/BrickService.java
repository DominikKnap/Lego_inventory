package pl.legoinventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PutMapping("/bricks/{number}")
    public ResponseEntity updateBrickNumber(@RequestBody Brick brick, @PathVariable("number")String number){
        List<Brick> brickFromDb = brickRepository.findAllByNumber(number);
        if (brickFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Brick brickToUpdate = brickFromDb.get(0);
        brickToUpdate.setNumber(brick.getNumber());
        brickRepository.save(brickToUpdate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/bricks/{number}")
    public ResponseEntity removeBrick(@PathVariable("number")String number) {
        List<Brick> brickFromDb = brickRepository.findAllByNumber(number);
        if (brickFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        brickRepository.delete(brickFromDb.get(0));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
