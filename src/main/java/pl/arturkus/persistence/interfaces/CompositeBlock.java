package pl.arturkus.persistence.interfaces;

import java.util.List;

public interface CompositeBlock extends Block {
    List<Block> getBlocks();
}