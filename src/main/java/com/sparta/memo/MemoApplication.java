package com.sparta.memo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication 들어가보면 @ComponentScan가 있다.
// @ComponentScan는 해당 패키지와 하위패키지에 @Component가 있으면 다 읽어서 bean에 등록한다.
// 즉, 그러한 기능을 포함하고 있는 @SpringBootApplication가 com.sparta.memo 바로 하위에 MemoApplication 이라는 클래스에 있으나,
// 얘를 포함해서 그 하위 패키지에 있는 모든 클래스들을 다 훑어주는 것이다. @Component가 있는지 없는지를.
@SpringBootApplication
public class MemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoApplication.class, args);
    }

}
