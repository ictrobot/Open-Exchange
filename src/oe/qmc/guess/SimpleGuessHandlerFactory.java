package oe.qmc.guess;

import java.util.ArrayList;
import java.util.List;
import oe.api.GuessHandler;
import oe.api.GuessHandlerFactory;

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