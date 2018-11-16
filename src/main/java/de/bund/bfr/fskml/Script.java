package de.bund.bfr.fskml;

import java.util.LinkedList;
import java.util.List;

public abstract class Script {
  String script;
  final List<String> libraries = new LinkedList<>();
  final List<String> sources = new LinkedList<>();

 

 
  /**
   * Gets the script.
   * 
   * @return the script.
   */
  public String getScript() {
      return script;
  }

  /**
   * Gets the names of the source files linked in the script.
   * 
   * @return the names of the source files linked in the script.
   */
  public List<String> getSources() {
      return sources;
  }

  /**
   * Gets the names of the libraries imported in the script.
   * 
   * @return the names of the libraries imported in the script.
   */
  public List<String> getLibraries() {
      return libraries;
  }
}
