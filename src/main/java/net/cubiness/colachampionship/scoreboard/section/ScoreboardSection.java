package net.cubiness.colachampionship.scoreboard.section;

import java.util.ArrayList;
import java.util.List;

import net.cubiness.colachampionship.minigame.MinigamePlayer;

public abstract class ScoreboardSection {

  private final List<String> rows = new ArrayList<>();
  private final int height;
  private String title;
  private int row;

  public ScoreboardSection(int height, String title, int row) {
    this.height = height;
    this.title = title;
    this.row = row;
    for (int i = 0; i < height - 1; i++) {
      rows.add("");
    }
  }

  /**
   * Used to set a row in the scoreboard section
   *
   * @param row The row to set
   * @param content The new content to put in that row
   */
  protected final void setRow(int row, String content) {
    if (row < rows.size()) {
      rows.set(row, content);
    } else {
      throw new IndexOutOfBoundsException("Cannot set row " + row + " of scoreboard section");
    }
  }

  /**
   * Will clear all of the rows in this section
   */
  protected final void clearRows() {
    for (int i = 0; i < height - 1; i++) {
      rows.set(i, "");
    }
  }

  /**
   * Will set the bold title of this section
   *
   * @param title The new title to use
   */
  public final void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the position in the whole scoreboard that this section is
   *
   * @return The row that this section starts on
   */
  public final int getPosition() {
    return row;
  }

  /**
   * Sets the position in the scoreboard
   *
   * @param row The new row to be displayed at
   */
  public final void setPosition(int row) {
    this.row = row;
  }

  /**
   * Gets the contents of this whole section
   * The shows the title in bold, and the contents padded with two spaces below it
   *
   * @param p The player that this is being shown to. This can be used for extensions of this class, that need to display something different for each player
   * @return A list of strings representing the content
   */
  public List<String> getContents(MinigamePlayer p) {
    List<String> contents = new ArrayList<>();
    contents.add(title);
    rows.forEach(row -> contents.add("  " + row));
    return contents;
  }

  /**
   * Gets the height of this section
   *
   * @return The height (in rows) of this section
   */
  public final int getHeight() {
    return height;
  }

  /**
   * Called when the whole scoreboard is being updated
   */
  public void update() {
    if (needsUpdate()) {
      updateRows();
    }
  }

  /**
   * Called when the section needs to be updated
   */
  protected abstract void updateRows();

  /**
   * Used to know if this section needs to get updated
   *
   * @return True if this section needs an update, fals if otherwise
   */
  protected abstract boolean needsUpdate();
}
