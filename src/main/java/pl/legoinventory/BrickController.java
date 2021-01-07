package pl.legoinventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class BrickController {

    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    BrickService brickService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/brick/saveBrickDetails")
    @ResponseBody
    public ResponseEntity<?> createProduct(@RequestParam("number") String number, Model model,
                                           HttpServletRequest request, final @RequestParam("image")MultipartFile file) {
        try {
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            log.info("uploadDirectory:: " + uploadDirectory);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            log.info("FileName: " + file.getOriginalFilename());
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            String[] numbers = number.split(",");
            log.info("Number " + numbers[0] + " " + filePath);
            try {
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    log.info("Folder created");
                    dir.mkdirs();
                }
                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                log.info("in catch");
                e.printStackTrace();
            }
            byte[] imageData = file.getBytes();
            Brick brick = new Brick();
            brick.setNumber(numbers[0]);
            brick.setImage(imageData);
            brickService.addBrick(brick);
            log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/image/display/{id}")
    void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Brick> brick) throws IOException {
        log.info("Id :: " + id);
        brick = brickService.getBrickById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(brick.get().getImage());
        response.getOutputStream().close();
    }

    @GetMapping("/image/imageDetails")
    String showProductDetails(@RequestParam("id") Long id, Optional<Brick> brick, Model model) {
        try {
            log.info("Id :: " + id);
            if (id != 0) {
                brick = brickService.getBrickById(id);

                log.info("products :: " + brick);
                if (brick.isPresent()) {
                    model.addAttribute("id", brick.get().getId());
                    model.addAttribute("number", brick.get().getNumber());
                    return "brickdetails";
                }
                return "redirect:/home";
            }
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
    }

    @GetMapping("/image/show")
    String show(Model map) throws JsonProcessingException {
        List<Brick> bricks = brickService.getBricks();
        return "bricks";
    }
}
