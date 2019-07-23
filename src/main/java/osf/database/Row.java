package osf.database;

import java.util.Arrays;
import java.util.Iterator;

public class Row implements Iterable<Cell>
{


	private Cell[] cells;

	public Row(Cell[] cells) {
		this.cells = cells;
	}

	public Cell[] getCells() {
		return this.cells;
	}

	public Cell[] getClonedCells() { return Arrays.copyOf(cells, cells.length); }

	public int size() {
		return this.cells.length;
	}

	public Cell getCell(String columnName) {
		for (Cell cell : cells) {
			if (cell.getColumnName().equalsIgnoreCase(columnName))
				return cell;
		}
		return null;
	}

	public void updateCell(String columnName, Object newValue) {
		getCell(columnName).setValue(newValue);
	}

	public Cell getPrimaryKey() {
		for (Cell column : cells) {
			if (column.isPrimaryKey())
				return column;
		}
		return null;
	}

	@Override
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < size();
			}

			@Override
			public Cell next() {
				return getCells()[index++];
			}

		};
	}

}
