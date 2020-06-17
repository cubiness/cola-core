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

  protected final void setRow(int row, String content) {
    if (row < rows.size()) {
      rows.set(row, content);
    } else {
      throw new IndexOutOfBoundsException("Cannot set row " + row + " of scoreboard section");
    }
  }

  protected final void clearRows() {
    for (int i = 0; i < height - 1; i++) {
      rows.set(i, "");
    }
  }

  public final void setTitle(String title) {
    this.title = title;
  }

  public final int getPosition() {
    return row;
  }

  public final void setPosition(int row) {
    this.row = row;
  }

  public final List<String> getContents() {
    List<String> contents = new ArrayList<>();
    contents.add(title);
    rows.forEach(row -> contents.add("  " + row));
    return contents;
  }

  public final int getHeight() {
    return height;
  }

  public void update() {
    if (needsUpdate()) {
      updateRows();
    }
  }

  protected abstract void updateRows();

  protected abstract boolean needsUpdate();
}
