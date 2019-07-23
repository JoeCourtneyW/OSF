package osf.database;

import java.sql.JDBCType;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;

public class Cell {
    private String columnName;
    private final CellValue defaultValue;
    private CellValue value;
    private JDBCType sqlType;
    private int length;
    private boolean isPrimaryKey;

    public Cell(String columnName, Object defaultValue, JDBCType sqlType, int length) {
        this.columnName = columnName;
        this.defaultValue = new CellValue(defaultValue);
        this.value = new CellValue(defaultValue);
        this.sqlType = sqlType;
        this.length = length;
    }

    // Used during constructing
    public Cell PRIMARY_KEY() {
        isPrimaryKey = true;
        return this;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public CellValue getDefaultValue() {
        return this.defaultValue;
    }

    public CellValue getValue() {
        return this.value;
    }

    public void setValue(Object newValue) {
        this.value.set(newValue);
    }

    public void mutateValue(Function<Double, Double> mutator) {
        this.setValue(mutator.apply(getValue().asDouble()));
        // Might change the underlying type of value Object, but calling asInt will still return the correct numbers, hopefully the person using this method knows what the heck they're doing
    }

    public JDBCType getDatabaseType() {
        return this.sqlType; // INT, FLOAT(32), VARCHAR(50)
    }

    public String getLength() {
        if(length == 0)
            return "";
        else
            return "(" + length + ")";

    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    /**
     * Checks if the object is equivalent to the value of this column
     */
    @Override
    public boolean equals(Object object) {
        return this.getValue().equals(object);
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash *= 37 * this.value.hashCode();
        hash *= 37 * this.defaultValue.hashCode();
        hash *= 37 * this.columnName.hashCode();

        return hash;
    }

    public class CellValue {
        private Object value;

        private CellValue(Object value) {
            this.value = value;
        }

        private void set(Object value) {
            this.value = value;
        }

        public Object asObject() {
            return value;
        }

        public String asString() {
            return value.toString();
        }

        public Integer asInt() {
            return new Integer(value.toString());
        }

        public Double asDouble() {
            return new Double(value.toString());
        }

        public Float asFloat() {
            return new Float(value.toString());
        }

        public UUID asUUID() {
            return UUID.fromString(value.toString());
        }

        public String asSerializedIntArray() {
            StringJoiner joiner = new StringJoiner(",");
            Integer[] ary = asIntArray();
            for (int element : ary)
                joiner.add(element + "");
            return joiner.toString();
        }

        public Integer[] asIntArray() {
            return (Integer[]) value;
        }

        @Override
        public String toString() {
            return asString();
        }

        @Override
        public boolean equals(Object object) {
            return object.toString().equals(this.toString());
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}
