package Team.HelloWorld.service;

import Team.HelloWorld.entity.Board;
import Team.HelloWorld.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    // 글 작성 처리
    public void boardWrite(Board board, MultipartFile file) throws Exception{

        String projectpath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        //uuid   랜덤의 텍스트를 넣어주는 것
        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectpath, fileName);

        file.transferTo(saveFile);

        board.setFilename(fileName);
        //경로의 경우 static 뒤부터 적으면 됨.
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 게시글 상세 보기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).get();
    }

    //void는 Return이 필요없고 id받아서 id 가지고 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }
}
