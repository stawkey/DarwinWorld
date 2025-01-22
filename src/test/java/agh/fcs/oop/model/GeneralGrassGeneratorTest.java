package agh.fcs.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GeneralGrassGeneratorTest {

    Map<Vector2d, Grass> grassMap = new HashMap<>();

    @Test
    void testGeneratingGrassWithinBounds() {
        int minWidth = 0, minHeight = 0, maxWidth = 4, maxHeight = 4, grassCount = 10;
        GeneralGrassGenerator generator = new GeneralGrassGenerator(minWidth, minHeight, maxWidth, maxHeight, grassCount, grassMap);

        for (Vector2d position : generator) {
            assertTrue(position.x() >= minWidth && position.x() <= maxWidth);
            assertTrue(position.y() >= minHeight && position.y() <= maxHeight);
        }
    }

    @Test
    void testGeneratingUniqueGrassPositions() {
        int minWidth = 0, minHeight = 0, maxWidth = 4, maxHeight = 4, grassCount = 10;
        GeneralGrassGenerator generator = new GeneralGrassGenerator(minWidth, minHeight, maxWidth, maxHeight, grassCount, grassMap);

        Set<Vector2d> generatedPositions = new HashSet<>();

        for (Vector2d position : generator) {
            assertFalse(generatedPositions.contains(position));
            generatedPositions.add(position);
        }
    }

    @Test
    void testStopWhenAllPositionsOccupied() {
        int minWidth = 0, minHeight = 0, maxWidth = 2, maxHeight = 2, grassCount = 5;

        // Fill all positions
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int y = minHeight; y <= maxHeight; y++) {
                grassMap.put(new Vector2d(x, y), new Grass(new Vector2d(x, y)));
            }
        }

        GeneralGrassGenerator generator = new GeneralGrassGenerator(minWidth, minHeight, maxWidth, maxHeight, grassCount, grassMap);
        assertFalse(generator.hasNext());
    }

    @Test
    void testGeneratedGrassCount() {
        int minWidth = 0, minHeight = 0, maxWidth = 4, maxHeight = 4, grassCount = 5;
        GeneralGrassGenerator generator = new GeneralGrassGenerator(minWidth, minHeight, maxWidth, maxHeight, grassCount, grassMap);

        int generatedCount = 0;
        for (Vector2d position : generator) {
            generatedCount++;
        }

        assertEquals(grassCount, generatedCount);
    }

    @Test
    void testIteratorBehavior() {
        int minWidth = 0, minHeight = 0, maxWidth = 4, maxHeight = 4, grassCount = 5;
        GeneralGrassGenerator generator = new GeneralGrassGenerator(minWidth, minHeight, maxWidth, maxHeight, grassCount, grassMap);

        while (generator.hasNext()) {
            assertNotNull(generator.next());
        }

        assertThrows(UnsupportedOperationException.class, generator::next);
    }
}
