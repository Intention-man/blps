package aviasales;

import java.time.LocalDateTime;
import java.util.List;

public class Utils {
    public static <T> List<T> sublistByPageAndLimit(List<T> list, int page, int limit) {
        int toIndex = Math.max(Math.min((page + 1) * limit, list.size()) - 1, 0);
        int fromIndex = Math.max(0, toIndex + 1 - limit);
        return list.subList(fromIndex, toIndex);
    }

    public static double calculateDiffInHours(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return java.time.Duration.between(departureDateTime, arrivalDateTime).toMinutes() / 60.0;
    }
}
