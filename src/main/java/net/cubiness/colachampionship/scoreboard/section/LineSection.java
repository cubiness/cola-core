package net.cubiness.colachampionship.scoreboard.section;

public class LineSection extends ScoreboardSection {

  public LineSection(String title, int row) {
    super(1, title, row);
  }

  @Override
  protected void updateRows() {

  }

  @Override
  protected boolean needsUpdate() {
    return false;
  }

}
