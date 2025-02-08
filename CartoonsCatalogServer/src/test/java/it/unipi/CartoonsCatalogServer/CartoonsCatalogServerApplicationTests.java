package it.unipi.CartoonsCatalogServer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartoonsCatalogServerApplicationTests {
    
        @Autowired
        private MockMvc mvc;

	@Test
	void contextLoads() {
	}
        
        @Test
        public void getAllCharacters_response200()
                throws Exception {
            mvc.perform( MockMvcRequestBuilders
                .get("/607453/tutti")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        }
        
        @Test
        public void removeCharacter_response200()
                throws Exception {
            mvc.perform( MockMvcRequestBuilders
                .post("/607453/rimuovi?id=20")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        }

}
