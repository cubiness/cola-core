package net.cubiness.colachampionship.scoreboard.section;

import java.util.ArrayList;
import java.util.List;

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

  protected void setRow(int row, String content) {
    if (row < rows.size()) {
      rows.set(row, content);
    } else {
      throw new IndexOutOfBoundsException("Cannot set row " + row + " of scoreboard section");
    }
  }

  protected void clearRows() {
    for (int i = 0; i < height - 1; i++) {
      rows.set(i, "");
    }
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getPosition() {
    return row;
  }

  public void setPosition(int row) {
    this.row = row;
  }

  public List<String> getContents() {
    List<String> contents = new ArrayList<>();
    contents.add(title);
    rows.forEach(row -> contents.add("  " + row));
    return contents;
  }

  public int getHeight() {
    return height;
  }
}
