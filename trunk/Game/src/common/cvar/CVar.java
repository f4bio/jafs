/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.cvar;

/**
 *
 * @param <T>
 * @author miracle
 */
public class CVar<T extends java.lang.Comparable<T>> {
    /**
     *
     */
    public static final class ValueOutOfRangeException extends Exception { }
    /**
     *
     */
    public static final class DatatypeMismatchException extends Exception { }

    private T standard = null;
    private T max = null;
    private T min = null;
    private T value = null;
    private String datatype = null;
    private boolean readOnly;

    /**
     *
     * @param standard
     * @param max
     * @param min
     * @param readOnly
     * @throws Exception
     */
    public CVar(T standard, T max, T min, boolean readOnly) throws Exception {
        if(standard == null || max == null || min == null)
            throw new NullPointerException();

        this.standard = standard;
        this.value = standard;
        this.datatype = standard.getClass().getName();
        this.max = max;
        this.min = min;
        this.readOnly = readOnly;
    }

    /**
     *
     * @return
     */
    public T getValue() {
        return value;
    }

    /**
     *
     * @param newValue
     * @throws Exception
     */
    public void setValue(T newValue) throws Exception{
        if(readOnly)
            return;
        
        if(newValue == null)
            throw new NullPointerException();

        if(!newValue.getClass().getName().equals(datatype))
            throw new DatatypeMismatchException();

        int relMax = newValue.compareTo(max);
        int relMin = newValue.compareTo(min);

        if(relMax <= 0 && relMin >= 0)
            value = newValue;
        else
            throw new ValueOutOfRangeException();
    }

    /**
     *
     * @return
     */
    public T getMax() {
        return max;
    }

    /**
     *
     * @param max
     */
    public void setMax(T max) {
        if(max != null)
            this.max = max;
    }

    /**
     *
     * @return
     */
    public T getMin() {
        return min;
    }

    /**
     *
     * @param min
     */
    public void setMin(T min) {
        if(min != null)
            this.min = min;
    }

    /**
     *
     * @return
     */
    public T getStandardValue() {
        return standard;
    }

    /**
     *
     * @param standard
     */
    public void setStandardValue(T standard) {
        if(standard != null)
            this.standard = standard;
    }

    /**
     *
     * @param readOnly
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     *
     * @return
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     *
     * @return
     */
    public String getDatatype() {
        return datatype;
    }

    /**
     *
     */
    public void reset() {
        value = standard;
    }

    /**
     *
     * @param comp
     * @return
     */
    public boolean isOfSameValue(T comp) {
        if(comp.compareTo(value) == 0)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
