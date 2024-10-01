package com.sparta.memo.entity;

import com.sparta.memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 데이터베이스와 소통할 클래스
public class Memo {
    private Long id;
    private String username;
    private String contents;

    public Memo(MemoRequestDto requestDto) {
        username=requestDto.getUsername();
        contents=requestDto.getContents();
    }

    public void update(MemoRequestDto requestDto) {
        username=requestDto.getUsername();
        contents=requestDto.getContents();
    }
}
