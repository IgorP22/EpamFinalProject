package com.podverbnyj.provider.utils;

import org.junit.*;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;
import static org.junit.Assert.assertEquals;

public class HashPasswordTest {

    @Test
    public void securePasswordTest() throws Exception {
        String text = "user9";
        String md5 = "0FB8D3C5DFAF81A387BF0BA439AB40E6343D2155FB4DDF6978A52D9B9EA8D0F8";

        assertEquals(md5, securePassword(text));
    }
}