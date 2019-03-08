/*
  User: Cloudy
  Date: 16-Feb-19
  Time: 13:24
*/

package cz.cloudy.economysystem;

public class FillUtil {
    public static <T> T fill(Object value, T def) {
        return value == null ? def : (T) value;
    }
}
