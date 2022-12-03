package ch.bfh.memory.utils;

import static org.junit.Assert.*;

import android.content.Intent;

import org.junit.Test;

import java.util.List;

import ch.bfh.memory.models.MemoryCard;
import ch.bfh.memory.models.MemoryPair;

public class LogAppUtilTest {

    String expected = "{\"task\": \"Memory\",\"solution\": [[\"$word1\",\"$word2\"], [\"$word3\", \"$word4\"], ...]}";

    @Test
    public void testCreateIntent()
    {
        List<MemoryPair> memoryPairs = List.of(new MemoryPair(new MemoryCard("$word1", "word1"), new MemoryCard("$word2", "word2")),
                new MemoryPair(new MemoryCard("$word3", "word1"), new MemoryCard("$word4", "word1")));

        Intent intent = LogAppUtil.createIntent(memoryPairs);
        assertEquals(expected, intent.getStringExtra(LogAppUtil.APPLICATION_PATH_LOGAPP));
    }

}