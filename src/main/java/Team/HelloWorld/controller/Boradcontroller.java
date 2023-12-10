package Team.HelloWorld.controller;

// Entity를 사용하려면 임포트 해줘야함.

import Team.HelloWorld.entity.Board;
import Team.HelloWorld.service.BoardService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class Boradcontroller {


    @Autowired
    private BoardService boardService;
    @GetMapping("/board/write")

    public String boardWriteForm() {

        return "BoardWrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {

        boardService.boardWrite(board, file);

        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0,size = 10,sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {


        Page<Board> list = null;

        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword,pageable);
        }


        //페이지 시작은 0 이기에
        int nowPage = list.getPageable().getPageNumber() + 1;

        //둘 중 큰값이므로 1 아래로 내려가지 않게
        int startPage = Math.max(nowPage - 4,1);
        int endPage = Math.min(nowPage + 5,list.getTotalPages());

        //boardList에 있는걸 "list"라는 이름으로 전송
        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "boardlist";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {
        model.addAttribute("board",boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping("board/modify/{id}")
    //@PathVariable 경로의 {id}값을 받아 Integer id 로 넘기는 것
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {

        model.addAttribute("board",boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board,Model model, MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.boardWrite(boardTemp, file);

        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");


        return "message";
    }
}
