package aviasales.ytsaurus;

public interface YTsaurusConnection {
    void addStatisticRow(long statTime, long ticketCount) throws Exception;
    void close();
}