import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransactionRoutingConfiguration {

    @Bean
    @Primary
    public DataSource dataSource() {
        TenantDataSource transactionRoutingDataSource = new TenantDataSource();

        DataSource datasource1 = buildDataSourceFromProperties("datasource1");
        DataSource datasource2 = buildDataSourceFromProperties("datasource2");
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("client1", datasource1);
        targetDataSources.put("client2", datasource2);

        transactionRoutingDataSource.setDefaultTargetDataSource(datasource1);
        transactionRoutingDataSource.setTargetDataSources(targetDataSources);
        transactionRoutingDataSource.afterPropertiesSet();
        return transactionRoutingDataSource;
    }
}
