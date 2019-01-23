package com.hogenboom.finane.utils.parser.ing;




import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FinancialRecordManagerTest {

    private Path testDir = Paths.get(String.format("target/test/%s", getClass().getSimpleName()));

    @Before
    public void setup() throws IOException {
        Files.createDirectories(testDir);
    }

    @Test
    public void deTest() {
        System.out.println("test");

    }
}