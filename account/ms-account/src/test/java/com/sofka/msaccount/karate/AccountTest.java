package com.sofka.msaccount.karate;

import com.intuit.karate.junit5.Karate;

public class AccountTest {

    @Karate.Test
    Karate accountTest(){
        return Karate.run("../../../../../account").relativeTo(getClass());
    }
}
