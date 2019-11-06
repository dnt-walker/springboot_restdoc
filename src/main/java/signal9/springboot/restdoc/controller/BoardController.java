package signal9.springboot.restdoc.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signal9.springboot.restdoc.dto.Post;
import signal9.springboot.restdoc.service.BoardService;

@Slf4j
@RestController
public class BoardController {
    @Autowired
    BoardService boardService;

    @GetMapping(value = "list/{pageNo:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity list(@PathVariable("pageNo") int pageNo) {
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("status", "success");
        body.add("posts", gson.toJsonTree(boardService.getList(pageNo)).getAsJsonObject());
        return ResponseEntity.status(200).body(body.toString());
    }

    @PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody final Post post) {
        boardService.add(post);
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("status", "success");
        return ResponseEntity.status(200).body(body.toString());
    }
}
