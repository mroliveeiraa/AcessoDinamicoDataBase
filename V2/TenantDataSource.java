import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> ctx = new ThreadLocal<>();

    public static void setClient(String client) {
        ctx.set(client);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return ctx.get();
    }
}
