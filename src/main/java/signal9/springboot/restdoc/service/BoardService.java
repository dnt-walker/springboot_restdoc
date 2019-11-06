package signal9.springboot.restdoc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import signal9.springboot.restdoc.dto.Post;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BoardService {
    public Map<String, Post> getList(int pageNo) {
        Map<String, Post> postsMap = new HashMap<>();

        for (int idx = (pageNo - 1) * 10 ; idx < (pageNo * 10) ; idx++) {
            postsMap.put(String.valueOf(idx),
                    Post.builder().subject("posting no. " + idx).content(", bla~ bla~ bla~.").build());
        }
        return postsMap;
    }

    public boolean add(Post post) {
        log.info("add post : " + post.getSubject() + ", " + post.getContent());
        return true;
    }
}
