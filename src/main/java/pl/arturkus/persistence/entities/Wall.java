package pl.arturkus.persistence.entities;

import pl.arturkus.persistence.interfaces.Block;
import pl.arturkus.persistence.interfaces.CompositeBlock;
import pl.arturkus.persistence.interfaces.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Wall implements Structure {

    private final List<Block> blocks;

    public Wall(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public Optional<Block> findBlockByColor(String color) {
        return findBlocks(prepareEqualsPredicate(Block::color, color)).stream().findFirst();
    }

    @Override
    public List<Block> findBlocksByMaterial(String material) {
        return findBlocks(prepareEqualsPredicate(Block::material, material));
    }

    @Override
    public int count() {
        return visit(new CountingBlockVisitor(), blocks);
    }

    public List<Block> findBlocks(Predicate<Block> predicate) {
        List<Block> allBlocks = visit(new FindingBlockVisitor(), blocks);
        return allBlocks.stream().filter(predicate).collect(Collectors.toList());
    }

    private <T> Predicate<Block> prepareEqualsPredicate(Function<Block, T> selector, T elementToCheck) {
        return block -> selector.apply(block).equals(elementToCheck);
    }

    private <T> T visit(BlockVisitor<T> blockVisitor, List<Block> blocks) {
        for (Block block : blocks) {
            if (block instanceof CompositeBlock) {
                blockVisitor.visit((CompositeBlock) block);
            } else blockVisitor.visit(block);
        }
        return blockVisitor.getResult();
    }

    interface BlockVisitor<T> {
        void visit(Block block);

        void visit(CompositeBlock block);

        T getResult();
    }

    static class FindingBlockVisitor implements BlockVisitor<List<Block>> {

        private final List<Block> result = new ArrayList<>();

        @Override
        public void visit(Block block) {
            result.add(block);
        }

        @Override
        public void visit(CompositeBlock block) {
            for (Block blockBlock : block.getBlocks()) {
                visit(blockBlock);
            }
        }

        @Override
        public List<Block> getResult() {
            return result;
        }
    }

    static class CountingBlockVisitor implements BlockVisitor<Integer> {
        int counter = 0;

        @Override
        public void visit(Block block) {
            counter++;
        }

        @Override
        public void visit(CompositeBlock block) {
            for (Block blockBlock : block.getBlocks()) {
                visit(blockBlock);
            }
        }

        @Override
        public Integer getResult() {
            return counter;
        }
    }
}