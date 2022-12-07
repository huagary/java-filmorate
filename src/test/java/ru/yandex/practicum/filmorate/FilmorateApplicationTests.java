package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.util.FileToJsonString;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;
    @Autowired
    private FilmController filmController;
    private final String resPath = "src" + File.separator + "test" + File.separator + "resources" + File.separator;


    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
        assertThat(filmController).isNotNull();
    }

    @Test
    void getUser() throws Exception {
        isOkStatus("/users", "UserCreate.json", "post");
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\"" + ":" + "1")))
                .andExpect(content().string(containsString("\"email\"" + ":" + "\"mail@mail.ru\"")))
                .andExpect(content().string(containsString("\"login\"" + ":" + "\"dolore\"")))
                .andExpect(content().string(containsString("\"name\"" + ":" + "\"NickName\"")))
                .andExpect(content().string(containsString("\"birthday\"" + ":" + "\"1946-08-20\"")))
                .andReturn();
    }

    @Test
    void getFilm() throws Exception {
        isOkStatus("/films", "FilmCreate.json", "post");
        this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\"" + ":" + "1")))
                .andExpect(content().string(containsString("\"name\"" + ":" + "\"nisieiusmod\"")))
                .andExpect(content().string(containsString("\"description\"" + ":" + "\"adipisicing\"")))
                .andExpect(content().string(containsString("\"releaseDate\"" + ":" + "\"1967-03-25\"")))
                .andExpect(content().string(containsString("\"duration\"" + ":" + "100")))
                .andReturn();
    }

    @Test
    void postFilm() throws Exception {
        isOkStatus("/films", "FilmCreate.json", "post");
    }

    @Test
    void postUser() throws Exception {
        isOkStatus("/users", "UserCreate.json", "post");
    }

    @Test
    void postEmptyUser() throws Exception {
        isBadRequestStatus("/users", "Empty.json", "post");
    }

    @Test
    void postEmptyFilm() throws Exception {
        isBadRequestStatus("/films", "Empty.json", "post");
    }

    @Test
    void putUser() throws Exception {
        isOkStatus("/users", "UserCreate.json", "post");
        isOkStatus("/users", "UpdateUser.json", "put");
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\"" + ":" + "1")))
                .andExpect(content().string(containsString("\"email\"" + ":" + "\"mail@yandex.ru\"")))
                .andExpect(content().string(containsString("\"login\"" + ":" + "\"doloreUpdate\"")))
                .andExpect(content().string(containsString("\"name\"" + ":" + "\"estadipisicing\"")))
                .andExpect(content().string(containsString("\"birthday\"" + ":" + "\"1976-09-20\"")))
                .andReturn();
    }

    @Test
    void putFilm() throws Exception {
        isOkStatus("/films", "FilmCreate.json", "post");
        isOkStatus("/films", "UpdateFilm.json", "put");
        this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\"" + ":" + "1")))
                .andExpect(content().string(containsString("\"name\"" + ":" + "\"FilmUpdated\"")))
                .andExpect(content().string(containsString("\"description\"" + ":" + "\"Newfilmupdatedecription\"")))
                .andExpect(content().string(containsString("\"releaseDate\"" + ":" + "\"1989-04-17\"")))
                .andExpect(content().string(containsString("\"duration\"" + ":" + "190")))
                .andReturn();
    }

    void isBadRequestStatus(String endpoint, String fileName, String method) throws Exception {
        switch (method) {
            case "post":
                this.mockMvc.perform(post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(FileToJsonString.readFile(resPath + fileName, StandardCharsets.UTF_8)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andReturn();
                break;
            case "put":
                this.mockMvc.perform(put(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(FileToJsonString.readFile(resPath + fileName, StandardCharsets.UTF_8)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andReturn();
                break;
        }
    }

    void isOkStatus(String endpoint, String fileName, String method) throws Exception {
        switch (method) {
            case "post":
                this.mockMvc.perform(post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(FileToJsonString.readFile(resPath + fileName, StandardCharsets.UTF_8)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
                break;
            case "put":
                this.mockMvc.perform(put(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(FileToJsonString.readFile(resPath + fileName, StandardCharsets.UTF_8)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
                break;
        }
    }
}
