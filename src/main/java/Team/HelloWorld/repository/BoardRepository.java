package Team.HelloWorld.repository;

import Team.HelloWorld.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    //findBy(Title)Containing  Title컬럼에서 검색하는 문자를 포함한 모든것 검색 %임%  과 같음
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}
