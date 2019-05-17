package com.dell.marsover.commands;

import com.dell.marsover.MarsRover;

public class MoveCommand implements ICommand {

    @Override
    public void execute(final MarsRover rover) {
        rover.move();
    }

}
