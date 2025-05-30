package aviasales.ytsaurus;

import tech.ytsaurus.client.ApiServiceTransaction;
import tech.ytsaurus.client.YTsaurusClient;
import tech.ytsaurus.client.request.*;
import tech.ytsaurus.client.rows.UnversionedRow;
import tech.ytsaurus.client.rows.UnversionedRowset;
import tech.ytsaurus.core.cypress.CypressNodeType;
import tech.ytsaurus.core.cypress.YPath;
import tech.ytsaurus.core.tables.ColumnSchema;
import tech.ytsaurus.core.tables.ColumnSortOrder;
import tech.ytsaurus.core.tables.TableSchema;
import tech.ytsaurus.typeinfo.TiType;
import tech.ytsaurus.ysontree.YTree;

import java.util.List;
import java.util.Map;

public class YTsaurusConnectionImpl implements YTsaurusConnection {
    private final YTsaurusClient client;
    private final String tablePath = "//home/statistics";
    private final String cluster = "localhost:8000";
    private final TableSchema tableSchema = TableSchema.builder()
            .setUniqueKeys(true)
            .add(
                    ColumnSchema.builder("time", TiType.timestamp())
                            .setSortOrder(ColumnSortOrder.ASCENDING)
                            .build()
            )
            .add(
                    ColumnSchema.builder("count", TiType.int64())
                            .build()
            )
            .build();

    public YTsaurusConnectionImpl(String endpoint, String token) {
        this.client = YTsaurusClient.builder()
                .setCluster(cluster)
                .build();
    }

    @Override
    public void addStatisticRow(long time, long count) {
        YTsaurusClient client = YTsaurusClient.builder()
                .setCluster(cluster)
                .build();

        try (client) {
            CreateNode createNode = CreateNode.builder()
                    .setPath(YPath.simple(tablePath))
                    .setType(CypressNodeType.TABLE)
                    .setAttributes(Map.of(
                            "dynamic", YTree.booleanNode(true),
                            "schema", tableSchema.toYTree()
                    ))
                    .setIgnoreExisting(true)
                    .build();

            client.createNode(createNode).join();
            client.mountTable(tablePath).join();

            try (ApiServiceTransaction transaction =
                         client.startTransaction(new StartTransaction(TransactionType.Tablet)).join()) {
                transaction.modifyRows(
                        ModifyRowsRequest.builder()
                                .setPath(tablePath)
                                .setSchema(tableSchema)
                                .addInsert(List.of(time, count))
                                .build()).join();
                transaction.commit().join();
            }

            try (ApiServiceTransaction transaction =
                         client.startTransaction(new StartTransaction(TransactionType.Tablet)).join()) {
                UnversionedRowset rowset = transaction.lookupRows(
                        LookupRowsRequest.builder()
                                .setPath(tablePath)
                                .setSchema(tableSchema.toLookup())
                                .build()).join();

                System.out.println("====== LOOKUP RESULT ======");
                for (UnversionedRow row : rowset.getRows()) {
                    System.out.println(row.toYTreeMap(tableSchema, true));
                }
                System.out.println("====== END LOOKUP RESULT ======");

                transaction.commit().join();
            }

//            client.unmountTable(tablePath).join();
        }
    }

    public void close() {
        client.close();
    }
}