# A Board Game Engine - in Java

This is a small board game engine. 

## Tutorial

As with all board games, we first have to set up the game, 
and then we can play the game.

### Setup

The setup is done through the `Board.Factory`. Where you 
can specify the layout of the board and the content of the fields.

```java
import dtu.boardengine.Board;
import dtu.boardengine.Field;
import dtu.boardengine.GameController;

import java.awt.*;

public class Game implements GameController {
    // The game state stored in a variable.
    Game game;

    @Override
    public Board.Factory setup() {
        Board.Factory bf = Board.make();
        bf.setBackground(Color.green);
        bf.addField(Field.make()
                      .setTitle('START')
                      .setBackground(Color.red)
        );
        return bf;
    }

    public static void main(String[] args) {
        new Game().runGame();
    }
}
```

### Running the Game

Like almost all game-engines we follow an "input"-"update"-"draw" cycle.
First we get an input from the user. This will happen as method call
to the game controller, methods like events like `clickField`, `clickBoard` or 
`clickDie`. Then we update the state of the game.

```
public void clickBoard(Board board) {
    // When the user clicks the board then we want to roll the die.
    int [] result = game.rollDies(2);
    // then we call the representive 
    board.rollDies(result);
}
```



## Scope

- This library only supports square tiles.