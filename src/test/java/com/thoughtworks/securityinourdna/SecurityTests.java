package com.thoughtworks.securityinourdna;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityTests {

    @Mock
    private UserRepo userRepo;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        AdminController controller = new AdminController(userRepo);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void admin_should_delete_vendor_with_csrf_token() throws Exception {
        mockMvc.perform(
                post("/admin/deleteVendor")
                .sessionAttr("csrfToken", "validToken")
                .sessionAttr("userState", new UserState(true))
                .param("vendor", "deleteMe")
                .param("csrfToken", "validToken")
        ).andExpect(status().isFound());

        verify(userRepo).delete("deleteMe");
    }

    @Test
    public void admin_cannot_delete_vendor_with_incorrect_csrf_token() throws Exception {
        mockMvc.perform(
                post("/admin/deleteVendor")
                        .sessionAttr("csrfToken", "validToken")
                        .sessionAttr("userState", new UserState(true))
                        .param("vendor", "deleteMe")
                        .param("csrfToken", "wrong")
        ).andExpect(status().isUnauthorized());

        verify(userRepo, never()).delete("deleteMe");
    }

    @Test
    public void non_admin_cannot_delete_vendor() throws Exception {
        mockMvc.perform(
                post("/admin/deleteVendor")
                        .sessionAttr("csrfToken", "validToken")
                        .sessionAttr("userState", new UserState(false))
                        .param("vendor", "deleteMe")
                        .param("csrfToken", "validToken")
        ).andExpect(status().isUnauthorized());

        verify(userRepo, never()).delete("deleteMe");
    }
}
