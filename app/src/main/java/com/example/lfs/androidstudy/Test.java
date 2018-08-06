package com.example.lfs.androidstudy;

/**
 * Created by lfs on 2018/8/2.
 */

class Test {
    private static final Test ourInstance = new Test();

    static Test getInstance() {
        return ourInstance;
    }

    private Test() {
    }
}
