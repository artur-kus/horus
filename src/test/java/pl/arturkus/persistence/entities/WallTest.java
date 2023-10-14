package pl.arturkus.persistence.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.arturkus.persistence.interfaces.Block;
import pl.arturkus.persistence.interfaces.CompositeBlock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {

    private Wall wall;

    public record TestBlock(String color, String material) implements Block {
    }

    @BeforeEach
    public void setUp() {
        wall = new Wall(List.of(
                new CompositeBlock() {
                    @Override
                    public List<Block> getBlocks() {
                        return Arrays.asList(
                                new TestBlock("Red", "Wood"),
                                new TestBlock("Blue", "Metal"),
                                new TestBlock("Green", "Plastic"));
                    }

                    @Override
                    public String color() {
                        return "Black";
                    }

                    @Override
                    public String material() {
                        return "Asphalt";
                    }
                },
                new TestBlock("Blue", "Metal"),
                new TestBlock("Green", "Plastic"))

        );
    }


    @Test
    public void shouldFindBlockByColorWhenColorExists() {
        Optional<Block> result = wall.findBlockByColor("Red");
        assertTrue(result.isPresent());
    }

    @Test
    public void cannotFindBlockByColorWhenColorIsNotExists() {
        Optional<Block> result = wall.findBlockByColor("Black");
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldFindBlocksByMaterialWhenMaterialExists() {
        List<Block> metal = wall.findBlocksByMaterial("Metal");
        assertFalse(metal.isEmpty());
    }

    @Test
    public void cannotFindBlocksByMaterialWhenMaterialIsNotExists() {
        List<Block> result = wall.findBlocksByMaterial("Cardboard");
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnCorrectCountOfBlocksWhenThreeBlocksExist() {
        int count = wall.count();
        assertEquals(5, count);
    }

    @Test
    public void shouldReturnIncorrectCountOfBlocksWhenThreeBlocksExist() {
        int count = wall.count();
        assertNotEquals(8, count);
    }
}