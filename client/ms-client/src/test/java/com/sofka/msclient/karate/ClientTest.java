package com.sofka.msclient.karate;

import com.intuit.karate.junit5.Karate;

public class ClientTest {

    @Karate.Test
    Karate testClient(){
        return Karate.run("../../../../../client").relativeTo(getClass());
    }
}
