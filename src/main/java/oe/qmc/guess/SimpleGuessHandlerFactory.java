package oe.qmc.guess;

import oe.api.qmc.guess.GuessHandler;
import oe.api.qmc.guess.GuessHandlerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimpleGuessHandlerFactory extends GuessHandlerFactory {

  private final GuessHandler handler;

  public SimpleGuessHandlerFactory(GuessHandler handler) {
    this.handler = handler;
  }

  public List<GuessHandler> handlers() {
    List<GuessHandler> handlers = new ArrayList<GuessHandler>();
    handlers.add(handler);
    return handlers;
  }
}
