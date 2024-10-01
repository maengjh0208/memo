package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
// @Controller: MVC 패턴에 사용됨. 뷰 템플릿을 사용해서 Html 페이지 같은 뷰를 반환하는데 사용
// @RestController: RESTful 웹서비스에서 사용되며, HTTP 요청에 대한 응답을 Json 또는 XML 데이터 형식으로 직접 반환하는데 사용
// @RestController = @Controller + @ResponseBody
@RequestMapping("/api")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // requestDto -> Entity (DB에 저장해야해서)
        Memo memo = new Memo(requestDto);

        // Memo Max Id Check
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoList.put(maxId, memo);

        // Entity -> ResponseDto
        MemoResponseDto responseDto = new MemoResponseDto(memo);
        return responseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map To List
        List<MemoResponseDto> responseList = memoList.values().stream().map(MemoResponseDto::new).toList();

        return responseList;
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        if (!memoList.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 메모입니다.");
        }

        Memo memo = memoList.get(id);

        memo.update(requestDto);

        return memo.getId();
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        if (!memoList.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 메모입니다.");
        }

        memoList.remove(id);
        return id;
    }
}
