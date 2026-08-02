package com.courtly.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.courtly.user.entity.User;

public class UserTest {
    
    @Test
    void shouldRejectNegativeCreditBalance(){
        User user = new User("roberto", "roberto@courtly.com", "encoded-password-placeholder");

        try{
            user.setCredits(-1);
            fail("Expected IllegalArgumentException to be throw");
        } catch (IllegalArgumentException e){
            assertThat(e.getMessage()).isEqualTo("Credits must be greater than 0");
        }
    }
}
